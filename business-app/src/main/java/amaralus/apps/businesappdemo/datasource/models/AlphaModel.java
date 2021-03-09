package amaralus.apps.businesappdemo.datasource.models;

import lombok.*;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "alpha")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@SQLDelete(sql = "update alpha set deleted = 'Y', modified_date=now(), row_version=row_version+1 where alpha_code = ?")
@Loader(namedQuery = "loadAlphaById")
@NamedQuery(name = "loadAlphaById", query = "select a from AlphaModel a where a.alphaCode=?1 and a.deleted=false")
public class AlphaModel extends AbstractModel<String> {

    @Id
    @Column(name = "alpha_code", unique = true, nullable = false)
    private String alphaCode;
    @Column(name = "update_field")
    private String updateField;

    @OneToMany(mappedBy = "alphaModel", cascade = ALL, fetch = EAGER)
    private List<AlphaVersionModel> alphaVersionModels = new ArrayList<>();

    public List<AlphaVersionModel> getAlphaVersionModels() {
        alphaVersionModels = new ArrayList<>(alphaVersionModels);
        alphaVersionModels.sort(Collections.reverseOrder(Comparator.comparingDouble(version -> Double.parseDouble(version.getVersionValue()))));
        return alphaVersionModels;
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

