package chess.adapter;

import chess.engine.ChessEngine;
import chess.engine.EngineException;
import chess.engine.EngineTimeoutException;
import chess.engine.Evaluation;
import chess.engine.InvalidPositionException;
import chess.legacy.DosChessEngine;
import chess.model.Position;

import java.util.Objects;

public class DosEngineAdapter implements ChessEngine {

    private static final String NAME = "DosChess (legacy)";
    private static final int PLIES_PER_MOVE = 2;
    private static final char LEGACY_EMPTY = '.';

    private final DosChessEngine legacy;

    public DosEngineAdapter(DosChessEngine legacy) {
        this.legacy = Objects.requireNonNull(legacy, "legacy");
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Evaluation evaluate(Position position, int depth) {
        boolean blackToMove = !position.whiteToMove();
        int score = legacy.think(depth * PLIES_PER_MOVE, toLegacyBoard(position), blackToMove);
        int error = legacy.getLastError();
        if (score == DosChessEngine.NO_SCORE || error != DosChessEngine.OK) {
            throw translate(error);
        }
        return new Evaluation(blackToMove ? -score : score, NAME);
    }

    private char[] toLegacyBoard(Position position) {
        char[] board = new char[Position.BOARD_SIZE * Position.BOARD_SIZE];
        int index = 0;
        for (int rank = Position.BOARD_SIZE; rank >= 1; rank--) {
            for (int file = 0; file < Position.BOARD_SIZE; file++) {
                char piece = position.pieceAt(file, rank);
                board[index++] = piece == Position.EMPTY ? LEGACY_EMPTY : piece;
            }
        }
        return board;
    }

    private EngineException translate(int error) {
        switch (error) {
            case DosChessEngine.ERR_ILLEGAL_BOARD:
                return new InvalidPositionException("the position is not legal");
            case DosChessEngine.ERR_NO_KING:
                return new InvalidPositionException(InvalidPositionException.KINGS_MISSING);
            case DosChessEngine.ERR_OUT_OF_TIME:
                return new EngineTimeoutException("the engine ran out of time at this depth");
            default:
                return new EngineException("the engine could not evaluate the position");
        }
    }
}
