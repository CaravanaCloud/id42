package id42.entity;

import id42.Strings;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static javax.persistence.EnumType.STRING;

@Entity
@NamedQueries({
        @NamedQuery(name = "Delivery.byLocator",
                query = "SELECT d FROM Delivery d WHERE d.locator = :locator"),
})
public class Delivery extends PanacheEntity {
    String locator;
    @Enumerated(STRING)
    DeliveryState state;
    LocalDateTime createTime;
    LocalDateTime pickTime;
    String pickLocation;

    String pickSpot;

    String pickContact;
    String dropLocation;
    String deliveryNote;

    LocalDateTime updateTime;

    @ManyToOne
    DeliveriesRequest request;

    public Delivery() {
    }

    public Delivery(DeliveriesRequest request,
                    String locator,
                    LocalDateTime pickTime,
                    String pickLocation,
                    String pickContact,
                    String dropLocation,
                    String deliveryNote) {
        this.createTime = LocalDateTime.now();
        this.pickTime = pickTime;
        this.pickLocation = pickLocation;
        this.pickContact = pickContact;
        this.dropLocation = dropLocation;
        this.deliveryNote = deliveryNote;
        this.locator = locator;
        this.state = DeliveryState.created;
        this.request = request;
    }

    //TODO: Review factory methos
    public static Delivery of(DeliveriesRequest request,
                              String locator,
                              String pickDate,
                              String pickTime,
                              String pickLocation,
                              String pickContact,
                              String dropLocation,
                              String deliveryNote) {
        var pickT = (LocalDateTime) null;
        if (pickDate != null && pickTime != null) {
            pickT = Strings.parseTime(pickDate, pickTime);
        }
        var delivery = new Delivery(request,
                locator,
                pickT,
                pickLocation,
                pickContact,
                dropLocation,
                deliveryNote);
        return delivery;
    }

    public String pickDateFmt() {
        return pickTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
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

    public String pickTimeFmt() {
        return Strings.formatTime(pickTime);
    }

    public String pickLocation() {
        return pickLocation;
    }

    public void pickLocation(String pickLocation) {
        this.pickLocation = pickLocation;
    }


    public void locator(String locator) {
        this.locator = locator;
    }

    public void pickTime(String pickDate, String pickTime) {
        this.pickTime = Strings.parseTime(pickDate, pickTime);
    }

    public void pickSpot(String s) {
        this.pickSpot = s;
    }

    public void pickContact(String s) {
        this.pickContact = s;
    }

    public void dropLocation(String s) {
        this.dropLocation = s;
    }

    public void deliveryNote(String s) {
        this.deliveryNote = s;
    }
}
