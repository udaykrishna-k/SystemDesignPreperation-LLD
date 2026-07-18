package service;

import models.*;

import java.util.List;

public class MoveService {
    public void makeMove(Move move){
        move.executeMove();
    }

    public void undoMove(Move move){
        move.undoMove();
    }
}
