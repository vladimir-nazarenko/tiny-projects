package com.nazarenko;

/**
 * Created by Vladimir Nazarenko on 7/30/16.
 */

/**
 * Matrix multiplication in divide and conquer style.
 */
public class RecursiveMultiplier extends Multiplier {

    public RecursiveMultiplier() {
        this(new StraightforwardMultiplier());
    }

    public RecursiveMultiplier(Multiplier multiplier) {
//        if (multiplier instanceof RecursiveMultiplier)
//            throw new IllegalArgumentException("Recursive initialization");
        this(multiplier, 16);
    }

    public RecursiveMultiplier(Multiplier multiplier, int baseDim) {
        base = multiplier;
        this.baseDim = baseDim;
    }

    private final Multiplier base;
    private final int baseDim;

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
    void multiply(int lUpperRow, int lLeftCol, int lLowerRow, int lRightCol, int rUpperRow, int rLeftCol, 
                  int rLowerRow, int rRightCol, int[][] left, int[][] right, int[][] result) {
        if (lLowerRow < lUpperRow || lRightCol < lLeftCol ||
                (rLowerRow < rUpperRow || rRightCol < rLeftCol))
            return;
        if ((lLowerRow - lUpperRow + lRightCol - lLeftCol + rRightCol - rLeftCol) <= baseDim * 3 - 3) {
            base.multiply(lUpperRow, lLeftCol, lLowerRow, lRightCol, rUpperRow, rLeftCol, rLowerRow, rRightCol,
                    left, right, result);
        }
        else {
            // left matrix rows etc.
            final int midLR = (lUpperRow + lLowerRow) / 2;
            final int midLC = (lLeftCol  + lRightCol) / 2;
            final int midRR = midLC;
            final int midRC = (rLeftCol  + rRightCol) / 2;
            // AE AF CF CE BG BH DH DG
            multiply(lUpperRow, lLeftCol, midLR    , midLC    , rUpperRow, rLeftCol, midRR    , midRC    , left, right, result);
            multiply(lUpperRow, lLeftCol, midLR    , midLC    , rUpperRow, midRC+1 , midRR    , rRightCol, left, right, result);
            multiply(midLR+1  , lLeftCol, lLowerRow, midLC    , rUpperRow, midRC+1 , midRR    , rRightCol, left, right, result);
            multiply(midLR+1  , lLeftCol, lLowerRow, midLC    , rUpperRow, rLeftCol, midRR    , midRC    , left, right, result);
            multiply(lUpperRow, midLC+1 , midLR    , lRightCol, midRR+1  , rLeftCol, rLowerRow, midRC    , left, right, result);
            multiply(lUpperRow, midLC+1 , midLR    , lRightCol, midRR+1  , midRC+1 , rLowerRow, rRightCol, left, right, result);
            multiply(midLR+1  , midLC+1 , lLowerRow, lRightCol, midRR+1  , midRC+1 , rLowerRow, rRightCol, left, right, result);
            multiply(midLR+1  , midLC+1 , lLowerRow, lRightCol, midRR+1  , rLeftCol, rLowerRow, midRC    , left, right, result);
        }
    }
}
