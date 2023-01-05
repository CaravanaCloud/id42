package id42.chat;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static id42.chat.bot.Intent.State.PARTIAL;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class RequestDeliveryESTest extends ChatTest {


    @Test
    public void testCase0() {
        var intent = ask("programar entrega");
        var intentName = intent.name();
        assertNotNull(intent);
        assertEquals(PARTIAL, intent.state());
    }

    @Test
    public void testCase1() {
        var intent = ask("Programa " +
                "Jueves 22/12/2022 " +
                "7:30 - Entrega Eric e Benjamin a Muntaner 321");
        assertNotNull(intent);
        var slots = intent.slots();
        assertNotNull(slots);
        assertEquals("07:30", slots.get("pickupTime"));
        assertEquals("2022-12-22", slots.get("pickupDate"));
        //TODO: assertEquals("Eric & Benjamin", slots.get("pickupContact"));
        //TODO: assertEquals("Jueves", slots.get("weekDay"));
        //TODO: assertEquals("Muntaner 321", slots.get("pickupLocation"));
    }

    @Test
    public void testCase2() {
        var intent = ask("Que entregas hay para Jueves 22/12/2022");
        //Que entregas hay para Jueves 22/12/2022
        //cuantas entregas de Delacrem hemos hecho
        /*
        Programa entregas viernes 23/12/2022
        7:30 - Entrega Eric Benjamín a Muntaner 331
        8:30 - Ruta Animal Coffe
        9:30 - Entrega Frutal a Tokio
        10:00 - Entrega Eric & Benjamín a Rosello 21, SA1
        11:30 - Entrega Eroica Caffe a Noru
        11:30 - Entrega Maka Foods a Vida Meva
        12:30 - Entrega Kibuka a Kibuka
        13:00 - Entrega Eric Benjamín a Rare
        13:00 - Entregas DelaCrem
        13:30 - Entrega Bosabe a Holmes place
        13:30 - Entregas Sabor do Brasil
        16:00 - Entregas DelaCrem
        17:00 - Entregas 12 Graus
        */

        //TODO: Build list deliveries intent
        assertNotNull(intent);
    }

    @Test
    public void testCase3() {
        var intent = ask("Program entrega para Jueves 22/12/2022 9:30 - Entrega Frutal a Tokio");
        //TODO: Build list deliveries intent
        assertNotNull(intent);
    }

    @Test
    public void testCase4() {
        var prompt = "programar entrega "
        + "\nCliente: Eric & Benjamín"
        + "\nPunto: Pastiseria Baixes"
        + "\nEntrega: Muntaner 331"
        + "\nDia: 2 de enero 2023"
        + "\nHora: 18:00";
        var intent = ask(prompt);
        //TODO: Build list deliveries intent
        var slots = intent.slots();
        var pickupContact = slots.get("pickupContact");
        var pickupLocation = slots.get("pickupLocation");
        var dropLocation = slots.get("dropLocation");
        var pickupDate = slots.get("pickupDate");
        var pickupTime = slots.get("pickupTime");
        assertNotNull(intent);
        assertFalse(pickupContact.isBlank());
        assertFalse(pickupLocation.isBlank());
        assertFalse(dropLocation.isBlank());
        assertFalse(pickupDate.isBlank());
        assertFalse(pickupTime.isBlank());
    }

    @Test
    public void testCaseN() {
        var intent = ask("?");
        assertNotNull(intent);
    }





}
