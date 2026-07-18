package models;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Cell {
    private int row;
    private int col;
    @Setter
    private Piece piece;

    public Cell(int row, int col){
        this.row = row;
        this.col = col;
        this.piece = null;
    }
}
