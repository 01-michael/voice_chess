package Pieces;

import java.util.ArrayList;

import BoardComponents.Position;
import Information.Tag.Side;

public class Rook extends Piece {
    public Rook(Side side, Position start, String imageFileName) {
        super(side, start, imageFileName);
    }

    @Override
    public ArrayList<Position> getLegalMoves(Position[][] gameBoard) {
        return getLegalOrthogonalPositions(gameBoard, this.getPosition());
    }

    @Override
    public String name() {
        return "(R)";
    }
}