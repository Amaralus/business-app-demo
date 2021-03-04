package amaralus.apps.businesappdemo.datasource.models;

import lombok.*;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "alpha_version")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@Where(clause = "deleted=false")
@SQLDelete(sql = "update alpha_version set deleted=true, modified_date=now(), row_version=row_version+1 where version_id=?")
@Loader(namedQuery = "loadAlphaVersionById")
@NamedQuery(name = "loadAlphaVersionById", query = "select a from AlphaVersionModel a where a.versionId=?1 and a.deleted=false")
public class AlphaVersionModel extends AbstractModel {

    @Id
    @Column(name = "version_id", unique = true, nullable = false)
    private String versionId;
    @Column(name = "version_value", nullable = false)
    private String versionValue;
    @Column(name = "alpha_code", nullable = false)
    private String alphaCode;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "alpha_code", insertable = false, updatable = false, nullable = false,
            foreignKey = @ForeignKey(name = "fk_alphaversion_alphacode_alpha_alphacode"))
    private AlphaModel alphaModel;
}
