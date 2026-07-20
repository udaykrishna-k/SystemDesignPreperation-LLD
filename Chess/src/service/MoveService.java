package service;

import lombok.AllArgsConstructor;
import models.*;

import java.util.List;
import java.util.Stack;

@AllArgsConstructor
public class MoveService {
    private Stack<Move> undoStack;
    private Stack<Move> redoStack;

    public MoveService(){
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    public Boolean canUndo(){
        Boolean value = ! this.undoStack.empty();
        if (! value){
            System.out.println("Undo stack is empty.");
        }
        return value;
    }

    public Boolean canRedo(){
        Boolean value = ! this.redoStack.empty();
        if (! value){
            System.out.println("Redo stack is empty.");
        }
        return value;
    }

    public void undoMove(){
        Move undoMove = undoStack.pop();
        undoMove.undoMove();
        this.redoStack.push(undoMove);
    }

    public void redoMove(){
        Move redoMove = redoStack.pop();
        this.makeMove(redoMove);
        this.undoStack.push(redoMove);
    }

    public void makeMove(Move move){
        move.executeMove();
        this.undoStack.push(move);
    }

    public void clearRedoStack() {
        this.redoStack.clear();
    }

}
