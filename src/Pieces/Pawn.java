package Pieces;

import java.util.ArrayList;

import BoardComponents.Position;
import Information.Tag;
import Information.Tag.Side;

public class Pawn extends Piece {
    private int up;
    private Position firsPosition;
    
    public Pawn(Side side, Position start, String imageFileName) {
        super(side, start, imageFileName);
        this.firsPosition = start;
        if (this.getSide() == Tag.Side.BLACK) this.up = 1;
        else this.up = -1;
    }

    private boolean isFirstMove() { return this.getPosition() == firsPosition; }

    @Override
    public ArrayList<Position> getLegalMoves(Position[][] gameBoard) {
        ArrayList<Position> pawnLegalMoves = new ArrayList<Position>();
        final int startX = this.getPosition().getPosY(); // swapping x with y
        final int startY = this.getPosition().getPosX(); // swapping y with x
        final int moveYPos = (startY + 1 * this.up);
        final int moveYNeg = (startY - 1 * this.up);
        final int moveX = (startX + 1 * this.up);

        // first move condition
        if (isFirstMove())
            for(int i = 1; i <= 2; i++ )
                if (gameBoard[startX  + i * this.up][startY].isFree())
                    pawnLegalMoves.add(gameBoard[startX  + i * this.up][startY]);
                else break;

        // check one spot in front, left, and right
        if (positionInBounds(moveX)) {
            if (gameBoard[moveX][startY].isFree())
                pawnLegalMoves.add(gameBoard[moveX][startY]);
            if (positionInBounds(moveYNeg) && complexLegalPosition(gameBoard, moveX, moveYNeg))
                pawnLegalMoves.add(gameBoard[moveX][moveYNeg]);
            if (positionInBounds(moveYPos) && complexLegalPosition(gameBoard, moveX, moveYPos))
                pawnLegalMoves.add(gameBoard[moveX][moveYPos]);
        }
        return pawnLegalMoves;
    }

    @Override
    public String name() {
        return "(P)";
    }
}