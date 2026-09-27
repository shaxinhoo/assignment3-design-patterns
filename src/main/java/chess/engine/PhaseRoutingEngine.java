package chess.engine;

import chess.model.GamePhase;
import chess.model.Position;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class PhaseRoutingEngine implements ChessEngine {

    private static final String NAME = "phase router";

    private final Map<GamePhase, ChessEngine> engines;

    public PhaseRoutingEngine(Map<GamePhase, ChessEngine> engines) {
        for (GamePhase phase : GamePhase.values()) {
            if (engines.get(phase) == null) {
                throw new IllegalArgumentException("no engine for phase " + phase);
            }
        }
        this.engines = Collections.unmodifiableMap(new EnumMap<>(engines));
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Evaluation evaluate(Position position, int depth) {
        return engines.get(position.phase()).evaluate(position, depth);
    }
}
