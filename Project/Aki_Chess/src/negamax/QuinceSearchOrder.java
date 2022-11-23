/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negamax;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.move.Move;
import evaluation_function.Evaluation;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Goran
 */
public class QuinceSearchOrder {

    public List<Move> orderMoves(Board board, List<Move> moves) {

        List<Move> orderedMoves = new ArrayList<>();

        for (Move move : moves) {

            try {
                if (!board.isMoveLegal(move, true)) {
                    continue;
                }

                Piece attacker = board.getPiece(move.getFrom());
                Piece victim = board.getPiece(move.getTo());

                int attackerValue = valuePiece(attacker);
                int victimValue = valuePiece(victim);
                int valueCapture = victimValue - attackerValue;

                int i;
                for (i = 0; i < orderedMoves.size(); i++) {

                    Move orderdMove = orderedMoves.get(i);
                    attacker = board.getPiece(orderdMove.getFrom());
                    victim = board.getPiece(orderdMove.getTo());

                    attackerValue = valuePiece(attacker);
                    victimValue = valuePiece(victim);
                    int valueOrder = victimValue - attackerValue;

                    if (valueCapture >= valueOrder) {
                        orderedMoves.add(i, move);
                        break;
                    }

                }
                if (i >= orderedMoves.size()) {
                    orderedMoves.add(move);
                }
            }catch(Exception e){
            }
        }

        return orderedMoves;
    }

    private int valuePiece(Piece piece) {

        String type = piece.getFenSymbol();
        switch (type) {
            case "P":
                return 100;
            case "N":
                return 275;
            case "B":
                return 300;
            case "R":
                return 600;
            case "Q":
                return 900;
            case "K":
                return 1000000;
            case "p":
                return 100;
            case "n":
                return 275;
            case "b":
                return 300;
            case "r":
                return 600;
            case "q":
                return 900;
            case "k":
                return 1000000;
            default:
                throw new IllegalArgumentException("Los Simbol: ");
        }

    }

}
