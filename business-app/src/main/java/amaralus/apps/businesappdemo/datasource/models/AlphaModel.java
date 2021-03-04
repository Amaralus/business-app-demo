package amaralus.apps.businesappdemo.datasource.models;

import lombok.*;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "alpha")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@Where(clause = "deleted=false")
@SQLDelete(sql = "update alpha set deleted=true, modified_date=now(), row_version=row_version+1 where alpha_code=?")
@Loader(namedQuery = "loadAlphaById")
@NamedQuery(name = "loadAlphaById", query = "select a from AlphaModel a where a.alphaCode=?1 and a.deleted=false")
public class AlphaModel extends AbstractModel<String> {

    @Id
    @Column(name = "alpha_code", unique = true, nullable = false)
    private String alphaCode;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "alphaModel", cascade = ALL, fetch = EAGER, orphanRemoval = true)
    @OrderBy("versionValue desc")
    List<AlphaVersionModel> alphaVersionModels;

    @Override
    public String getId() {
        return alphaCode;
    }

    @Override
    public void setId(String id) {
        alphaCode = id;
    }
}
