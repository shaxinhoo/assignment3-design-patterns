package chess.engine;

import chess.Positions;
import chess.StubEngine;
import chess.adapter.DosEngineAdapter;
import chess.legacy.DosChessEngine;
import chess.model.GamePhase;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PhaseRoutingEngineTest {

    private final StubEngine opening = StubEngine.returning("opening", 10);
    private final StubEngine middlegame = StubEngine.returning("middlegame", 20);
    private final StubEngine endgame = StubEngine.returning("endgame", 30);
    private final PhaseRoutingEngine router = new PhaseRoutingEngine(Map.of(
            GamePhase.OPENING, opening,
            GamePhase.MIDDLEGAME, middlegame,
            GamePhase.ENDGAME, endgame));

    @Test
    void choosesTheEngineFromThePositionItself() {
        assertEquals("opening", router.evaluate(Positions.START, 2).engineName());
        assertEquals("middlegame", router.evaluate(Positions.MIDDLEGAME, 2).engineName());
        assertEquals("endgame", router.evaluate(Positions.ENDGAME_WHITE_TO_MOVE, 2).engineName());
        assertEquals(1, opening.calls());
        assertEquals(1, middlegame.calls());
        assertEquals(1, endgame.calls());
    }

    @Test
    void passesDepthToTheChosenEngine() {
        router.evaluate(Positions.MIDDLEGAME, 4);

        assertEquals(4, middlegame.lastDepth());
    }

    @Test
    void picksTheAdaptedLegacyEngineForEndgames() {
        PhaseRoutingEngine real = new PhaseRoutingEngine(Map.of(
                GamePhase.OPENING, opening,
                GamePhase.MIDDLEGAME, middlegame,
                GamePhase.ENDGAME, new DosEngineAdapter(new DosChessEngine())));

        assertEquals("dos legacy", real.evaluate(Positions.ENDGAME_BLACK_TO_MOVE, 2).engineName());
    }

    @Test
    void refusesToStartWithoutAnEngineForEveryPhase() {
        assertThrows(IllegalArgumentException.class,
                () -> new PhaseRoutingEngine(Map.of(GamePhase.OPENING, opening)));
    }
}
