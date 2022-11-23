/*
 * To change eval license header, choose License Headers in Project Properties.
 * To change eval template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negamax;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.File;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Rank;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import evaluation_function.Evaluation;
import java.util.List;

/**
 *
 * @author Goran
 */
public class ExplanationMove {

    private evaluation_function.Evaluation eval = new Evaluation();

    public void printExplanation(Board board, Move moveToPlay, int player) {
        // white player = 0, black player = 1
        System.out.println(" _______________________________________________________");
        System.out.println("    POSITION EXPLANATION    \n");
        System.out.println("Position in FEN: " + board.getFen());
        System.out.println("BestMove: " + moveToPlay + " \n");
        
        board.doMove(moveToPlay);
        
        if( player == 0){
            this.whiteBishopEval(board);
            this.whiteKnightOutpost(board);
            this.whiteRookEval(board);
            this.pawnChainProtectionEvalWhite(board);
            this.passedPawnWhite(board);
        }else{
            this.blackBishopEval(board);
            this.blackKnightOutpost(board);
            this.blackRookEval(board);
            this.pawnChainProtectionEvalBlack(board);
            this.passedPawnBlack(board);
        }
        
        board.undoMove();
        return;
    }

    private void whiteKnightOutpost(Board board) {

        List<Square> knight = board.getPieceLocation(Piece.WHITE_KNIGHT);
        for (Square squareKnight : knight) {
            boolean flagDefendedKnignt = false;
            Square[] squares = eval.getPawnSideSquaresWhite(squareKnight);
            for (Square sq : squares) {
                if (sq != null && board.getPiece(sq) == Piece.WHITE_PAWN) {
                    System.out.println("GOOD: Knight at " + squareKnight + " is defended by a pawn.");
                    flagDefendedKnignt = true;
                }
            }
            int rankKnight = eval.getRank(squareKnight.getRank());
            if (rankKnight >= 5 && flagDefendedKnignt) {
                int fileKnight = eval.getFile(squareKnight.getFile());
                List<Square> blackPawns = board.getPieceLocation(Piece.BLACK_PAWN);
                boolean outpostFlag = true;
                for (Square blackPawn : blackPawns) {
                    int filePawn = eval.getFile(blackPawn.getFile());
                    int rankPawn = eval.getRank(blackPawn.getRank());
                    if (rankPawn > rankKnight && (filePawn == (fileKnight - 1) || filePawn == (fileKnight + 1))) {
                        outpostFlag = false;
                        break;
                    }
                }

                if (outpostFlag) {
                    System.out.println("GREAT: Knight at " + squareKnight + " is an outpost knight what means can not be attacked by enemy pawns and is on enemy half of board");
                }
            }
        }
    }

    private void blackKnightOutpost(Board board) {

        List<Square> knight = board.getPieceLocation(Piece.BLACK_KNIGHT);
        int ret = 0;
        for (Square squareKnight : knight) {
            boolean flagDefendedKnignt = false;
            Square[] squares = eval.getPawnSideSquaresBlack(squareKnight);
            for (Square sq : squares) {
                if (sq != null && board.getPiece(sq) == Piece.BLACK_PAWN) {
                    System.out.println("GOOD: Knight at " + squareKnight + " is defended by a pawn.");
                    flagDefendedKnignt = true;
                }
            }
            int rankKnight = eval.getRank(squareKnight.getRank());
            if (rankKnight <= 4 && flagDefendedKnignt) {
                int fileKnight = eval.getFile(squareKnight.getFile());
                List<Square> whitePawns = board.getPieceLocation(Piece.WHITE_PAWN);
                boolean outpostFlag = true;
                for (Square blackPawn : whitePawns) {
                    int filePawn = eval.getFile(blackPawn.getFile());
                    int rankPawn = eval.getRank(blackPawn.getRank());
                    if (rankPawn < rankKnight && (filePawn == (fileKnight - 1) || filePawn == (fileKnight + 1))) {
                        outpostFlag = false;
                        break;
                    }
                }
                if (outpostFlag) {
                    System.out.println("GREAT: Knight at " + squareKnight + " is an outpost knight what means can not be attacked by enemy pawns and is on enemy half of board");
                }
            }
        }

    }

