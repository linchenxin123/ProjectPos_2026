package pos.model;

/**
 * SalesLineItem - represents a single line item in a sale.
 * GRASP: Information Expert - knows quantity and product price, so it
 * calculates its own subtotal.
 */
public class SalesLineItem {
    private final int quantity;
    private final ProductDescription productDescription;

    public SalesLineItem(ProductDescription productDescription, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.productDescription = productDescription;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductDescription getProductDescription() {
        return productDescription;
    }

    /**
     * Information Expert: calculates subtotal because it knows
     * the quantity and the product price.
     */
    public Money getSubtotal() {
        return productDescription.getPrice().multiply(quantity);
    }

    @Override
    public String toString() {
        return String.format("  %-12s %-20s x%d  %s",
            productDescription.getItemID(),
            productDescription.getDescription(),
            quantity,
            getSubtotal());
    }
}
