package amaralus.apps.businesappdemo.datasource.models;

import lombok.*;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "alpha_version")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@SQLDelete(sql = "update alpha_version set deleted='Y', modified_date=now(), row_version=row_version+1 where version_id=?")
@Loader(namedQuery = "loadAlphaVersionById")
@NamedQuery(name = "loadAlphaVersionById", query = "select a from AlphaVersionModel a where a.versionId=?1 and a.deleted=false")
public class AlphaVersionModel extends AbstractModel<String> implements CompositeKey {

    @Id
    @Column(name = "version_id", unique = true, nullable = false)
    private String versionId;
    @Column(name = "version_value", nullable = false)
    private String versionValue;
    @Column(name = "alpha_code", nullable = false)
    private String alphaCode;
    @Column(name = "update_field")
    private String updateField;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alpha_code", insertable = false, updatable = false, nullable = false,
            foreignKey = @ForeignKey(name = "fk_alphaversion_alphacode_alpha_alphacode"))
    private AlphaModel alphaModel;

    @Override
    public String getId() {
        return versionId;
    }

    @Override
    public void setId(String id) {
        versionId = id;
    }

    @Override
    public Collection<String> getUniqueFields() {
        return List.of(versionValue, alphaCode);
    }
}