    private void whiteBishopEval(Board board) {

        List<Square> bishops = board.getPieceLocation(Piece.WHITE_BISHOP);
        for (Square squareBishop : bishops) {
            //defended
            boolean flagDefendedBishop = false;
            Square[] squares = eval.getPawnSideSquaresWhite(squareBishop);
            for (Square sq : squares) {
                if (sq != null && board.getPiece(sq) == Piece.WHITE_PAWN) {
                    System.out.println("GOOD: Bishop at " + squareBishop + " is defended by a pawn.");
                    flagDefendedBishop = true;
                }
            }

            int rankBishop = eval.getRank(squareBishop.getRank());
            int fileBishop = eval.getFile(squareBishop.getFile());
            //outpost
            if (flagDefendedBishop) {
                List<Square> blackPawns = board.getPieceLocation(Piece.BLACK_PAWN);
                boolean outpostFlag = true;
                for (Square blackPawn : blackPawns) {
                    int filePawn = eval.getFile(blackPawn.getFile());
                    int rankPawn = eval.getRank(blackPawn.getRank());
                    if (rankPawn > rankBishop && (filePawn == (fileBishop - 1) || filePawn == (fileBishop + 1))) {
                        outpostFlag = false;
                        break;
                    }
                }
                if (outpostFlag) {
                    System.out.println("GREAT: Bishop at " + squareBishop + " is an outpost bishop what means can not be attacked by enemy pawns");
                }
            }

            //badBishop
            Square[] fowardSquares = eval.getSquareBadBishopWhite(squareBishop);
            int i = 0;
            for (Square fowardSquare : fowardSquares) {

                if (fowardSquare != null && board.getPiece(fowardSquare) == Piece.WHITE_PAWN) {
                    // blokiran bishop
                    int filePawn = eval.getFile(fowardSquare.getFile());
                    if (filePawn > 2 && filePawn < 7) {
                        System.out.println("VERY BAD: Bishop at " + squareBishop + " is blocked by your pawn at " + fowardSquare + " in the middle of board.");
                    } else {
                        System.out.println("BAD: Bishop at " + squareBishop + " is blocked by your pawn at" + fowardSquare + ".");
                    }
                }
                i++;
            }

        }

    }

    private void blackBishopEval(Board board) {

        List<Square> bishops = board.getPieceLocation(Piece.BLACK_BISHOP);
        for (Square squareBishop : bishops) {
            //defended
            boolean flagDefendedBishop = false;
            Square[] squares = eval.getPawnSideSquaresBlack(squareBishop);
            for (Square sq : squares) {
                if (sq != null && board.getPiece(sq) == Piece.BLACK_PAWN) {
                    System.out.println("GOOD: Bishop at " + squareBishop + " is defended by a pawn.");
                    flagDefendedBishop = true;
                }
            }

            int rankBishop = eval.getRank(squareBishop.getRank());
            int fileBishop = eval.getFile(squareBishop.getFile());
            //outpost
            if (flagDefendedBishop) {
                List<Square> whitePawns = board.getPieceLocation(Piece.WHITE_PAWN);
                boolean outpostFlag = true;
                for (Square whitePawn : whitePawns) {
                    int filePawn = eval.getFile(whitePawn.getFile());
                    int rankPawn = eval.getRank(whitePawn.getRank());
                    if (rankPawn < rankBishop && (filePawn == (fileBishop - 1) || filePawn == (fileBishop + 1))) {
                        outpostFlag = false;
                        break;
                    }
                }
                if (outpostFlag) {
                    System.out.println("GREAT: Bishop at " + squareBishop + " is an outpost bishop what means can not be attacked by enemy pawns");
                }
            }

            //badBishop
            Square[] fowardSquares = eval.getSquareBadBishopBlack(squareBishop);
            int i = 0;
            for (Square fowardSquare : fowardSquares) {

                if (fowardSquare != null && board.getPiece(fowardSquare) == Piece.BLACK_PAWN) {
                    // blokiran bishop
                    int filePawn = eval.getFile(fowardSquare.getFile());
                    if (filePawn > 2 && filePawn < 7) {
                        System.out.println("VERY BAD: Bishop at " + squareBishop + " is blocked by your pawn at" + fowardSquare + "in the middle of board.");
                    } else {
                        System.out.println("BAD: Bishop at " + squareBishop + " is blocked by your pawn at" + fowardSquare + ".");
                    }

                    if (i % 2 == 0) {
                        i++;
                    }
                }
                i++;
            }

        }

    }

