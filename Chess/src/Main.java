import enums.PieceColour;
import enums.PlayerType;
import models.HumanPlayer;
import models.Player;
import models.Spectator;
import service.GameService;

public class Main {
    public static void main(String[] args) {

        System.out.println("Let's Play Chess");

        Player player1 = new HumanPlayer("uday", PieceColour.WHITE);
        Player player2 = new HumanPlayer("john", PieceColour.BLACK);

        Spectator spectator = new Spectator("Hari");

        GameService game = new GameService(player1, player2);
        game.addSpectator(spectator);
        game.start();
    }
}