package model.items.accessories;

import model.items.Item;
import model.items.spells.Spell;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Pentagram extends JewelryItem {
    private static final Sprite SPRITE = new ItemSprite(1, 9, MyColors.TAN, MyColors.DARK_RED);

    public Pentagram() {
        super("Pentagram", 14);
    }

    @Override
    public int getSpellDiscount(Spell sp) {
        if (sp.getColor() == MyColors.BLACK) {
            return 1;
        }
        return 0;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Pentagram();
    }

    @Override
    public int getAP() {
        return 0;
    }

    @Override
    public String getExtraText() {
        return "Black Spells cost 1 less HP";
    }
}
