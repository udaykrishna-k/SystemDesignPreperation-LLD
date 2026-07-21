package service;

import enums.PieceColour;
import enums.PlayerType;
import models.*;
import observer.Publisher;

import java.util.Optional;
import java.util.Scanner;

public class GameService extends Publisher {
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Board board;
    private MoveService moveService;
    private CheckService checkService;

    public GameService(Player player1, Player player2){
        super();
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1.getPieceColour() == PieceColour.WHITE ? player1 : player2;
        this.board = new Board();
        this.moveService = new MoveService();
        this.checkService = new CheckService();
    }

    public void startgame(){
        this.start();
    }

    public void start(){
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println();
            board.printBoard();

            System.out.print(currentPlayer.getName() + "'s move (e.g. e2 e4): ");

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            String[] move = input.split("\\s+");

            if (move.length != 2) {
                System.out.println("Invalid input.");
                continue;
            }

            Cell src = board.getCell(move[0]);
            Cell dest = board.getCell(move[1]);

            String moveMessage = "Player " + this.currentPlayer.getName() + " moved a piece from " + move[0] + " to " + move[1];

            Piece piece = src.getPiece();

            if (piece == null) {
                System.out.println("No piece at source.");
                continue;
            }

            if (piece.getPieceColour() != currentPlayer.getPieceColour()) {
                System.out.println("That's not your piece.");
                continue;
            }

            Optional<Move> moveResult = piece.move(src, dest, board);

            if (moveResult.isEmpty()) {
                System.out.println("Invalid move.");
                continue;
            }

            this.moveService.makeMove(moveResult.get());

            this.publish(moveMessage);

            System.out.println();
            board.printBoard();

            Player opponentPlayer = this.currentPlayer == this.player1 ? this.player2 : this.player1;

            this.checkKingStatus(opponentPlayer);

            if (this.checkService.isCheckMate(opponentPlayer, this.board)){
                String message = "Player " + this.currentPlayer.getName() + " has won the game";
                this.publish(message);
                System.out.println(message);
                break;
            }

            if (currentPlayer.getPlayerType() == PlayerType.HUMAN){
                this.postMoveOptions(scanner);
            }

            switchPlayer();

        }

        scanner.close();
    }

    public void checkKingStatus(Player opponentPlayer){
        if (this.checkService.isKingUnderCheck(opponentPlayer, this.board)){
            System.out.println("Player " + currentPlayer.getName() + " has a check on " + opponentPlayer.getName() + "'s King");
        }
    }

    public void postMoveOptions(Scanner scanner){
        while (true) {
            System.out.println();
            System.out.println("(U)ndo (C)ontinue");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("U")) {
                if (moveService.canUndo()) {
                    String message = "Player " + this.currentPlayer.getName() + " has undone the move";
                    moveService.undoMove();
                    this.publish(message);
                    switchPlayer();
                    break;
                }
            } else if (input.equalsIgnoreCase("R")) {
                if (this.moveService.canRedo()) {
                    this.moveService.redoMove();
                }
                break;
            } else if (input.equalsIgnoreCase("C")) {
                this.moveService.clearRedoStack();
                break;
            }
            else{
                System.out.println("Please enter valid inputs only (U)ndo (C)ontinue");
            }
        }
    }

    private void switchPlayer(){
        this.currentPlayer = currentPlayer == player1 ? player2 : player1;
    }

}
