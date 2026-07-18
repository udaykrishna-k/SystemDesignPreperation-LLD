package models;

import enums.PieceColour;
import enums.PieceType;
import factory.PieceFactory;
import lombok.Getter;

@Getter
public class Board {
    private Cell[][] board = new Cell[8][8];

    public Board(){
        initializeCells();
        initializePieces();
    }
    public Cell getCell(String input){
        int row = input.charAt(1) - '1';
        int col = input.charAt(0) - 'a';
        return this.board[row][col];
    }

    private void initializeCells() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = new Cell(row, col);
            }
        }
    }

    private void initializePieces(){
        // White major pieces
        board[0][0].setPiece(PieceFactory.createPiece(PieceType.ROOK, PieceColour.WHITE));
        board[0][1].setPiece(PieceFactory.createPiece(PieceType.KNIGHT, PieceColour.WHITE));
        board[0][2].setPiece(PieceFactory.createPiece(PieceType.BISHOP, PieceColour.WHITE));
        board[0][3].setPiece(PieceFactory.createPiece(PieceType.QUEEN, PieceColour.WHITE));
        board[0][4].setPiece(PieceFactory.createPiece(PieceType.KING, PieceColour.WHITE));
        board[0][5].setPiece(PieceFactory.createPiece(PieceType.BISHOP, PieceColour.WHITE));
        board[0][6].setPiece(PieceFactory.createPiece(PieceType.KNIGHT, PieceColour.WHITE));
        board[0][7].setPiece(PieceFactory.createPiece(PieceType.ROOK, PieceColour.WHITE));

        // White pawns
        for (int col = 0; col < 8; col++) {
            board[1][col].setPiece(
                    PieceFactory.createPiece(PieceType.PAWN, PieceColour.WHITE));
        }

        // Black major pieces
        board[7][0].setPiece(PieceFactory.createPiece(PieceType.ROOK, PieceColour.BLACK));
        board[7][1].setPiece(PieceFactory.createPiece(PieceType.KNIGHT, PieceColour.BLACK));
        board[7][2].setPiece(PieceFactory.createPiece(PieceType.BISHOP, PieceColour.BLACK));
        board[7][3].setPiece(PieceFactory.createPiece(PieceType.QUEEN, PieceColour.BLACK));
        board[7][4].setPiece(PieceFactory.createPiece(PieceType.KING, PieceColour.BLACK));
        board[7][5].setPiece(PieceFactory.createPiece(PieceType.BISHOP, PieceColour.BLACK));
        board[7][6].setPiece(PieceFactory.createPiece(PieceType.KNIGHT, PieceColour.BLACK));
        board[7][7].setPiece(PieceFactory.createPiece(PieceType.ROOK, PieceColour.BLACK));

        // Black pawns
        for (int col = 0; col < 8; col++) {
            board[6][col].setPiece(
                    PieceFactory.createPiece(PieceType.PAWN, PieceColour.BLACK));
        }
    }

    public Cell getCell(int row, int col){
        return board[row][col];
    }

    public void printBoard() {
        StringBuilder sb = new StringBuilder();

        sb.append("    a    b    c    d    e    f    g    h\n");

        for (int row = 7; row >= 0; row--) {

            sb.append("  +----+----+----+----+----+----+----+----+\n");
            sb.append(row + 1).append(" |");

            for (int col = 0; col < 8; col++) {

                Piece piece = board[row][col].getPiece();

                if (piece == null) {
                    sb.append("  . |");
                } else {
                    sb.append(" ")
                            .append(piece.getSymbol())
                            .append(" |");
                }
            }

            sb.append("\n");
        }

        sb.append("  +----+----+----+----+----+----+----+----+\n");
        sb.append("    a    b    c    d    e    f    g    h\n");

        System.out.println(sb.toString());
    }
}
