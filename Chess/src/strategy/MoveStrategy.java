package strategy;

import models.Board;
import models.Cell;
import models.Move;

import java.util.Optional;

public interface MoveStrategy {
    public Boolean isValidMove(Cell src, Cell dest, Board board);
    public Optional<Move> move(Cell src, Cell dest, Board board);
}
