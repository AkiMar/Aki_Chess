/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negamax;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;

/**
 *
 * @author Goran
 */
public class TranspositionTable {
    
    public static int SizeTable = 1000000;
    
    TableNode[] tableAlways = new TableNode[SizeTable];  // po default je popunjeno sa null
    TableNode[] tableDeep = new TableNode[SizeTable];
    
    public void addPosition(long ZobrystKey, int Eval, int depth, int flag, Move bestMove){
        
        int keyHash = getHash(ZobrystKey);
        TableNode node = new TableNode(Eval, depth, flag, bestMove);
        // replacement strategy Deep + allways
        TableNode valNode = tableDeep[keyHash];
        if( valNode == null || valNode.depth < depth){
            tableDeep[keyHash] = node;
        }else{
            tableAlways[keyHash] = node;
        }
    }

    public TableNode getTable(long ZobrystKey, Board position){
        try{
        int keyHash = getHash(ZobrystKey);
        TableNode table = tableDeep[keyHash];
        if( table != null && table.bestMove != null && position.isMoveLegal(table.bestMove, true)){
            return table;
        }
        table = tableAlways[keyHash];
        if( table != null && table.bestMove != null && position.isMoveLegal(table.bestMove, true)){
            return table;
        }
        return null;
        }
        catch(Exception e){
            return null;
        }
    }
    
    
    private int getHash(long ZobrystKey){
        
        int hashValue = (int) (ZobrystKey % SizeTable);
        return ( hashValue >=0 )? hashValue : -hashValue; 
    }
    
    
}

class TableNode{
    
    int Eval;
    int depth;
    int flag; // =0 exact value, =1 cut-off
    
    Move bestMove;

    public TableNode(int Eval, int depth, int flag, Move bestMove) {
        this.Eval = Eval;
        this.depth = depth;
        this.flag = flag;
        this.bestMove = bestMove;
    }
    
    
    
}