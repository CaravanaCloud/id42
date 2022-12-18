
package id42.rs;

import javax.ws.rs.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.core.MediaType;

import id42.entity.Delivery;
import io.quarkus.runtime.StartupEvent;

@Path("/deliveries")
public class DeliveryResource {

    @Transactional
    public void onStart(@Observes StartupEvent ev){
        Delivery.of("Teste live1!",LocalDateTime.now()).persist();
        Delivery.of("Teste live2!",LocalDateTime.now()).persist();
        Delivery.of("Teste live3!",LocalDateTime.now()).persist();

    }

    //TODO: request new delivery
    //TODO: update delivery status:
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Delivery> getDeliveries(){
        return Delivery.listAll();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Delivery getDelivery(@PathParam("id") Long id){
        return Delivery.findById(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Delivery update(Delivery d){
        return d;
    }
}
