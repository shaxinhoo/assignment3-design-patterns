package chess.engine;

import chess.model.PieceValues;
import chess.model.Position;

public class PositionalEngine implements ChessEngine {

    private static final String NAME = "Positional";
    private static final int MAX_DEPTH = 8;
    private static final int CENTER_BONUS = 20;
    private static final int DEVELOPMENT_BONUS = 15;
    private static final int FIRST_CENTER_FILE = 3;
    private static final int LAST_CENTER_FILE = 4;
    private static final int FIRST_CENTER_RANK = 4;
    private static final int LAST_CENTER_RANK = 5;
    private static final int WHITE_BACK_RANK = 1;
    private static final int BLACK_BACK_RANK = 8;

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Evaluation evaluate(Position position, int depth) {
        if (!position.hasBothKings()) {
            throw new InvalidPositionException(InvalidPositionException.KINGS_MISSING);
        }
        if (depth > MAX_DEPTH) {
            throw new EngineTimeoutException("the engine ran out of time at depth " + depth);
        }
        int score = PieceValues.balance(position) + centerControl(position) + development(position);
        return new Evaluation(score, NAME);
    }

    private int centerControl(Position position) {
        int score = 0;
        for (int rank = FIRST_CENTER_RANK; rank <= LAST_CENTER_RANK; rank++) {
            for (int file = FIRST_CENTER_FILE; file <= LAST_CENTER_FILE; file++) {
                score += sideSign(position.pieceAt(file, rank)) * CENTER_BONUS;
            }
        }
        return score;
    }

    private int development(Position position) {
        int score = 0;
        for (int rank = 1; rank <= Position.BOARD_SIZE; rank++) {
            for (int file = 0; file < Position.BOARD_SIZE; file++) {
                char piece = position.pieceAt(file, rank);
                if (isMinorPiece(piece) && !onOwnBackRank(piece, rank)) {
                    score += sideSign(piece) * DEVELOPMENT_BONUS;
                }
            }
        }
        return score;
    }

    private boolean isMinorPiece(char piece) {
        char lower = Character.toLowerCase(piece);
        return lower == 'n' || lower == 'b';
    }

    private boolean onOwnBackRank(char piece, int rank) {
        return Character.isUpperCase(piece) ? rank == WHITE_BACK_RANK : rank == BLACK_BACK_RANK;
    }

    private int sideSign(char piece) {
        if (piece == Position.EMPTY) {
            return 0;
        }
        return Character.isUpperCase(piece) ? 1 : -1;
    }
}
