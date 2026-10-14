package pos.model;

/**
 * Money - Value Object representing a monetary amount.
 * Immutable; all operations return new instances.
 */
public class Money {
    private final double amount;
    private final String currency;

    public Money(double amount) {
        this(amount, "CNY");
    }

    public Money(double amount, String currency) {
        this.amount = Math.round(amount * 100.0) / 100.0;
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Money add(Money other) {
        return new Money(this.amount + other.amount, this.currency);
    }

    public Money subtract(Money other) {
        return new Money(this.amount - other.amount, this.currency);
    }

    public Money multiply(int factor) {
        return new Money(this.amount * factor, this.currency);
    }

    public boolean isNegative() {
        return amount < 0;
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", amount, currency);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Money)) return false;
        Money other = (Money) obj;
        return Double.compare(this.amount, other.amount) == 0
            && this.currency.equals(other.currency);
    }

    @Override
    public int hashCode() {
        return Double.hashCode(amount) * 31 + currency.hashCode();
    }
}
