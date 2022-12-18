package id42.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class Delivery extends PanacheEntity {
    String description;
    LocalDateTime createTime;
    LocalDateTime scheduledTime;
    LocalDateTime pickupTime;
    LocalDateTime deliveryTime;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

	public static Delivery of(String description, LocalDateTime createTime) {
        var d1 = new Delivery();
        d1.setDescription(description);
        d1.setCreateTime(createTime);
		return d1;
	}



    /*
    Location source;
    Location destination;

    Person rider;
    Person recipient;
 */
}
