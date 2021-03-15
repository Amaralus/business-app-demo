package amaralus.apps.businesappdemo.datasource.models;

import lombok.*;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "theta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@SQLDelete(sql = "update theta set deleted = 'Y' where theta_id = ?")
@Loader(namedQuery = "loadThetaById")
@NamedQuery(name = "loadThetaById", query = "select t from ThetaModel t where t.thetaId=?1 and t.deleted=false")
public class ThetaModel extends AbstractModel<UUID> {

    @Id
    @Column(name = "theta_id", unique = true, nullable = false)
    @Type(type = "uuid-char")
    private UUID thetaId;
    @Column(name = "theta_code", unique = true, nullable = false)
    private String thetaCode;

    @Override
    public UUID getId() {
        return thetaId;
    }

    @Override
    public void setId(UUID id) {
        thetaId = id;
    }
}
