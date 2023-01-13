package id42.entity;

import id42.service.LocationService;
import id42.Strings;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@NamedQueries({
        @NamedQuery(name = "Delivery.byLocator",
                query = "SELECT d FROM Delivery d WHERE d.locator = :locator"),
})
public class Delivery extends PanacheEntity {
    String locator;
    LocalDateTime createTime;
    LocalDateTime pickupTime;
    String pickupLocation;

    String pickupSpot;

    String pickupContact;
    String dropLocation;
    String deliveryNote;

    LocalDateTime updateTime;

    public Delivery() {}

    public Delivery(String locator,
                    LocalDateTime pickupTime,
                    String pickupLocation,
                    String pickupContact,
                    String dropLocation,
                    String deliveryNote) {
        this.createTime = LocalDateTime.now();
        this.pickupTime = pickupTime;
        this.pickupLocation = pickupLocation;
        this.pickupContact = pickupContact;
        this.dropLocation = dropLocation;
        this.deliveryNote = deliveryNote;
        this.locator = locator;
    }

    public String pickupDateFmt() {
        return pickupTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    //TODO: Review factory methos
    public static Delivery of(String locator,
                              String pickupDate,
                              String pickupTime,
                              String pickupLocation,
                              String pickupContact,
                              String dropLocation,
                              String deliveryNote) {
        var createT = LocalDateTime.now();
        var pickupT = Strings.parseTime(pickupDate, pickupTime);
        var delivery = new Delivery(locator,
                pickupT,
                pickupLocation,
                pickupContact,
                dropLocation,
                deliveryNote);
        return delivery;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }


    public void touch() {
        this.updateTime = LocalDateTime.now();
    }

    public String pickupTimeFmt() {
            return Strings.formatTime(pickupTime);
    }

    public String pickupLocation() {
        return pickupLocation;
    }
}
