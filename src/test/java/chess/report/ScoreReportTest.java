package chess.report;

import chess.Positions;
import chess.StubEngine;
import chess.engine.EngineTimeoutException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScoreReportTest {

    @Test
    void delegatesToEngineAndFormatsTheScore() {
        StubEngine engine = StubEngine.returning("stub", 150);

        String text = new ScoreReport(engine).build(Positions.START);

        assertEquals("score: +1.50, white is slightly better (stub)", text);
        assertEquals(1, engine.calls());
        assertSame(Positions.START, engine.lastPosition());
        assertEquals(ScoreReport.DEPTH, engine.lastDepth());
    }

    @Test
    void showsBlackAdvantageWithMinusSign() {
        StubEngine engine = StubEngine.returning("stub", -30);

        String text = new ScoreReport(engine).build(Positions.START);

        assertEquals("score: -0.30, position is equal (stub)", text);
    }

    @Test
    void passesEngineFailureToTheCaller() {
        StubEngine engine = StubEngine.failing(new EngineTimeoutException("too slow"));

        assertThrows(EngineTimeoutException.class, () -> new ScoreReport(engine).build(Positions.START));
    }
}
