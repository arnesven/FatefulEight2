package model;

import model.characters.GameCharacter;

import java.io.Serializable;
import java.util.List;

public class HallOfFameEntry implements Serializable {
    private List<GameCharacter> characters;
    private int score;
    public HallOfFameEntry(List<GameCharacter> chars, int score) {
        this.characters = chars;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public List<GameCharacter> getCharacters() {
        return characters;
    }
}
