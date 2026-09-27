package chess.engine;

public class InvalidPositionException extends EngineException {

    public static final String KINGS_MISSING = "each side needs one king";

    public InvalidPositionException(String message) {
        super(message);
    }
}
