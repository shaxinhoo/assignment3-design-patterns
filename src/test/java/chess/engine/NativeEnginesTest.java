package chess.engine;

import chess.Positions;
import chess.model.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NativeEnginesTest {

    private static final Position WHITE_UP_A_QUEEN =
            Position.fromFen("4k3/8/8/8/8/8/8/3QK3 w - - 0 30");

    @Test
    void materialEngineCountsPieces() {
        assertEquals(900, new MaterialEngine().evaluate(WHITE_UP_A_QUEEN, 1).centipawns());
        assertEquals(0, new MaterialEngine().evaluate(Positions.START, 1).centipawns());
        assertEquals(-500, new MaterialEngine().evaluate(Positions.MIDDLEGAME, 1).centipawns());
    }

    @Test
    void positionalEngineRewardsDevelopmentAndCenter() {
        Position italian = Position.fromFen("r1bqkbnr/pppp1ppp/2n5/4p3/2B1P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 3 3");

        assertEquals(15, new PositionalEngine().evaluate(italian, 2).centipawns());
    }

    @Test
    void nativeEnginesUseTheSameFailureTypes() {
        assertThrows(InvalidPositionException.class,
                () -> new MaterialEngine().evaluate(Positions.NO_BLACK_KING, 1));
        assertThrows(InvalidPositionException.class,
                () -> new PositionalEngine().evaluate(Positions.NO_BLACK_KING, 1));
        assertThrows(EngineTimeoutException.class,
                () -> new PositionalEngine().evaluate(Positions.START, 9));
    }
}
