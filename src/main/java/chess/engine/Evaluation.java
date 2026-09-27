package chess.engine;

import java.util.Locale;
import java.util.Objects;

public record Evaluation(int centipawns, String engineName) {

    private static final int EQUAL_LIMIT = 50;
    private static final int WINNING_LIMIT = 200;
    private static final double CENTIPAWNS_PER_PAWN = 100.0;

    public Evaluation {
        Objects.requireNonNull(engineName, "engineName");
    }

    public String verdict() {
        int size = Math.abs(centipawns);
        if (size < EQUAL_LIMIT) {
            return "the position is equal";
        }
        String side = centipawns > 0 ? "White" : "Black";
        return size < WINNING_LIMIT ? side + " is slightly better" : side + " is winning";
    }

    public String formattedScore() {
        return String.format(Locale.US, "%+.2f", centipawns / CENTIPAWNS_PER_PAWN);
    }
}
