package observer;

import lombok.NoArgsConstructor;
import models.Spectator;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public abstract class Publisher {
    private final List<Spectator> spectators = new ArrayList<>();

    public void addSpectator(Spectator spectator){
        this.spectators.add(spectator);
    }

    public void removeSpectator(Spectator spectator){
        this.spectators.remove(spectator);
    }

    public void publish(String message){
        for(Spectator spectator: this.spectators){
            spectator.publish(message);
        }
    }

}
