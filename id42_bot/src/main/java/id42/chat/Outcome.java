package id42.chat;

public record Outcome(Outcome.Type type,
                      String message) {
    public enum Type {
        OK,
        PARTIAL,
        FAIL
    }
    public static Outcome fail(String s) {
        return new Outcome(Type.FAIL, s);
    }
    public static Outcome ok(String s) {
        return new Outcome(Type.OK, s);
    }

    public static Outcome partial(String s) {
        return new Outcome(Type.PARTIAL, s);
    }

}
