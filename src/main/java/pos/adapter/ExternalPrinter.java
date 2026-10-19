package pos.adapter;

/**
 * Adaptee - external printer with an incompatible interface.
 * This simulates a third-party printer API.
 */
public class ExternalPrinter {
    public void openPort() {
        System.out.println("[ExternalPrinter] Port opened.");
    }

    public void sendData(byte[] data) {
        System.out.println("[ExternalPrinter] Data sent: " + data.length + " bytes");
    }

    public void closePort() {
        System.out.println("[ExternalPrinter] Port closed.");
    }
}
