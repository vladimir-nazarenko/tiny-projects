package com.nazarenko;

/**
 * Created by Vladimir Nazarenko on 7/30/16.
 */
public final class TestSetup {
    // # rows int the left matrix
    private final int m = 123;
    // # columns in the left matrix (# rows in the right)
    private final int n = 239;
    // # columns in the right matrix
    private final int k = 367;
    // Contains two matrices: m x n and n x k
    private MatrixProvider provider;
    // Correct result of multiplication matrix
    private int[][] reference;

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }

    public int getK() {
        return k;
    }

    public MatrixProvider getProvider() {
        return provider;
    }

    public int[][] getReference() {
        return reference;
    }

    public void initMatrices() {
        provider = new MatrixProvider(m, n, k);
        // declare as a constant to help compiler optimizing code
        final MatrixProvider prov = provider;
        reference = new int[m][k];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < k; j++)
                for (int l = 0; l < n; l++)
                    reference[i][j] += prov.getLeftMatrix()[i][l] * prov.getRightMatrix()[l][j];
    }

    public void invalidateMatrices() {
        provider = null;
        reference = null;
    }
}
