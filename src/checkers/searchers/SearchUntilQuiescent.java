package checkers.searchers;

import checkers.core.Checkerboard;
import checkers.core.CheckersSearcher;
import checkers.core.Move;
import core.Duple;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.ToIntFunction;

public class SearchUntilQuiescent extends CheckersSearcher{
    private int numNodes = 0;
    private int win = Integer.MAX_VALUE;
    private int lose = -win;

    public SearchUntilQuiescent(ToIntFunction<Checkerboard> e) {
        super(e);
    }

    @Override
    public int numNodesExpanded() {
        return numNodes;
    }

    @Override
    public Optional<Duple<Integer, Move>> selectMove(Checkerboard board) {
        return selectMoveHelp(board, 0, lose, win);
    }

    private Optional<Duple<Integer, Move>> selectMoveHelp(Checkerboard board, int depth, int alpha, int beta) {
        depth += 1;
        ArrayList<Checkerboard> nextMoves = board.getNextBoards();
        ArrayList<Duple<Integer, Move>> moveList = new ArrayList<>();
        int scoreFor = getEvaluator().applyAsInt(board);
        if (board.gameOver()) {
            if (board.playerWins(board.getCurrentPlayer())) {
                return Optional.of(new Duple<>(win, board.getLastMove()));
            }
            return Optional.of(new Duple<>(lose, board.getLastMove()));
        }
        if (depth >= getDepthLimit() && board.allCaptureMoves(board.getCurrentPlayer()).isEmpty()) {
            return Optional.of(new Duple<>(scoreFor, board.getLastMove()));
        }
        for (Checkerboard c : nextMoves) {
            numNodes += 1;
            if (board.getCurrentPlayer() != c.getCurrentPlayer()) {
                Optional<Duple<Integer, Move>> recursiveResult = selectMoveHelp(c, depth + 1, beta*-1, alpha*-1);
                moveList.add(new Duple<>(recursiveResult.get().getFirst() * -1, c.getLastMove()));
                alpha = Math.max(alpha, recursiveResult.get().getFirst()*-1);
            } else {
                Optional<Duple<Integer, Move>> recursiveResult = selectMoveHelp(c, depth + 1, alpha, beta);
                moveList.add(new Duple<>(recursiveResult.get().getFirst(), c.getLastMove()));
                alpha = Math.max(alpha, recursiveResult.get().getFirst());
            }
            if (alpha >= beta) {
                break;
            }
        }
        int max = -Integer.MAX_VALUE;
        Move m = null;
        for (Duple<Integer, Move> b : moveList) {
            if (b.getFirst() >= max) {
                max = b.getFirst();
                m = b.getSecond();
            }
        }
        return Optional.of(new Duple<>(max, m));
    }
}
