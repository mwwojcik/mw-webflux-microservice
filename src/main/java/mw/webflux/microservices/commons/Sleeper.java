package mw.webflux.microservices.commons;

public class Sleeper {

    public static void sleepSecconds(int sec) {
        sleepMillis(sec * 1000);
    }

    public static void sleepMillis(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
