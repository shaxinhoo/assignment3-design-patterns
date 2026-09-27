Assignment 3. Adapter and Bridge

Theme: chess position analysis

Bridge: PositionReport is the abstraction, ScoreReport and CoachReport are refined abstractions. ChessEngine is the implementor, MaterialEngine, PositionalEngine and DosEngineAdapter implement it

Adapter: DosEngineAdapter wraps the old class DosChessEngine, which has a different method, different parameters and error codes instead of exceptions. The old class is not changed

Complexity module: dynamic implementor selection. PhaseRoutingEngine picks the engine from the position itself (opening, middlegame or endgame)

Build and run tests:

./mvnw package

Run:

java -jar target/chess-analysis.jar

UML diagram is in docs/uml.png, design rationale is in docs/design-rationale.md and docs/design-rationale.pdf
