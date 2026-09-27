package chess;

import chess.model.Position;

public final class Positions {

    public static final Position START =
            Position.fromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    public static final Position MIDDLEGAME =
            Position.fromFen("r3r1k1/pp3ppp/2n5/3p4/3P4/2N5/PP3PPP/4R1K1 w - - 0 20");
    public static final Position ENDGAME_WHITE_TO_MOVE =
            Position.fromFen("8/5k2/8/3K4/4P3/8/8/8 w - - 0 50");
    public static final Position ENDGAME_BLACK_TO_MOVE =
            Position.fromFen("8/8/4k3/8/2p5/8/B5K1/8 b - - 0 44");
    public static final Position NO_BLACK_KING =
            Position.fromFen("8/8/8/8/8/8/4K3/8 w - - 0 60");
    public static final Position PAWN_ON_LAST_RANK =
            Position.fromFen("4k2P/8/8/8/8/8/8/4K3 w - - 0 70");

    private Positions() {
    }
}
