package pos.catalog;

import pos.model.ItemID;
import pos.model.Money;
import pos.model.ProductDescription;

import java.util.HashMap;
import java.util.Map;

/**
 * ProductCatalog - GoF Factory pattern.
 * Encapsulates creation/retrieval of ProductDescription objects.
 * The client (Register) does not know how descriptions are stored or created.
 */
public class ProductCatalog {
    private final Map<ItemID, ProductDescription> products;

    public ProductCatalog() {
        this.products = new HashMap<>();
        // Initialize with sample products
        addProduct(new ItemID("1001"), new Money(3.50),  "Coca-Cola 330ml");
        addProduct(new ItemID("1002"), new Money(5.00),  "Lays Chips Large");
        addProduct(new ItemID("1003"), new Money(2.00),  "Mineral Water 500ml");
        addProduct(new ItemID("1004"), new Money(7.50),  "Chocolate Bar 100g");
        addProduct(new ItemID("1005"), new Money(12.00), "Instant Noodles (Pack)");
        addProduct(new ItemID("1006"), new Money(8.50),  "Milk 1L");
        addProduct(new ItemID("1007"), new Money(15.00), "Bread Loaf");
        addProduct(new ItemID("1008"), new Money(6.00),  "Egg (Dozen)");
        addProduct(new ItemID("1009"), new Money(25.00), "Cooking Oil 1L");
        addProduct(new ItemID("1010"), new Money(4.50),  "Tea Bags (50ct)");
    }

    /**
     * Factory method: retrieves or creates ProductDescription by ItemID.
     */
    public ProductDescription getProductDescription(ItemID id) {
        ProductDescription pd = products.get(id);
        if (pd == null) {
            throw new IllegalArgumentException("Product not found: " + id);
        }
        return pd;
    }

    public void addProduct(ItemID id, Money price, String description) {
        products.put(id, new ProductDescription(id, price, description));
    }

    public int getProductCount() {
        return products.size();
    }

    public void listAllProducts() {
        System.out.println("\n===== Product Catalog =====");
        System.out.printf("%-8s %-25s %s%n", "ItemID", "Description", "Price");
        System.out.println("-------------------------------------------");
        for (ProductDescription pd : products.values()) {
            System.out.printf("%-8s %-25s %s%n",
                pd.getItemID(), pd.getDescription(), pd.getPrice());
        }
        System.out.println("-------------------------------------------");
    }
}
