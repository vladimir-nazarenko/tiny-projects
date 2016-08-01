package com.nazarenko;

/**
 * Created by Vladimir Nazarenko on 7/29/16.
 */

/**
 * Interface for submatrix multiplication strategy.
 */
public abstract class Multiplier {
    /**
     * Strategy method. Facilitates the multiplication of two submatrices. Gets two submatrices in terms of
     * upper-left element and lower-right element and adds their product to the corresponding submatrix of the
     * matrix "result". We call matrices left and right, i.e. in the expression A*B A is the left and B is the right.
     * Hence prefix l is for left and b is for right.
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
     *                  of the result [(lUpperRow, rLeftCol), (lLowerRow, rRightCol)].
     */
    abstract void multiply(int lUpperRow, int lLeftCol, int lLowerRow, int lRightCol,
                     int rUpperRow, int rLeftCol, int rLowerRow, int rRightCol,
                     int[][] left, int[][] right, int[][] result);

    /**
     * Multiplies two matrices and returns the result.
     * @param left  left  matrix, (m x n).
     * @param right right matrix, (m x k).
     * @return the result of the multiplication (m x k).
     */
    public int[][] multiply(int[][] left, int[][] right) {
        int[][] result = new int[left.length][right[0].length];
        multiply(0, 0, left.length - 1, left[0].length - 1, 0, 0, right.length - 1, right[0].length - 1,
                left, right, result);
        return result;
    }
}
