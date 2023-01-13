package id42.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import static javax.persistence.EnumType.STRING;

@Entity
public class DeliveriesRequest extends PanacheEntity {
    @Enumerated(STRING)
    DeliveryRequestState state;
}
