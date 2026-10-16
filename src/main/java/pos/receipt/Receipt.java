package pos.receipt;

import pos.model.Money;
import pos.model.Sale;
import pos.model.SalesLineItem;

/**
 * Receipt - generated after a sale or return is completed.
 */
public class Receipt {
    private final String receiptNo;
    private final Sale sale;
    private final String dateTime;

    public Receipt(Sale sale) {
        this.sale = sale;
        this.dateTime = sale.getDateTime();
        this.receiptNo = "R" + System.currentTimeMillis() % 1000000;
    }

    public String generate() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("========================================\n");
        sb.append("          SUPERMARKET POS SYSTEM         \n");
        sb.append("         Official Sales Receipt          \n");
        sb.append("========================================\n");
        sb.append(String.format("Receipt No: %s%n", receiptNo));
        sb.append(String.format("Date/Time:  %s%n", dateTime));
        sb.append("----------------------------------------\n");
        sb.append(String.format("%-8s %-18s %-4s %s%n", "ItemID", "Description", "Qty", "Subtotal"));
        sb.append("----------------------------------------\n");

        for (SalesLineItem sli : sale.getLineItems()) {
            sb.append(String.format("%-8s %-18s %-4d %s%n",
                sli.getProductDescription().getItemID(),
                sli.getProductDescription().getDescription(),
                sli.getQuantity(),
                sli.getSubtotal()));
        }

        sb.append("----------------------------------------\n");
        sb.append(String.format("TOTAL:                         %s%n", sale.getTotal()));
        sb.append(String.format("Cash Tendered:                 %s%n", sale.getPayment().getCashTendered()));
        sb.append(String.format("CHANGE:                         %s%n", sale.getChange()));
        sb.append("========================================\n");
        sb.append("       Thank you for shopping!          \n");
        sb.append("========================================\n");

        return sb.toString();
    }

    public String getReceiptNo() {
        return receiptNo;
    }
}
