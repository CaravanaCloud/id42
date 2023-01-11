package id42.entity;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Person extends PanacheEntity {
    String name;
    String email;
    String phone;
    String observation;
}
