package model.items.potions;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.races.ElvenRace;
import model.races.Race;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class BeerPotion extends IntoxicatingPotion {
    private static final Sprite SPRITE = new ItemSprite(8, 13, MyColors.WHITE, MyColors.GOLD);

    public BeerPotion() {
        super("Beer", 1, "elves");
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new BeerPotion();
    }

    @Override
    public boolean doesReject(Model model, Race race) {
        return race instanceof ElvenRace;
    }
}
