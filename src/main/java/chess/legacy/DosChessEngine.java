package chess.legacy;

public class DosChessEngine {

    public static final int OK = 0;
    public static final int ERR_ILLEGAL_BOARD = 3;
    public static final int ERR_OUT_OF_TIME = 7;
    public static final int ERR_NO_KING = 9;
    public static final int NO_SCORE = -32000;
    public static final int MAX_PLIES = 12;

    private static final int CELLS = 64;
    private static final int ROW_LENGTH = 8;
    private static final int LAST_ROW = 7;
    private static final int KING_CENTER_BONUS = 10;
    private static final int MAX_CENTER_DISTANCE = 3;
    private static final int PAWN_VALUE = 100;
    private static final int MINOR_VALUE = 300;
    private static final int ROOK_VALUE = 500;
    private static final int QUEEN_VALUE = 900;

    private int lastError = OK;

    public int think(int plies, char[] board, boolean blackToMove) {
        lastError = check(plies, board);
        if (lastError != OK) {
            return NO_SCORE;
        }
        int whiteScore = material(board) + kingActivity(board);
        return blackToMove ? -whiteScore : whiteScore;
    }

    public int getLastError() {
        return lastError;
    }

    private int check(int plies, char[] board) {
        if (board == null || board.length != CELLS || pawnOnLastRow(board)) {
            return ERR_ILLEGAL_BOARD;
        }
        if (count(board, 'K') != 1 || count(board, 'k') != 1) {
            return ERR_NO_KING;
        }
        if (plies > MAX_PLIES) {
            return ERR_OUT_OF_TIME;
        }
        return OK;
    }

    private boolean pawnOnLastRow(char[] board) {
        for (int i = 0; i < CELLS; i++) {
            int row = i / ROW_LENGTH;
            boolean edge = row == 0 || row == LAST_ROW;
            if (edge && (board[i] == 'P' || board[i] == 'p')) {
                return true;
            }
        }
        return false;
    }

    private int count(char[] board, char piece) {
        int count = 0;
        for (char cell : board) {
            if (cell == piece) {
                count++;
            }
        }
        return count;
    }

    private int material(char[] board) {
        int score = 0;
        for (char cell : board) {
            int value = oldValue(cell);
            score += Character.isUpperCase(cell) ? value : -value;
        }
        return score;
    }

    private int oldValue(char cell) {
        switch (Character.toLowerCase(cell)) {
            case 'p':
                return PAWN_VALUE;
            case 'n':
            case 'b':
                return MINOR_VALUE;
            case 'r':
                return ROOK_VALUE;
            case 'q':
                return QUEEN_VALUE;
            default:
                return 0;
        }
    }

    private int kingActivity(char[] board) {
        int score = 0;
        for (int i = 0; i < CELLS; i++) {
            if (board[i] == 'K') {
                score += centerBonus(i);
            } else if (board[i] == 'k') {
                score -= centerBonus(i);
            }
        }
        return score;
    }

    private int centerBonus(int index) {
        int file = index % ROW_LENGTH;
        int row = index / ROW_LENGTH;
        int distance = Math.max(Math.abs(file * 2 - LAST_ROW), Math.abs(row * 2 - LAST_ROW)) / 2;
        return (MAX_CENTER_DISTANCE - distance) * KING_CENTER_BONUS;
    }
}
