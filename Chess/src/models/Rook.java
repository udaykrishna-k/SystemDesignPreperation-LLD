package models;

import enums.PieceColour;
import enums.PieceType;
import strategy.HorizontalMoveStrategy;
import strategy.MoveStrategy;
import strategy.VerticalMovingStrategy;

import java.util.Optional;

public class Rook extends Piece{

    public Rook(PieceColour pieceColour){
        super(pieceColour);
        moveStrategies.add(new HorizontalMoveStrategy());
        moveStrategies.add(new VerticalMovingStrategy());
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.ROOK;
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
        return getPieceColour() == PieceColour.WHITE ? "WR" : "BR";
    }
}
