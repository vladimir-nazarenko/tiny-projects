package com.nazarenko;

import org.junit.Test;

import java.util.AbstractMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by Vladimir Nazarenko on 8/1/16.
 */
public abstract class MultiplierTest{

    private final static int squareSize = 300;
    // # rows int the left matrix
    private final static int M = 123;
    // # columns in the left matrix (# rows in the right)
    private final static int N = 239;
    // # columns in the right matrix
    private final static int K = 367;

    private enum MatrixSetup {
        SQUARE(squareSize, squareSize, squareSize),
        ARBITRARY(M, N, K),
        TWOxTWO(2, 2, 2),
        ONExONE(1, 1, 1),
        ROWxCOL(1, squareSize, 1);


        private final int[][] left;
        private final int[][] right;
        private final int[][] reference;
        private final int m;
        private final int n;
        private final int k;

        MatrixSetup(int m, int n, int k) {
            final MatrixProvider provider = new MatrixProvider(m, n, k);
            left = provider.getLeftMatrix();
            right = provider.getRightMatrix();
            reference = new int[m][k];
            for (int i = 0; i < m; i++)
                for (int j = 0; j < k; j++)
                    for (int l = 0; l < n; l++)
                        reference[i][j] += left[i][l] * right[l][j];
            this.m = m;
            this.n = n;
            this.k = k;
        }

        public int[][] getLeft() {
            return left;
        }
        public int[][] getRight() {
            return right;
        }
        public int[][] getReference() {
            return reference;
        }
        public int getM() {
            return m;
        }
        public int getN() {
            return n;
        }

        public int getK() {
            return k;
        }
    }

    protected abstract Multiplier getMultiplier();

    private static class MismatchInfo {
        final int row;
        final int col;
        final boolean mismatched;
        MismatchInfo() { row = -1; col = -1; mismatched = false; }
        MismatchInfo(int row, int col) { this.row = row; this.col = col; mismatched = true; }
        public boolean equals(Object other) {
            if (!(other instanceof MismatchInfo))
                return false;
            else return ((MismatchInfo) other).mismatched == mismatched;
        }
        public String toString() {
            return String.format("Row: %d; Col: %d; Mismatched: %s", row, col, mismatched ? "true" : "false");
        }
    }

    private MismatchInfo compare(int[][] reference, int[][] actual) {
        for (int i = 0; i < reference.length; i++)
            for (int j = 0; j < reference[0].length; j++)
                if (reference[i][j] != actual[i][j])
                    return new MismatchInfo(i, j);
        return new MismatchInfo();
    }

    private void performStandartTest(MatrixSetup setup) {
        final int[][] actual = getMultiplier().multiply(setup.getLeft(), setup.getRight());
        final int[][] reference = setup.getReference();
        assertEquals("Width mismatch" , actual.length   , reference.length   );
        assertEquals("Height mismatch", actual[0].length, reference[0].length);
        MismatchInfo mismatchInfo = compare(reference, actual);
        final MismatchInfo expected = new MismatchInfo();
        assertEquals(
                String.format("%d expected at (%d, %d), found %d",
                        mismatchInfo.mismatched ? reference[mismatchInfo.row][mismatchInfo.col] : -1,
                        mismatchInfo.row, mismatchInfo.col,
                        mismatchInfo.mismatched ? actual[mismatchInfo.row][mismatchInfo.col] : -1),
                expected, mismatchInfo);
    }

    @Test
    public void multiplySquareReturnsCorrectProduct() {
        performStandartTest(MatrixSetup.SQUARE);
    }

    @Test
    public void multiplyArbitraryReturnsCorrectProduct() {
        performStandartTest(MatrixSetup.ARBITRARY);
    }

    @Test
    public void multiply2by2ReturnsCorrectProduct() {
        performStandartTest(MatrixSetup.TWOxTWO);
    }

    @Test
    public void multiply1by1ReturnsCorrectProduct() {
        performStandartTest(MatrixSetup.ONExONE);
    }

    @Test
    public void multiplyRowByColumnReturnsCorrectProduct() {
        performStandartTest(MatrixSetup.ROWxCOL);
    }

    @Test
    public void multiplySubmatrixReturnsCorrectProduct() {
//        final int[][] actual = getMultiplier().multiply(setup.getLeft(), setup.getRight());
//        final int[][] reference = setup.getReference();
//        assertEquals("Width mismatch" , actual.length   , reference.length   );
//        assertEquals("Height mismatch", actual[0].length, reference[0].length);
//        Map.Entry<Integer, Integer> mismatchIndex = compare(reference, actual);
//        final AbstractMap.SimpleEntry<Integer, Integer> expected = new AbstractMap.SimpleEntry<>(-1, -1);
//        assertEquals(
//                String.format("%d expected at (%d, %d), found %d",
//                        reference[mismatchIndex.getKey()][mismatchIndex.getValue()],
//                        mismatchIndex.getKey(), mismatchIndex.getValue(),
//                        actual[mismatchIndex.getKey()][mismatchIndex.getValue()])
//                expected, mismatchIndex);
    }
}
