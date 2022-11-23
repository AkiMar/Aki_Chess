/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negamax;

import com.github.bhlangonijr.chesslib.move.Move;
import java.util.List;

/**
 *
 * @author Goran
 */
public class BestMoves {

    List<Move> curentMoves = null;
    Move[] array;
    int len;

    public BestMoves() {
        len = 6;
        array = new Move[len];
        for (int i = 0; i < len; i++) {
            array[i] = null;
        }
    }

    public void setCurentMoves(List<Move> curentMoves) {
        this.curentMoves = curentMoves;
    }

    public void putMove(Move move) {

        for (int i = len - 1; i > 0; i--) {
            array[i] = array[i - 1];
        }
        array[0] = move;
    }
    
    public Move[] getArray(){
        return array;
    }

    public void clear() {
        for (int i = 0; i < len; i++) {
            array[i] = null;
        }
    }

}
