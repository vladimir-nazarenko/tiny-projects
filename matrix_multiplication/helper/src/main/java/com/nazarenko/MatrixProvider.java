package com.nazarenko;

import java.util.Random;

/**
 * Created by Vladimir Nazarenko on 7/29/16.
 */
public final class MatrixProvider {
    private int[][] left;
    private int[][] right;

    public MatrixProvider(int m, int n, int k) {
        Random rnd = new Random();
        left = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                left[i][j] = rnd.nextInt(100);
            }
        }

        right = new int[n][k];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                right[i][j] = rnd.nextInt(100);
            }
        }
    }

    public int[][] getLeftMatrix() {
        return left;
    }

    public int[][] getRightMatrix() {
        return right;
    }
}
