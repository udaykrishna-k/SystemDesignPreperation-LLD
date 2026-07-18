package models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Move {
    private List<Action> actions;

    public void executeMove(){
        for (Action action : actions){
            action.executeAction();
        }
    }

    public void undoMove(){
        for (int i=actions.size()-1; i>=0; i--){
            Action action = actions.get(i);
            action.undoAction();
        }
    }
}
