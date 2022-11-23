/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negamax;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;
import java.util.List;
import evaluation_function.Evaluation;

/**
 *
 * @author Goran
 */
public class NegaMax {

    static public Evaluation evalFun = new Evaluation();

    static public final int depthSearch = 6;
    static public final int alphaInit = -Evaluation.MaxValue * 10;
    static public final int bethInit = Evaluation.MaxValue * 10;
    static public final int MinValue = -Evaluation.MaxValue * 10;
    static public final int MaxValue = Evaluation.MaxValue * 10;
    static final double pawnValue = 100.0;

    static private final int AvgMoves = 20;
    static private long startTime;
    static private long timeMove = -1;

    static public KillerHeuristic killer = new KillerHeuristic(NegaMax.depthSearch);
    static public BestMoves bestMoves = new BestMoves();
    static public TranspositionTable transpositionTable = new TranspositionTable();
    static public HistoryHeuristic historyHeuristic = new HistoryHeuristic();
    static public QuinceSearchOrder quinceSearchOrder = new QuinceSearchOrder();

    static public int windowSize = 33; //  eval pawn = 100

    Move moveToPlay;
    int lastIterationEval = 0;
    int depthNow = 0;
    boolean flagEarlyStop = false;
    int cntNode = 0;
    int playerEngine = 0;

    LinesFound line = new LinesFound();

    // player = 0 -> WHITE(MAX)  player = 1 -> BLACK(MIN) 
    public int negamaxSearch(Board positionToSearch, int depth, int alpha, int beta, int player) {
        cntNode++;
        //provera za vreme da se ne prekoraci
        if (outOfTime() || flagEarlyStop) {
            return MinValue;
        }

        Move bestMove = null; // najbolji potez trenutne pretrage table
        long ZobrystKeyTable = positionToSearch.getZobristKey(); // ZobrystKet trenute table

        int ply = depthNow - depth; // ply potez igraca trenutno

        if (depth <= 0) {

            if (player == 0) { // beli treba da odigra sledeci potez
                int eval = QuiescenceSearch(positionToSearch, alpha, beta, player, ply);
                printInfo(eval, player, ply);
                return eval;
                
            } else {// crni treda da odigra sledeci potez
                int eval = QuiescenceSearch(positionToSearch, alpha, beta, player, ply);
                printInfo(eval, player, ply);
                return eval;
                
            }
        }

        List<Move> moves = positionToSearch.legalMoves();
        int index = 0;
        if (depth == depthNow) {
            index = addBestMoves(moves);
        }

        if (moves.isEmpty()) {
            if (player == 0) { // beli treba da odigra sledeci potez
                int eval = evalFun.MaterialEval(positionToSearch, ply);;
                printInfo(eval, player, ply);
                return eval;
            } else {// crni treda da odigra sledeci potez
                int eval = -evalFun.MaterialEval(positionToSearch, ply);
                printInfo(eval, player, ply);
                return eval;
            }
        }

        // transposition Table -> za sad samo bestMove uzimamo
        TableNode table = transpositionTable.getTable(ZobrystKeyTable, positionToSearch);
        if (table != null) {
            if (table.depth >= depth) {
                line.addMove(table.bestMove, ply, depthNow);
                if (depth == depthNow) {
                    moveToPlay = table.bestMove;  // zapamtimo potez koji treba da se odigra
                    bestMoves.putMove(table.bestMove);
                }
                return table.Eval;
            } else {
                moves.remove(table.bestMove);
                moves.add(0, table.bestMove);
                index++;
            }
        }

        List<Move> listCaptures = positionToSearch.pseudoLegalCaptures();

        addKillerMoves(moves, ply, positionToSearch, index);
        historyHeuristic.orderMoves(moves, index, player, listCaptures);

        int positionValue = MinValue;

        for (Move move : moves) {
            //provera za vreme da se ne prekoraci
            if (outOfTime() || flagEarlyStop) {
                return MinValue;
            }

            line.addMove(move, ply, depthNow);

            int nextPlayer = (1 + player) % 2;
            //zeroSearch obrada
            positionToSearch.doMove(move);
            int newValue = -zeroSearch(positionToSearch, nextPlayer, -alpha, depth - 1);

            if ((newValue > alpha) && (newValue < beta)) {
                // nije proso zeroSearch uradi negaMax dubinsku pretragu

                newValue = -negamaxSearch(positionToSearch, depth - 1, -beta, -alpha, nextPlayer);
            }
            positionToSearch.undoMove();
            //provera za vreme da se ne prekoraci
            if (outOfTime() || flagEarlyStop) {
                return MinValue;
            }
            if (newValue > positionValue) {
                positionValue = newValue;
                bestMove = move;

                if (depth == depthNow && !flagEarlyStop) {
                    moveToPlay = move;  // zapamtimo potez koji treba da se odigra
                    bestMoves.putMove(move);
                }
            }
            if (positionValue > alpha) {
                alpha = positionValue;
            }

            if (alpha >= beta) {
                //provera za vreme da se ne prekoraci
                if (outOfTime() || flagEarlyStop) {
                    return MinValue;
                }

                killer.putMove(move, depth);

                transpositionTable.addPosition(ZobrystKeyTable, positionValue, depth, 1, bestMove);
                if (!listCaptures.contains(move)) {
                    historyHeuristic.incrementMove(bestMove, player, depth);
                }

                return positionValue;
            }
        }
        //provera za vreme da se ne prekoraci
        if (outOfTime() || flagEarlyStop) {
            return MinValue;
        }
        transpositionTable.addPosition(ZobrystKeyTable, positionValue, depth, 0, bestMove);

        if (!listCaptures.contains(bestMove)) {
            historyHeuristic.incrementMove(bestMove, player, depth);
        }

        return positionValue;
    }

