package pos.model;

/**
 * ProductDescription - Description class (Specification pattern).
 * Contains catalog information about a product: ID, price, and description.
 */
public class ProductDescription {
    private final ItemID itemID;
    private final Money price;
    private final String description;

    public ProductDescription(ItemID itemID, Money price, String description) {
        this.itemID = itemID;
        this.price = price;
        this.description = description;
    }

    public ItemID getItemID() {
        return itemID;
    }

    public Money getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("ProductDescription{id=%s, price=%s, desc=%s}",
            itemID, price, description);
    }
}
