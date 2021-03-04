package amaralus.apps.businesappdemo.datasource.models;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class AbstractModel {


    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;
    @Column(name = "row_version")
    private Long rowVersion = 1L;
    @Column
    @Type(type = "yes_no")
    private boolean deleted = false;
}
