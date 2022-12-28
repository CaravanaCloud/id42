package id42.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Location extends PanacheEntity {
    Double lat;
    Double lon;
    String address;
    String complement;
    String observation;
    String text;

    public static Location of(String text) {
        var loc = new Location();
        loc.text = text;
        return loc;
    }
}
