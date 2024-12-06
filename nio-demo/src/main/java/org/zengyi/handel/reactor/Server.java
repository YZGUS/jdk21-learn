package org.zengyi.handel.reactor;

public class Server {

    public static void main(String[] args) {
        try (final Reactor reactor = new Reactor()) {
            reactor.run();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
