package vlab.server_java.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Random;

public class Matrix {
    private double[][] matrix;

    public Matrix() {
    }

    public Matrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public static Matrix randomWithSize(int size) {
        Random random = new Random();

        double[][] result = new double[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; j++) {
                double value = random.nextDouble(); //[0..1)
                result[i][j] = round(value, 2);
            }
        }

        return new Matrix(result);
    }

    public static double round(double value, int decimals) {
        if (decimals < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(decimals, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix matrix1 = (Matrix) o;
        return Arrays.deepEquals(matrix, matrix1.matrix);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(matrix);
    }
}
