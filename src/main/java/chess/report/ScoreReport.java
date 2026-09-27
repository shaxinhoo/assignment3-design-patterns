package chess.report;

import chess.engine.ChessEngine;
import chess.engine.Evaluation;
import chess.model.Position;

public class ScoreReport extends PositionReport {

    static final int DEPTH = 2;

    public ScoreReport(ChessEngine engine) {
        super(engine);
    }

    @Override
    public String build(Position position) {
        Evaluation evaluation = engine.evaluate(position, DEPTH);
        return "score: " + evaluation.formattedScore() + ", " + evaluation.verdict()
                + " (" + evaluation.engineName() + ")";
    }
}
