package id42.rs;

import javax.ws.rs.Produces;

import java.time.LocalDateTime;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import id42.entity.Delivery;
import io.quarkus.runtime.StartupEvent;

@Path("/deliveries")
public class DeliveryResource {
    @Inject
    EntityManager em;

    @Transactional
    public void onStart(@Observes StartupEvent ev){
        em.persist(Delivery.of("Teste live1!",LocalDateTime.now()));
        em.persist(Delivery.of("Teste live2!",LocalDateTime.now()));
    }

    //TODO: request new delivery
    //TODO: update delivery status:
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Delivery> getDeliveries(){
        var result = em.createQuery("select d from Delivery d", 
        Delivery.class).getResultList();
        return result;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Delivery update(Delivery d){
        return d;
    }
}
