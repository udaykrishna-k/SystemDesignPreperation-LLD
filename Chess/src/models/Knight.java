package models;

import enums.PieceColour;
import enums.PieceType;
import strategy.LShapedStrategy;
import strategy.MoveStrategy;

import java.util.Optional;

public class Knight extends Piece{
    public Knight(PieceColour pieceColour) {
        super(pieceColour);
        this.moveStrategies.add(new LShapedStrategy());
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.KNIGHT;
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
