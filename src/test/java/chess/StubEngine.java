package chess;

import chess.engine.ChessEngine;
import chess.engine.EngineException;
import chess.engine.Evaluation;
import chess.model.Position;

public class StubEngine implements ChessEngine {

    private final String name;
    private final int score;
    private final EngineException failure;
    private Position lastPosition;
    private int lastDepth;
    private int calls;

    private StubEngine(String name, int score, EngineException failure) {
        this.name = name;
        this.score = score;
        this.failure = failure;
    }

    public static StubEngine returning(String name, int score) {
        return new StubEngine(name, score, null);
    }

    public static StubEngine failing(EngineException failure) {
        return new StubEngine("failing", 0, failure);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Evaluation evaluate(Position position, int depth) {
        calls++;
        lastPosition = position;
        lastDepth = depth;
        if (failure != null) {
            throw failure;
        }
        return new Evaluation(score, name);
    }

    public Position lastPosition() {
        return lastPosition;
    }

    public int lastDepth() {
        return lastDepth;
    }

    public int calls() {
        return calls;
    }
}
