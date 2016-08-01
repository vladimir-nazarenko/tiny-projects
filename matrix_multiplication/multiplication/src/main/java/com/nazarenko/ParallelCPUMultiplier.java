package com.nazarenko;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Vladimir Nazarenko on 7/31/16.
 */
public class ParallelCPUMultiplier extends Multiplier {
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
    void multiply(int lUpperRow, int lLeftCol, int lLowerRow, int lRightCol, int rUpperRow, int rLeftCol, int rLowerRow, int rRightCol, int[][] left, int[][] right, int[][] result) {
        final int numOfProc = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numOfProc);

    }

//    private final static class MatrixWorker implements Runnable {
//        private final int startRow;
//        private final int endRow;
//        Multiplier multiplier;
//
//        public MatrixWorker() {
//
//        }
//
//        @Override
//        public void run() {
//            multiplier.multiply(startRow);
//        }
//    }
}
