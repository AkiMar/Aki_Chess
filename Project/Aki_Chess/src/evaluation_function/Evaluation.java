/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evaluation_function;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.File;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.PieceType;
import com.github.bhlangonijr.chesslib.Rank;
import com.github.bhlangonijr.chesslib.Square;
import evaluation_function.EvaluationHashTable.NodeEval;
import evaluation_function.PawnHash.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Goran
 */
public class Evaluation {

    PawnHash pawnHashTableMg = new PawnHash();
    PawnHash pawnHashTableEg = new PawnHash();
    public static int hit = 0;
    public static int miss = 0;

    EvaluationHashTable evalHashTable = new EvaluationHashTable();

    Piece[] allPieces = Piece.allPieces;

    int[] mg_pawn_Table;
    int[] mg_knight_Table;
    int[] mg_bishop_Table;
    int[] mg_rook_Table;
    int[] mg_queen_Table;
    int[] mg_king_Table;

    int[] eg_pawn_Table;
    int[] eg_knight_Table;
    int[] eg_bishop_Table;
    int[] eg_rook_Table;
    int[] eg_queen_Table;
    int[] eg_king_Table;

    static final int TotalPhase = 24;
    static final int KingAttacked = 30; // dodatna evaluacija za sah
    public static final int MaxValue = 100000;

    public Evaluation() {

        init_Tables();
        for (int i = 0; i < 64; i++) {

            mg_pawn_Table[i] += 82;
            mg_knight_Table[i] += 337;
            mg_bishop_Table[i] += 365;
            mg_rook_Table[i] += 477;
            mg_queen_Table[i] += 1025;
            mg_king_Table[i] += 12000;

            eg_pawn_Table[i] += 94;
            eg_knight_Table[i] += 281;
            eg_bishop_Table[i] += 297;
            eg_rook_Table[i] += 512;
            eg_queen_Table[i] += 936;
            eg_king_Table[i] += 12000;

        }
        // igraj se i ti malo sa ovim evaluacijama :)

    }

