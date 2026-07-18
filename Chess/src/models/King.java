package models;

import enums.PieceColour;
import enums.PieceType;
import strategy.KingMovingStrategy;
import strategy.MoveStrategy;

import java.util.Optional;

public class King extends Piece{
    public King(PieceColour pieceColour) {
        super(pieceColour);
        this.moveStrategies.add(new KingMovingStrategy());
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.KING;
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
        return getPieceColour() == PieceColour.WHITE ? "WK" : "BK";
    }
}
