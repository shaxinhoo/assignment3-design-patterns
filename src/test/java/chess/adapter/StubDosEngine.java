package chess.adapter;

import chess.legacy.DosChessEngine;

class StubDosEngine extends DosChessEngine {

    private final int score;
    private final int error;
    private int lastPlies;
    private char[] lastBoard;
    private boolean lastBlackToMove;

    StubDosEngine(int score, int error) {
        this.score = score;
        this.error = error;
    }

    @Override
    public int think(int plies, char[] board, boolean blackToMove) {
        lastPlies = plies;
        lastBoard = board;
        lastBlackToMove = blackToMove;
        return score;
    }

    @Override
    public int getLastError() {
        return error;
    }

    int lastPlies() {
        return lastPlies;
    }

    char[] lastBoard() {
        return lastBoard;
    }

    boolean lastBlackToMove() {
        return lastBlackToMove;
    }
}
