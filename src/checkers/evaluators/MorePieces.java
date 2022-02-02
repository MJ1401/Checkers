package checkers.evaluators;

import checkers.core.Checkerboard;

import java.util.function.ToIntFunction;

public class MorePieces implements ToIntFunction<Checkerboard> {
    @Override
    public int applyAsInt(Checkerboard value) {
        return value.numPiecesOf(value.getCurrentPlayer()) - value.numPiecesOf(value.getCurrentPlayer().opponent());
    }
}
