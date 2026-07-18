package models;

import enums.PieceColour;
import enums.PieceType;
import strategy.DiognalMovingStrategy;
import strategy.MoveStrategy;

import java.util.Optional;

public class Bishop extends Piece{
    public Bishop(PieceColour pieceColour) {
        super(pieceColour);
        this.moveStrategies.add(new DiognalMovingStrategy());
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.BISHOP;
    }

    @Override
    public Optional<Move> move(Cell src, Cell dest, Board board) {
        for(MoveStrategy moveStrategy: moveStrategies){
            Optional<Move> move = moveStrategy.move(src, dest, board);
            if (move.isPresent()){
                return move;
            }
        }
        return Optional.empty();
    }

    @Override
    public String getSymbol() {
        return getPieceColour() == PieceColour.WHITE ? "WB" : "BB";
    }
}
