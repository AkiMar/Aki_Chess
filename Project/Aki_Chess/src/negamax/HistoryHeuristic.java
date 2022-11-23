/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negamax;

import com.github.bhlangonijr.chesslib.File;
import com.github.bhlangonijr.chesslib.Rank;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import java.util.List;

/**
 *
 * @author Goran
 */
public class HistoryHeuristic {
    
    int[][] blackMoveHistory = new int[64][64];   // [from][to]
    int[][] whiteMoveHistory = new int[64][64];
    
    public void incrementMove(Move move, int player, int depth){
        // white -> 0; black -> 1
        if( move == null)
            return;
        
        Square sqFrom = move.getFrom();
        int indexFrom = (8 - getRank(sqFrom.getRank())) * 8 + getFile(sqFrom.getFile()) - 1;
        
        Square sqTo = move.getTo();
        int indexTo = (8 - getRank(sqTo.getRank())) * 8 + getFile(sqTo.getFile()) - 1;
        
        
        if ( player == 0) {
            
            whiteMoveHistory[indexFrom][indexTo]+= 2 << depth;
            
        } else {
            
            blackMoveHistory[indexFrom][indexTo]+= 2 << depth;
            
        }
        
        
    }
    
    public void initHistory(){
    
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                blackMoveHistory[i][j] = 0;
                whiteMoveHistory[i][j] = 0;
            }
        }
    
    }
    
    private int getRank(Rank r) {

        String rank = r.toString();
        switch (rank) {
            case "RANK_1":
                return 1;
            case "RANK_2":
                return 2;
            case "RANK_3":
                return 3;
            case "RANK_4":
                return 4;
            case "RANK_5":
                return 5;
            case "RANK_6":
                return 6;
            case "RANK_7":
                return 7;
            case "RANK_8":
                return 8;
            default:
                throw new IllegalArgumentException("Los RANK: ");
        }

    }

    private int getFile(File f) {

        String file = f.toString();
        switch (file) {
            case "FILE_A":
                return 1;
            case "FILE_B":
                return 2;
            case "FILE_C":
                return 3;
            case "FILE_D":
                return 4;
            case "FILE_E":
                return 5;
            case "FILE_F":
                return 6;
            case "FILE_G":
                return 7;
            case "FILE_H":
                return 8;
            default:
                throw new IllegalArgumentException("Los RANK: ");
        }

    }
    
    
    public void orderMoves(List<Move> list, int index, int player, List<Move> listCaptures){
    
        for (int i = index + 1; i < list.size(); i++) {
            
            Move move = list.get(i);
            if( move == null || listCaptures.contains(move))
                continue;
            
            int valueMove = valueMoveHistory(move, player);
            
            for (int j = index; j < i; j++) {
                Move moveCmp = list.get(j);
                
                if( moveCmp == null || listCaptures.contains(moveCmp))
                    continue;
                
                int valueMoveHis = valueMoveHistory(moveCmp, player);
                if( valueMove > valueMoveHis ){
                    list.remove(i);
                    list.add(j, move);
                    break;
                }
                
            }
            
            
        }
    
    }
    
    private int valueMoveHistory(Move move, int player){
        
        Square sqFrom = move.getFrom();
        int indexFrom = (8 - getRank(sqFrom.getRank())) * 8 + getFile(sqFrom.getFile()) - 1;
        
        Square sqTo = move.getTo();
        int indexTo = (8 - getRank(sqTo.getRank())) * 8 + getFile(sqTo.getFile()) - 1;
        
        
        if ( player == 0) {
            
            return whiteMoveHistory[indexFrom][indexTo];
            
        } else {
            
            return blackMoveHistory[indexFrom][indexTo];
            
        }
    
    }
    
}
