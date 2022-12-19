package id42.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class TelegramMessage extends PanacheEntity {

    //@Type(type = "json")
    @Column( columnDefinition = "json" )
    String updateJson;

    public TelegramMessage(){}
    public TelegramMessage(String updateJson) {
        this.updateJson = updateJson;
    }

    public String getUpdateJson() {
        return updateJson;
    }

    public void setUpdateJson(String updateJson) {
        this.updateJson = updateJson;
    }
}
