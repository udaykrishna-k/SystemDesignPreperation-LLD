package service;

import enums.PieceType;
import models.Board;
import models.Cell;
import models.Player;
import strategy.MoveStrategy;

import java.util.List;

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
}
