package amaralus.apps.businesappdemo.datasource.models;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class AbstractModel<I extends Serializable> {

    @Column(name = "modified_date")
    @UpdateTimestamp
    private LocalDateTime modifiedDate = LocalDateTime.now();
    @Column(name = "row_version")
    private Long rowVersion = 1L;
    @Column
    @Type(type = "yes_no")
    private boolean deleted = false;

    @PreUpdate
    public void preUpdate() {
        rowVersion++;
    }

    public String getTableName() {
        return this.getClass().getAnnotation(Table.class).name();
    }

    public abstract I getId();

    public abstract void setId(I id);

    public void restore() {
        deleted = false;
    }
}
