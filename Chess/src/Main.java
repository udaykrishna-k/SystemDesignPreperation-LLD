import enums.PieceColour;
import enums.PlayerType;
import models.HumanPlayer;
import models.Player;
import service.GameService;

public class Main {
    public static void main(String[] args) {

        System.out.println("Let's Play Chess");

        Player player1 = new HumanPlayer("uday", PieceColour.WHITE);
        Player player2 = new HumanPlayer("john", PieceColour.BLACK);

        GameService game = new GameService(player1, player2);
        game.start();
    }
}