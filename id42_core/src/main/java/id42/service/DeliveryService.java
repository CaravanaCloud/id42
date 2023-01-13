package id42.service;

import id42.entity.Delivery;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class DeliveryService {


    @Transactional
    public Optional<Delivery> findByLocator(String code) {
        return Delivery.find("locator", code)
                .firstResultOptional();
    }
}
