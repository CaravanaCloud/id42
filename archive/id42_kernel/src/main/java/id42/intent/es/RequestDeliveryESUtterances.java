package id42.intent.es;

import java.util.List;

public class RequestDeliveryESUtterances {
    private static final List<String> VALUES = List.of(
           "Programar entrega"
                    + "\nDia: {pickDate}"
                    + "\nHora: {pickTime}"
                    + "\nRetirada Cliente: {pickContact}"
                    + "\nRetirada Direccion: {pickAddress}"
                    + "\nRetirada Detalle: {pickAddressDetail}"
                    + "\nRetirada Punto: {pickSpot}"
                    + "\nEntrega Cliente: {dropContact}"
                    + "\nEntrega Direccion: {dropAddress}"
                    + "\nEntrega Detalle: {dropAddressDetail}"
                    + "\nEntrega Punto: {dropSpot}",
            "Programar entrega",
            "Programa entrega",
            "Programa una entrega",
            "Programar un pedido",
            "Programar entrega para {weekDay}",
            "Programar entrega para {weekDay} a las {pickTime}",
            "Organizar la entrega de {pickAddress} a {dropAddress}",
            "Necesito un rider",
            "Necesito un recogido",
            "Coordinar una entrega para {weekDay} {pickDate} {pickTime} recoger con {pickContact} a {pickAddress}",
            "Programa retirada el {weekDay} {pickDate} a las {pickTime} recoger con {pickContact} en {pickAddress}",
            "Necesito de una entrega para {weekDay} {pickDate} a las {pickTime} en {pickAddress}",
            "Program entrega para {weekDay} {pickDate} {pickTime} - de {pickAddress} a {dropAddress}",
            "Agendar la entrega para {weekDay} {pickDate} {pickTime} desde {pickAddress} a {dropAddress}",
            "Programa entrega para {weekDay} {pickDate} {pickTime} - de {pickAddress} a {dropAddress} nota que {deliveryNote}",
            "Fijar la entrega para {weekDay} {pickDate} {pickTime} - desde {pickAddress} a {dropAddress} sepa que {deliveryNote}",
            "Programa entrega para {weekDay} {pickDate} {pickTime} de {pickAddress} a {dropAddress} mira, {deliveryNote}"
    );
    public static List<String> values() {
        return VALUES;
    }
}
