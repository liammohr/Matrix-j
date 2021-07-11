package com.odycyber.matrix;

// Immutable Fraction class
public class Fraction {
    private int nominator;
    private int denominator;

    public Fraction(int value) {
        this.nominator = value;
        this.denominator = 1;
    }

    public Fraction(int nominator, int denominator) {
        this.nominator = nominator;
        this.denominator = denominator;

        if (this.nominator == 0) { // there is only one representation for 0
            this.denominator = 1;
        }

        int myGcd = gcd(this.nominator, this.denominator);
        this.nominator /= myGcd;
        this.denominator /= myGcd;

        if (this.denominator < 0) { // denominator is always positive
            this.nominator *= -1;
            this.denominator *= -1;
        }
    }

    public Fraction(double x, double y) {
        this((int) (x * Math.pow(10, 20)), (int) (y * Math.pow(10, 20)));
    }

    public Fraction(Fraction a, Fraction b) {
        this(a.nominator * b.denominator, a.denominator * b.nominator);
    }

    public static Fraction zero() {
        return new Fraction(0);
    }

    private int gcd(int a, int b) {
        if (b == 0) {
            return a;
        } else {
            return gcd(b, a % b);
        }
    }

    public int getNominator() {
        return this.nominator;
    }

    public int getDenominator() {
        return this.denominator;
    }

    public Fraction add(Fraction b) {
        return new Fraction(this.nominator * b.denominator + this.denominator * b.nominator,
                this.denominator * b.denominator);
    }

    public Fraction subtract(Fraction b) {
        return new Fraction(this.nominator * b.denominator - this.denominator * b.nominator,
                this.denominator * b.denominator);
    }

    public Fraction multiply(Fraction b) {
        return new Fraction(this.nominator * b.nominator, this.denominator * b.denominator);
    }

    public Fraction divide(Fraction b) {
        return new Fraction(this.nominator * b.denominator, this.denominator * b.nominator);
    }

    public Fraction negative() {
        return new Fraction(-this.nominator, this.denominator);
    }

    public Fraction opposite() {
        return new Fraction(this.denominator, this.nominator);
    }

    public boolean equals(Fraction b) {
        return this.nominator == b.nominator && this.denominator == b.denominator;
    }

    public String toString() {
        return this.nominator + (this.denominator == 1 ? "" : "/" + this.denominator);
    }

    public Fraction sqrt() {
        return new Fraction(Math.sqrt(this.nominator), Math.sqrt(this.denominator));
    }
}