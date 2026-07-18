package strategy;

import models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HorizontalMoveStrategy implements MoveStrategy{
    @Override
    public Boolean isValidMove(Cell src, Cell dest, Board board){

        Piece piece = src.getPiece();
        int srcRow = src.getRow();
        int srcCol = src.getCol();
        int destRow = dest.getRow();
        int destCol = dest.getCol();

        // In horizontal moving the row has to remain same

        if (srcRow != destRow){
            return false;
        }

        // source and destination have to different
        if (srcCol == destCol){
            return false;
        }

        int colStep = destCol > srcCol ? 1 : -1;

        int i = srcRow;
        int j = srcCol + colStep;

        while (j != destCol){
            if (j < 0 || j >= 8) {
                return false;
            }
            if (board.getCell(i, j).getPiece() != null) {
                return false;
            }

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
