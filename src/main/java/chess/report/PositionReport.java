package chess.report;

import chess.engine.ChessEngine;
import chess.model.Position;

import java.util.Objects;

public abstract class PositionReport {

    protected final ChessEngine engine;

    protected PositionReport(ChessEngine engine) {
        this.engine = Objects.requireNonNull(engine, "engine");
    }

    public abstract String build(Position position);
}
