package BoardComponents;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import GUI.GameGUI;
import Information.Tag;
import Information.Tag.Side;
import Pieces.Bishop;
import Pieces.King;
import Pieces.Knight;
import Pieces.Pawn;
import Pieces.Piece;
import Pieces.Queen;
import Pieces.Rook;

public class Board extends JPanel implements MouseListener {
    // static private data members shared among all Positions
    private static final Dimension BOARD_DIMENSION = new Dimension((Tag.IMAGE_WIDTH + 10) * Tag.SIZE_MAX, (Tag.IMAGE_HEIGHT + 10) * Tag.SIZE_MAX);
    private static final boolean SET_SHADE_FALSE = false;
    private static final boolean SET_SHADE_TRUE = true;

    // private data members
    private Side turn;
    private int selectedX;
    private int selectedY;
    private GameGUI gameGUI;
    private boolean playerOne;
    private Piece selectedPiece;
    private String blackPlayerName;
    private String whitePlayerName;
    private Position[][] gameBoard;
    public ArrayList<Position> selectedMovablePositions;

    // constructor
    public Board(GameGUI gui, String blackPlayerName, String whitePlayerName) {
        this.setGameGUI(gui);
        this.blackPlayerName = blackPlayerName.isEmpty() ? "Black" : blackPlayerName;
        this.whitePlayerName = whitePlayerName.isEmpty() ? "White" : whitePlayerName;
        this.setGameBoard(new Position[Tag.SIZE_MAX][Tag.SIZE_MAX]);
        this.setLayout(new GridLayout(Tag.SIZE_MAX, Tag.SIZE_MAX, 0, 0));
        this.addMouseListener(this);
        this.createNewBoardPositions();
        this.initializePiecesToBoard();
        this.setPanelDimensions(BOARD_DIMENSION);
        this.setTurn(Side.WHITE);
    }

    // set board dimensions
    private void setPanelDimensions(Dimension size){
        this.setPreferredSize(size);
        this.setMaximumSize(size);
        this.setMinimumSize(size);
        this.setSize(size);
    }

    /***
     * creation of the board results in x and  y coordinates being fliped,
     * compensated in getting legal moves by altering x and y for y and x 
     */
    private void createNewBoardPositions() {
        for(int i = 0; i < Tag.SIZE_MAX; i++)
            for(int j = 0; j < Tag.SIZE_MAX; j++)
                if(((i % 2) == 0 && (j % 2) == 0) || ((i % 2) == 1 && (j % 2) == 1)) {
                    this.gameBoard[i][j] = new Position(j, i, SET_SHADE_FALSE);
                    this.add(gameBoard[i][j]);
                }
                else {
                    this.gameBoard[i][j] = new Position(j, i, SET_SHADE_TRUE);
                    this.add(gameBoard[i][j]);
                }
    }

