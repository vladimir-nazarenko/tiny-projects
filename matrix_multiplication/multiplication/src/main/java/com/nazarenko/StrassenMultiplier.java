package com.nazarenko;

import java.util.stream.IntStream;

/**
 * Created by Vladimir Nazarenko on 7/30/16.
 */
public class StrassenMultiplier extends Multiplier {

    final Multiplier base = new RowWiseMultiplier();

    private int[][] sumSquare(int[][]... A) {
        final int numArrays = A.length;
        final int dim = A[0].length;
        int[][] sum = new int[dim][dim];
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                for (int k = 0; k < numArrays; k++)
                    sum[i][j] += A[k][i][j];
        return sum;
    }

    private int[][] subtractSquare(int[][] A1, int[][]... A) {
        final int numArrays = A.length;
        final int dim = A[0].length;
        int[][] sum = new int[dim][dim];
        for (int i = 0; i < dim; i++)
            System.arraycopy(A1[i], 0, sum[i], 0, dim);

        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                for (int k = 0; k < numArrays; k++)
                    sum[i][j] -= A[k][i][j];
        return sum;
    }

    private int[][] splitSquare(int[][] A, int row, int col) {
        final int n = A.length;
        final int rowOffset = (row - 1) * n / 2;
        final int colOffset = (col - 1) * n / 2;
        int[][] result = new int[n/2][n/2];
        for (int i = rowOffset, i1 = 0; i < n / 2 + rowOffset; i++, i1++)
            for (int j = colOffset, j1 = 0; j < n / 2 + colOffset; j++, j1++)
                result[i1][j1] = A[i][j];
        return result;
    }

    private void joinSquare(int[][] source, int[][] target, int row, int col) {
        final int n = source.length;
        final int rowOffset = (row - 1) * n;
        final int colOffset = (col - 1) * n;
        for (int i = rowOffset, i1 = 0; i1 < n; i++, i1++)
            for (int j = colOffset, j1 = 0; j1 < n; j++, j1++)
                target[i][j] = source[i1][j1];
    }

    private int[][] multiplyPowerOfTwo(int[][] left, int[][] right) {
        final int n = left.length;
//        if (n == 1)
//            return new int[][]{{left[0][0] * right[0][0]}};
        if (n <= 64)
            return base.multiply(left, right);

        int[][] A11 = splitSquare(left, 1, 1);
        int[][] A12 = splitSquare(left, 1, 2);
        int[][] A21 = splitSquare(left, 2, 1);
        int[][] A22 = splitSquare(left, 2, 2);

        int[][] B11 = splitSquare(right, 1, 1);
        int[][] B12 = splitSquare(right, 1, 2);
        int[][] B21 = splitSquare(right, 2, 1);
        int[][] B22 = splitSquare(right, 2, 2);

        int[][] P1 = multiplyPowerOfTwo(A11, subtractSquare(B12, B22));
        int[][] P2 = multiplyPowerOfTwo(sumSquare(A11, A12), B22);
        int[][] P3 = multiplyPowerOfTwo(sumSquare(A21, A22), B11);
        int[][] P4 = multiplyPowerOfTwo(A22, subtractSquare(B21, B11));
        int[][] P5 = multiplyPowerOfTwo(sumSquare(A11, A22), sumSquare(B11, B22));
        int[][] P6 = multiplyPowerOfTwo(subtractSquare(A12, A22), sumSquare(B21, B22));
        int[][] P7 = multiplyPowerOfTwo(subtractSquare(A11, A21), sumSquare(B11, B12));

        int[][] C11 = subtractSquare(sumSquare(P5, P4, P6), P2);
        int[][] C12 = sumSquare(P1, P2);
        int[][] C21 = sumSquare(P3, P4);
        int[][] C22 = subtractSquare(sumSquare(P1, P5), P3, P7);

        int[][] result = new int[n][n];
        joinSquare(C11, result, 1, 1);
        joinSquare(C12, result, 1, 2);
        joinSquare(C21, result, 2, 1);
        joinSquare(C22, result, 2, 2);
        return result;
    }

    /**
     * Strategy method. Facilitates the multiplication of two submatrices. Gets two submatrices in terms of
     * upper-left element and lower-right element and adds their product to the corresponding submatrix of the
     * matrix "result". We call matrices left and right, i.e. in the expression A*B A is the left and B is the right.
     * Hence prefix l is for left and b is for right.
     *
     * @param lUpperRow index of the first row    of the submatrix in the left  matrix.
     * @param lLeftCol  index of the first column of the submatrix in the left  matrix.
     * @param lLowerRow index of the last  row    of the submatrix in the left  matrix.
     * @param lRightCol index of the last  column of the submatrix in the left  matrix.
     * @param rUpperRow index of the first row    of the submatrix in the right matrix.
     * @param rLeftCol  index of the first column of the submatrix in the right matrix.
     * @param rLowerRow index of the last  row    of the submatrix in the right matrix.
     * @param rRightCol index of the last  column of the submatrix in the right matrix.
     * @param left      matrix, containing the left  submatrix.
     * @param right     matrix, containing the right submatrix.
     * @param result    matrix, where the result will be stored. The result will be added to the submatrix
     */
    @Override
    void multiply(int lUpperRow, int lLeftCol, int lLowerRow, int lRightCol, int rUpperRow, int rLeftCol, int rLowerRow,
                  int rRightCol, int[][] left, int[][] right, int[][] result) {
        int maxDim = IntStream.of(lLowerRow - lUpperRow + 1,
                lRightCol - lLeftCol + 1, rRightCol - rLeftCol + 1).max().getAsInt();
        int nearestPowerOf2 = 1;
        while (nearestPowerOf2  < maxDim) nearestPowerOf2 <<= 1;
        int[][] squareLeft   = new int[nearestPowerOf2][nearestPowerOf2];
        int[][] squareRight  = new int[nearestPowerOf2][nearestPowerOf2];

        for (int i = lUpperRow; i <= lLowerRow; i++)
            System.arraycopy(left[i], lLeftCol, squareLeft[i-lUpperRow], 0, lRightCol - lLeftCol + 1);

        for (int i = rUpperRow; i <= rLowerRow; i++)
            System.arraycopy(right[i], rLeftCol, squareRight[i-rUpperRow], 0, rRightCol - rLeftCol + 1);


        int[][] squareResult = multiplyPowerOfTwo(squareLeft, squareRight);

        for (int i = 0; i <= lLowerRow - lUpperRow; i++)
            System.arraycopy(squareResult[i], 0, result[i], 0, rRightCol - rLeftCol + 1);
    }
}
