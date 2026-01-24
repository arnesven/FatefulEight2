package model.items.special;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class CommunicatorDevice extends StoryItem {

    private static final Sprite SPRITE = new ItemSprite(5, 17,
            MyColors.PEACH, MyColors.YELLOW, MyColors.YELLOW);
    private final boolean identified;

    public CommunicatorDevice(boolean identified) {
        super(identified ? "Xelbi's Communicator" : "Strange Device", 0);
        this.identified = identified;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public String getShoppingDetails() {
        return "";
    }

    @Override
    public Item copy() {
        return new CommunicatorDevice(identified);
    }

    @Override
    public boolean canBeUsedFromMenu() {
        return identified;
    }

    @Override
    public String useFromMenu(Model model, GameCharacter gc) {
        return "You speak into the device and wait for a reply, but it stays completely silent.";
    }
}
