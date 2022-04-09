package BoardComponents;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import Pieces.Piece;

/**
 * Position class inherits JComponents
 * important parameters to positions 
 * @parameter1 int ( x, y )     --> 2D coordinate of position
 * @parameter2 Piece piece      --> current occupying piece
 * @parameter3 bool (shade)     --> represents if the position is lighter or darker shade
 * @parameter4 bool (highlight) --> represents if the position is highlighted
 */
public class Position extends JComponent {
     // static const private data members shared among all Positions
    private static final Color GEYSER = new Color(212, 219, 225);
    private static final Color SHADE_RED =  new Color(252, 0, 0);
    private static final Color ECRU_WHITE = new Color(251, 252, 247);
    private static final Color SHUTTLE_GRAY = new Color(89, 96, 112);
    private static final Color ATHS_SPECIAL = new Color(234, 240, 216);

    // private data members
    private int posX;
    private int posY;
    private Piece piece;
    private boolean highlight;
    private boolean lighterShade;
    private boolean displayPiece;

    // constructor
    public Position(int x, int y, boolean light) {
        setPosX(x);
        setPosY(y);
        setShade(light);
        setHighlight(false);
        setDisplayPiece(false);
        this.setBorder(BorderFactory.createEmptyBorder());
    }

    // getters
    public int getPosX() { return this.posX; }
    public int getPosY() { return this.posY; }
    public Piece getPiece() { return this.piece; }
    public boolean isFree() { return (this.piece == null); }
    public boolean getDisplayPiece() { return this.displayPiece; }
    public boolean isHighlighted() { return this.highlight == true; }
    public boolean isLighterShade() { return this.lighterShade == true; }

    // setters
    public void setPosX(int x) { this.posX = x; }
    public void setPosY(int y) { this.posY = y; }
    public void setShade(Boolean shade) { this.lighterShade = shade; }
    public void setHighlight(Boolean highlighted) { this.highlight = highlighted; }
    public void setDisplayPiece(boolean display) { this.displayPiece = display; }

    public void setPiece(Piece piece) {
        this.piece = piece;
        setDisplayPiece(true);
        piece.setPosition(this);
    }

    /**
     * Method to remove piece at this position
     * @return the piece that was removed
     */
    public Piece removePiece() {
        Piece temp = piece;
        piece = null;
        setDisplayPiece(false);
        return temp;
    }

    /**
     * Method to draw position and the 
     * current piece at the position to screen
     */
     public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw light or dark position && highlight position border
        g.setColor(this.lighterShade ? (highlight ? ECRU_WHITE : ATHS_SPECIAL) : (highlight ? GEYSER : SHUTTLE_GRAY));
        this.setBorder(highlight ? BorderFactory.createLineBorder(SHADE_RED, 2) : BorderFactory.createEmptyBorder());

        // display piece if it is at current position
        g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        if(this.piece != null && displayPiece) piece.draw(g);
    }
}