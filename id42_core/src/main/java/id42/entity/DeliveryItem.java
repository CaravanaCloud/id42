package id42.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ManyToAny;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class DeliveryItem extends PanacheEntity {
    /*
    @ManyToOne
    Product product;

    Fragility fragility;
 */
}