    private void whiteRookEval(Board board) {
        List<Square> rooks = board.getPieceLocation(Piece.WHITE_ROOK);
        if (rooks.size() == 0) {
            return;
        }
        // open and semi-open file
        for (Square rook : rooks) {
            if (eval.open_SemiOpenFile(rook.getFile(), board, 0) > 0) {
                System.out.println("GREAT: Rook at " + rook + " is on opne/semi-open file.");
            }
            // 7/8 rank infiltration
            if( rook.getRank() == Rank.RANK_7 || rook.getRank() == Rank.RANK_8){
                System.out.println("GREAT: Rook at " + rook + " has infiltrade on enemy rank " + rook.getRank() + ".");
            }
        }
        // rooks connected 
        if (rooks.size() == 2) {
            if ( eval.rooksConnected(rooks.get(0), rooks.get(1), board) > 0){
                System.out.println("GREAT: Rooks are connected");
            }
        }
        // x-ray crnu damu
        List<Square> sqBlackQueen = board.getPieceLocation(Piece.BLACK_QUEEN);
        if (sqBlackQueen.isEmpty()) {
            return;
        }

        Square queenBlack = sqBlackQueen.get(0);
        for (Square rook : rooks) {
            if( eval.xRayQueenByRook(rook, queenBlack) > 0 ){
                System.out.println("GOOD: Rook at " + rook  + " is X-raying enemy queen");
            }
        }
    }

    private void blackRookEval(Board board) {
        List<Square> rooks = board.getPieceLocation(Piece.BLACK_ROOK);
        if (rooks.size() == 0) {
            return;
        }
        // open and semi-open file
        for (Square rook : rooks) {
            if( eval.open_SemiOpenFile(rook.getFile(), board, 1) > 0){
                System.out.println("GREAT: Rook at " + rook + " is on opne/semi-open file.");
            }
            // 1/2 rank infiltration
            if( rook.getRank() == Rank.RANK_1 || rook.getRank() == Rank.RANK_2){
                System.out.println("GREAT: Rook at " + rook + " has infiltrade on enemy rank " + rook.getRank() + ".");
            }
        }
        // rooks connected 
        if (rooks.size() == 2) {
            if( eval.rooksConnected(rooks.get(0), rooks.get(1), board) > 0 ){
               System.out.println("GREAT: Rooks are connected"); 
            }
        }
        // x-ray belu damu
        List<Square> sqWhiteQueen = board.getPieceLocation(Piece.WHITE_QUEEN);
        if (sqWhiteQueen.isEmpty()) {
            return;
        }

        Square queenWhite = sqWhiteQueen.get(0);
        for (Square rook : rooks) {
            if( eval.xRayQueenByRook(rook, queenWhite) > 0 ){
                System.out.println("GOOD: Rook at " + rook  + " is X-raying enemy queen");
            }
        }

        return;
    }

    private void pawnChainProtectionEvalWhite(Board board) {

        List<Square> pawnsWhite = board.getPieceLocation(Piece.WHITE_PAWN);
        // pawn structure
        for (Square pawn : pawnsWhite) {
            Square[] squares = eval.getPawnSideSquaresWhite(pawn);
            if (squares[0] != null) {
                if (board.getPiece(squares[0]) == Piece.WHITE_PAWN) {
                    System.out.println("GOOD: Pawn  at " + pawn + " is protected by anatoher pawn." );
                    continue;
                }
            }
            if (squares[1] != null) {
                if (board.getPiece(squares[1]) == Piece.WHITE_PAWN) {
                    System.out.println("GOOD: Pawn  at " + pawn + " is protected by anatoher pawn." );
                }
            }
        }
        return;
    }
    
