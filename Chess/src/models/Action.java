package models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Action {
    private Cell source;
    private Cell destination;
    private Piece movedPiece;
    private Piece capturedPiece;

    public void executeAction(){
        this.source.setPiece(null);
        this.destination.setPiece(this.movedPiece);
        this.movedPiece.incrementMoves();
    }

    public void undoAction() {
        this.source.setPiece(this.movedPiece);
        this.movedPiece.decreamentMoves();
        this.destination.setPiece(this.capturedPiece);
    }
}
