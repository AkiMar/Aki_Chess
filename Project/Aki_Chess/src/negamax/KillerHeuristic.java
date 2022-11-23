/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negamax;

import com.github.bhlangonijr.chesslib.move.Move;

/**
 *
 * @author Goran
 */
public class KillerHeuristic {
    
    Move[][] array;
    int len;

    public KillerHeuristic() {
        array = new Move[5][2];
        len = 5;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                array[i][j] = null;
            }
        }
    }
    
    public KillerHeuristic(int size){
        len = size + 1;
        array = new Move[len][2];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < 2; j++) {
                array[i][j] = null;
            }
        }
    }
    
    
    public void putMove(Move move, int ply){
        array[ply][1] = array[ply][0];
        array[ply][0] = move;
    }
    
    public Move getMove0(int ply){
        return array[ply][0];
    }
    
    public Move getMove1(int ply){
        return array[ply][1];
    }
    
    public void clear(){
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < 2; j++) {
                array[i][j] = null;
            }
        }
    }
}