    public void addKillerMoves(List<Move> list, int ply, Board board, int index) {
        if (board.isDraw() || board.isStaleMate() || board.isRepetition() || board.isRepetition(3) || board.isInsufficientMaterial()) {
            return;
        }
        //int cnt = 0;
        Move move = killer.getMove1(ply);
        try {
            if (move != null && board.isMoveLegal(move, true)) {
                list.add(index, move);
                //cnt++;
            }
            move = killer.getMove0(ply);
            if (move != null && board.isMoveLegal(move, true)) {
                list.add(index, move);
                //cnt++;
            }

        } catch (Exception e) {
        }
        return;
    }

    public int addBestMoves(List<Move> list) {
        int index = 0;
        Move[] array = bestMoves.array;
        for (int i = bestMoves.len - 1; i >= 0; i--) {
            if (array[i] != null) {
                list.remove(array[i]);
                list.add(0, array[i]);
                index++;
            }
        }
        return index;
    }

    // QuiescenceSearch of depth besk : implementiraj u kod kad ti se program ubrza
    public int QuiescenceSearch(Board board, int alpha, int beta, int player, int ply) {
        cntNode++;
        //provera za vreme da se ne prekoraci
        if (outOfTime() || flagEarlyStop) {
            return MinValue;
        }

        int stand_pat;
        if (player == 0) {
            stand_pat = evalFun.MaterialEval(board, ply);
        } else {
            stand_pat = -evalFun.MaterialEval(board, ply);
        }
        if (stand_pat >= beta) {
            return beta;
        }
        if (alpha < stand_pat) {
            alpha = stand_pat;
        }

        List<Move> moves = board.pseudoLegalCaptures();
        moves = quinceSearchOrder.orderMoves(board, moves);
        int score;
        int nextPlayer = (1 + player) % 2;
        for (Move move : moves) {
            //provera za vreme da se ne prekoraci
            if (outOfTime() || flagEarlyStop) {
                return MinValue;
            }

            if (!board.isMoveLegal(move, true)) {
                continue;
            }
            board.doMove(move);
            score = -QuiescenceSearch(board, -beta, -alpha, nextPlayer, ply);
            board.undoMove();

            if (score >= beta) {
                return beta;
            }
            if (score > alpha) {
                alpha = score;
            }
        }
        return alpha;
    }

    // Zero Null window search
    public int zeroSearch(Board board, int player, int beta, int depth) {
        cntNode++;
        int ply = depthNow - depth; // ply potez igraca trenutno
        if (depth == 0) {
            if (player == 0) { // beli treba da odigra sledeci potez
                return QuiescenceSearch(board, beta - 1, beta, player, ply);
            } else {// crni treda da odigra sledeci potez
                return QuiescenceSearch(board, beta - 1, beta, player, ply);
            }
        }

        int score = MinValue;
        //alpha == beta - 1
        List<Move> moves = board.legalMoves();
        for (Move move : moves) {
            int nextPlayer = (1 + player) % 2;
            board.doMove(move);
            score = -zeroSearch(board, nextPlayer, 1 - beta, depth - 1);
            board.undoMove();
            if (score >= beta) {
                return score;// imamo odsecanje jer score kad se negira bice gore od onoga sto mu je prosledjeno
            }
        }
        return beta - 1;// nema odsecanja vraca se alpha + 1u gornjem programu posle negiranja
    }

