package pos.payment;

import pos.model.Money;

/**
 * Payment - records the payment for a sale.
 */
public class Payment {
    private final Money amount;
    private final Money cashTendered;
    private final String method;

    public Payment(Money cashTendered) {
        this(cashTendered, "Cash");
    }

    public Payment(Money cashTendered, String method) {
        this.cashTendered = cashTendered;
        this.amount = cashTendered;
        this.method = method;
    }

    public Money getAmount() {
        return amount;
    }

    public Money getCashTendered() {
        return cashTendered;
    }

    public String getMethod() {
        return method;
    }

    public Money getChange(Money total) {
        return cashTendered.subtract(total);
    }

    public Money getRefund(Money total) {
        return total;
    }

    @Override
    public String toString() {
        return String.format("Payment{amount=%s, method=%s}", amount, method);
    }
}
