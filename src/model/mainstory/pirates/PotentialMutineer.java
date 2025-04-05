package model.mainstory.pirates;

import model.characters.GameCharacter;

import java.io.Serializable;

public class PotentialMutineer implements Serializable {
    private GameCharacter character;
    private boolean isTrans;
    private boolean likesRum;

    public PotentialMutineer(GameCharacter chara, boolean trans, boolean likesRum) {
        this.character = chara;
        this.isTrans = trans;
        this.likesRum = likesRum;
    }
}
