package strategy;

import models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LShapedStrategy implements MoveStrategy{
    @Override
    public Boolean isValidMove(Cell src, Cell dest, Board board) {

        int rowDiff = Math.abs(dest.getRow() - src.getRow());
        int colDiff = Math.abs(dest.getCol() - src.getCol());

        // Knight moves in an L shape
        boolean isKnightMove =
                (rowDiff == 2 && colDiff == 1) ||
                        (rowDiff == 1 && colDiff == 2);

        if (!isKnightMove) {
            return false;
        }

        Piece srcPiece = src.getPiece();
        Piece destPiece = dest.getPiece();

        // Cannot capture own piece
        if (destPiece != null &&
                destPiece.getPieceColour() == srcPiece.getPieceColour()) {
            return false;
        }

        return true;
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
