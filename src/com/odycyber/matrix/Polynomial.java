package com.odycyber.matrix;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;

public class Polynomial {
    private ArrayList<Complex> arr;
    public static final Polynomial ZERO = new Polynomial(Complex.zero());
    public static final Polynomial ONE = new Polynomial(new Complex(1));

    public Polynomial() {
        this.arr = new ArrayList<Complex>();
    }

    public Polynomial(Complex... arr) {
        this.arr = new ArrayList<Complex>(Arrays.asList(arr));
        this.minify();
    }

    public Polynomial(ArrayList<Complex> arr) {
        this.arr = arr;
        this.minify();
    }

    private void minify() {
        ArrayList<Complex> minifiedArr = new ArrayList<Complex>();
        int realLen = 0;
        int i = 0;
        for (Complex c : this.arr) {
            i++;
            if (!c.equals(Complex.zero())) {
                realLen = i;
            }
        }
        Iterator<Complex> iter = this.arr.iterator();
        for (int j = 0; j < realLen; j++) {
            minifiedArr.add(iter.next());
        }
        this.arr = minifiedArr;
    }

    public int getDegree() {
        this.minify();
        return Math.max(0, this.arr.size() - 1);
    }

    public Complex getCoefficient(int i) {
        if (i < this.arr.size()) {
            return this.arr.get(i);
        } else {
            return Complex.zero();
        }
    }

    private void setCoefficient(int i, Complex c) {
        if (i < this.arr.size()) {
            this.arr.set(i, c);
        } else {
            while (this.arr.size() <= i) {
                this.arr.add(Complex.zero());
            }
            this.arr.set(i, c);
        }
    }

    public Polynomial add(Polynomial b) {
        int max = Math.max(this.arr.size(), b.arr.size());
        ArrayList<Complex> newArr = new ArrayList<Complex>();
        for (int i = 0; i < max; i++) {
            newArr.add(this.getCoefficient(i).add(b.getCoefficient(i)));
        }
        return new Polynomial(newArr);
    }

    public static Polynomial add(Polynomial a, Polynomial b) {
        return a.add(b);
    }

    public Polynomial sub(Polynomial b) {
        int max = Math.max(this.arr.size(), b.arr.size());
        ArrayList<Complex> newArr = new ArrayList<Complex>();
        for (int i = 0; i < max; i++) {
            newArr.add(this.getCoefficient(i).subtract(b.getCoefficient(i)));
        }
        return new Polynomial(newArr);
    }

    public static Polynomial sub(Polynomial a, Polynomial b) {
        return a.sub(b);
    }

    public Polynomial multiply(Polynomial b) {
        Polynomial result = new Polynomial();
        for (int i = 0; i <= this.getDegree(); i++) {
            for (int j = 0; j <= b.getDegree(); j++) {
                result.setCoefficient(i + j,
                        result.getCoefficient(i + j).add(this.getCoefficient(i).multiply(b.getCoefficient(j))));
            }
        }
        return result;
    }

    public static Polynomial multiply(Polynomial a, Polynomial b) {
        return a.multiply(b);
    }

    /*
     * @pre this.getDegree() == 0 && b.getDegree() == 0
     */
    public Polynomial divide(Polynomial b) {
        if (this.getDegree() == 0 && b.getDegree() == 0) {
            return new Polynomial(this.getCoefficient(0).divide(b.getCoefficient(0)));
        } else {
            throw new Error("No Implementation.");
        }
    }

    public static Polynomial divide(Polynomial a, Polynomial b) {
        return a.divide(b);
    }

    public Complex assign(Complex c) {
        Complex currPow = new Complex(1);
        Complex result = Complex.zero();
        for (Complex x : this.arr) {
            result = result.add(currPow.multiply(x));
            currPow.multiply(c);
        }
        return result;
    }

    public String toString() {
        if (this.arr.size() == 0) {
            return "0";
        }
        String res = "";
        for (int i = this.getDegree(); i >= 0; i--) {
            if (!this.arr.get(i).equals(Complex.zero())) {
                res += "(" + this.arr.get(i).toString() + (i != 0 ? ")*X^" + i : ")") + (i == 0 ? "" : " + ");
            }
        }
        return res;
    }

    /*
     * @pre this.getDegree() == 0
     */
    public Polynomial sqrt() {
        if (this.getDegree() == 0) {
            return new Polynomial(this.getCoefficient(0).sqrt());
        } else {
            throw new Error("No Implementation.");
        }
    }

    public static Polynomial sqrt(Polynomial a) {
        return a.sqrt();
    }
}