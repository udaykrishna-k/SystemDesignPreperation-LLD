package service;

import enums.PieceColour;
import enums.PlayerType;
import models.*;

import java.util.Optional;
import java.util.Scanner;
import java.util.Stack;

public class GameService {
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Board board;
    private Stack<Move> undoStack;
    private Stack<Move> redoStack;
    private MoveService moveService;
    private CheckService checkService;

    public GameService(Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1.getPieceColour() == PieceColour.WHITE ? player1 : player2;
        this.board = new Board();
        this.moveService = new MoveService();
        this.checkService = new CheckService();
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
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
            this.undoStack.push(moveResult.get());

            board.printBoard();
            this.checkKingStatus();

            if (currentPlayer.getPlayerType() == PlayerType.HUMAN){
                this.postMoveOptions(scanner);
            }

            switchPlayer();

        }

        scanner.close();
    }

    public void checkKingStatus(){
        Player opponentPlayer = this.currentPlayer == player1 ? this.player2 : this.player1;
        if (this.checkService.isKingUnderCheck(opponentPlayer, board)){
            System.out.println("Player " + currentPlayer.getName() + " has a check on " + opponentPlayer.getName() + "'s King");
        }
    }

    public void postMoveOptions(Scanner scanner){
        while (true) {
            System.out.println();
            System.out.println("(U)ndo (C)ontinue");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("U")) {
                System.out.println("Undo size = " + this.undoStack.size());
                if (!undoStack.empty()) {
                    this.undoMove();
                    switchPlayer();
                    break;
                }
            } else if (input.equalsIgnoreCase("R")) {
                System.out.println("Redo size = " + this.redoStack.size());
                if (!this.redoStack.empty()) {
                    this.redoMove();
                }
                break;
            } else if (input.equalsIgnoreCase("C")) {
                this.redoStack.clear();
                break;
            }
            else{
                System.out.println("Please enter valid inputs only (U)ndo (R)edo (C)ontinue");
            }
        }
    }

    private void switchPlayer(){
        this.currentPlayer = currentPlayer == player1 ? player2 : player1;
    }

    private void undoMove(){
        if (undoStack.empty()){
            System.out.println("Cannot undo, No previous moves present");
            return;
        }
        Move undoMove = undoStack.pop();
        this.moveService.undoMove(undoMove);
        this.redoStack.push(undoMove);
    }

    private void redoMove(){
        if ( this.redoStack.empty()){
            System.out.println("Cannot redo, No executed moved present");
            return;
        }
        Move redoMove = redoStack.pop();
        this.moveService.makeMove(redoMove);
        this.undoStack.push(redoMove);
    }

}
