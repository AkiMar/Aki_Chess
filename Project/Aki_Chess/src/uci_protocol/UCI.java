/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uci_protocol;

import java.util.*;
import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.*;
import negamax.NegaMax;
import openning_book.Openning_Book;

public class UCI {

    String Engine_Name = "Aki_Chess";
    Board board = new Board();
    int cntMove = 0;

    Openning_Book openningBook = new Openning_Book();
    boolean openingFlag = true;

    negamax.NegaMax negaMax = new NegaMax();
    static final double pawnValue = 100.0;
    
    public void uci_Communication() {
        Scanner input = new Scanner(System.in);
        while (true) {
            String inputString = input.nextLine();
            if ("uci".equals(inputString)) {
                startUCI();
            } else if (inputString.startsWith("setoption")) {
                setOption(inputString);
            } else if ("isready".equals(inputString)) {
                isReady();
            } else if ("ucinewgame".equals(inputString)) {
                UCINewGame();
            } else if (inputString.startsWith("position")) {
                inputPosition(inputString);
            } else if (inputString.startsWith("go")) {
                inputGo(inputString);
            } else if (inputString.startsWith("stop")) {
                inputGo(inputString);
            } else if (inputString.equals("quit")) {
                return;
            } else if ("print".equals(inputString)) {
                inputPrint();
            }
        }
    }

    public void startUCI() {
        System.out.println("id name " + Engine_Name);
        System.out.println("id author Aki");
        //options go here
        System.out.println("uciok");
    }

    public void setOption(String inputString) {
        //set options
    }

    public void isReady() {
        System.out.println("readyok");
    }

    public void UCINewGame() {
        this.board = new Board();
        this.cntMove = 0;
        this.openingFlag = true;
        System.out.println("newGame Started");
    }

    public void inputPosition(String input) {
        input = input.substring(9).concat(" ");
        //System.out.println(input);
        if (input.contains("fen")) {
            input = input.substring(4);
            this.board = new Board();
            this.cntMove = 0;
            this.openingFlag = true;
            this.board.loadFromFen(input);
            // nemoj koristiti uopste
            if (input.contains("moves")) {
                String[] moves = input.substring(input.indexOf("moves") + 6).split(" ");
                // napravi poteze
                for (int i = cntMove; i < moves.length; i++) {
                    Move move = moveDone(moves[i]);
                    System.out.println("Potez:" + moves[i]);
                    this.board.doMove(move);
                }
                cntMove = moves.length;
            }
        } else {
            if (input.contains("moves")) {
                String[] moves = input.substring(input.indexOf("moves") + 6).split(" ");
                // napravi poteze
                for (int i = cntMove; i < moves.length; i++) {
                    Move move = moveDone(moves[i]);
                    System.out.println("Potez:" + moves[i]);
                    this.board.doMove(move);
                }
                cntMove = moves.length;
            } else if (input.contains("startpos")) {
                input = input.substring(9);
                this.board = new Board();
                this.cntMove = 0;
                this.openingFlag = true;
            }
        }
    }

    public Move moveDone(String moveStr) {
        String pos1 = moveStr.substring(0, 2);
        String pos2 = moveStr.substring(2);
        Move move;
        if (pos2.length() == 2) {
            move = new Move(squarePosition(pos1), squarePosition(pos2));
        } else {
            move = new Move(squarePosition(pos1), squarePosition(pos2), piecePromotion(pos2));
        }
        return move;
    }

    public Piece piecePromotion(String pos2) {

        if (pos2.charAt(1) == '8') {
            switch (pos2.charAt(2)) {
                case 'q':
                    return Piece.WHITE_QUEEN;
                case 'r':
                    return Piece.WHITE_ROOK;
                case 'b':
                    return Piece.WHITE_BISHOP;
                case 'n':
                    return Piece.WHITE_KNIGHT;
                default:
                    return null;
            }
        } else {
            switch (pos2.charAt(2)) {
                case 'q':
                    return Piece.BLACK_QUEEN;
                case 'r':
                    return Piece.BLACK_ROOK;
                case 'b':
                    return Piece.BLACK_BISHOP;
                case 'n':
                    return Piece.BLACK_KNIGHT;
                default:
                    return null;
            }
        }
    }

