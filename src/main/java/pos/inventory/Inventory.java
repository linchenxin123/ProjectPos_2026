package pos.inventory;

import pos.model.ItemID;
import pos.model.Money;
import pos.model.Sale;
import pos.model.SalesLineItem;

import java.util.HashMap;
import java.util.Map;

/**
 * Inventory - tracks stock levels for each product.
 * Singleton pattern: ensures a single instance manages all stock.
 */
public class Inventory {
    private static Inventory instance;
    private final Map<ItemID, Integer> stock;

    private Inventory() {
        this.stock = new HashMap<>();
        // Initialize with sample stock levels
        stock.put(new ItemID("1001"), 100);
        stock.put(new ItemID("1002"), 50);
        stock.put(new ItemID("1003"), 200);
        stock.put(new ItemID("1004"), 75);
        stock.put(new ItemID("1005"), 60);
        stock.put(new ItemID("1006"), 80);
        stock.put(new ItemID("1007"), 40);
        stock.put(new ItemID("1008"), 30);
        stock.put(new ItemID("1009"), 25);
        stock.put(new ItemID("1010"), 90);
    }

    public static synchronized Inventory getInstance() {
        if (instance == null) {
            instance = new Inventory();
        }
        return instance;
    }

    /**
     * Decrease stock after a sale.
     */
    public void updateSale(Sale sale) {
        for (SalesLineItem sli : sale.getLineItems()) {
            ItemID id = sli.getProductDescription().getItemID();
            int current = stock.getOrDefault(id, 0);
            int newStock = current - sli.getQuantity();
            stock.put(id, Math.max(0, newStock));
        }
    }

    /**
     * Increase stock after a return.
     */
    public void updateReturn(ItemID id, int quantity) {
        int current = stock.getOrDefault(id, 0);
        stock.put(id, current + quantity);
    }

    public int getStock(ItemID id) {
        return stock.getOrDefault(id, 0);
    }

    public void displayInventory() {
        System.out.println("\n===== Current Inventory =====");
        System.out.printf("%-8s %s%n", "ItemID", "Stock");
        System.out.println("----------------------");
        for (Map.Entry<ItemID, Integer> entry : stock.entrySet()) {
            System.out.printf("%-8s %d%n", entry.getKey(), entry.getValue());
        }
        System.out.println("----------------------");
    }
}