    // initializes new pieces to board
    private void initializePiecesToBoard() {
        // generate rook
        gameBoard[0][0].setPiece(new Rook(Tag.Side.BLACK, gameBoard[0][0], Tag.BLACK_ROOK));
        gameBoard[0][7].setPiece(new Rook(Tag.Side.BLACK, gameBoard[0][7], Tag.BLACK_ROOK));
        gameBoard[7][0].setPiece(new Rook(Tag.Side.WHITE, gameBoard[7][0], Tag.WHITE_ROOK));
        gameBoard[7][7].setPiece(new Rook(Tag.Side.WHITE, gameBoard[7][7], Tag.WHITE_ROOK));
        
        // generate knight
        gameBoard[0][1].setPiece(new Knight(Tag.Side.BLACK, gameBoard[0][1], Tag.BLACK_KNIGHT));
        gameBoard[0][6].setPiece(new Knight(Tag.Side.BLACK, gameBoard[0][6], Tag.BLACK_KNIGHT));
        gameBoard[7][1].setPiece(new Knight(Tag.Side.WHITE, gameBoard[7][1], Tag.WHITE_KNIGHT));
        gameBoard[7][6].setPiece(new Knight(Tag.Side.WHITE, gameBoard[7][6], Tag.WHITE_KNIGHT));
        
        // generate bishop
        gameBoard[0][2].setPiece(new Bishop(Tag.Side.BLACK, gameBoard[0][2], Tag.BLACK_BISHOP));
        gameBoard[0][5].setPiece(new Bishop(Tag.Side.BLACK, gameBoard[0][5], Tag.BLACK_BISHOP));
        gameBoard[7][2].setPiece(new Bishop(Tag.Side.WHITE, gameBoard[7][2], Tag.WHITE_BISHOP));
        gameBoard[7][5].setPiece(new Bishop(Tag.Side.WHITE, gameBoard[7][5], Tag.WHITE_BISHOP));
        
        // generate queen
        gameBoard[0][3].setPiece(new Queen(Tag.Side.BLACK, gameBoard[0][3], Tag.BLACK_QUEEN));
        gameBoard[7][3].setPiece(new Queen(Tag.Side.WHITE, gameBoard[7][3], Tag.WHITE_QUEEN));
        
        // generate king
        gameBoard[0][4].setPiece(new King(Tag.Side.BLACK, gameBoard[0][4], Tag.BLACK_KING));
        gameBoard[7][4].setPiece(new King(Tag.Side.WHITE, gameBoard[7][4], Tag.WHITE_KING));
        
        // generate Pawn
        for(int i = 0; i < 8; i++) {
            gameBoard[1][i].setPiece(new Pawn(Tag.Side.BLACK, gameBoard[1][i], Tag.BLACK_PAWN));
            gameBoard[6][i].setPiece(new Pawn(Tag.Side.WHITE, gameBoard[6][i], Tag.WHITE_PAWN));
        }
    }

    // setter
    public void kingWasTaken() { turn = Side.OVER; }
    public void setTurn(Side side) { this.turn = side; }
    public void setGameGUI(GameGUI gui) { this.gameGUI = gui; }
    public void setSelectedX(int selected) { this.selectedX = selected; }
    public void setSelectedY(int selected) { this.selectedY = selected; }
    public void setGameBoard(Position[][] board) { this.gameBoard = board; }
    public void setSelectedPiece(Piece selected) { this.selectedPiece = selected; }
    public void nextTurn() { turn = (this.turn == Side.BLACK) ? Side.WHITE : Side.BLACK; }
    public void setSelectedMovablePositions(Piece piece) { this.selectedMovablePositions = piece.getLegalMoves(this.gameBoard); }

    public void determineNextOrOver(boolean kingTaken) {         
        if(kingTaken) kingWasTaken();
        else nextTurn();

        if(this.getTurn() == Tag.Side.OVER) {
            JOptionPane.showMessageDialog(this,
            (playerOne == true ? whitePlayerName : blackPlayerName) + " won!"+
            "\nThanks for playing voice chess!" +
            "\nTo start a new game, go to the main menu",
            "Game Over!", JOptionPane.OK_OPTION);
        }
    }

    // getter
    public Side getTurn() { return this.turn; }
    public GameGUI getGameGUI() { return this.gameGUI; }
    public int getSelectedX() { return this.selectedX; }
    public int getSelectedY() { return this.selectedY; }
    public Position[][] getGameBoard() { return this.gameBoard; }
    public Piece getSelectedPiece() { return this.selectedPiece; }
    public ArrayList<Position> getMovablePositions() { return this.selectedMovablePositions; }

