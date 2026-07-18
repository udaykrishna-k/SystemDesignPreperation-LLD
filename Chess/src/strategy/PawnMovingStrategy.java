package strategy;

import enums.PieceColour;
import models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PawnMovingStrategy implements MoveStrategy {

    @Override
    public Boolean isValidMove(Cell src, Cell dest, Board board) {

        Piece srcPiece = src.getPiece();
        Piece destPiece = dest.getPiece();

        int srcRow = src.getRow();
        int srcCol = src.getCol();

        int destRow = dest.getRow();
        int destCol = dest.getCol();

        int direction =
                srcPiece.getPieceColour() == PieceColour.WHITE ? 1 : -1;

        int rowDiff = destRow - srcRow;
        int colDiff = Math.abs(destCol - srcCol);

        // One square forward
        if (colDiff == 0 && rowDiff == direction) {
            return destPiece == null;
        }

        // Two squares forward (only if pawn has never moved)
        if (srcPiece.getNumOfMoves() == 0 &&
                colDiff == 0 &&
                rowDiff == 2 * direction) {

            int intermediateRow = srcRow + direction;

            return destPiece == null &&
                    board.getCell(intermediateRow, srcCol).getPiece() == null;
        }

        // Diagonal capture
        if (rowDiff == direction && colDiff == 1) {
            return destPiece != null &&
                    destPiece.getPieceColour() != srcPiece.getPieceColour();
        }

        return false;
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