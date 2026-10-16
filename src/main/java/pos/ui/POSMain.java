package pos.ui;

import pos.adapter.ExternalPrinter;
import pos.adapter.PrinterAdapter;
import pos.adapter.ReceiptPrinter;
import pos.catalog.ProductCatalog;
import pos.inventory.Inventory;
import pos.model.ItemID;
import pos.model.Money;
import pos.model.Register;
import pos.receipt.Receipt;

import java.util.Scanner;

/**
 * POS System - Main entry point.
 * Command-line interface for the Point-of-Sale system.
 */
public class POSMain {

    private static final String LINE = "=============================================";
    private static final String SHORT_LINE = "----------------------";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize the system
        ProductCatalog catalog = new ProductCatalog();
        Register register = new Register(catalog);

        System.out.println(LINE);
        System.out.println("   Welcome to the POS System (ProjectPos_2026)");
        System.out.println("   Small Supermarket Checkout System");
        System.out.println(LINE);

        boolean running = true;
        while (running) {
            displayMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    processSale(register, scanner, catalog);
                    break;
                case "2":
                    handleReturns(register, scanner, catalog);
                    break;
                case "3":
                    catalog.listAllProducts();
                    break;
                case "4":
                    Inventory.getInstance().displayInventory();
                    break;
                case "5":
                    System.out.println("\nThank you for using the POS System. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1-5.");
            }
        }
        scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("\n" + LINE);
        System.out.println("  MAIN MENU");
        System.out.println(LINE);
        System.out.println("  1. Process Sale");
        System.out.println("  2. Handle Returns");
        System.out.println("  3. View Product Catalog");
        System.out.println("  4. View Inventory");
        System.out.println("  5. Exit");
        System.out.println(LINE);
        System.out.print("  Please select (1-5): ");
    }

    // ==================== Process Sale ====================
    private static void processSale(Register register, Scanner scanner, ProductCatalog catalog) {
        System.out.println("\n" + LINE);
        System.out.println("  PROCESS SALE");
        System.out.println(LINE);

        // 1. Start a new sale
        register.startSale();

        // 2. Enter items
        catalog.listAllProducts();
        boolean enteringItems = true;
        while (enteringItems) {
            System.out.print("\nEnter Item ID (or 'done' to finish): ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("done")) {
                enteringItems = false;
                continue;
            }
            try {
                ItemID itemID = new ItemID(input);
                System.out.print("Enter quantity: ");
                int qty = Integer.parseInt(scanner.nextLine().trim());

                // System event: enterItem
                register.enterItem(itemID, qty);
                System.out.println(SHORT_LINE);
                System.out.println(register.getCurrentSale().getLineItems());
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid quantity. Please enter a number.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        // 3. End sale and show total
        Money total = register.endSale();
        System.out.println("\n" + LINE);
        System.out.printf("  TOTAL AMOUNT DUE: %s%n", total);
        System.out.println(LINE);

        // 4. Process payment
        System.out.print("  Enter cash amount: ");
        try {
            double cash = Double.parseDouble(scanner.nextLine().trim());
            Money cashTendered = new Money(cash);

            if (cash < total.getAmount()) {
                System.out.println("Error: Insufficient cash. Amount due: " + total);
                return;
            }

            // System event: makePayment
            register.makePayment(cashTendered);

            // 5. Generate and print receipt
            Receipt receipt = register.generateReceipt();
            String receiptText = receipt.generate();
            System.out.println(receiptText);

            // Use Adapter pattern to print
            ReceiptPrinter printer = new PrinterAdapter(new ExternalPrinter());
            printer.print(receiptText);

        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid cash amount.");
        }
    }

    // ==================== Handle Returns ====================
    private static void handleReturns(Register register, Scanner scanner, ProductCatalog catalog) {
        System.out.println("\n" + LINE);
        System.out.println("  HANDLE RETURNS");
        System.out.println(LINE);

        register.startReturn();
        catalog.listAllProducts();

        boolean enteringItems = true;
        while (enteringItems) {
            System.out.print("\nEnter Item ID to return (or 'done' to finish): ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("done")) {
                enteringItems = false;
                continue;
            }
            try {
                ItemID itemID = new ItemID(input);
                System.out.print("Enter quantity to return: ");
                int qty = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("Enter reason (defective/unwanted/wrong/other): ");
                String reason = scanner.nextLine().trim();

                register.enterReturnItem(itemID, qty, reason);
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid quantity.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        // Calculate and show refund total
        Money refundTotal = register.getRefundTotal();
        System.out.println("\n" + LINE);
        System.out.printf("  TOTAL REFUND DUE: %s%n", refundTotal);
        System.out.println(LINE);

        // Generate return receipt
        String returnReceipt = register.generateReturnReceipt();
        System.out.println(returnReceipt);

        // Print using Adapter
        ReceiptPrinter printer = new PrinterAdapter(new ExternalPrinter());
        printer.print(returnReceipt);
    }
}
