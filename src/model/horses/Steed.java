package model.horses;

import model.characters.GameCharacter;
import model.races.Race;
import view.MyColors;

public abstract class Steed extends Horse {

    public Steed(String name, int cost, MyColors avatarColor) {
        super("Steed", name, cost, avatarColor);
    }

    @Override
    public String getInfo() {
        return "Halflings and dwarves must ride together with non-halfling, non-dwarf rider.";
    }

    @Override
    public boolean canBeRiddenBy(GameCharacter chosen) {
        return !chosen.getRace().isShort();
    }
}
