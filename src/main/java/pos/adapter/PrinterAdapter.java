package pos.adapter;

/**
 * PrinterAdapter - GoF Adapter pattern.
 * Adapts the ExternalPrinter interface to the ReceiptPrinter interface
 * expected by the POS system.
 */
public class PrinterAdapter implements ReceiptPrinter {
    private final ExternalPrinter externalPrinter;

    public PrinterAdapter(ExternalPrinter printer) {
        this.externalPrinter = printer;
    }

    @Override
    public void print(String text) {
        // Adapt the interface: translate print(text) into the external printer's API
        externalPrinter.openPort();
        externalPrinter.sendData(text.getBytes());
        System.out.println("[PrinterAdapter] Receipt printed successfully.");
        externalPrinter.closePort();
    }
}
