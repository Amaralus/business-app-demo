package amaralus.apps.businesappdemo.datasource.models;

import amaralus.apps.businesappdemo.datasource.models.link.AlphaThetaLinkModel;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.ALL;
import static org.hibernate.annotations.LazyCollectionOption.FALSE;

@Entity
@Table(name = "alpha")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@SQLDelete(sql = "update alpha set deleted = 'Y' where alpha_code = ?")
@Loader(namedQuery = "loadAlphaById")
@NamedQuery(name = "loadAlphaById", query = "select a from AlphaModel a where a.alphaCode=?1 and a.deleted=false")
public class AlphaModel extends AbstractModel<String> implements Cleanable<AlphaModel> {

    @Id
    @Column(name = "alpha_code", unique = true, nullable = false)
    private String alphaCode;
    @Column(name = "update_field")
    private String updateField;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "alphaModel", cascade = ALL)
    @LazyCollection(FALSE)
    private List<AlphaVersionModel> alphaVersionModels = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "alphaModel", cascade = ALL, orphanRemoval = true)
    @LazyCollection(FALSE)
    private Set<AlphaThetaLinkModel> thetaLinks = new HashSet<>();

    public List<AlphaVersionModel> getAlphaVersionModels() {
        alphaVersionModels = new ArrayList<>(alphaVersionModels);
        alphaVersionModels.sort(Collections.reverseOrder(Comparator.comparingDouble(version -> Double.parseDouble(version.getVersionValue()))));
        return alphaVersionModels;
    }

    public void setThetas(Set<ThetaModel> thetaModels) {
        thetaLinks.forEach(AbstractModel::delete);
        var newLinks = thetaModels.stream()
                .map(theta -> new AlphaThetaLinkModel(this, theta))
                .collect(Collectors.toSet());

        // чтобы persistence context контекст не ругался на добавление сущетвующей сущности
        // нужно убрать из новых существующие по thetaId и восстановить старую на случай если удалена
        // в итоге будут добавлятся только новые у которых еще нет сгенерированных айдишников
        for (var existedLink : thetaLinks)
            for (var newLink : new HashSet<>(newLinks))
                if (existedLink.getThetaId().equals(newLink.getThetaId())) {
                    newLinks.remove(newLink);
                    existedLink.restore();
                }

        thetaLinks.addAll(newLinks);
    }

    public Set<ThetaModel> getThetas() {
        return getThetaLinks().stream()
                .map(AlphaThetaLinkModel::getThetaModel)
                .collect(Collectors.toSet());
    }

    @Override
    public AlphaModel clearDeleted() {
        alphaVersionModels.removeIf(AbstractModel::isDeleted);
        thetaLinks.removeIf(AbstractModel::isDeleted);
        return this;
    }

    @Override
    public String getId() {
        return alphaCode;
    }

    @Override
    public void setId(String id) {
        alphaCode = id;
    }
}

