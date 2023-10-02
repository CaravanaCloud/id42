package id42.intent;

import id42.chat.*;
import id42.intent.es.ESChat;

//TODO: Map more precise slot types (Day of Week, PartialDate, LocationAlias ...)
//TODO: Add integration
public class ID42Chat {


    public static Chats of(){
            System.out.println("Initializing chat engine");
            var chats = new Chats();
            ESChat.add(chats);
            return chats;
    }



}
