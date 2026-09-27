Assignment 3. Adapter and Bridge

Theme: chess position analysis.

Bridge: PositionReport is the abstraction (ScoreReport, CoachReport). ChessEngine is the implementor (MaterialEngine, PositionalEngine, DosEngineAdapter).

Adapter: DosEngineAdapter wraps the legacy class DosChessEngine, which has a different method, different parameters and error codes instead of exceptions. The legacy class is not modified.

Complexity module: dynamic implementor selection. PhaseRoutingEngine chooses the engine from the position itself (opening, middlegame or endgame).

Build and run the tests (Java 17 or newer, Maven is downloaded by the wrapper):

./mvnw package

Run the demo:

java -jar target/chess-analysis.jar
java -jar target/chess-analysis.jar "8/5k2/8/3K4/4P3/8/8/8 w - - 0 50"

UML diagram: docs/uml.png
Design rationale: docs/design-rationale.md (and docs/design-rationale.pdf)
