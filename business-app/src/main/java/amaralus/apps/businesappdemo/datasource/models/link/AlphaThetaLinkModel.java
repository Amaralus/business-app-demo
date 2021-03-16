package amaralus.apps.businesappdemo.datasource.models.link;

import amaralus.apps.businesappdemo.datasource.models.AbstractModel;
import amaralus.apps.businesappdemo.datasource.models.AlphaModel;
import amaralus.apps.businesappdemo.datasource.models.CompositeKey;
import amaralus.apps.businesappdemo.datasource.models.ThetaModel;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "alpha_theta_link")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@SQLDelete(sql = "update alpha_theta_link set deleted = 'Y' where link_id = ?")
@Loader(namedQuery = "loadAlphaThetaLinkById")
@NamedQuery(name = "loadAlphaThetaLinkById", query = "select t from AlphaThetaLinkModel t where t.linkId=?1 and t.deleted=false")
public class AlphaThetaLinkModel extends AbstractModel<String> implements CompositeKey {

    @Id
    @Column(name = "link_id", unique = true, nullable = false)
    @GeneratedValue(generator = "composite-generator")
    @GenericGenerator(name = "composite-generator", strategy = "amaralus.apps.businesappdemo.datasource.IdGenerator")
    private String linkId;
    @Column(name = "alpha_code", nullable = false)
    private String alphaCode;
    @Column(name = "theta_id", nullable = false)
    @Type(type = "uuid-char")
    private UUID thetaId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "alpha_code", insertable = false, updatable = false, nullable = false,
            foreignKey = @ForeignKey(name = "fk_alphathetalink_alphacode_alpha_alphacode"))
    private AlphaModel alphaModel;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "theta_id", insertable = false, updatable = false, nullable = false,
            foreignKey = @ForeignKey(name = "fk_alphathetalink_thetaid_theta_thetaid"))
    private ThetaModel thetaModel;

    public AlphaThetaLinkModel(AlphaModel alphaModel, ThetaModel thetaModel) {
        this.alphaModel = alphaModel;
        this.thetaModel = thetaModel;
        alphaCode = alphaModel.getAlphaCode();
        thetaId = thetaModel.getThetaId();
    }

    @Override
    public Collection<String> getUniqueFields() {
        return List.of(alphaCode, thetaId.toString());
    }

    @Override
    public String getId() {
        return linkId;
    }

    @Override
    public void setId(String id) {
        linkId = id;
    }
}
