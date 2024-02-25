package model.items;

import model.horses.Horse;
import model.horses.Steed;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class HorseStartingItem extends InventoryDummyItem{
    private static final Sprite STEED_SPRITE = new ItemSprite(15, 12, MyColors.DARK_BROWN, MyColors.BROWN, MyColors.DARK_BROWN);
    private static final Sprite PONY_SPRITE = new ItemSprite(14, 12, MyColors.BROWN, MyColors.BEIGE, MyColors.PEACH);
    private final Horse horse;

    public HorseStartingItem(Horse horse) {
        super(horse instanceof Steed ? "Horse (Steed)" : "Pony", horse.getCost());
        this.horse = horse;
    }

    @Override
    protected Sprite getSprite() {
        if (horse instanceof Steed) {
            return STEED_SPRITE;
        }
        return PONY_SPRITE;
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public void addYourself(Inventory inventory) {
        throw new IllegalStateException("Should not be called!");
    }

    @Override
    public String getShoppingDetails() {
        return ", " + horse.getInfo();
    }

    public Horse getHorse() {
        return horse;
    }
}
