package chess.model;

public final class PieceValues {

    public static final int PAWN = 100;
    public static final int KNIGHT = 320;
    public static final int BISHOP = 330;
    public static final int ROOK = 500;
    public static final int QUEEN = 900;

    private PieceValues() {
    }

    public static int of(char piece) {
        switch (Character.toLowerCase(piece)) {
            case 'p':
                return PAWN;
            case 'n':
                return KNIGHT;
            case 'b':
                return BISHOP;
            case 'r':
                return ROOK;
            case 'q':
                return QUEEN;
            default:
                return 0;
        }
    }

    public static int signed(char piece) {
        return Character.isUpperCase(piece) ? of(piece) : -of(piece);
    }

    public static int balance(Position position) {
        int balance = 0;
        for (int rank = 1; rank <= Position.BOARD_SIZE; rank++) {
            for (int file = 0; file < Position.BOARD_SIZE; file++) {
                balance += signed(position.pieceAt(file, rank));
            }
        }
        return balance;
    }
}