    private boolean outOfTime() {

        if (depthNow > 2 && timeMove > 0) {
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000;
            if (duration >= timeMove) {
                flagEarlyStop = true;
                moveToPlay = bestMoves.getArray()[0];
                // mora da se vrati kao da je ocajan potez da bi se kad istekne vreme svi ostali cvorovi isekli
                return true;
            }
        }
        return false;
    }

    public int getEval() {
        return lastIterationEval;
    }

    public void printInfo(int positionEval, int playerEval, int ply) {

        if (playerEval != playerEngine) {
            positionEval = -positionEval;
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        String lineInfo;
        if (checkPaternFound(positionEval)) {
            int mateIn = ply / 2 + 1;
            if (positionEval < 0) {
                mateIn = -mateIn;
            }
            lineInfo = "info depth " + depthNow + " score mate " + Integer.toString(mateIn) + " hashfull 0 time " + duration + " nodes " + cntNode + " pv";
            lineInfo += line.getLine();
        } else {

            lineInfo = "info depth " + depthNow + " score cp " + Integer.toString(positionEval) + " hashfull 0 time " + duration + " nodes " + cntNode + " pv";
            lineInfo += line.getLine();
        }
        System.out.println(lineInfo);

    }

    public void printInfoFinal(int positionEval, int ply, Board board) {

        String Info;
        if (checkPaternFound(positionEval)) {
            int mateIn = ply / 2 + 1;
            if (positionEval < 0) {
                mateIn = -mateIn;
            }
            Info = "info score mate " + Integer.toString(mateIn);
        } else {

            Info = "info score cp " + Integer.toString(positionEval);
            this.printExplanationMove(board);
        }
        System.out.println(Info);

    }

    // basic eval of depth 1 for all moves NE RADI
    // implementiracemo i iterative deepening
    public Move getNextMove(Board board, String time) {

        timeMove = -1;
        startTime = System.nanoTime();

        switch (board.getSideToMove().name()) {
            case "WHITE":
                playerEngine = 0;
                break;
            case "BLACK":
                playerEngine = 1;
                break;
            default:
                throw new IllegalArgumentException("Los IGRAC: ");
        }

        // dobijanje vremena koje ima chess engine
        if (!time.equals("infinite") && time.startsWith("wtime")) {
            int moveCnt = AvgMoves - board.getMoveCounter();
            if (moveCnt < 6) {
                moveCnt = 6;
            }
            moveCnt -= 2;
            String[] split = time.split(" ");
            long wTime = Integer.parseInt(split[1]);
            long bTime = Integer.parseInt(split[3]);
            if (playerEngine == 0) {
                timeMove = wTime / moveCnt;
            } else {
                timeMove = bTime / moveCnt;
            }
        }

        depthNow = 1;
        historyHeuristic.initHistory();
        cntNode = 0;
        lastIterationEval = 0;
        flagEarlyStop = false;
        line.clearLine();
        lastIterationEval = negamaxSearch(board, depthNow, alphaInit, bethInit, playerEngine);
        int backupEval = lastIterationEval;
        for (int i = 2; i <= depthSearch; i++) {

            // prvo ide Aspiration Window search
            flagEarlyStop = false;
            line.clearLine();
            if (checkPaternFound(lastIterationEval)) {
                break;
            }
            depthNow = i;
            lastIterationEval = negamaxSearch(board, depthNow, alphaInit, bethInit, playerEngine);
            if (flagEarlyStop) {
                lastIterationEval = backupEval;
                break;
            }
            //provera provedenog vremena
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000;
            if (timeMove > 0 && timeMove <= (duration << 1) || flagEarlyStop) {
                break;
            }

        }
        killer.clear();
        bestMoves.clear();
        this.printInfoFinal(lastIterationEval, depthNow, board);
        flagEarlyStop = false;
        return moveToPlay;
    }

    private boolean checkPaternFound(int lastVariation) {
        return lastVariation > 10000 || lastVariation < -10000;
    }

    private void printExplanationMove(Board board) {
        if (timeMove < 0) {
            ExplanationMove explanation = new ExplanationMove();
            explanation.printExplanation(board, moveToPlay, playerEngine);
        }
    }

}
