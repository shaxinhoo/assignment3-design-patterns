package chess.report;

import chess.engine.ChessEngine;
import chess.engine.Evaluation;
import chess.model.GamePhase;
import chess.model.Position;

import java.util.Map;

public class CoachReport extends PositionReport {

    static final int DEPTH = 5;

    private static final Map<GamePhase, String> ADVICE = Map.of(
            GamePhase.OPENING, "develop knights and bishops, fight for the center and castle",
            GamePhase.MIDDLEGAME, "look for tactics and improve your worst piece",
            GamePhase.ENDGAME, "activate your king and push passed pawns");

    public CoachReport(ChessEngine engine) {
        super(engine);
    }

    @Override
    public String build(Position position) {
        Evaluation evaluation = engine.evaluate(position, DEPTH);
        String side = position.whiteToMove() ? "White" : "Black";
        return "Coach: " + evaluation.verdict() + ". " + side + " to move, "
                + ADVICE.get(position.phase()) + ". [" + evaluation.engineName() + "]";
    }
}
