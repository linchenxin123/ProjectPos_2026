package pos.model;

import pos.payment.Payment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sale - represents a single sales transaction.
 * GRASP: Creator - creates SalesLineItem objects (it aggregates them
 * and has the initialization data).
 * GRASP: Information Expert - calculates total because it knows all
 * its line items.
 */
public class Sale {
    private boolean isComplete;
    private final List<SalesLineItem> lineItems;
    private Payment payment;
    private final String dateTime;

    public Sale() {
        this.isComplete = false;
        this.lineItems = new ArrayList<>();
        this.dateTime = java.time.LocalDateTime.now().toString();
    }

    /**
     * Creator pattern: Sale creates SalesLineItem because it
     * aggregates line items and has the initialization data.
     */
    public void makeLineItem(ProductDescription pd, int quantity) {
        if (isComplete) {
            throw new IllegalStateException("Cannot add items to a completed sale");
        }
        lineItems.add(new SalesLineItem(pd, quantity));
    }

    /**
     * Information Expert: calculates total because it knows all line items.
     */
    public Money getTotal() {
        Money total = new Money(0);
        for (SalesLineItem sli : lineItems) {
            total = total.add(sli.getSubtotal());
        }
        return total;
    }

    public void makePayment(Money cashTendered) {
        this.payment = new Payment(cashTendered);
        this.isComplete = true;
    }

    public Money getChange() {
        if (payment == null) return new Money(0);
        return payment.getChange(getTotal());
    }

    public void becomeComplete() {
        this.isComplete = true;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public List<SalesLineItem> getLineItems() {
        return Collections.unmodifiableList(lineItems);
    }

    public Payment getPayment() {
        return payment;
    }

    public String getDateTime() {
        return dateTime;
    }

    public int getItemCount() {
        return lineItems.size();
    }
}
