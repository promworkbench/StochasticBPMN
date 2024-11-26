package org.processmining.stochasticbpmn.models.stochastic;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Probability extends Number implements Comparable<Probability> {
    // Define constants for 0 and 1
    public static final Probability ZERO = Probability.of(BigDecimal.ZERO);
    public static final Probability ONE = Probability.of(BigDecimal.ONE);
    private final BigDecimal value;

    private Probability(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("Probability must be between 0 and 1 (inclusive).");
        }
        this.value = value;
    }

    /**
     * Static factory method to create a Probability.
     */
    public static Probability of(BigDecimal value) {
        if (value == null) {
            throw new NullPointerException("Value cannot be null.");
        }
        return new Probability(value);
    }

    /**
     * Static factory method to create a Probability from a double.
     */
    public static Probability of(double value) {
        return of(BigDecimal.valueOf(value));
    }

    public static Probability of(String value) {
        return of(new BigDecimal(value));
    }

    /**
     * Getter for the underlying BigDecimal value.
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * Adds two probabilities.
     * Throws an exception if the result exceeds 1.
     */
    public Probability add(Probability other) {
        return Probability.of(this.value.add(other.value));
    }

    /**
     * Subtracts another probability.
     * Throws an exception if the result is less than 0.
     */
    public Probability subtract(Probability other) {
        return Probability.of(this.value.subtract(other.value));
    }

    /**
     * Multiplies two probabilities.
     */
    public Probability multiply(Probability other) {
        return Probability.of(this.value.multiply(other.value));
    }

    /**
     * Divides this probability by another.
     * Throws an exception for division by zero or out-of-bound results.
     */
    public Probability divide(Probability other) {
        return Probability.of(this.value.divide(other.value, 30, RoundingMode.DOWN));
    }

    public Probability divide(Probability other, int scale, RoundingMode roundingMode) {
        return Probability.of(this.value.divide(other.value, scale, roundingMode));
    }

    public Probability divide(Probability other, RoundingMode roundingMode) {
        return Probability.of(this.value.divide(other.value, roundingMode));
    }

    @Override
    public String toString() {
        return "Probability{" + "value=" + value + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Probability that = (Probability) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int intValue() {
        return value.intValue();
    }

    @Override
    public long longValue() {
        return value.longValue();
    }

    @Override
    public float floatValue() {
        return value.floatValue();
    }

    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public int compareTo(Probability probability) {
        return getValue().compareTo(probability.getValue());
    }
}
