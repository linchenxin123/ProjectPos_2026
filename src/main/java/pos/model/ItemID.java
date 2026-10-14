package pos.model;

/**
 * ItemID - Value Object identifying a product.
 * Immutable.
 */
public class ItemID {
    private final String id;

    public ItemID(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ItemID cannot be null or empty");
        }
        this.id = id.trim();
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemID)) return false;
        return this.id.equals(((ItemID) obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
