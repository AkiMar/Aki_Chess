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
public class LinesFound {
    
    
    private Move[] line = new Move[35];
    
    
    public void addMove(Move move, int ply, int depthSearch){
        this.clearLinePly(ply, depthSearch);
        int index = ply;
        line[index] = move;
    }
    
    public String getLine(){
        String lineString = "";
        for (Move move : line) {
            if( move != null)
               lineString += " " + move.toString();
        }
        return lineString;
    }
    
    public void clearLine(){
        
        for (int i = 0; i < line.length ; i++) {
            line[i] = null;
        }
    }
    
    private void clearLinePly(int ply, int depthSearch){
        if( depthSearch > line.length)
            depthSearch = line.length;
        for (int i = ply; i < depthSearch ; i++) {
            line[i] = null;
        }
    
    }
    
}
