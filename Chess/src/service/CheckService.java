package service;

import enums.PieceType;
import models.Board;
import models.Cell;
import models.Move;
import models.Player;

import java.util.Optional;

public class CheckService {

    public Cell findKingCell(Player player, Board board){
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                Cell cell = board.getCell(i, j);
                if (cell.getPiece() == null) continue;
                if (cell.getPiece().getPieceColour() != player.getPieceColour()) continue;
                if (cell.getPiece().getPieceType() != PieceType.KING) continue;
                return cell;
            }
        }
        return null;
    }

    public Boolean isKingUnderCheck(Player player, Board board){
        Cell kingCell = this.findKingCell(player, board);
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                Cell cell = board.getCell(i, j);
                if (cell.getPiece() == null) continue;
                if (cell.getPiece().getPieceColour() == player.getPieceColour()) continue;
                if (cell.getPiece().canMove(cell, kingCell, board)) return true;
            }
        }
        return false;
    }

    public Boolean isCheckMate(Player player, Board board) {

        if (!isKingUnderCheck(player, board)) {
            return false;
        }

        for (int srcRow = 0; srcRow < 8; srcRow++) {
            for (int srcCol = 0; srcCol < 8; srcCol++) {

                Cell src = board.getCell(srcRow, srcCol);

                if (src.getPiece() == null) {
                    continue;
                }

                if (src.getPiece().getPieceColour() != player.getPieceColour()) {
                    continue;
                }

                for (int destRow = 0; destRow < 8; destRow++) {
                    for (int destCol = 0; destCol < 8; destCol++) {

                        Cell dest = board.getCell(destRow, destCol);

                        Optional<Move> move = src.getPiece().move(src, dest, board);

                        if (move.isEmpty()) {
                            continue;
                        }

                        move.get().executeMove();

                        boolean stillInCheck =
                                isKingUnderCheck(player, board);

                        move.get().undoMove();

                        if (!stillInCheck) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }
}
