package id42.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class DeliveriesRequest extends PanacheEntity {

    LocalDateTime createTime;

    public DeliveriesRequest() {
        this.createTime = LocalDateTime.now();
    }

    public static DeliveriesRequest of() {
        return new DeliveriesRequest();
    }
}
