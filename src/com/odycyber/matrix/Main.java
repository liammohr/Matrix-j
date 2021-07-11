package com.odycyber.matrix;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Matrix x = new Matrix(3, 3);
        x.set(0, 0, new Polynomial(new Complex(3)));
        x.set(0, 1, new Polynomial(new Complex(2)));
        x.set(1, 0, new Polynomial(new Complex(1)));
        x.set(1, 1, new Polynomial(new Complex(2)));
        x.set(2, 2, new Polynomial(new Complex(1)));
        System.out.println("This is the Matrix:\n"+x);
        Matrix[] QRresult = Matrix.QR(new Matrix(3, 3), x);
        System.out.println("This is Q:\n"+QRresult[0] + "\nThis is R:\n" + QRresult[1]);
        System.out.println("Their multiplication is:\n" + Matrix.mul(QRresult[0], QRresult[1]));
        System.out.println("Inverse is:\n" + Matrix.inverse(x));
        System.out.println("Multiplication is:\n" + Matrix.mul(x, Matrix.inverse(x)));
    }
}
