package strategy;

import models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DiognalMovingStrategy implements MoveStrategy{
    @Override
    public Boolean isValidMove(Cell src, Cell dest, Board board) {
        Piece piece = src.getPiece();

        int srcRow = src.getRow();
        int srcCol = src.getCol();
        int destRow = dest.getRow();
        int destCol = dest.getCol();

        // source and destination have to different
        if (srcRow == destRow && srcCol == destCol){
            return false;
        }

        // both source and destination should belong to same diognal
        if (Math.abs(destRow - srcRow) != Math.abs(destCol - srcCol)){
            return false;
        }

        int rowStep = destRow > srcRow ? 1 : -1;
        int colStep = destCol > srcCol ? 1 : -1;

        int i = srcRow + rowStep, j = srcCol + colStep;

        while (i != destRow && j != destCol){
            if (i < 0 || i >= 8 || j < 0 || j >= 8) {
                return false;
            }
            if (board.getCell(i, j).getPiece() != null) {
                return false;
            }

            i += rowStep;
            j += colStep;
        }

        return dest.getPiece() == null || dest.getPiece().getPieceColour() != piece.getPieceColour();
    }

    @Override
    public Optional<Move> move(Cell src, Cell dest, Board board) {
        Boolean isValidMove = isValidMove(src, dest, board);
        if (! isValidMove) {
            return Optional.empty();
        }
        Piece capturedPiece = null;
        if (dest.getPiece() != null){
            capturedPiece = dest.getPiece();
        }
        List<Action> actions = new ArrayList<>();
        actions.add(new Action(src, dest, src.getPiece(), capturedPiece));
        Move move = new Move(actions);
        return Optional.of(move);
    }
}
