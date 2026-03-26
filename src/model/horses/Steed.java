package model.horses;

import view.MyColors;

public abstract class Steed extends Horse {

    public Steed(String name, int cost, MyColors avatarColor) {
        super("Steed", name, cost, avatarColor);
    }

    @Override
    public String getInfo() {
        return "Halflings and dwarves must ride together with non-halfling, non-dwarf rider.";
    }
}
