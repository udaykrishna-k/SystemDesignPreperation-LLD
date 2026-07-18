package models;

import enums.PieceColour;
import enums.PlayerType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Player {
    protected String name;
    protected PieceColour pieceColour;
    protected PlayerType playerType;
}
