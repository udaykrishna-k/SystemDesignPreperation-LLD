package models;

import enums.PieceColour;
import enums.PieceType;
import strategy.DiognalMovingStrategy;
import strategy.HorizontalMoveStrategy;
import strategy.MoveStrategy;
import strategy.VerticalMovingStrategy;

import java.util.Optional;

public class Queen extends Piece{
    public Queen(PieceColour pieceColour) {
        super(pieceColour);
        this.moveStrategies.add(new HorizontalMoveStrategy());
        this.moveStrategies.add(new VerticalMovingStrategy());
        this.moveStrategies.add(new DiognalMovingStrategy());
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.QUEEN;
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
        return this.getPieceColour() == PieceColour.WHITE ? "WQ" : "BQ";
    }
}
