package id42.entity;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Contact extends PanacheEntity {
    String nameString;
    String phone;
    String email;
    
}