    private void pawnChainProtectionEvalBlack(Board board) {

        List<Square> pawnsBlack = board.getPieceLocation(Piece.BLACK_PAWN);
        // pawn structure
        for (Square pawn : pawnsBlack) {
            Square[] squares = eval.getPawnSideSquaresBlack(pawn);
            if (squares[0] != null) {
                if (board.getPiece(squares[0]) == Piece.BLACK_PAWN) {
                    System.out.println("GOOD: Pawn  at " + pawn + " is protected by anatoher pawn." );
                    continue;
                }
            }
            if (squares[1] != null) {
                if (board.getPiece(squares[1]) == Piece.BLACK_PAWN) {
                    System.out.println("GOOD: Pawn  at " + pawn + " is protected by anatoher pawn." );
                }
            }
        }
        return;
    }

    private void passedPawnWhite(Board board){
    
        List<Square> pawnsWhite = board.getPieceLocation(Piece.WHITE_PAWN);
        // pawn structure
        for (Square pawn : pawnsWhite) {
            boolean passedPawnFlag = true;
            File FilePawn = pawn.getFile();
            int rank =eval.getRank(pawn.getRank());
            int file =eval.getFile(FilePawn);
            int index = (rank - 1) * 8 + (file - 1);
            // obrada svih ispred polja da li ima crnog piuna ide se ofc +7, +8 i +9 u odnosu na index sve dok je index < 56 = 7 * 8
            while( index < 56){
                
                int index1 = index + 7;
                int index2 = index + 8;
                int index3 = index + 9;
                if( FilePawn != File.FILE_A && board.getPiece(Square.squareAt(index1)) == Piece.BLACK_PAWN){
                    passedPawnFlag = false;
                    break;
                }
                if( board.getPiece(Square.squareAt(index2)) == Piece.BLACK_PAWN){
                    passedPawnFlag = false;
                    break;
                }
                if( FilePawn != File.FILE_H && board.getPiece(Square.squareAt(index3)) == Piece.BLACK_PAWN){
                    passedPawnFlag = false;
                    break;
                }
                index += 8;
            }
            if( passedPawnFlag ){
                System.out.println("GREAT: Pawn  at " + pawn + " is a passed pawn, later in a game it can be converted to a queen." );
            }
        }
        return;
    
    }
    
    private void passedPawnBlack(Board board){
    
        List<Square> pawnsBlack = board.getPieceLocation(Piece.BLACK_PAWN);
        // pawn structure
        for (Square pawn : pawnsBlack) {
            boolean passedPawnFlag = true;
            File FilePawn = pawn.getFile();
            int rank =eval.getRank(pawn.getRank());
            int file =eval.getFile(FilePawn);
            int index = (rank - 1) * 8 + (file - 1);
            // obrada svih ispred polja da li ima belog piuna ide se ofc -7, -8 i -9 u odnosu na index sve dok je index > 7 = 1 * 7
            while( index > 7){
                
                int index1 = index - 7;
                int index2 = index - 8;
                int index3 = index - 9;
                if( FilePawn != File.FILE_H && board.getPiece(Square.squareAt(index1)) == Piece.WHITE_PAWN){
                    passedPawnFlag = false;
                    break;
                }
                if( board.getPiece(Square.squareAt(index2)) == Piece.WHITE_PAWN){
                    passedPawnFlag = false;
                    break;
                }
                if( FilePawn != File.FILE_A && board.getPiece(Square.squareAt(index3)) == Piece.WHITE_PAWN){
                    passedPawnFlag = false;
                    break;
                }
                index -= 8;
            }
            if( passedPawnFlag ){
                System.out.println("GREAT: Pawn  at " + pawn + " is a passed pawn, later in a game it can be converted to a queen." );
            }
        }
        return;
    
    }
    
    
    
}
