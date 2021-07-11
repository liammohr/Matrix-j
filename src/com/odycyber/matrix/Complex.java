package com.odycyber.matrix;

// Immutable complex number class
public class Complex {
    private Fraction real;
    private Fraction complex;

    public Complex(Fraction real) {
        this.real = real;
        this.complex = Fraction.zero();
    }

    public Complex(int real) {
        this.real = new Fraction(real);
        this.complex = Fraction.zero();
    }

    public Complex(Fraction real, Fraction complex) {
        this.real = real;
        this.complex = complex;
    }

    public Complex(int real, int complex) {
        this.real = new Fraction(real);
        this.complex = new Fraction(complex);
    }

    public static Complex zero() {
        return new Complex(Fraction.zero());
    }

    public Fraction getReal() {
        return this.real;
    }

    public Fraction getComplex() {
        return this.complex;
    }

    public Complex conjugate() {
        return new Complex(this.real, this.complex.negative());
    }

    public Complex negative() {
        return new Complex(this.real.negative(), this.complex.negative());
    }

    public Complex add(Complex b) {
        return new Complex(this.real.add(b.real), this.complex.add(b.complex));
    }

    public Complex subtract(Complex b) {
        return new Complex(this.real.subtract(b.real), this.complex.subtract(b.complex));
    }

    public Complex multiply(Complex b) {
        return new Complex(this.real.multiply(b.real).subtract(this.complex.multiply(b.complex)),
                this.real.multiply(b.complex).add(this.complex.multiply(b.real)));
    }

    public Complex divide(Complex b) {
        // (a+ib) / (c+id) = (ac+bd)/(c^2+d^2)+i((bc-ad)/(c^2+d^2))
        Fraction c2pd2 = b.real.multiply(b.real).add(b.complex.multiply(b.complex));
        Fraction real = this.real.multiply(b.real).add(this.complex.multiply(b.complex)).divide(c2pd2);
        Fraction complex = this.complex.multiply(b.real).subtract(this.real.multiply(b.complex)).divide(c2pd2);
        return new Complex(real, complex);
    }

    public Complex opposite() {
        return (new Complex(1)).divide(this);
    }

    public boolean equals(Complex b) {
        return this.real.equals(b.real) && this.complex.equals(b.complex);
    }

    public String toString() {
        if (this.complex.equals(Fraction.zero())) {
            return this.real.toString();
        } else {
            return this.real.toString() + " + (" + this.complex.toString() + ")i";
        }
    }
}