package checkers.evaluators;

import checkers.core.Checkerboard;
import checkers.core.PlayerColor;

import java.util.function.ToIntFunction;

public class Evaulator1 implements ToIntFunction<Checkerboard> {
    @Override
    public int applyAsInt(Checkerboard value) {
        PlayerColor pro = value.getCurrentPlayer();
        PlayerColor ant = value.getCurrentPlayer().opponent();
        int pieceDiff = 10* (value.numPiecesOf(pro) - value.numPiecesOf(ant));
        int kingsDiff = 25* (value.numKingsOf(pro) - value.numKingsOf(ant));
        int badSpotDiff = 7* (value.allCaptureMoves(pro).size() - value.allCaptureMoves(ant).size());
        int numMovesDiff = 3* (value.getLegalMoves(pro).size() - value.getLegalMoves(ant).size());
        int total = pieceDiff + kingsDiff + badSpotDiff + numMovesDiff;
        return total;
    }
}