    public Square squarePosition(String squarePos) {

        String position = squarePos.substring(0, 2);

        switch (position) {
            case "a1":
                return Square.A1;
            case "a2":
                return Square.A2;
            case "a3":
                return Square.A3;
            case "a4":
                return Square.A4;
            case "a5":
                return Square.A5;
            case "a6":
                return Square.A6;
            case "a7":
                return Square.A7;
            case "a8":
                return Square.A8;

            case "b1":
                return Square.B1;
            case "b2":
                return Square.B2;
            case "b3":
                return Square.B3;
            case "b4":
                return Square.B4;
            case "b5":
                return Square.B5;
            case "b6":
                return Square.B6;
            case "b7":
                return Square.B7;
            case "b8":
                return Square.B8;

            case "c1":
                return Square.C1;
            case "c2":
                return Square.C2;
            case "c3":
                return Square.C3;
            case "c4":
                return Square.C4;
            case "c5":
                return Square.C5;
            case "c6":
                return Square.C6;
            case "c7":
                return Square.C7;
            case "c8":
                return Square.C8;

            case "d1":
                return Square.D1;
            case "d2":
                return Square.D2;
            case "d3":
                return Square.D3;
            case "d4":
                return Square.D4;
            case "d5":
                return Square.D5;
            case "d6":
                return Square.D6;
            case "d7":
                return Square.D7;
            case "d8":
                return Square.D8;

            case "e1":
                return Square.E1;
            case "e2":
                return Square.E2;
            case "e3":
                return Square.E3;
            case "e4":
                return Square.E4;
            case "e5":
                return Square.E5;
            case "e6":
                return Square.E6;
            case "e7":
                return Square.E7;
            case "e8":
                return Square.E8;

            case "f1":
                return Square.F1;
            case "f2":
                return Square.F2;
            case "f3":
                return Square.F3;
            case "f4":
                return Square.F4;
            case "f5":
                return Square.F5;
            case "f6":
                return Square.F6;
            case "f7":
                return Square.F7;
            case "f8":
                return Square.F8;

            case "g1":
                return Square.G1;
            case "g2":
                return Square.G2;
            case "g3":
                return Square.G3;
            case "g4":
                return Square.G4;
            case "g5":
                return Square.G5;
            case "g6":
                return Square.G6;
            case "g7":
                return Square.G7;
            case "g8":
                return Square.G8;

            case "h1":
                return Square.H1;
            case "h2":
                return Square.H2;
            case "h3":
                return Square.H3;
            case "h4":
                return Square.H4;
            case "h5":
                return Square.H5;
            case "h6":
                return Square.H6;
            case "h7":
                return Square.H7;
            case "h8":
                return Square.H8;

            default:
                throw new IllegalArgumentException("Los potez");

        }
    }

    public void inputGo(String inputGo) {
        
        // openning move check
        if (openingFlag) {
            String fen = fenOpenning(board.getFen());
            String moveStr = this.openningBook.getMove(fen);
            System.out.println("Oppening:" + moveStr);
            if (moveStr != null) {
                Move move = moveDone(moveStr);
                board.doMove(move);
                cntMove++;
                System.out.println("bestmove " + move);
                return;
            }
            openingFlag = false;
        }
        
        String time = inputGo.substring(3);

        //nadji najbolji potez
        Integer cp;
        Move move = negaMax.getNextMove(board, time);
        cp = negaMax.getEval();
        board.doMove(move);
        cntMove++;
        System.out.println("bestmove " + move);
    }

    public void inputPrint() {
        System.out.println(this.board.toString());
    }

    public String fenOpenning(String fen) {

        String[] fields = fen.split(" ");
        fields[3] = "-";
        String rez = "";
        for (int i = 0; i < 5; i++) {
            rez += fields[i] + " ";
        }
        rez += fields[5];
        return rez;
    }

}
