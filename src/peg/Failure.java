package peg;

public final class Failure extends ParseResult {
    public static Failure INST = new Failure();
    
    private Failure() {}

    public String toString() {
        return "Failure";
    }
}
