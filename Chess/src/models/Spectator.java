package models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Spectator {
    private String name;

    public void publish(String message) {
        System.out.println("Spectator " + this.name  + " got a message: " + message);
    }
}
