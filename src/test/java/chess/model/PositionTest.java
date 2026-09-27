package chess.model;

import chess.Positions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PositionTest {

    @Test
    void readsTheStartPosition() {
        Position start = Positions.START;

        assertEquals('R', start.pieceAt(0, 1));
        assertEquals('k', start.pieceAt(4, 8));
        assertEquals(Position.EMPTY, start.pieceAt(4, 4));
        assertEquals(32, start.pieceCount());
        assertTrue(start.whiteToMove());
        assertTrue(start.hasBothKings());
    }

    @Test
    void detectsThePhaseFromTheBoardAndMoveNumber() {
        assertEquals(GamePhase.OPENING, Positions.START.phase());
        assertEquals(GamePhase.MIDDLEGAME, Positions.MIDDLEGAME.phase());
        assertEquals(GamePhase.ENDGAME, Positions.ENDGAME_BLACK_TO_MOVE.phase());
        assertFalse(Positions.ENDGAME_BLACK_TO_MOVE.whiteToMove());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "8/8/8/8/8/8/8/8 w - - 0",
            "8/8/8/8/8/8/8 w - - 0 1",
            "9/8/8/8/8/8/8/8 w - - 0 1",
            "8/8/8/8/8/8/8/7x w - - 0 1",
            "8/8/8/8/8/8/8/8 x - - 0 1",
            "8/8/8/8/8/8/8/8 w - - 0 zero",
            "8/8/8/8/8/8/8/8 w - - 0 0"})
    void rejectsBrokenFen(String fen) {
        assertThrows(IllegalArgumentException.class, () -> Position.fromFen(fen));
    }

    @Test
    void rejectsMissingFen() {
        assertThrows(IllegalArgumentException.class, () -> Position.fromFen(null));
    }
}
