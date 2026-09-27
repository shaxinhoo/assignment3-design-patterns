package chess.report;

import chess.Positions;
import chess.StubEngine;
import chess.engine.InvalidPositionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CoachReportTest {

    @Test
    void delegatesToEngineAndGivesEndgameAdvice() {
        StubEngine engine = StubEngine.returning("stub", -350);

        String text = new CoachReport(engine).build(Positions.ENDGAME_BLACK_TO_MOVE);

        assertEquals("coach: black is winning, black to move, activate your king and push passed pawns (stub)", text);
        assertEquals(1, engine.calls());
        assertSame(Positions.ENDGAME_BLACK_TO_MOVE, engine.lastPosition());
        assertEquals(CoachReport.DEPTH, engine.lastDepth());
    }

    @Test
    void givesOpeningAdviceForTheStartPosition() {
        StubEngine engine = StubEngine.returning("stub", 20);

        String text = new CoachReport(engine).build(Positions.START);

        assertEquals("coach: position is equal, white to move, "
                + "develop knights and bishops, fight for the center and castle (stub)", text);
    }

    @Test
    void passesEngineFailureToTheCaller() {
        StubEngine engine = StubEngine.failing(new InvalidPositionException("bad"));

        assertThrows(InvalidPositionException.class, () -> new CoachReport(engine).build(Positions.START));
    }
}
