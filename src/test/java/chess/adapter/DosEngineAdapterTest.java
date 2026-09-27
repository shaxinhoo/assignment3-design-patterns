package chess.adapter;

import chess.Positions;
import chess.engine.EngineException;
import chess.engine.EngineTimeoutException;
import chess.engine.Evaluation;
import chess.engine.InvalidPositionException;
import chess.legacy.DosChessEngine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DosEngineAdapterTest {

    @Test
    void convertsDepthToPliesAndBoardToLegacyFormat() {
        StubDosEngine legacy = new StubDosEngine(40, DosChessEngine.OK);

        new DosEngineAdapter(legacy).evaluate(Positions.ENDGAME_WHITE_TO_MOVE, 3);

        assertEquals(6, legacy.lastPlies());
        assertFalse(legacy.lastBlackToMove());
        char[] board = legacy.lastBoard();
        assertEquals(64, board.length);
        assertEquals('.', board[0]);
        assertEquals('k', board[13]);
        assertEquals('K', board[27]);
        assertEquals('P', board[36]);
    }

    @Test
    void keepsScoreWhenWhiteIsToMove() {
        StubDosEngine legacy = new StubDosEngine(120, DosChessEngine.OK);

        Evaluation evaluation = new DosEngineAdapter(legacy).evaluate(Positions.ENDGAME_WHITE_TO_MOVE, 2);

        assertEquals(120, evaluation.centipawns());
        assertEquals("DosChess (legacy)", evaluation.engineName());
    }

    @Test
    void flipsScoreToWhitePerspectiveWhenBlackIsToMove() {
        StubDosEngine legacy = new StubDosEngine(120, DosChessEngine.OK);

        Evaluation evaluation = new DosEngineAdapter(legacy).evaluate(Positions.ENDGAME_BLACK_TO_MOVE, 2);

        assertTrue(legacy.lastBlackToMove());
        assertEquals(-120, evaluation.centipawns());
    }

    @Test
    void translatesMissingKingToInvalidPosition() {
        StubDosEngine legacy = new StubDosEngine(DosChessEngine.NO_SCORE, DosChessEngine.ERR_NO_KING);

        InvalidPositionException e = assertThrows(InvalidPositionException.class,
                () -> new DosEngineAdapter(legacy).evaluate(Positions.START, 2));

        assertEquals(InvalidPositionException.KINGS_MISSING, e.getMessage());
    }

    @Test
    void translatesIllegalBoardToInvalidPosition() {
        StubDosEngine legacy = new StubDosEngine(DosChessEngine.NO_SCORE, DosChessEngine.ERR_ILLEGAL_BOARD);

        assertThrows(InvalidPositionException.class,
                () -> new DosEngineAdapter(legacy).evaluate(Positions.START, 2));
    }

    @Test
    void translatesOutOfTimeToTimeout() {
        StubDosEngine legacy = new StubDosEngine(DosChessEngine.NO_SCORE, DosChessEngine.ERR_OUT_OF_TIME);

        assertThrows(EngineTimeoutException.class,
                () -> new DosEngineAdapter(legacy).evaluate(Positions.START, 2));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 42, 999})
    void translatesUnknownCodeToGeneralEngineErrorWithoutLeakingTheCode(int code) {
        StubDosEngine legacy = new StubDosEngine(DosChessEngine.NO_SCORE, code);

        EngineException e = assertThrows(EngineException.class,
                () -> new DosEngineAdapter(legacy).evaluate(Positions.START, 2));

        assertEquals(EngineException.class, e.getClass());
        assertFalse(e.getMessage().contains(String.valueOf(code)));
    }

    @Test
    void failsWhenErrorIsSetEvenIfScoreLooksNormal() {
        StubDosEngine legacy = new StubDosEngine(55, DosChessEngine.ERR_OUT_OF_TIME);

        assertThrows(EngineTimeoutException.class,
                () -> new DosEngineAdapter(legacy).evaluate(Positions.START, 2));
    }

    @Test
    void worksWithTheRealLegacyEngine() {
        DosEngineAdapter adapter = new DosEngineAdapter(new DosChessEngine());

        assertEquals(120, adapter.evaluate(Positions.ENDGAME_WHITE_TO_MOVE, 2).centipawns());
        assertEquals(190, adapter.evaluate(Positions.ENDGAME_BLACK_TO_MOVE, 2).centipawns());
        assertThrows(InvalidPositionException.class,
                () -> adapter.evaluate(Positions.NO_BLACK_KING, 2));
        assertThrows(InvalidPositionException.class,
                () -> adapter.evaluate(Positions.PAWN_ON_LAST_RANK, 2));
        assertThrows(EngineTimeoutException.class,
                () -> adapter.evaluate(Positions.ENDGAME_WHITE_TO_MOVE, 7));
    }
}
