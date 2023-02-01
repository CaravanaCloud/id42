package id42.entity;

import id42.Strings;
import id42.intent.DeliveryRequestSlots;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static javax.persistence.EnumType.STRING;

@Entity
@NamedQueries({
        @NamedQuery(name = "Delivery.byLocator",
                query = "SELECT d FROM Delivery d WHERE d.locator = :locator"),
})
public class Delivery extends PanacheEntity {
    @Column(unique = true)
    String locator;

    @ManyToOne
    DeliveriesRequest request;

    @Enumerated(STRING)
    DeliveryState state;

    LocalDateTime createTime;

    LocalDateTime pickTime;

    String pickAddress;
    String pickAddressDetail;
    String pickSpot;
    String pickContact;

    String dropAddress;
    String dropAddressDetail;
    String dropSpot;
    String dropContact;

    String deliveryNote;

    LocalDateTime updateTime;


    public Delivery() {}

    private Delivery(String locator,
                     DeliveriesRequest request,
                     LocalDateTime pickTime,
                     String pickAddress,
                     String pickAddressDetail,
                     String pickSpot,
                     String pickContact,
                     String dropAddress,
                     String dropAddressDetail,
                     String dropSpot,
                     String dropContact,
                     String deliveryNote,
                     String updateTime) {
        this.state=DeliveryState.created;
        this.createTime= LocalDateTime.now();

        this.locator=null;
        this.request=null;
        this.pickTime=null;
        this.pickAddress=null;
        this.pickAddressDetail=null;
        this.pickSpot=null;
        this.pickContact=null;
        this.dropAddress=null;
        this.dropAddressDetail=null;
        this.dropSpot=null;
        this.dropContact=null;
        this.deliveryNote=null;
        this.updateTime=null;
    }

    //TODO: Review factory methos
    public static Delivery of(String locator,
                              DeliveriesRequest request,
                              String pickDate,
                              String pickTime,
                              String pickAddress,
                              String pickAddressDetail,
                              String pickSpot,
                              String pickContact,
                              String dropAddress,
                              String dropAddressDetail,
                              String dropSpot,
                              String dropContact,
                              String deliveryNote,
                              String updateTime) {
        var pickT = (LocalDateTime) null;
        if (pickDate != null && pickTime != null) {
            pickT = Strings.parseTime(pickDate, pickTime);
        }
        var delivery = new Delivery(locator,
                request,
                pickT,
                pickAddress,
                pickAddressDetail,
                pickSpot,
                pickContact,
                dropAddress,
                dropAddressDetail,
                dropSpot,
                dropContact,
                deliveryNote,
                updateTime);
        return delivery;
    }

    public static Delivery of(DeliveriesRequest request) {
        return new Delivery(null,
                request,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
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

    public void deliveryNote(String s) {
        this.deliveryNote = s;
    }

    public void pickAddress(String s) {
        this.pickAddress = s;
    }

    public void pickAddressDetail(String s) {
        this.pickAddress = s;
    }

    public void dropAddress(String s) {
        this.dropAddress = s;
    }

    public void dropAddressDetail(String s) {
        this.pickAddress = s;
    }

    public void dropSpot(String s) {
        this.dropSpot = s;
    }

    public void dropContact(String s) {
        this.dropContact = s;
    }

    public String pickAddress() {
        return pickAddress;
    }
}
