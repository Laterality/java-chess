package chess.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class TestBoardStateFactory implements AbstractBoardStateFactory {

    private Map<CoordinatePair, ChessPiece> state;

    public TestBoardStateFactory(List<List<ChessPiece>> initiailState) {
        state = new HashMap<>();
        IntStream.range(0, initiailState.size())
            .forEach(y ->
                IntStream.range(0, initiailState.get(y).size())
                    .forEach(x ->
                        state.put(CoordinatePair.of(CoordinateX.valueOf(x).get(), CoordinateY.valueOf(y).get()),
                            initiailState.get(y).get(x))));
    }

    @Override
    public GameBoardState create() {
        return GameBoardState.of(new HashMap<>(state));
    }
}