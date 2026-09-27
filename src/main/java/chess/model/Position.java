package chess.model;

public final class Position {

    public static final char EMPTY = ' ';
    public static final int BOARD_SIZE = 8;

    private static final int FEN_FIELDS = 6;
    private static final int ENDGAME_MAX_PIECES = 10;
    private static final int OPENING_MAX_MOVE = 10;
    private static final String PIECE_LETTERS = "KQRBNPkqrbnp";
    private static final String RANK_SEPARATOR = "/";

    private final String fen;
    private final char[][] squares;
    private final boolean whiteToMove;
    private final int moveNumber;

    private Position(String fen, char[][] squares, boolean whiteToMove, int moveNumber) {
        this.fen = fen;
        this.squares = squares;
        this.whiteToMove = whiteToMove;
        this.moveNumber = moveNumber;
    }

    public static Position fromFen(String fen) {
        if (fen == null) {
            throw new IllegalArgumentException("fen is missing");
        }
        String[] fields = fen.trim().split("\\s+");
        if (fields.length != FEN_FIELDS) {
            throw new IllegalArgumentException("fen must have " + FEN_FIELDS + " fields: " + fen);
        }
        return new Position(fen.trim(), parseBoard(fields[0]), parseSide(fields[1]), parseMoveNumber(fields[5]));
    }

    public char pieceAt(int file, int rank) {
        return squares[BOARD_SIZE - rank][file];
    }

    public boolean whiteToMove() {
        return whiteToMove;
    }

    public int moveNumber() {
        return moveNumber;
    }

    public int pieceCount() {
        int count = 0;
        for (char[] row : squares) {
            for (char square : row) {
                if (square != EMPTY) {
                    count++;
                }
            }
        }
        return count;
    }

    public int count(char piece) {
        int count = 0;
        for (char[] row : squares) {
            for (char square : row) {
                if (square == piece) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean hasBothKings() {
        return count('K') == 1 && count('k') == 1;
    }

    public GamePhase phase() {
        if (pieceCount() <= ENDGAME_MAX_PIECES) {
            return GamePhase.ENDGAME;
        }
        if (moveNumber <= OPENING_MAX_MOVE) {
            return GamePhase.OPENING;
        }
        return GamePhase.MIDDLEGAME;
    }

    public String fen() {
        return fen;
    }

    @Override
    public String toString() {
        return fen;
    }

    private static char[][] parseBoard(String placement) {
        String[] ranks = placement.split(RANK_SEPARATOR);
        if (ranks.length != BOARD_SIZE) {
            throw new IllegalArgumentException("board must have " + BOARD_SIZE + " ranks: " + placement);
        }
        char[][] board = new char[BOARD_SIZE][];
        for (int i = 0; i < BOARD_SIZE; i++) {
            board[i] = parseRank(ranks[i]);
        }
        return board;
    }

    private static char[] parseRank(String rank) {
        char[] row = new char[BOARD_SIZE];
        int file = 0;
        for (char symbol : rank.toCharArray()) {
            if (symbol >= '1' && symbol <= '8' && file + (symbol - '0') <= BOARD_SIZE) {
                int gap = symbol - '0';
                for (int i = 0; i < gap; i++) {
                    row[file++] = EMPTY;
                }
            } else if (PIECE_LETTERS.indexOf(symbol) >= 0 && file < BOARD_SIZE) {
                row[file++] = symbol;
            } else {
                throw new IllegalArgumentException("bad rank: " + rank);
            }
        }
        if (file != BOARD_SIZE) {
            throw new IllegalArgumentException("rank must have " + BOARD_SIZE + " squares: " + rank);
        }
        return row;
    }

    private static boolean parseSide(String side) {
        if (side.equals("w")) {
            return true;
        }
        if (side.equals("b")) {
            return false;
        }
        throw new IllegalArgumentException("side to move must be w or b: " + side);
    }

    private static int parseMoveNumber(String field) {
        try {
            int number = Integer.parseInt(field);
            if (number < 1) {
                throw new IllegalArgumentException("move number must be positive: " + field);
            }
            return number;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("move number is not a number: " + field);
        }
    }
}
