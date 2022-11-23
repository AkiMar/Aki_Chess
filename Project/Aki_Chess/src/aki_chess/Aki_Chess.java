/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aki_chess;

import uci_protocol.*;
/**
 *
 * @author Goran
 */
public class Aki_Chess {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      
        UCI uciProtocol = new UCI();
        System.out.println("Pocelo");
        uciProtocol.uci_Communication();
        
    }
    
    
}