    // Method to draw board to screen
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int i = 0; i < Tag.SIZE_MAX; i++) 
            for(int j = 0; j < Tag.SIZE_MAX; j++) 
                this.gameBoard[j][i].paintComponent(g);

        if(selectedPiece != null && selectedPiece.getSide() == turn)
            g.drawImage(selectedPiece.getImage(), selectedX, selectedY, null);
    }

    /**
     * shades current array of positions
     * @param positions --> positions to shade
     * @param shade --> boolean to determine if positions are shaded
     */
    private void shadePositions(ArrayList<Position> positions, boolean shade) {
        for(int i = 0; i < positions.size(); i++)
            positions.get(i).setHighlight(shade);
        repaint();
    }

    // resets positions to original shade and clears current piece selection
    private void deselectPiece() {
        if(selectedPiece != null) {
            shadePositions(selectedMovablePositions, SET_SHADE_FALSE);
            selectedPiece = null;
            repaint();
        }
    }

    /**
     * sets selected piece to current piece at position
     * @param position --> position to select piece at
     */
    private void selectPieceAt(Position position) {
        if(!position.isFree() && position.getPiece().getSide() == turn) {
            selectedPiece = position.getPiece();
            setSelectedMovablePositions(selectedPiece);
            shadePositions(selectedMovablePositions, SET_SHADE_TRUE);
        }
        else deselectPiece();
    }

    /**
     * moves selected piece to position
     * @param position --> position to move selected piece to
     */
    private void movePieceTo(Position position) {
        if(selectedMovablePositions.contains(position) && (position.isFree() || position.getPiece().getSide() != turn)) {
            boolean kingTaken = !position.isFree() && position.getPiece().name().equals("(K)");
            if(kingTaken) playerOne = position.getPiece().getSide() == Tag.Side.BLACK;
            selectedPiece.move(position);
            deselectPiece();
            determineNextOrOver(kingTaken);
        }
        else deselectPiece();
    }

    /**
     * Method selects piece at the current position if no piece has been selected 
     * or if the piece at the current position is the same color. Or it moves the 
     * selected piece to position. Method repaints after processing selection or move.
     * @param position --> position to select piece at and execute move on
     */
    private void selectMoveRepaintAt(Position position) {
        if(selectedPiece == null)
            selectPieceAt(position);
        else if(selectedPiece != null && !position.isFree() && position.getPiece().getSide() == turn) {
            deselectPiece();
            selectedMovablePositions.clear();
            selectPieceAt(position);
        }
        else movePieceTo(position);
        repaint();
    }
    
    /***
     * controlling the game via mouse
     * left-click to select piece and move
     * right-click to deselect
     */
    @Override
    public void mousePressed(MouseEvent e) {
        Position clickedPosition = (Position) this.getComponentAt(new Point(e.getX(), e.getY()));
        if(e.getButton() == MouseEvent.BUTTON1)
            selectMoveRepaintAt(clickedPosition);
        else 
            deselectPiece();
    }

    /**
     * Saying clear         --> clears current piece selection
     * Saying <unk>         --> recognizer returning unknown, speech not recognized
     * Saying NATO + number --> recognizer picks up valid command
     * @param speechReceived --> string object that represents the current parsed speech
     */
    public void speechCalled(String speechReceived) {
        // say clear to unselect piece, clear because sphinx 4 can't understand unselect
        if(speechReceived.equals("clear")) {
            deselectPiece();
            return;
    	}

        // <unk> means recognizer did not understand speech
    	if(speechReceived.equals("<unk>")) {
    		System.out.println("I did not understand what you said"); 
            return;
    	}

    	String[] coordinates = speechReceived.split(" ");
        if(coordinates.length == 1) {
            // rarely passes in single word that is not unk, should only be passing in two words, prevents
            // out of bounds error by accessing coordinates[1] below
            System.out.println("I did not understand what you said");
            return;
        }

        // valid command processing 
        int x = 0, y = 0;
        for(int i = 0; i < 8; i++) {
            if(Tag.xcoords[i].equals(coordinates[0])) x = i;
            if(Tag.ycoords[i].equals(coordinates[1])) y = i;
        }

        // 7 - y to account for the board being reversed
        Position spokenPosition = gameBoard[7 - y][x]; // board display is flipped on x and y
        selectMoveRepaintAt(spokenPosition);
    }

    /**
     * since the board implements MouseListener, the following methods have to be overridden. 
     * currently left empty as they are not needed
     */
    @Override
    public void mouseClicked(MouseEvent e) { }
    
    @Override
    public void mouseReleased(MouseEvent e) { }
    
    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }   
}
