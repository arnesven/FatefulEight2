package model.items.potions;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.races.AllRaces;
import model.races.Race;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class WinePotion extends IntoxicatingPotion {
    private static final Sprite SPRITE = new ItemSprite(9, 13, MyColors.WHITE, MyColors.DARK_RED);

    public WinePotion() {
        super("Wine", 1, "dwarves");
    }

    @Override
    public String getTypicalGlass() {
        return "cup";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new WinePotion();
    }

    @Override
    public boolean doesReject(Model model, Race race) {
        return race.id() == AllRaces.DWARF.id();
    }
}
