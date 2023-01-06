package id42.data;

//https://stackoverflow.com/questions/10146864/is-there-a-java-parser-that-can-parse-addresses-like-this
public class SLAP {
    String address;
    String complement;
    String contact;

    public SLAP of() {
        return new SLAP();
    }

    public void parse(String text){
        //TODO: actually parse
    }
}