    public void init_Tables() {

        mg_pawn_Table = new int[]{
            0, 0, 0, 0, 0, 0, 0, 0,
            98, 134, 61, 95, 68, 126, 34, -11,
            -6, 7, 26, 31, 65, 56, 25, -20,
            -14, 13, 6, 21, 23, 12, 17, -23,
            -27, -2, -5, 12, 17, 6, 10, -25,
            -26, -4, -4, -10, 3, 3, 33, -12,
            -35, -1, -20, -23, -15, 24, 38, -22,
            0, 0, 0, 0, 0, 0, 0, 0,};

        eg_pawn_Table = new int[]{
            0, 0, 0, 0, 0, 0, 0, 0,
            178, 173, 158, 134, 147, 132, 165, 187,
            94, 100, 85, 67, 56, 53, 82, 84,
            32, 24, 13, 5, -2, 4, 17, 17,
            13, 9, -3, -7, -7, -8, 3, -1,
            4, 7, -6, 1, 0, -5, -1, -8,
            13, 8, 8, 10, 13, 0, 2, -7,
            0, 0, 0, 0, 0, 0, 0, 0,};

        mg_knight_Table = new int[]{
            -167, -89, -34, -49, 61, -97, -15, -107,
            -73, -41, 72, 36, 23, 62, 7, -17,
            -47, 60, 37, 65, 84, 129, 73, 44,
            -9, 17, 19, 53, 37, 69, 18, 22,
            -13, 4, 16, 13, 28, 19, 21, -8,
            -23, -9, 12, 10, 19, 17, 25, -16,
            -29, -53, -12, -3, -1, 18, -14, -19,
            -105, -21, -58, -33, -17, -28, -19, -23,};

        eg_knight_Table = new int[]{
            -58, -38, -13, -28, -31, -27, -63, -99,
            -25, -8, -25, -2, -9, -25, -24, -52,
            -24, -20, 10, 9, -1, -9, -19, -41,
            -17, 3, 22, 22, 22, 11, 8, -18,
            -18, -6, 16, 25, 16, 17, 4, -18,
            -23, -3, -1, 15, 10, -3, -20, -22,
            -42, -20, -10, -5, -2, -20, -23, -44,
            -29, -51, -23, -15, -22, -18, -50, -64,};

        mg_bishop_Table = new int[]{
            -29, 4, -82, -37, -25, -42, 7, -8,
            -26, 16, -18, -13, 30, 59, 18, -47,
            -16, 37, 43, 40, 35, 50, 37, -2,
            -4, 5, 19, 50, 37, 37, 7, -2,
            -6, 13, 13, 26, 34, 12, 10, 4,
            0, 15, 15, 15, 14, 27, 18, 10,
            4, 15, 16, 0, 7, 21, 33, 1,
            -33, -3, -14, -21, -13, -12, -39, -21,};

        eg_bishop_Table = new int[]{
            -14, -21, -11, -8, -7, -9, -17, -24,
            -8, -4, 7, -12, -3, -13, -4, -14,
            2, -8, 0, -1, -2, 6, 0, 4,
            -3, 9, 12, 9, 14, 10, 3, 2,
            -6, 3, 13, 19, 7, 10, -3, -9,
            -12, -3, 8, 10, 13, 3, -7, -15,
            -14, -18, -7, -1, 4, -9, -15, -27,
            -23, -9, -23, -5, -9, -16, -5, -17,};

        mg_rook_Table = new int[]{
            32, 42, 32, 51, 63, 9, 31, 43,
            27, 32, 58, 62, 80, 67, 26, 44,
            -5, 19, 26, 36, 17, 45, 61, 16,
            -24, -11, 7, 26, 24, 35, -8, -20,
            -36, -26, -12, -1, 9, -7, 6, -23,
            -45, -25, -16, -17, 3, 0, -5, -33,
            -44, -16, -20, -9, -1, 11, -6, -71,
            -19, -13, 1, 17, 16, 7, -37, -26,};

        eg_rook_Table = new int[]{
            13, 10, 18, 15, 12, 12, 8, 5,
            11, 13, 13, 11, -3, 3, 8, 3,
            7, 7, 7, 5, 4, -3, -5, -3,
            4, 3, 13, 1, 2, 1, -1, 2,
            3, 5, 8, 4, -5, -6, -8, -11,
            -4, 0, -5, -1, -7, -12, -8, -16,
            -6, -6, 0, 2, -9, -9, -11, -3,
            -9, 2, 3, -1, -5, -13, 4, -20,};

        mg_queen_Table = new int[]{
            -28, 0, 29, 12, 59, 44, 43, 45,
            -24, -39, -5, 1, -16, 57, 28, 54,
            -13, -17, 7, 8, 29, 56, 47, 57,
            -27, -27, -16, -16, -1, 17, -2, 1,
            -9, -26, -9, -10, -2, -4, 3, -3,
            -14, 2, -11, -2, -5, 2, 14, 5,
            -35, -8, 11, 2, 8, 15, -3, 1,
            -1, -18, -9, 10, -15, -25, -31, -50,};

        eg_queen_Table = new int[]{
            -9, 22, 22, 27, 27, 19, 10, 20,
            -17, 20, 32, 41, 58, 25, 30, 0,
            -20, 6, 9, 49, 47, 35, 19, 9,
            3, 22, 24, 45, 57, 40, 57, 36,
            -18, 28, 19, 47, 31, 34, 39, 23,
            -16, -27, 15, 6, 9, 17, 10, 5,
            -22, -23, -30, -16, -16, -23, -36, -32,
            -33, -28, -22, -43, -5, -32, -20, -41,};

        mg_king_Table = new int[]{
            -65, 23, 16, -15, -56, -34, 2, 13,
            29, -1, -20, -7, -8, -4, -38, -29,
            -9, 24, 2, -16, -20, 6, 22, -22,
            -17, -20, -12, -27, -30, -25, -14, -36,
            -49, -1, -27, -39, -46, -44, -33, -51,
            -14, -14, -22, -46, -44, -30, -15, -27,
            1, 7, -8, -64, -43, -16, 9, 8,
            -15, 66, 12, -54, 8, -28, 24, 14,};

        eg_king_Table = new int[]{
            -74, -35, -18, -18, -11, 15, 4, -17,
            -12, 17, 14, 17, 17, 38, 23, 11,
            10, 17, 23, 15, 20, 45, 44, 13,
            -8, 22, 24, 27, 26, 33, 26, 3,
            -18, -4, 21, 24, 27, 23, 9, -11,
            -19, -3, 11, 21, 23, 16, 7, -9,
            -27, -11, 4, 13, 14, 4, -5, -17,
            -53, -34, -21, -11, -28, -14, -24, -43
        };

    }

