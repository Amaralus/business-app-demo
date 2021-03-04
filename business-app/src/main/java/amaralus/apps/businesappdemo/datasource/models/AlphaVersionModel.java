package amaralus.apps.businesappdemo.datasource.models;

import lombok.*;
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
