package models;

import enums.PieceColour;
import enums.PieceType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import strategy.MoveStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public abstract class Piece {
    protected PieceColour pieceColour;
    protected int numOfMoves = 0;
    protected final List<MoveStrategy> moveStrategies = new ArrayList<>();


    public Piece(PieceColour pieceColour){
        this.pieceColour = pieceColour;
    }

    public abstract PieceType getPieceType();
    public abstract String getSymbol();

    public Optional<Move> move(Cell src, Cell dest, Board board) {
        for(MoveStrategy moveStrategy: moveStrategies){
            Optional<Move> move = moveStrategy.move(src, dest, board);
            if (move.isPresent()){
                return move;
            }
        }
        return Optional.empty();
    }

    public Boolean canMove(Cell src, Cell dest, Board board){
        for(MoveStrategy moveStrategy: moveStrategies){
            Optional<Move> move = moveStrategy.move(src, dest, board);
            if (move.isPresent()){
                return true;
            }
        }
        return false;
    }

    public void incrementMoves() {
        this.numOfMoves += 1;
    }

    public void decreamentMoves() {
        this.numOfMoves -= 1;
    }
}
