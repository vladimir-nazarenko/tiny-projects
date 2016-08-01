package com.nazarenko;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Vladimir Nazarenko on 7/31/16.
 */
public class ParallelCPUMultiplier extends Multiplier {

    private Multiplier multiplier = new RowWiseMultiplier();
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
        int numOfProc = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numOfProc);
        List<Future> futures = new ArrayList<>(numOfProc);
        final int nRows = lLowerRow - lUpperRow + 1;
        final int nCols = rRightCol - rLeftCol + 1;
        final Map.Entry<Integer, Integer> gridDim = getDim(nRows, nCols, numOfProc);
        final int gridWidth  = gridDim.getKey();
        final int gridHeight = gridDim.getValue();

        int curRow  = lUpperRow;
        int curCol  = rLeftCol;
        final int rowStep = ceilDiv(nRows, gridHeight);
        final int colStep = ceilDiv(nCols, gridWidth);
        for (int i = 0; i < gridHeight; i++) {
            final int bottomRow = Integer.min(curRow + rowStep - 1, lLowerRow);
            curCol  = rLeftCol;
            for (int j = 0; j < gridWidth; j++) {
                final int rightCol = Integer.min(curCol + colStep - 1, rRightCol);
                MatrixWorker worker = new MatrixWorker(curRow, bottomRow, lLeftCol, lRightCol,
                        rUpperRow, rLowerRow, curCol, rightCol,
                        left, right, result, multiplier);
                futures.add(executor.submit(worker));
                curCol += colStep;
            }
            curRow += rowStep;
        }
        try {
            for (Future fut : futures)
                fut.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private int ceilDiv(int a, int b) {
        return (a + b - 1) / b;
    }

    private Map.Entry<Integer, Integer> getDim(int nRows, int nCols, int numOfProc) {
        int gridWidth  = 1;
        int gridHeight = 1;
        while (numOfProc > 1 && (nRows >= 2 || nCols >= 2)) {
            if (nRows >= 2) {
                gridHeight *= 2;
                nRows /= 2;
                numOfProc /= 2;
            }
            if (nCols >= 2 && numOfProc > 1) {
                gridWidth *= 2;
                nCols /= 2;
                numOfProc /= 2;
            }
        }
        return new AbstractMap.SimpleEntry<>(gridWidth, gridHeight);
    }

    private final static class MatrixWorker implements Runnable {

        public MatrixWorker(int lUpperRow, int lLeftCol, int lLowerRow, int lRightCol,
                            int rUpperRow, int rLeftCol, int rLowerRow, int rRightCol,
                            int[][] left, int[][] right, int[][] result, Multiplier multiplier) {
            this.lUpperRow = lUpperRow;
            this.lLowerRow = lLowerRow;
            this.lLeftCol = lLeftCol;
            this.lRightCol = lRightCol;
            this.rUpperRow = rUpperRow;
            this.rLowerRow = rLowerRow;
            this.rLeftCol = rLeftCol;
            this.rRightCol = rRightCol;
            this.left = left;
            this.right = right;
            this.result = result;
            this.multiplier = multiplier;
        }

        private int lUpperRow;
        private int lLowerRow;
        private int lLeftCol;
        private int lRightCol;
        private int rUpperRow;
        private int rLowerRow;
        private int rLeftCol;
        private int rRightCol;
        private int[][] left;
        private int[][] right;
        private int[][] result;
        Multiplier multiplier;



        @Override
        public void run() {
            multiplier.multiply(lUpperRow, lLowerRow, lLeftCol, lRightCol, rUpperRow, rLowerRow, rLeftCol, rRightCol, left, right, result);
        }
    }
}
