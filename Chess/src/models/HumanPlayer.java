package models;

import enums.PieceColour;
import enums.PlayerType;

public class HumanPlayer extends Player{
    public HumanPlayer(String name, PieceColour pieceColour) {
        super(name, pieceColour, PlayerType.HUMAN);
    }
}
