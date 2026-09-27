package chess.engine;

import chess.model.PieceValues;
import chess.model.Position;

public class MaterialEngine implements ChessEngine {

    private static final String NAME = "Material";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Evaluation evaluate(Position position, int depth) {
        if (!position.hasBothKings()) {
            throw new InvalidPositionException(InvalidPositionException.KINGS_MISSING);
        }
        return new Evaluation(PieceValues.balance(position), NAME);
    }
}
