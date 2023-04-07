package model;

import model.characters.GameCharacter;
import view.sprites.AvatarSprite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HallOfFameEntry implements Serializable {
    private final String leaderName;
    private List<AvatarSprite> characters;
    private int score;
    public HallOfFameEntry(List<GameCharacter> chars, int score) {
        this.characters = new ArrayList<>();
        this.leaderName = chars.get(0).getName();
        for (GameCharacter gc : chars) {
            characters.add(gc.getAvatarSprite());
        }
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public List<AvatarSprite> getCharacters() {
        return characters;
    }

    public String getName() {
        return leaderName;
    }
}
