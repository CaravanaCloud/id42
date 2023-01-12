package id42.entity;

import id42.service.LocationService;
import id42.Strings;
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
    LocalDateTime updateTime;

    public static Delivery of(String pickupTime, String pickupLocation, String pickupContact) {
        var createT = LocalDateTime.now();
        var pickupT = Strings.parseTime(pickupTime);
        var location = LocationService.of(pickupLocation);
        return new Delivery();
    }

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

    public void touch() {
        this.updateTime = LocalDateTime.now();
    }



    /*
    Location source;
    Location destination;

    Person rider;
    Person recipient;
 */
}
