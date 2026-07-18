package factory;

import enums.PieceColour;
import enums.PieceType;
import models.*;

public class PieceFactory {
    public static Piece createPiece(PieceType pieceType, PieceColour pieceColour){
        return switch (pieceType) {
            case KING -> new King(pieceColour);
            case QUEEN -> new Queen(pieceColour);
            case ROOK -> new Rook(pieceColour);
            case BISHOP -> new Bishop(pieceColour);
            case KNIGHT -> new Knight(pieceColour);
            case PAWN -> new Pawn(pieceColour);
        };
    }
}
