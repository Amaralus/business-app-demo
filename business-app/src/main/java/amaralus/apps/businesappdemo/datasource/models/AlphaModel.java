package amaralus.apps.businesappdemo.datasource.models;

import lombok.*;
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
public class AlphaModel extends AbstractModel {

    @Id
    @Column(name = "alpha_code", unique = true, nullable = false)
    private String alphaCode;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "alphaModel", cascade = ALL, fetch = EAGER, orphanRemoval = true)
    @OrderBy("versionValue desc")
    List<AlphaVersionModel> alphaVersionModels;
}
