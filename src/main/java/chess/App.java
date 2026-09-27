package chess;

import chess.adapter.DosEngineAdapter;
import chess.engine.ChessEngine;
import chess.engine.EngineException;
import chess.engine.MaterialEngine;
import chess.engine.PhaseRoutingEngine;
import chess.engine.PositionalEngine;
import chess.legacy.DosChessEngine;
import chess.model.GamePhase;
import chess.model.Position;
import chess.report.CoachReport;
import chess.report.PositionReport;
import chess.report.ScoreReport;

import java.util.List;
import java.util.Map;

public class App {

    private static final List<String> SAMPLE_POSITIONS = List.of(
            "r1bqkbnr/pppp1ppp/2n5/4p3/2B1P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 3 3",
            "r3r1k1/pp3ppp/2n5/3p4/3P4/2N5/PP3PPP/4R1K1 w - - 0 20",
            "8/5k2/8/3K4/4P3/8/8/8 w - - 0 50",
            "8/8/4k3/8/2p5/8/B5K1/8 b - - 0 44",
            "8/8/8/8/8/8/4K3/8 w - - 0 60");

    public static void main(String[] args) {
        List<String> inputs = args.length > 0 ? List.of(args) : SAMPLE_POSITIONS;
        ChessEngine engine = createEngine();
        List<PositionReport> reports = List.of(new ScoreReport(engine), new CoachReport(engine));
        for (String fen : inputs) {
            analyze(fen, reports);
        }
    }

    static ChessEngine createEngine() {
        return new PhaseRoutingEngine(Map.of(
                GamePhase.OPENING, new PositionalEngine(),
                GamePhase.MIDDLEGAME, new MaterialEngine(),
                GamePhase.ENDGAME, new DosEngineAdapter(new DosChessEngine())));
    }

    private static void analyze(String fen, List<PositionReport> reports) {
        System.out.println("position: " + fen);
        try {
            Position position = Position.fromFen(fen);
            System.out.println("  phase: " + position.phase().name().toLowerCase());
            for (PositionReport report : reports) {
                System.out.println("  " + report.build(position));
            }
        } catch (EngineException | IllegalArgumentException e) {
            System.out.println("  cannot analyze: " + e.getMessage());
        }
        System.out.println();
    }
}
