package util;

public class MyUnitTesting {
    public static void assertTrue(boolean cond, String failMessage) {
        if (!cond) {
            System.err.println("ASSERTION FAILED: " + failMessage);
            System.exit(1);
        }
    }
}
