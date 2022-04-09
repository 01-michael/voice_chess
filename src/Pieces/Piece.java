package Pieces;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import BoardComponents.Position;
import Information.Tag;
import Information.Tag.Side;

public abstract class Piece {
    // private data members all pieces have
    private Side side;
    private Position position;
    private BufferedImage image;

    // constructor
    public Piece(Side side, Position start, String imageFileName) {
        setSide(side);
        setPosition(start);
        setImage(imageFileName);
    }

    // setters
    public void setSide(Side side) { this.side = side; }
    public void setPosition(Position position) { this.position = position; }
    
    public void setImage(String imageFileName) { 
        if (image == null)
            try { 
                image = ImageIO.read(new File(imageFileName));
            } 
            catch (IOException e) { 
                System.out.println("Error: unable to find " + imageFileName);
            }
    }

    // getters
    public Side getSide() { return this.side; }
    public Image getImage() { return this.image; }
    public Position getPosition() { return this.position; }
    public void draw(Graphics g) { g.drawImage(this.getImage(), this.getPosition().getPosX(), this.getPosition().getPosY(), null); }

    // method handling move, returns true if piece moved and false otherwise
    public boolean move(Position desPosition) {
        Piece desPiece = desPosition.getPiece();

        if(desPiece != null)
            if(desPiece.getSide() == this.side) return false;
            else {
                desPiece = null;
                desPosition.setPiece(this.position.removePiece());
            }
        else desPosition.setPiece(this.position.removePiece());
        return true;
    }


    //! start: methods handling legal check
    public boolean positionInBounds(int value) {
        return (value >= Tag.SIZE_MIN && value < Tag.SIZE_MAX);
    }

    public boolean basicLegalPosition(Position[][] gameBoard, int x, int y) {
        return ((positionInBounds(x) && positionInBounds(y)) && (gameBoard[x][y].isFree() || gameBoard[x][y].getPiece().getSide() != this.getSide()));
    }

    public boolean complexLegalPosition(Position[][] gameBoard, int x, int y) {
        return (!gameBoard[x][y].isFree() && gameBoard[x][y].getPiece().getSide() != this.getSide());
    }

    private void addPositionToList(ArrayList<Position> list, Position position) {
        if (position.isFree() || position.getPiece().getSide() != this.getSide())
            list.add(position);
    }
    //! end: methods handling legal check

    /***
     * this method gets all legal position orthogonal from start position
     * @param gameBoard - board to check
     * @param start - starting position to get legal moves from
     * @return all legal positions north, south, east, and west from start
     */
    public ArrayList<Position> getLegalOrthogonalPositions(Position[][] gameBoard, Position start) {
        ArrayList<Position> linearPositions = new ArrayList<Position>();
        int startX = start.getPosY(); // swapping x with y
        int startY = start.getPosX(); // swapping y with x
        
        // check north
        for(int i = startX - 1; i >= Tag.SIZE_MIN; i--) {
            addPositionToList(linearPositions, gameBoard[i][startY]);
            if (!gameBoard[i][startY].isFree()) break;
        }
        // check south
        for(int i = startX + 1; i < Tag.SIZE_MAX; i++) {
            addPositionToList(linearPositions, gameBoard[i][startY]);
            if (!gameBoard[i][startY].isFree()) break;
        }
        // check east
        for(int i = startY + 1; i < Tag.SIZE_MAX; i++) {
            addPositionToList(linearPositions, gameBoard[startX][i]);
            if (!gameBoard[startX][i].isFree()) break;
        }
         // check west
         for(int i = startY - 1; i >= Tag.SIZE_MIN; i--) {
            addPositionToList(linearPositions, gameBoard[startX][i]);
            if (!gameBoard[startX][i].isFree()) break;
        }
        return linearPositions;
    }

   /***
     * Method that gets all legal diagonal positions from start position
     * @param gameBoard - board to check
     * @param start - starting position to get legal moves from
     * @return all legal positions north east, north west, south east, and south west from start
     */
    public ArrayList<Position> getLegalDiagonalPositions(Position[][] gameBoard, Position start) {
        ArrayList<Position> diagonalPositions = new ArrayList<Position>();
        int startX = start.getPosY(); // swapping x with y
        int startY = start.getPosX(); // swapping y with x
        int x = startX + 1;
        int y = startY + 1;

        // check north east
        while (x < Tag.SIZE_MAX && y < Tag.SIZE_MAX) {
            addPositionToList(diagonalPositions, gameBoard[x][y]);
            if (!gameBoard[x++][y++].isFree()) break;
        }
        // check south east
        x = startX - 1;
        y = startY - 1;
        while (x >= Tag.SIZE_MIN && y >= Tag.SIZE_MIN) {
            addPositionToList(diagonalPositions, gameBoard[x][y]);
            if (!gameBoard[x--][y--].isFree()) break;
        }
        // check north west
        x = startX - 1;
        y = startY + 1;
        while (x >= Tag.SIZE_MIN && y < Tag.SIZE_MAX) {
            addPositionToList(diagonalPositions, gameBoard[x][y]);
            if (!gameBoard[x--][y++].isFree()) break;
        }
        // check south west
        x = startX + 1;
        y = startY - 1;
        while (x < Tag.SIZE_MAX && y >= Tag.SIZE_MIN) {
            addPositionToList(diagonalPositions, gameBoard[x][y]);
            if (!gameBoard[x++][y--].isFree()) break;
        }
        return diagonalPositions;
    }

    /**
     * abstract methods to return all legal moves from current position of piece
     * @param gameBoard - board to get legal moves for
     * @return - all legal moves from current position on board
     */
    public abstract ArrayList<Position> getLegalMoves(Position[][] gameBoard);
    
    /**
     * General method to get name of piece
     * @return - string representation of piece in the formate (<First Letter of Piece>)
     * Since knight and king both start with K, king will return (K) and knight will return (N)
     */
    public String name() {
        return "(_)";
    }
}