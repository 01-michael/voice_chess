package Pieces;

import java.util.ArrayList;

import BoardComponents.Position;
import Information.Tag.Side;

public class Knight extends Piece {
    public Knight(Side side, Position start, String imageFileName) {
        super(side, start, imageFileName);
    }

    @Override
    public ArrayList<Position> getLegalMoves(Position[][] gameBoard) {
        ArrayList<Position> knightLegalMoves = new ArrayList<Position>();
        final int startX = this.getPosition().getPosY(); // swapping x with y
        final int startY = this.getPosition().getPosX(); // swapping y with x
        final int upDownTwo[] = {startY + 2, startY - 2};
        final int upDownOne[] = { startY + 1, startY - 1};
        final int leftRightTwo[] = { startX + 2, startX - 2};
        final int leftRightOne[] = {startX + 1, startX - 1};

         // check up down, left right
         for(int i = 0; i < 2; i++) {
             for(int j = 0; j < 2; j++) {
                if(positionInBounds(upDownTwo[i]) && positionInBounds(leftRightOne[j]) && basicLegalPosition(gameBoard, leftRightOne[j], upDownTwo[i]))
                    knightLegalMoves.add(gameBoard[leftRightOne[j]][upDownTwo[i]]);
                if(positionInBounds(leftRightTwo[i]) && positionInBounds(upDownOne[j]) && basicLegalPosition(gameBoard, leftRightTwo[i], upDownOne[j]))
                    knightLegalMoves.add(gameBoard[leftRightTwo[i]][upDownOne[j]]);
             }
         }

        return knightLegalMoves;
    }

    @Override
    public String name() {
        return "(N)";
    }
}