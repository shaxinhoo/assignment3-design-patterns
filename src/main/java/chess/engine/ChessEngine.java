package chess.engine;

import chess.model.Position;

public interface ChessEngine {

    String name();

    Evaluation evaluate(Position position, int depth);
}
