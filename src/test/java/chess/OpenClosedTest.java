package chess;

import chess.engine.ChessEngine;
import chess.engine.Evaluation;
import chess.engine.MaterialEngine;
import chess.engine.PhaseRoutingEngine;
import chess.model.GamePhase;
import chess.model.Position;
import chess.report.PositionReport;
import chess.report.ScoreReport;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OpenClosedTest {

    static class WhoIsBetterReport extends PositionReport {

        WhoIsBetterReport(ChessEngine engine) {
            super(engine);
        }

        @Override
        public String build(Position position) {
            return engine.evaluate(position, 1).verdict();
        }
    }

    static class DrawishEngine implements ChessEngine {

        @Override
        public String name() {
            return "drawish";
        }

        @Override
        public Evaluation evaluate(Position position, int depth) {
            return new Evaluation(0, name());
        }
    }

    @Test
    void newReportWorksWithExistingEngines() {
        PositionReport report = new WhoIsBetterReport(new MaterialEngine());

        assertEquals("black is winning", report.build(Positions.MIDDLEGAME));
    }

    @Test
    void newEngineWorksWithExistingReportsAndRouter() {
        ChessEngine router = new PhaseRoutingEngine(Map.of(
                GamePhase.OPENING, new DrawishEngine(),
                GamePhase.MIDDLEGAME, new MaterialEngine(),
                GamePhase.ENDGAME, new DrawishEngine()));

        assertEquals("score: +0.00, position is equal (drawish)",
                new ScoreReport(router).build(Positions.ENDGAME_WHITE_TO_MOVE));
    }
}
