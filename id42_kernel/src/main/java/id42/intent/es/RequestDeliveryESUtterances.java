package id42.intent.es;

import java.util.List;

public class RequestDeliveryESUtterances {
    private static final List<String> VALUES = List.of(
            "Programar entrega",
            "Programa entrega",
            "Programa una entrega",
            "Programar un pedido",
            "Programar entrega para {weekDay}",
            "Programar entrega para {weekDay} a las {pickTime}",
            "Organizar la entrega de {pickLocation} a {dropLocation}",
            "Necesito un rider",
            "Necesito un recogido",
            "Coordinar una entrega para {weekDay} {pickDate} {pickTime} recoger con {pickContact} a {pickLocation}",
            "Programa retirada el {weekDay} {pickDate} a las {pickTime} recoger con {pickContact} en {pickLocation}",
            "Necesito de una entrega para {weekDay} {pickDate} a las {pickTime} en {pickLocation}",
            "Program entrega para {weekDay} {pickDate} {pickTime} - de {pickLocation} a {dropLocation}",
            "Agendar la entrega para {weekDay} {pickDate} {pickTime} desde {pickLocation} a {dropLocation}",
            "Programa entrega para {weekDay} {pickDate} {pickTime} - de {pickLocation} a {dropLocation} nota que {deliveryNote}",
            "Fijar la entrega para {weekDay} {pickDate} {pickTime} - desde {pickLocation} a {dropLocation} sepa que {deliveryNote}",
            "Programa entrega para {weekDay} {pickDate} {pickTime} de {pickLocation} a {dropLocation} mira, {deliveryNote}",
            "Entrega para "
                + "\nCliente: {pickContact}"
                + "\nPunto: {pickLocation}"
                + "\nEntrega: {dropLocation}"
                + "\nDia: {pickDate}"
                + "\nHora: {pickTime}"
    );
    public static List<String> values() {
        return VALUES;
    }
}
