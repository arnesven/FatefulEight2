package model.items.potions;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.races.AllRaces;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class WinePotion extends IntoxicatingPotion {
    private static final Sprite SPRITE = new ItemSprite(9, 13, MyColors.WHITE, MyColors.DARK_RED);

    public WinePotion() {
        super("Wine", 1, "dwarves");
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
    protected boolean doesReject(Model model, GameCharacter gc) {
        return gc.getRace().id() == AllRaces.DWARF.id();
    }
}
