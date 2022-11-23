/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evaluation_function;

/**
 *
 * @author Goran
 */
public class EvaluationHashTable {
    
    private static final int sizeTable = 1000000;
    NodeEval[] EvalTable =  new NodeEval[sizeTable];

    
    public void addPosition(long Zobrystkey, int eval){
        
        int hashKey = getHash(Zobrystkey);
        NodeEval node = new NodeEval(eval, Zobrystkey);
        EvalTable[hashKey] = node;
    
    }
    
    public NodeEval getEval(long ZobrystKey){
        int hashKey = getHash(ZobrystKey);
        NodeEval node = EvalTable[hashKey];
        if( node != null && node.ZobrystKey == ZobrystKey){
            return node;
        }
        
        return null;
    }
    
    private int getHash(long ZobrystKey){
        
        int hashValue = (int) (ZobrystKey % sizeTable);
        return ( hashValue >=0 )? hashValue : -hashValue; 
    }
            
            
   class NodeEval{
        int eval;
        long ZobrystKey;

        public NodeEval(int eval, long ZobrystKey) {
            this.eval = eval;
            this.ZobrystKey = ZobrystKey;
        }
        
        
    }
}


