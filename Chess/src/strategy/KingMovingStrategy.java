package strategy;

import models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KingMovingStrategy implements MoveStrategy {

    @Override
    public Boolean isValidMove(Cell src, Cell dest, Board board) {

        int rowDiff = Math.abs(dest.getRow() - src.getRow());
        int colDiff = Math.abs(dest.getCol() - src.getCol());

        // King can move only one square in any direction
        if (rowDiff > 1 || colDiff > 1) {
            return false;
        }

        // Same square
        if (rowDiff == 0 && colDiff == 0) {
            return false;
        }

        Piece srcPiece = src.getPiece();
        Piece destPiece = dest.getPiece();

        // Cannot capture own piece
        return destPiece == null ||
                destPiece.getPieceColour() != srcPiece.getPieceColour();
    }

    @Override
    public Optional<Move> move(Cell src, Cell dest, Board board) {

        if (!isValidMove(src, dest, board)) {
            return Optional.empty();
        }

        Piece capturedPiece = dest.getPiece();

        List<Action> actions = new ArrayList<>();
        actions.add(new Action(src, dest, src.getPiece(), capturedPiece));

        return Optional.of(new Move(actions));
    }
}