package models;

import enums.PieceColour;
import enums.PieceType;
import strategy.MoveStrategy;
import strategy.PawnMovingStrategy;

import java.util.Optional;

public class Pawn extends Piece{
    public Pawn(PieceColour pieceColour) {
        super(pieceColour);
        this.moveStrategies.add(new PawnMovingStrategy());
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.PAWN;
    }

    @Override
    public String getSymbol() {
        return getPieceColour() == PieceColour.WHITE ? "WP" : "BP";
    }
}
