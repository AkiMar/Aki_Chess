/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evaluation_function;

import java.io.*;
/**
 *
 * @author Goran
 */
public class PawnHash {
    
    private static int sizeTable = 1000000;
    Node[] PawnEvalBlack =  new Node[sizeTable];
    Node[] PawnEvalWhite =  new Node[sizeTable];
    
    
    public void addPositionWhite(long bitBoard, int eval){
        int hashKey = getHashKeyWhite(bitBoard);
        if(PawnEvalWhite[ hashKey ] != null && PawnEvalWhite[ hashKey ].bitBoard == bitBoard )
            return;
        
        if( PawnEvalWhite[hashKey] != null ){ // 1 bucket list
            hashKey = (hashKey + 1000) % sizeTable;
            PawnEvalWhite[hashKey] = new Node(eval, bitBoard);
            return;
        }
        
        PawnEvalWhite[ hashKey ] = new Node(eval, bitBoard);
    }
    
    public void addPositionBlack(long bitBoard, int eval){
        int hashKey = getHashKeyBlack(bitBoard);
        if(PawnEvalBlack[hashKey] != null && PawnEvalBlack[hashKey].bitBoard == bitBoard )
            return;
        
        if( PawnEvalBlack[hashKey] != null ){ // 1 bucket list
            hashKey = (hashKey + 1000) % sizeTable;
            PawnEvalBlack[hashKey] = new Node(eval, bitBoard);
            return;
        }
        PawnEvalBlack[hashKey] = new Node(eval, bitBoard);
    }
    
    public Node getEvalBlack(long bitBoard){
        int hashKey = getHashKeyBlack(bitBoard);
        Node node = PawnEvalBlack[ hashKey ];
        if( node != null && node.bitBoard == bitBoard)
            return node;
        
        hashKey = (hashKey + 1000) % sizeTable;
        node = PawnEvalBlack[ hashKey ];
        if( node != null && node.bitBoard == bitBoard)
            return node;
        
        return null;
            
    }
    
    public Node getEvalWhite(long bitBoard){
        
        int hashKey = getHashKeyWhite(bitBoard);
        Node node = PawnEvalWhite[ hashKey ];
        if( node != null && node.bitBoard == bitBoard)
            return node;
        
        hashKey = (hashKey + 1000) % sizeTable;
        node = PawnEvalWhite[ hashKey ];
        if( node != null && node.bitBoard == bitBoard)
            return node;
        
        
        return null;
            
    }
    
    private long a = 1986910959;
    
    private int getHashKeyBlack(long bitBoard){
        long temp = (bitBoard ^ (bitBoard >> 15) << 7);
        temp |= a;
        
        int hashValue = (int) (temp % sizeTable);
        return ( hashValue >=0 )? hashValue : -hashValue; 
    }
    
    private int getHashKeyWhite(long bitBoard){
        int hashValue = (int) (bitBoard % sizeTable);
        return ( hashValue >=0 )? hashValue : -hashValue;
    }
    
    class Node{
        int eval;
        long bitBoard;

        public Node(int eval, long bitBoard) {
            this.eval = eval;
            this.bitBoard = bitBoard;
        }
        
        
    }
    
}