    public int MaterialEval(Board board, int ply) {

        // nereseno
        if (board.isDraw() || board.isStaleMate() || board.isRepetition() || board.isRepetition(2) || board.isInsufficientMaterial()) {
            return 0;
        }

        // mat: ako beli igra i mat je znaci crni je pobedio i obrnuto
        if (board.isMated()) {
            switch (board.getSideToMove().name()) {
                case "WHITE":
                    return -MaxValue + 1000*ply;
                case "BLACK":
                    return MaxValue - 1000*ply;
            }
        }

        long ZobrystKey = board.getZobristKey();
        NodeEval node = evalHashTable.getEval(ZobrystKey);
        if (node != null) {
            hit++;
            return node.eval;
        }
        miss++;

        int mg_White = 0;
        int eg_White = 0;
        int mg_Black = 0;
        int eg_Black = 0;
        double phase = TotalPhase;
        List<Square> list;

        int i = 0;
        int EvalWhitePawnMg = 0;
        int EvalWhitePawnEg = 0;
        int EvalBlackPawnMg = 0;
        int EvalBlackPawnEg = 0;
        long whiteBitBoard = board.getBitboard(Piece.WHITE_PAWN);
        long blackBitBoard = board.getBitboard(Piece.BLACK_PAWN);
        for (Piece piece : allPieces) {

            if (i == 0) { // beli Pawn je prvi
                Node nodeMg = pawnHashTableMg.getEvalWhite(whiteBitBoard);
                if (nodeMg != null && nodeMg.bitBoard == whiteBitBoard) {
                    Node nodeEg = pawnHashTableEg.getEvalWhite(whiteBitBoard);
                    mg_White += nodeMg.eval;
                    eg_White += nodeEg.eval;

                    i++;
                    continue;
                }

            }

            if (i == 6) { // crni pawn je prvi od crnih figura
                Node nodeMg = pawnHashTableMg.getEvalBlack(blackBitBoard);
                if (nodeMg != null && nodeMg.bitBoard == blackBitBoard) {
                    Node nodeEg = pawnHashTableEg.getEvalBlack(blackBitBoard);
                    mg_Black += nodeMg.eval;
                    eg_Black += nodeEg.eval;
                    i++;
                    continue;
                }
            }

            list = board.getPieceLocation(piece);
            for (Square square : list) {

                if (i < 6) { // prvo idu 6 belih figura
                    if (i == 0) { // white pawn
                        int temp = mg_whitePieceEval(piece, square);
                        EvalWhitePawnMg += temp;
                        mg_White += temp;

                        temp = eg_whitePieceEval(piece, square);
                        eg_White += temp;
                        EvalWhitePawnEg += temp;
                        
                        continue;
                    }
                    mg_White += mg_whitePieceEval(piece, square);
                    eg_White += eg_whitePieceEval(piece, square);
                } else {
                    if (i == 6) { // black pawn
                        int temp = mg_blackPieceEval(piece, square);
                        EvalBlackPawnMg += temp;
                        mg_Black += temp;

                        temp = eg_blackPieceEval(piece, square);
                        eg_Black += temp;
                        EvalBlackPawnEg += temp;
                        
                        continue;
                    }
                    mg_Black += mg_blackPieceEval(piece, square);
                    eg_Black += eg_blackPieceEval(piece, square);
                }
                phase -= phaseEval(piece);
            }
            if (i == 0) {
                //pawn chain and passedPanws
                int value =  this.pawnChainProtectionEvalWhite(board);
                
                EvalWhitePawnEg += value;
                EvalWhitePawnMg += value;
                eg_White += value;
                mg_White += value;
                
                value = this.passedPawnWhite(board);
                
                EvalWhitePawnEg += value;
                EvalWhitePawnMg += value >> 2;
                eg_White += value;
                mg_White += value >> 2;
                
                pawnHashTableMg.addPositionWhite(whiteBitBoard, EvalWhitePawnMg);
                pawnHashTableEg.addPositionWhite(whiteBitBoard, EvalWhitePawnEg);
            }
            if (i == 6) {
                //pawn chain and passedPanws
                int value =  this.pawnChainProtectionEvalBlack(board);
                
                EvalBlackPawnEg += value;
                EvalBlackPawnMg += value;
                eg_Black += value;
                mg_Black += value;
                
                value = this.passedPawnBlack(board);
                
                EvalBlackPawnEg += value;
                EvalBlackPawnMg += value >> 2;
                eg_Black += value;
                mg_Black += value >> 2;
                        
                pawnHashTableMg.addPositionBlack(blackBitBoard, EvalBlackPawnMg);
                pawnHashTableEg.addPositionBlack(blackBitBoard, EvalBlackPawnEg);
            }
            i++;
        }
        
        // knight Outpust check
        int whitePiece = this.whiteKnightOutpost(board);
        int blackPiece = this.blackKnightOutpost(board);

        //bishop evaluation
        whitePiece += this.whiteBishopEval(board);
        blackPiece += this.blackBishopEval(board);

        //rook evaluation
        whitePiece += this.whiteRookEval(board);
        blackPiece += this.blackRookEval(board);
        

        mg_White += whitePiece;
        eg_White += whitePiece;
        mg_Black += blackPiece;
        eg_Black += blackPiece;
        

        if (phase > 24) {
            phase = 24;
        }
        phase = (phase * 256 + (TotalPhase / 2)) / TotalPhase;
        int mg_Eval = mg_White - mg_Black;
        int eg_Eval = eg_White - eg_Black;
        

        double eval = ((mg_Eval * (256 - phase)) + (eg_Eval * phase)) / 256;

        //sah evaluacija
        eval += checkEval(board);

        evalHashTable.addPosition(ZobrystKey, (int) eval);
        return (int) eval;
    }

