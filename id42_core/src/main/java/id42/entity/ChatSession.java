package id42.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@NamedQueries({
    @NamedQuery(name="ChatSession.bySessionId",
        query = "SELECT c FROM ChatSession c WHERE c.sessionId = :sessionId")
})
@Entity
public class ChatSession extends PanacheEntity {
    String sessionId;
    LocalDateTime updateTime;
    @OneToOne
    DeliveriesRequest deliveriesRequest;

    public ChatSession() {}
    public ChatSession(String sessionId) {
        this.sessionId = sessionId;
    }

    public void touch() {
        this.updateTime = LocalDateTime.now();
    }
}
