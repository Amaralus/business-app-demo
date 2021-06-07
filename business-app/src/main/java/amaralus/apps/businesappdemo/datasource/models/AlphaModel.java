package amaralus.apps.businesappdemo.datasource.models;

import amaralus.apps.businesappdemo.datasource.models.link.AlphaThetaLinkModel;
import amaralus.apps.businesappdemo.datasource.models.link.ManyToManyLinkedModel;
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
// любой вызов на удаление сущности (кроме запроса delete) будет использовать этот апдейт
// его использование обязательно для каждой модели
@SQLDelete(sql = "update alpha set deleted = 'Y' where alpha_code = ?")
// для получения сущности нужно ипользовать лоадер с фильтром на флаг удаления,
// он будет вызываться всегда и даже вместо джоинов, его использование - обзательно
@Loader(namedQuery = "loadAlphaById")
@NamedQuery(name = "loadAlphaById", query = "select a from AlphaModel a where a.alphaCode=?1 and a.deleted=false")
public class AlphaModel
        extends AbstractModel<String>
        implements Cleanable<AlphaModel>, ManyToManyLinkedModel {

    @Id
    @Column(name = "alpha_code", unique = true, nullable = false)
    private String alphaCode;
    @Column(name = "update_field")
    private String updateField;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    // для всех связейс другими нужно всегда включать к каскадные операции
    // это нужно для автоматического управления вложеными сущностями
    @OneToMany(mappedBy = "alphaModel", cascade = ALL)
    @LazyCollection(FALSE)
    private List<AlphaVersionModel> alphaVersionModels = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    // удаление сирот нужно для случаев когда нам нужно полностьб заменить связи на новые
    // мы кладем в коллекцию нужные нам модели, а хибер вяснит моделей каких нет в коллекции и "удалит" их
    @OneToMany(mappedBy = "alphaModel", cascade = ALL, orphanRemoval = true)
    @LazyCollection(FALSE)
    private Set<AlphaThetaLinkModel> thetaLinks = new HashSet<>();

    public List<AlphaVersionModel> getAlphaVersionModels() {
        // сортировка версий вручную так как при использовании лоадера аннотация OrderBy игнорируется
        alphaVersionModels = new ArrayList<>(alphaVersionModels);
        alphaVersionModels.sort(Collections.reverseOrder(Comparator.comparingDouble(version -> Double.parseDouble(version.getVersionValue()))));
        return alphaVersionModels;
    }

    public void setThetas(Set<ThetaModel> thetaModels) {
        // создаем новые модели линков перед тем как отправить их на добавление в PersistenceSet
        var newLinks = thetaModels.stream()
                .map(theta -> new AlphaThetaLinkModel(this, theta))
                .collect(Collectors.toSet());

        replaceLinks(thetaLinks, newLinks);
    }

    public Set<ThetaModel> getThetas() {
        return getThetaLinks().stream()
                .map(AlphaThetaLinkModel::getThetaModel)
                .collect(Collectors.toSet());
    }

    @Override
    public AlphaModel clearDeleted() {
        // подчищаем все вложеные сущности (в связях хибер не использует лоадер с фильтром)
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

