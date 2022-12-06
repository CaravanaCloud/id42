package id42.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class Delivery extends PanacheEntity {
    LocalDateTime createTime;
    LocalDateTime scheduledTime;
    LocalDateTime pickupTime;
    LocalDateTime deliveryTime;
    Location source;
    Location destination;
    Person rider;
    Person recipient;
}