    public int getRank(Rank r) {

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

    public int getFile(File f) {

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

    public int mg_whitePieceEval(Piece piece, Square sq) {

        int index = (8 - getRank(sq.getRank())) * 8 + getFile(sq.getFile()) - 1;

        String type = piece.getFenSymbol();
        switch (type) {
            case "P":
                return mg_pawn_Table[index];
            case "N":
                return mg_knight_Table[index];
            case "B":
                return mg_bishop_Table[index];
            case "R":
                return mg_rook_Table[index];
            case "Q":
                return mg_queen_Table[index];
            case "K":
                return mg_king_Table[index];
            default:
                throw new IllegalArgumentException("Los Simbol: ");
        }

    }

    public int eg_whitePieceEval(Piece piece, Square sq) {

        int index = (8 - getRank(sq.getRank())) * 8 + getFile(sq.getFile()) - 1;

        String type = piece.getFenSymbol();
        switch (type) {
            case "P":
                return eg_pawn_Table[index];
            case "N":
                return eg_knight_Table[index];
            case "B":
                return eg_bishop_Table[index];
            case "R":
                return eg_rook_Table[index];
            case "Q":
                return eg_queen_Table[index];
            case "K":
                return eg_king_Table[index];
            default:
                throw new IllegalArgumentException("Los Simbol: ");
        }

    }

    public int mg_blackPieceEval(Piece piece, Square sq) {

        int index = ((8 - getRank(sq.getRank())) * 8 + getFile(sq.getFile()) - 1) ^ 56;

        String type = piece.getFenSymbol();
        switch (type) {
            case "p":
                return mg_pawn_Table[index];
            case "n":
                return mg_knight_Table[index];
            case "b":
                return mg_bishop_Table[index];
            case "r":
                return mg_rook_Table[index];
            case "q":
                return mg_queen_Table[index];
            case "k":
                return mg_king_Table[index];
            default:
                throw new IllegalArgumentException("Los Simbol: ");
        }

    }

    public int eg_blackPieceEval(Piece piece, Square sq) {

        int index = ((8 - getRank(sq.getRank())) * 8 + getFile(sq.getFile()) - 1) ^ 56;

        String type = piece.getFenSymbol();
        switch (type) {
            case "p":
                return eg_pawn_Table[index];
            case "n":
                return eg_knight_Table[index];
            case "b":
                return eg_bishop_Table[index];
            case "r":
                return eg_rook_Table[index];
            case "q":
                return eg_queen_Table[index];
            case "k":
                return eg_king_Table[index];
            default:
                throw new IllegalArgumentException("Los Simbol: ");
        }

    }

    public int phaseEval(Piece piece) {

        String type = piece.getFenSymbol();
        switch (type) {
            case "P":
                return 0;
            case "N":
                return 1;
            case "B":
                return 1;
            case "R":
                return 2;
            case "Q":
                return 4;
            case "K":
                return 0;
            case "p":
                return 0;
            case "n":
                return 1;
            case "b":
                return 1;
            case "r":
                return 2;
            case "q":
                return 4;
            case "k":
                return 0;
            default:
                throw new IllegalArgumentException("Los Simbol: ");
        }

    }

    public int checkEval(Board board) {
        if (board.isKingAttacked()) {
            switch (board.getSideToMove().name()) {
                case "WHITE":
                    return -KingAttacked; // beli je u sahu
                case "BLACK":
                    return KingAttacked; // crni je u sahu

                default:
                    throw new IllegalArgumentException("Losa tabla: ");
            }
        } else {
            return 0;
        }
    }

    private int whiteKnightOutpost(Board board) {

        List<Square> knight = board.getPieceLocation(Piece.WHITE_KNIGHT);
        int ret = 0;
        for (Square squareKnight : knight) {
            boolean flagDefendedKnignt = false;
            Square[] squares = this.getPawnSideSquaresWhite(squareKnight);
            for (Square sq : squares) {
                if (sq != null && board.getPiece(sq) == Piece.WHITE_PAWN) {
                    ret += 7;
                    flagDefendedKnignt = true;
                }
            }
            int rankKnight = getRank(squareKnight.getRank());
            if (rankKnight >= 5 && flagDefendedKnignt) {
                int fileKnight = getFile(squareKnight.getFile());
                List<Square> blackPawns = board.getPieceLocation(Piece.BLACK_PAWN);
                int add = 20;
                for (Square blackPawn : blackPawns) {
                    int filePawn = getFile(blackPawn.getFile());
                    int rankPawn = getRank(blackPawn.getRank());
                    if (rankPawn > rankKnight && (filePawn == (fileKnight - 1) || filePawn == (fileKnight + 1))) {
                        add = 0;
                        break;
                    }
                }
                ret += add;
            }
        }

        return ret;
    }

    private int blackKnightOutpost(Board board) {

        List<Square> knight = board.getPieceLocation(Piece.BLACK_KNIGHT);
        int ret = 0;
        for (Square squareKnight : knight) {
            boolean flagDefendedKnignt = false;
            Square[] squares = this.getPawnSideSquaresBlack(squareKnight);
            for (Square sq : squares) {
                if (sq != null && board.getPiece(sq) == Piece.BLACK_PAWN) {
                    ret += 7;
                    flagDefendedKnignt = true;
                }
            }
            int rankKnight = getRank(squareKnight.getRank());
            if (rankKnight <= 4 && flagDefendedKnignt) {
                int fileKnight = getFile(squareKnight.getFile());
                List<Square> whitePawns = board.getPieceLocation(Piece.WHITE_PAWN);
                int add = 20;
                for (Square blackPawn : whitePawns) {
                    int filePawn = getFile(blackPawn.getFile());
                    int rankPawn = getRank(blackPawn.getRank());
                    if (rankPawn < rankKnight && (filePawn == (fileKnight - 1) || filePawn == (fileKnight + 1))) {
                        add = 0;
                        break;
                    }
                }
                ret += add;
            }
        }

        return ret;
    }

    private int whiteBishopEval(Board board) {

        List<Square> bishops = board.getPieceLocation(Piece.WHITE_BISHOP);
        int ret = 0;
        for (Square squareBishop : bishops) {
            //defended
            boolean flagDefendedBishop = false;
            Square[] squares = this.getPawnSideSquaresWhite(squareBishop);
            for (Square sq : squares) {
                if (sq != null && board.getPiece(sq) == Piece.WHITE_PAWN) {
                    ret += 15;
                    flagDefendedBishop = true;
                }
            }

            int rankBishop = getRank(squareBishop.getRank());
            int fileBishop = getFile(squareBishop.getFile());
            //outpost
            if (flagDefendedBishop) {
                List<Square> blackPawns = board.getPieceLocation(Piece.BLACK_PAWN);
                int add = 15;
                for (Square blackPawn : blackPawns) {
                    int filePawn = getFile(blackPawn.getFile());
                    int rankPawn = getRank(blackPawn.getRank());
                    if (rankPawn > rankBishop && (filePawn == (fileBishop - 1) || filePawn == (fileBishop + 1))) {
                        add = 0;
                        break;
                    }
                }
                ret += add;
            }

            //badBishop
            Square[] fowardSquares = getSquareBadBishopWhite(squareBishop);
            int minus = 0;
            int i = 0;
            for (Square fowardSquare : fowardSquares) {

                if (fowardSquare != null && board.getPiece(fowardSquare) == Piece.WHITE_PAWN) {
                    // blokiran bishop
                    minus += 30 >> (i % 2);
                    int filePawn = getFile(fowardSquare.getFile());
                    if (filePawn > 2 && filePawn < 7) {
                        minus += 10;
                    }
                }
                i++;
            }

            ret -= minus;

        }

        return ret;

    }

    private int blackBishopEval(Board board) {

        List<Square> bishops = board.getPieceLocation(Piece.BLACK_BISHOP);
        int ret = 0;
        for (Square squareBishop : bishops) {
            //defended
            boolean flagDefendedBishop = false;
            Square[] squares = this.getPawnSideSquaresBlack(squareBishop);
            for (Square sq : squares) {
                if (sq != null && board.getPiece(sq) == Piece.BLACK_PAWN) {
                    ret += 15;
                    flagDefendedBishop = true;
                }
            }

            int rankBishop = getRank(squareBishop.getRank());
            int fileBishop = getFile(squareBishop.getFile());
            //outpost
            if (flagDefendedBishop) {
                List<Square> whitePawns = board.getPieceLocation(Piece.WHITE_PAWN);
                int add = 15;
                for (Square whitePawn : whitePawns) {
                    int filePawn = getFile(whitePawn.getFile());
                    int rankPawn = getRank(whitePawn.getRank());
                    if (rankPawn < rankBishop && (filePawn == (fileBishop - 1) || filePawn == (fileBishop + 1))) {
                        add = 0;
                        break;
                    }
                }
                ret += add;
            }

            //badBishop
            Square[] fowardSquares = getSquareBadBishopBlack(squareBishop);
            int minus = 0;
            int i = 0;
            for (Square fowardSquare : fowardSquares) {

                if (fowardSquare != null && board.getPiece(fowardSquare) == Piece.BLACK_PAWN) {
                    // blokiran bishop
                    minus += 30 >> (i % 2);
                    int filePawn = getFile(fowardSquare.getFile());
                    if (filePawn > 2 && filePawn < 7) {
                        minus += 10;
                    }

                    if (i % 2 == 0) {
                        i++;
                    }
                }
                i++;
            }

            ret -= minus;

        }

        return ret;

    }

    public int whiteRookEval(Board board) {
        int ret = 0;
        List<Square> rooks = board.getPieceLocation(Piece.WHITE_ROOK);
        if (rooks.size() == 0) {
            return 0;
        }
        // open and semi-open file
        for (Square rook : rooks) {
            ret += this.open_SemiOpenFile(rook.getFile(), board, 0);
        }
        // rooks connected 
        if (rooks.size() == 2) {
            ret += this.rooksConnected(rooks.get(0), rooks.get(1), board);
        }
        // x-ray crnu damu
        List<Square> sqBlackQueen = board.getPieceLocation(Piece.BLACK_QUEEN);
        if (sqBlackQueen.isEmpty()) {
            return ret;
        }

        Square queenBlack = sqBlackQueen.get(0);
        for (Square rook : rooks) {
            ret += this.xRayQueenByRook(rook, queenBlack);
        }

        return ret;
    }

    public int blackRookEval(Board board) {
        int ret = 0;
        List<Square> rooks = board.getPieceLocation(Piece.BLACK_ROOK);
        if (rooks.size() == 0) {
            return 0;
        }
        // open and semi-open file
        for (Square rook : rooks) {
            ret += this.open_SemiOpenFile(rook.getFile(), board, 1);
        }
        // rooks connected 
        if (rooks.size() == 2) {
            ret += this.rooksConnected(rooks.get(0), rooks.get(1), board);
        }
        // x-ray belu damu
        List<Square> sqWhiteQueen = board.getPieceLocation(Piece.WHITE_QUEEN);
        if (sqWhiteQueen.isEmpty()) {
            return ret;
        }

        Square queenWhite = sqWhiteQueen.get(0);
        for (Square rook : rooks) {
            ret += this.xRayQueenByRook(rook, queenWhite);
        }

        return ret;
    }

    public Square[] getPawnSideSquaresWhite(Square sq) {
        Square[] squares = new Square[2];
        int rank = getRank(sq.getRank());
        int file = getFile(sq.getFile());
        int index = (rank - 1) * 8 + (file - 1);

        if (rank <= 2) {
            return squares;
        }

        if (file != 1) { // levi
            squares[0] = Square.squareAt(index - 9);
        }

        if (file != 8) { // desni
            squares[1] = Square.squareAt(index - 7);
        }

        return squares;
    }

    public Square[] getPawnSideSquaresBlack(Square sq) {
        Square[] squares = new Square[2];
        int rank = getRank(sq.getRank());
        int file = getFile(sq.getFile());
        int index = (rank - 1) * 8 + (file - 1);

        if (rank >= 7) {
            return squares;
        }

        if (file != 1) { // levi
            squares[0] = Square.squareAt(index + 7);
        }

        if (file != 8) { // desni
            squares[1] = Square.squareAt(index + 9);
        }

        return squares;
    }

    public Square[] getSquareBadBishopWhite(Square sq) {
        // gledamo po 2 unapred movementa bishopa da li je blokiran
        Square[] squares = new Square[4];
        int rank = getRank(sq.getRank());
        int file = getFile(sq.getFile());
        int index = (rank - 1) * 8 + (file - 1);

        // bad bishop gledamo samo na nasoj polovini
        if (rank >= 5) {
            return squares;
        }

        if (file > 1) { // levi prvi 
            squares[0] = Square.squareAt(index + 7);
        }

        if (file > 2) { // levi drugi 14 index = 7 + 7
            squares[1] = Square.squareAt(index + 14);
        }

        if (file < 8) { // desni prvi
            squares[2] = Square.squareAt(index + 9);
        }

        if (file < 7) { // desni drugi 18 index = 9 + 9
            squares[3] = Square.squareAt(index + 18);
        }

        return squares;
    }

    public Square[] getSquareBadBishopBlack(Square sq) {
        // gledamo po 2 unapred movementa bishopa da li je blokiran
        Square[] squares = new Square[4];
        int rank = getRank(sq.getRank());
        int file = getFile(sq.getFile());
        int index = (rank - 1) * 8 + (file - 1);

        // bad bishop gledamo samo na nasoj polovini
        if (rank <= 4) {
            return squares;
        }

        if (file > 1) { // levi prvi
            squares[0] = Square.squareAt(index - 9);
        }

        if (file > 2) { // levi drugi
            squares[1] = Square.squareAt(index - 18);
        }

        if (file < 8) { // desni prvi
            squares[2] = Square.squareAt(index - 7);
        }

        if (file < 7) { // desni prvi
            squares[3] = Square.squareAt(index - 14);
        }

        return squares;
    }

    public int open_SemiOpenFile(File file, Board board, int player) {
        // player = 0 -> WHITE
        // player = 1 -> BLACK
        // open file bonus = 16; semi-opne file bonus = 8;
        int index = getFile(file) - 1;

        int ret = 16;
        if (player == 0) {

            for (int i = 0; i < 8; i++, index += 8) {
                Piece piece = board.getPiece(Square.squareAt(index));
                if (piece == Piece.WHITE_PAWN) {
                    return 0;
                }
                if (piece == Piece.BLACK_PAWN) {
                    ret = 8;
                }
            }

        } else {
            index += 56;
            for (int i = 0; i < 8; i++, index -= 8) {
                Piece piece = board.getPiece(Square.squareAt(index));
                if (piece == Piece.BLACK_PAWN) {
                    return 0;
                }
                if (piece == Piece.WHITE_PAWN) {
                    ret = 8;
                }
            }
        }
        return ret;
    }

    public int rooksConnected(Square rook1, Square rook2, Board board) {

        int fileRook1 = getFile(rook1.getFile());
        int fileRook2 = getFile(rook2.getFile());
        int rankRook1 = getRank(rook1.getRank());
        int rankRook2 = getRank(rook2.getRank());

        if (fileRook1 == fileRook2) { // obrada po File-u vertikali

            int index1 = (rankRook1 - 1) * 8 + (fileRook1 - 1);
            int index2 = (rankRook2 - 1) * 8 + (fileRook2 - 1);
            if (index1 > index2) {
                int temp = index1;
                index1 = index2;
                index2 = index1;
            }
            index1 += 8; // predji na jedno polje iznad
            while (index1 < index2) {
                if (board.getPiece(Square.squareAt(index1)) != Piece.NONE) {
                    return 0;
                }
                index1 += 8;
            }
            return 40;
        } else if (rankRook1 == rankRook2) { // obrada po Rank-u horizontali

            int index1 = (rankRook1 - 1) * 8 + (fileRook1 - 1);
            int index2 = (rankRook2 - 1) * 8 + (fileRook2 - 1);
            if (index1 > index2) {
                int temp = index1;
                index1 = index2;
                index2 = index1;
            }
            index1++; // predji na polje pored
            while (index1 < index2) {
                if (board.getPiece(Square.squareAt(index1)) != Piece.NONE) {
                    return 0;
                }
                index1++;
            }
            return 40;
        }

        return 0;
    }

    public int xRayQueenByRook(Square rook, Square queen) {
        // rook on same file as enemy queen
        if (rook.getFile() == queen.getFile()) {
            return 7;
        }
        return 0;
    }

    public int pawnChainProtectionEvalWhite(Board board) {

        List<Square> pawnsWhite = board.getPieceLocation(Piece.WHITE_PAWN);
        // pawn structure
        int ret = 0;
        for (Square pawn : pawnsWhite) {
            Square[] squares = this.getPawnSideSquaresWhite(pawn);
            if (squares[0] != null) {
                if (board.getPiece(squares[0]) == Piece.WHITE_PAWN) {
                    ret += 5;
                }
            }
            if (squares[1] != null) {
                if (board.getPiece(squares[1]) == Piece.WHITE_PAWN) {
                    ret += 5;
                }
            }
        }
        return ret;
    }
    
    public int pawnChainProtectionEvalBlack(Board board) {

        List<Square> pawnsBlack = board.getPieceLocation(Piece.BLACK_PAWN);
        // pawn structure
        int ret = 0;
        for (Square pawn : pawnsBlack) {
            Square[] squares = this.getPawnSideSquaresBlack(pawn);
            if (squares[0] != null) {
                if (board.getPiece(squares[0]) == Piece.BLACK_PAWN) {
                    ret += 5;
                }
            }
            if (squares[1] != null) {
                if (board.getPiece(squares[1]) == Piece.BLACK_PAWN) {
                    ret += 5;
                }
            }
        }
        return ret;
    }

    public int passedPawnWhite(Board board){
    
        List<Square> pawnsWhite = board.getPieceLocation(Piece.WHITE_PAWN);
        // pawn structure
        int ret = 0;
        for (Square pawn : pawnsWhite) {
            int add = 60;
            File FilePawn = pawn.getFile();
            int rank = getRank(pawn.getRank());
            int file = getFile(FilePawn);
            int index = (rank - 1) * 8 + (file - 1);
            // obrada svih ispred polja da li ima crnog piuna ide se ofc +7, +8 i +9 u odnosu na index sve dok je index < 56 = 7 * 8
            while( index < 56){
                
                int index1 = index + 7;
                int index2 = index + 8;
                int index3 = index + 9;
                if( FilePawn != File.FILE_A && board.getPiece(Square.squareAt(index1)) == Piece.BLACK_PAWN){
                    add = 0;
                    break;
                }
                if( board.getPiece(Square.squareAt(index2)) == Piece.BLACK_PAWN){
                    add = 0;
                    break;
                }
                if( FilePawn != File.FILE_H && board.getPiece(Square.squareAt(index3)) == Piece.BLACK_PAWN){
                    add = 0;
                    break;
                }
                index += 8;
            }
            ret += add;
        }
        return ret;
    
    }
    
    public int passedPawnBlack(Board board){
    
        List<Square> pawnsBlack = board.getPieceLocation(Piece.BLACK_PAWN);
        // pawn structure
        int ret = 0;
        for (Square pawn : pawnsBlack) {
            int add = 60;
            File FilePawn = pawn.getFile();
            int rank = getRank(pawn.getRank());
            int file = getFile(FilePawn);
            int index = (rank - 1) * 8 + (file - 1);
            // obrada svih ispred polja da li ima belog piuna ide se ofc -7, -8 i -9 u odnosu na index sve dok je index > 7 = 1 * 7
            while( index > 7){
                
                int index1 = index - 7;
                int index2 = index - 8;
                int index3 = index - 9;
                if( FilePawn != File.FILE_H && board.getPiece(Square.squareAt(index1)) == Piece.WHITE_PAWN){
                    add = 0;
                    break;
                }
                if( board.getPiece(Square.squareAt(index2)) == Piece.WHITE_PAWN){
                    add = 0;
                    break;
                }
                if( FilePawn != File.FILE_A && board.getPiece(Square.squareAt(index3)) == Piece.WHITE_PAWN){
                    add = 0;
                    break;
                }
                index -= 8;
            }
            ret += add;
        }
        return ret;
    
    }
    
}
