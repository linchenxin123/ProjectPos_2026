package pos.model;

/**
 * ReturnItem - represents a single returned item.
 */
public class ReturnItem {
    private final int quantity;
    private final ProductDescription productDescription;
    private final String reason;

    public ReturnItem(ProductDescription pd, int quantity, String reason) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.productDescription = pd;
        this.quantity = quantity;
        this.reason = reason != null ? reason : "Not specified";
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductDescription getProductDescription() {
        return productDescription;
    }

    public String getReason() {
        return reason;
    }

    public Money getRefundAmount() {
        return productDescription.getPrice().multiply(quantity);
    }

    @Override
    public String toString() {
        return String.format("  Return: %-12s %-20s x%d  Refund: %s  Reason: %s",
            productDescription.getItemID(),
            productDescription.getDescription(),
            quantity,
            getRefundAmount(),
            reason);
    }
}
