package model;

import model.characters.GameCharacter;
import view.sprites.AvatarSprite;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HallOfFameEntry implements Serializable {
    private final String leaderName;
    private List<AvatarSprite> characters;
    private int score;
    private Date when;

    public HallOfFameEntry(List<GameCharacter> chars, int score) {
        this.characters = new ArrayList<>();
        this.leaderName = chars.get(0).getName();
        when = new Date();
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

    public String getWhen() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(when);
    }
}
