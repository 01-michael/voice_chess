package Information;


public class Tag {
    // 1. keeps track current active side during game session
    // 2. keeps track of piece/player side
    public static enum Side { BLACK, WHITE, OVER }

    // size of image assets
    public static final int IMAGE_WIDTH = 75;
    public static final int IMAGE_HEIGHT = 75;

    // stores word representation of positions
    public static final String[] xcoords = {"alpha", "bravo", "charlie", "delta", "echo", "foxtrot", "golf", "hotel"};
    public static final String[] ycoords = {"one", "two", "three", "four", "five", "six", "seven", "eight"};

    // lazy chess icon
    public static final String LAZY_ICON = "assets/lazy_chess_icon.jpg";
    public static final String TITLE = "Voice Chess";

    // white piece images
    public static final String WHITE_KING = "assets/white_king.png";
    public static final String WHITE_QUEEN = "assets/white_queen.png";
    public static final String WHITE_KNIGHT = "assets/white_knight.png";
    public static final String WHITE_ROOK = "assets/white_rook.png";
    public static final String WHITE_BISHOP = "assets/white_bishop.png";
    public static final String WHITE_PAWN = "assets/white_pawn.png";

    // black piece images
    public static final String BLACK_KING = "assets/black_king.png";
    public static final String BLACK_QUEEN = "assets/black_queen.png";
    public static final String BLACK_KNIGHT = "assets/black_knight.png";
    public static final String BLACK_ROOK = "assets/black_rook.png";
    public static final String BLACK_BISHOP = "assets/black_bishop.png";
    public static final String BLACK_PAWN = "assets/black_pawn.png";

    // const values for board size
    public static final int SIZE_MAX = 8;
    public static final int SIZE_MIN = 0;
}
