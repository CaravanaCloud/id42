package id42.cdk.chat.es;

import java.util.List;

public class RequestDeliveryESUtterances {
    private static final List<String> VALUES = List.of(
            "Programa una entrega",
            "Programar un pedido",
            "Programar entrega para {weekday}",
            "Programar entrega para {weekday} a las {pickupTime}",
            "Organizar la entrega de {pickupLocation} a {dropLocation}",
            "Necesito un rider",
            "Necesito un recogido",
            "Coordinar una entrega para {weekDay} {pickupDate} {pickupTime} recoger con {pickupContact} a {pickupLocation}",
            "Programa retirada el {weekDay} {pickupDate} a las {pickupTime} recoger con {pickupContact} en {pickupLocation}",
            "Necesito de una entrega para {weekDay} {pickupDate} a las {pickupTime} en {pickupLocation}",
            "Program entrega para {weekDay} {pickupDate} {pickupTime} - de {pickupLocation} a {dropLocation}",
            "Agendar la entrega para {weekDay} {pickupDate} {pickupTime} desde {pickupLocation} a {dropLocation}",
            "Programa entrega para {weekDay} {pickupDate} {pickupTime} - de {pickupLocation} a {dropLocation} nota que {deliveryNote}",
            "Fijar la entrega para {weekDay} {pickupDate} {pickupTime} - desde {pickupLocation} a {dropLocation} sepa que {deliveryNote}",
            "Programa entrega para {weekDay} {pickupDate} {pickupTime} de {pickupLocation} a {dropLocation} mira, {deliveryNote}",
            "Entrega para "
                + "\nCliente: {pickupContact}"
                + "\nPunto: {pickupLocation}"
                + "\nEntrega: {dropLocation}"
                + "\nDia: {pickupDate}"
                + "\nHora: {pickupTime}"
    );
    public static List<String> values() {
        return VALUES;
    }
}
