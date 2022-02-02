package checkers.searchers;

import checkers.core.Checkerboard;
import checkers.core.CheckersSearcher;
import checkers.core.Move;
import checkers.core.PlayerColor;
import checkers.evaluators.MorePieces;
import core.Duple;
import search.SearchNode;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.ToIntFunction;

public class NegaMax extends CheckersSearcher {
    private int numNodes = 0;
    public NegaMax(ToIntFunction<Checkerboard> e) {
        super(e);
    }

    @Override
    public int numNodesExpanded() {
        return numNodes;
    }

    @Override
    public Optional<Duple<Integer, Move>> selectMove(Checkerboard board) {
        return selectMoveHelp(board, 0);
    }

    private Optional<Duple<Integer, Move>> selectMoveHelp(Checkerboard board, int depth) {
        int win = Integer.MAX_VALUE;
        int lose = -win;
        ArrayList<Checkerboard> nextMoves = board.getNextBoards();
        ArrayList<Duple<Integer, Move>> moveList = new ArrayList<>();
        if (board.gameOver()) {
            if (board.playerWins(board.getCurrentPlayer())) {
                return Optional.of(new Duple<>(win, board.getLastMove()));
            }
            return Optional.of(new Duple<>(lose, board.getLastMove()));
        }
        if (depth >= getDepthLimit()) {
            int scoreFor = getEvaluator().applyAsInt(board);
            return Optional.of(new Duple<>(scoreFor, board.getLastMove()));
        }
        for (Checkerboard c:nextMoves) {
            numNodes += 1;
            int negation = board.getCurrentPlayer() != c.getCurrentPlayer() ? -1 : 1;
            Optional<Duple<Integer, Move>> recursiveResult = selectMoveHelp(c, depth + 1);
            moveList.add(new Duple<>(recursiveResult.get().getFirst() * negation, c.getLastMove()));
        }
        int max = -Integer.MAX_VALUE;
        Move m = board.getLastMove();
        for (Duple<Integer, Move> b:moveList) {
            if (b.getFirst() >= max) {
                max = b.getFirst();
                m = b.getSecond();
            }
        }
        return Optional.of(new Duple<>(max, m));
    }
}
