package checkers.evaluators;

import checkers.core.Checkerboard;
import checkers.core.PlayerColor;

import java.util.function.ToIntFunction;

public class TestingEval implements ToIntFunction<Checkerboard> {
    @Override
    public int applyAsInt(Checkerboard value) {
        PlayerColor pro = value.getCurrentPlayer();
        PlayerColor ant = value.getCurrentPlayer().opponent();
        int pieceDiff = 10* (value.numPiecesOf(pro) - value.numPiecesOf(ant));
        int kingsDiff = 25* (value.numKingsOf(pro) - value.numKingsOf(ant));
//        int badSpotDiff = 7* (value.allCaptureMoves(pro).size() - value.allCaptureMoves(ant).size());
        int numMovesDiff = 3* (value.getLegalMoves(pro).size() - value.getLegalMoves(ant).size());
        int proBack = 0;
        int antBack = 0;
        for (int i = 0; i < value.maxCol(); i++) {
            if (!value.kingAt(value.minRow(), i)) {
                if (value.colorAt(value.minRow(), i, pro)) {
                    proBack += 1;
                }
                if (value.colorAt(value.minRow(), i, ant)) {
                    antBack += 1;
                }
                if (value.colorAt(value.maxRow(), i, pro)) {
                    proBack += 1;
                }
                if (value.colorAt(value.maxRow(), i, ant)) {
                    antBack += 1;
                }
            }
        }
        int backDiff = 5* (proBack - antBack);
        int total = pieceDiff + kingsDiff + numMovesDiff + backDiff;
        return total;
    }
}
