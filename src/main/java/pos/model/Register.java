package pos.model;

import pos.catalog.ProductCatalog;
import pos.inventory.Inventory;
import pos.receipt.Receipt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Register - GRASP Controller (Facade Controller).
 * Receives all system events from the UI and delegates to domain objects.
 * Also handles return transactions.
 */
public class Register {
    private final ProductCatalog catalog;
    private Sale currentSale;
    private final List<ReturnItem> returnItems;

    public Register(ProductCatalog catalog) {
        this.catalog = catalog;
        this.returnItems = new ArrayList<>();
    }

    // ==================== Process Sale ====================

    /**
     * System event: start a new sale.
     */
    public void startSale() {
        this.currentSale = new Sale();
        this.returnItems.clear();
        System.out.println("[Register] New sale started.");
    }

    /**
     * System event: enter an item.
     * Controller delegates to ProductCatalog (Factory) to get the description,
     * then to Sale (Creator) to create the line item.
     */
    public void enterItem(ItemID id, int quantity) {
        // Factory pattern: get product description from catalog
        ProductDescription pd = catalog.getProductDescription(id);
        // Creator pattern: Sale creates SalesLineItem
        currentSale.makeLineItem(pd, quantity);
        // Information Expert: SalesLineItem calculates subtotal
        System.out.printf("[Register] Item entered: %s x%d = %s%n",
            pd.getDescription(), quantity, currentSale.getTotal());
    }

    /**
     * System event: end the sale.
     */
    public Money endSale() {
        currentSale.becomeComplete();
        Money total = currentSale.getTotal();
        System.out.printf("[Register] Sale ended. Total: %s%n", total);
        return total;
    }

    /**
     * System event: make payment.
     */
    public void makePayment(Money cashTendered) {
        currentSale.makePayment(cashTendered);
        // Update inventory after sale
        Inventory.getInstance().updateSale(currentSale);
        System.out.printf("[Register] Payment recorded. Cash: %s, Change: %s%n",
            cashTendered, currentSale.getChange());
    }

    /**
     * Generate receipt for the current sale.
     */
    public Receipt generateReceipt() {
        return new Receipt(currentSale);
    }

    // ==================== Handle Returns ====================

    /**
     * System event: start a new return transaction.
     */
    public void startReturn() {
        this.currentSale = new Sale();
        this.returnItems.clear();
        System.out.println("[Register] New return transaction started.");
    }

    /**
     * System event: enter a return item.
     */
    public void enterReturnItem(ItemID id, int quantity, String reason) {
        ProductDescription pd = catalog.getProductDescription(id);
        ReturnItem ri = new ReturnItem(pd, quantity, reason);
        returnItems.add(ri);
        // Update inventory: add returned items back
        Inventory.getInstance().updateReturn(id, quantity);
        System.out.printf("[Register] Return item: %s x%d, Refund: %s, Reason: %s%n",
            pd.getDescription(), quantity, ri.getRefundAmount(), reason);
    }

    /**
     * Calculate total refund amount for all return items.
     */
    public Money getRefundTotal() {
        Money total = new Money(0);
        for (ReturnItem ri : returnItems) {
            total = total.add(ri.getRefundAmount());
        }
        return total;
    }

    /**
     * Generate return receipt.
     */
    public String generateReturnReceipt() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("========================================\n");
        sb.append("         RETURN RECEIPT                 \n");
        sb.append("========================================\n");
        sb.append(String.format("Date/Time:  %s%n", java.time.LocalDateTime.now()));
        sb.append("----------------------------------------\n");
        sb.append(String.format("%-8s %-18s %-4s %-8s %s%n", "ItemID", "Description", "Qty", "Reason", "Refund"));
        sb.append("----------------------------------------\n");
        for (ReturnItem ri : returnItems) {
            sb.append(String.format("%-8s %-18s %-4d %-8s %s%n",
                ri.getProductDescription().getItemID(),
                ri.getProductDescription().getDescription(),
                ri.getQuantity(),
                ri.getReason(),
                ri.getRefundAmount()));
        }
        sb.append("----------------------------------------\n");
        sb.append(String.format("TOTAL REFUND:                  %s%n", getRefundTotal()));
        sb.append("========================================\n");
        sb.append("       Return processed. Thank you!      \n");
        sb.append("========================================\n");
        return sb.toString();
    }

    public Sale getCurrentSale() {
        return currentSale;
    }

    public List<ReturnItem> getReturnItems() {
        return Collections.unmodifiableList(returnItems);
    }
}
