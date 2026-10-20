package pos;

import pos.catalog.ProductCatalog;
import pos.inventory.Inventory;
import pos.model.*;

/**
 * Unit tests for POS System core classes.
 */
public class POSTest {

    public static void main(String[] args) {
        System.out.println("===== POS System Unit Tests =====\n");

        testMoneyOperations();
        testItemID();
        testProductDescription();
        testSalesLineItem();
        testSale();
        testProductCatalog();
        testInventory();
        testRegisterProcessSale();
        testRegisterHandleReturns();

        System.out.println("\n===== All tests passed! =====");
    }

    private static void testMoneyOperations() {
        System.out.print("Test Money operations... ");
        Money m1 = new Money(10.50);
        Money m2 = new Money(5.25);
        assert m1.add(m2).getAmount() == 15.75 : "Money add failed";
        assert m1.subtract(m2).getAmount() == 5.25 : "Money subtract failed";
        assert m1.multiply(3).getAmount() == 31.50 : "Money multiply failed";
        assert !m1.isNegative() : "Money isNegative failed";
        assert new Money(-5).isNegative() : "Money isNegative should be true";
        System.out.println("PASSED");
    }

    private static void testItemID() {
        System.out.print("Test ItemID... ");
        ItemID id1 = new ItemID("1001");
        ItemID id2 = new ItemID("1001");
        ItemID id3 = new ItemID("1002");
        assert id1.equals(id2) : "ItemID equals failed";
        assert !id1.equals(id3) : "ItemID not equals failed";
        assert id1.hashCode() == id2.hashCode() : "ItemID hashCode failed";
        System.out.println("PASSED");
    }

    private static void testProductDescription() {
        System.out.print("Test ProductDescription... ");
        ItemID id = new ItemID("1001");
        Money price = new Money(3.50);
        ProductDescription pd = new ProductDescription(id, price, "Coca-Cola");
        assert pd.getItemID().equals(id) : "PD itemID failed";
        assert pd.getPrice().equals(price) : "PD price failed";
        assert pd.getDescription().equals("Coca-Cola") : "PD description failed";
        System.out.println("PASSED");
    }

    private static void testSalesLineItem() {
        System.out.print("Test SalesLineItem (Information Expert)... ");
        ProductDescription pd = new ProductDescription(
            new ItemID("1001"), new Money(3.50), "Coca-Cola");
        SalesLineItem sli = new SalesLineItem(pd, 3);
        Money subtotal = sli.getSubtotal();
        assert subtotal.getAmount() == 10.50 : "SLI subtotal failed: " + subtotal;
        System.out.println("PASSED");
    }

    private static void testSale() {
        System.out.print("Test Sale (Creator + Information Expert)... ");
        Sale sale = new Sale();
        ProductDescription pd1 = new ProductDescription(
            new ItemID("1001"), new Money(3.50), "Coca-Cola");
        ProductDescription pd2 = new ProductDescription(
            new ItemID("1002"), new Money(5.00), "Lays Chips");
        sale.makeLineItem(pd1, 2); // 7.00
        sale.makeLineItem(pd2, 1); // 5.00
        Money total = sale.getTotal();
        assert total.getAmount() == 12.00 : "Sale total failed: " + total;
        assert sale.getItemCount() == 2 : "Sale item count failed";

        sale.makePayment(new Money(20.00));
        Money change = sale.getChange();
        assert change.getAmount() == 8.00 : "Sale change failed: " + change;
        assert sale.isComplete() : "Sale should be complete";
        System.out.println("PASSED");
    }

    private static void testProductCatalog() {
        System.out.print("Test ProductCatalog (Factory)... ");
        ProductCatalog catalog = new ProductCatalog();
        ProductDescription pd = catalog.getProductDescription(new ItemID("1001"));
        assert pd != null : "Catalog getProductDescription failed";
        assert pd.getPrice().getAmount() == 3.50 : "Catalog price failed";
        assert catalog.getProductCount() == 10 : "Catalog count failed";
        System.out.println("PASSED");
    }

    private static void testInventory() {
        System.out.print("Test Inventory (Singleton)... ");
        Inventory inv1 = Inventory.getInstance();
        Inventory inv2 = Inventory.getInstance();
        assert inv1 == inv2 : "Inventory singleton failed";
        int stock = inv1.getStock(new ItemID("1001"));
        assert stock == 100 : "Inventory stock failed: " + stock;
        System.out.println("PASSED");
    }

    private static void testRegisterProcessSale() {
        System.out.print("Test Register Process Sale (Controller)... ");
        ProductCatalog catalog = new ProductCatalog();
        Register register = new Register(catalog);
        register.startSale();
        register.enterItem(new ItemID("1001"), 2);
        register.enterItem(new ItemID("1003"), 1);
        Money total = register.endSale();
        // 3.50*2 + 2.00*1 = 9.00
        assert total.getAmount() == 9.00 : "Register total failed: " + total;
        register.makePayment(new Money(10.00));
        Money change = register.getCurrentSale().getChange();
        assert change.getAmount() == 1.00 : "Register change failed: " + change;

        // Check inventory was updated
        int stock = Inventory.getInstance().getStock(new ItemID("1001"));
        assert stock == 98 : "Inventory update after sale failed: " + stock;
        System.out.println("PASSED");
    }

    private static void testRegisterHandleReturns() {
        System.out.print("Test Register Handle Returns... ");
        ProductCatalog catalog = new ProductCatalog();
        Register register = new Register(catalog);
        register.startReturn();
        register.enterReturnItem(new ItemID("1001"), 1, "defective");
        Money refund = register.getRefundTotal();
        assert refund.getAmount() == 3.50 : "Register refund failed: " + refund;

        // Check inventory was updated (added back)
        int stock = Inventory.getInstance().getStock(new ItemID("1001"));
        assert stock == 99 : "Inventory update after return failed: " + stock;
        System.out.println("PASSED");
    }
}
