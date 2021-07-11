package com.odycyber.matrix;
import java.util.List;

import com.odycyber.matrix.Matrix.PeulatDerugScalar;

import java.util.ArrayList;

public class Matrix {
	private int row, col;
	private Polynomial det;
	public Polynomial[][] mat;

	Matrix(int row, int col) {
		this(row, col, Polynomial.ZERO);
		for (int i = 0; i < Math.min(row, col); i++) {
			this.mat[i][i] = Polynomial.ONE;
		}
	}

	Matrix(int row, int col, Polynomial init) {
		this.row = row;
		this.col = col;
		this.det = null;
		this.mat = new Polynomial[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				this.mat[i][j] = init;
			}
		}
	}

	public Polynomial get(int row, int col) {
		return this.mat[row][col];
	}

	public int getColumnDimension() {
		return this.col;
	}

	public int getRowDimension() {
		return this.row;
	}

	public void set(int r, int c, Polynomial elem) {
		this.mat[r][c] = elem;
	}

	public List<Matrix> getColumns() {
		ArrayList<Matrix> columns = new ArrayList<Matrix>();
		for (int j = 0; j < this.getColumnDimension(); j++) {
			Matrix c = new Matrix(this.getRowDimension(), 1);
			for (int i = 0; i < this.getRowDimension(); i++) {
				c.set(i, 0, this.get(i, j));
			}
			columns.add(c);
		}
		return columns;
	}

	public static Matrix fromColumns(List<Matrix> columns) {
		Matrix result = new Matrix(columns.get(0).getRowDimension(), columns.size());
		int j = 0;
		for (Matrix c : columns) {
			for (int i = 0; i < result.getRowDimension(); i++) {
				result.set(i, j, c.get(i, 0));
			}
			j++;
		}
		return result;
	}

	public static Matrix add(Matrix A, Matrix B) {
		int row = A.getRowDimension(), col = B.getColumnDimension();
		Matrix C = new Matrix(row, col, Polynomial.ZERO);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				C.set(i, j, Polynomial.add(A.get(i, j), B.get(i, j)));
			}
		}
		return C;
	}

	public static Matrix sub(Matrix A, Matrix B) {
		int row = A.getRowDimension(), col = B.getColumnDimension();
		Matrix C = new Matrix(row, col, Polynomial.ZERO);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				C.set(i, j, Polynomial.sub(A.get(i, j), B.get(i, j)));
			}
		}
		return C;
	}

	public static Matrix mul(Matrix A, Matrix B) {
		int row = A.getRowDimension(), col = B.getColumnDimension();
		Matrix C = new Matrix(row, col, Polynomial.ZERO);
		Polynomial sum;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				sum = Polynomial.ZERO;
				for (int k = 0; k < B.getRowDimension(); k++) {
					sum = sum.add(Polynomial.multiply(A.get(i, k), B.get(k, j)));
				}
				C.set(i, j, sum);
			}
		}
		return C;
	}
	
	public static Matrix mul(Matrix A, Polynomial B) {
		int row = A.getRowDimension(), col = A.getColumnDimension();
		Matrix C = A.clone();
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				C.set(i,j, Polynomial.multiply(A.get(i,j),B));
			}
		}
		return C;
	}

	public static Matrix pow(Matrix A, int n) {
		if (n == 0) {
			return new Matrix(A.getRowDimension(), A.getColumnDimension());
		}
		return mul(A, pow(A, n - 1));

	}

	public Matrix clone() {
		return add(this, new Matrix(this.getRowDimension(), this.getColumnDimension(), Polynomial.ZERO));
	}

	public String toString() {
		String s = "";
		for (int i = 0; i < this.getRowDimension(); i++) {
			for (int j = 0; j < this.getColumnDimension(); j++) {
				s += this.get(i, j) + " ";
			}
			s += "\n";
		}
		return s;
	}

	public boolean equals(Matrix A) {
		for (int i = 0; i < this.getRowDimension(); i++) {
			for (int j = 0; j < this.getColumnDimension(); j++) {
				if (this.get(i, j) != A.get(i, j)) {
					return false;
				}
			}
		}
		return true;
	}

	public Polynomial getDet() {
		this.rowReduce();
		return this.det;
	}

	public static abstract class PeulatDerug {
		public int rows;
		public int cols;

		public PeulatDerug(int rows, int cols) {
			this.rows = rows;
			this.cols = cols;
		}

		public Matrix getMatrix() {
			return new Matrix(this.rows, this.cols);
		}
		public Matrix apply(Matrix a) {
			return Matrix.mul(this.getMatrix(), a);
		}
	}
	public static class PeulatDerugAdd extends PeulatDerug {
		public int i;
		public int j;
		public Complex alpha;

		public PeulatDerugAdd(int i, int j, Complex alpha, int rows, int cols) {
			super(rows, cols);
			this.i = i;
			this.j = j;
			this.alpha = alpha;
		}
		public String toString() {
			return "R" + this.i + "->R" + this.i + " + " + this.alpha + " * R" + this.j;
		}
		public Matrix getMatrix() {
			Matrix x = super.getMatrix();
			x.set(this.i, this.j, new Polynomial(this.alpha));
			return x;
		}
	}
	public static class PeulatDerugScalar extends PeulatDerug {
		public int i;
		public Complex alpha;
		public PeulatDerugScalar(int i, Complex alpha, int rows, int cols) {
			super(rows, cols);
			this.i = i;
			this.alpha = alpha;
		}
		public String toString() {
			return "R" + this.i + "->R" + this.i + " * " + this.alpha;
		}
		public Matrix getMatrix() {
			Matrix x = super.getMatrix();
			x.set(this.i, this.i, new Polynomial(this.alpha));
			return x;
		}
	}
	public static class PeulatDerugSwitch extends PeulatDerug {
		public int i;
		public int j;
		public PeulatDerugSwitch(int i, int j, int rows, int cols) {
			super(rows, cols);
			this.i = i;
			this.j = j;
		}
		public String toString() {
			return "R" + this.i + "<->R" + this.j;
		}
		public Matrix getMatrix() {
			Matrix x = super.getMatrix();
			x.set(this.i, this.i, Polynomial.ZERO);
			x.set(this.j, this.j, Polynomial.ZERO);
			
			x.set(this.i, this.j, Polynomial.ONE);
			x.set(this.j, this.i, Polynomial.ONE);
			return x;
		}
	}

	/*
	 * @pre this.get(i,j).getDegree()==0;
	 */
	public Matrix rowReduce(List<PeulatDerug> peulot) {
		Matrix B = this.clone();
		Polynomial de = Polynomial.ONE;
		for (int i = 0; i < B.getRowDimension(); i++) {
			int ind = i;
			boolean bool = false;
			while (ind < B.getColumnDimension() && !(bool)) {
				if (!(B.get(i, ind).equals(Polynomial.ZERO))) {
					bool = true;
				} else {
					ind++;
				}
			}
			if (bool) {
				if (ind != i) {
					de = Polynomial.multiply(de, new Polynomial(new Complex(-1)));
					// switch rows
					if (peulot != null) {
						peulot.add(new PeulatDerugSwitch(ind, i, this.getRowDimension(), this.getColumnDimension()));
					}
				}
				Polynomial[] tmp = B.mat[i];
				B.mat[i] = B.mat[ind];
				B.mat[ind] = tmp;
				Polynomial m = B.get(i, i);
				de = Polynomial.multiply(de, m);
				if (peulot != null) {
					// save the peulot
					if (!m.getCoefficient(0).equals(new Complex(1)) && !m.getCoefficient(0).equals(Complex.zero())) {  // if this is true then it's useless
						peulot.add(new PeulatDerugScalar(i, (new Complex(1)).divide(m.getCoefficient(0)), this.getRowDimension(), this.getColumnDimension()));
					}
				for (int t = i; t < B.getColumnDimension(); t++) {
					// divide the whole row by m
					B.set(i, t, Polynomial.divide(B.get(i, t), m));
					}
				}
				for (int j = 0; j < B.getRowDimension(); j++) {
					if (i != j) {
						Polynomial n = B.get(j, i);
						if (peulot != null) {
							peulot.add(new PeulatDerugAdd(j, i, n.getCoefficient(0).negative(), this.getRowDimension(), this.getColumnDimension()));
						}
						for (int k = i; k < B.getColumnDimension(); k++) {
							// subtract n*i from j
							B.set(j, k, Polynomial.sub(B.get(j, k), Polynomial.multiply(B.get(i, k), n)));
						}
					}
				}
			}
		}
		if (B.equals(new Matrix(B.getRowDimension(), B.getColumnDimension()))) {
			this.det = de;
		}
		return B;
	}
	public Matrix rowReduce() { return this.rowReduce(null); }

	public static Matrix transpose(Matrix A) {
		Matrix B = new Matrix(A.getColumnDimension(), A.getRowDimension(), Polynomial.ZERO);
		for (int i = 0; i < A.getRowDimension(); i++) {
			for (int j = 0; j < A.getColumnDimension(); j++) {
				B.set(j, i, A.get(i, j));
			}
		}
		return B;
	}
	/*
	 * @pre A.getRowDimension()=1, B.getRowDimension()=1
	 */
	public static Polynomial DP(Matrix G, Matrix A, Matrix B) {
		return(mul(mul(transpose(A),G),B)).get(0, 0);
	}

	public static Matrix projection(Matrix G, Matrix A, List<Matrix> lst, List<Polynomial> saveTo){
		Matrix res =new Matrix(A.getRowDimension(),A.getColumnDimension(),Polynomial.ZERO);
		for(Matrix B: lst){
			Polynomial vixi = DP(G, A, B);
			Polynomial xixi = DP(G, B, B);
			Polynomial foo = vixi.divide(xixi);
			res = add(res, mul(B, foo));
			if (saveTo != null) {
				saveTo.add(foo);
			}
		}
		return res;
	}
	public static Matrix projection(Matrix G, Matrix A, List<Matrix> lst){
		return projection(G, A, lst, null);
	}

	public static List<Matrix> GramSchmidt(Matrix G, List<Matrix> A) {
		ArrayList<Matrix> result = new ArrayList<Matrix>();
		for (Matrix x : A) {
			result.add(Matrix.sub(x, projection(G, x, result)));
		}
		return result;
	}

	public static Matrix inverse(Matrix A) {
		ArrayList<PeulatDerug> peulot = new ArrayList<PeulatDerug>();
		Matrix X = A.rowReduce(peulot);// i assume this is I
		// apply the operations on I
		for (PeulatDerug p : peulot) {
			X = p.apply(X);
		}
		return X;
	}

	/* @pre - you are not stupid
	 * @yoav yus - sus
	 */
	public static Matrix[] QR(Matrix G, Matrix A) {
		// gram schmidt with saving
		Matrix Q = new Matrix(A.getRowDimension(),A.getColumnDimension(),Polynomial.ZERO);
		Matrix[] QRs = new Matrix[2];
		ArrayList<Matrix> result = new ArrayList<Matrix>();
		for (Matrix x : A.getColumns()) {
			ArrayList<Polynomial> Rs = new ArrayList<Polynomial>();
			result.add(Matrix.sub(x, projection(G, x, result, Rs)));
		}
		Q=fromColumns(result);
		QRs[0] = Q;
		QRs[1] = mul(inverse(Q), A);
		return QRs;
	}
}