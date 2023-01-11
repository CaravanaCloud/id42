package id42.service;

import id42.entity.Delivery;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class DeliveryService {

    @Transactional
    public Delivery request(String pickupTime,
                            String pickupLocation,
                            String pickupContact) {
        Delivery delivery = Delivery.of(pickupTime, pickupLocation, pickupContact);
        delivery.persist();
        return delivery;
    }
}
