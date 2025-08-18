package model.states.dailyaction.shops;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.items.Item;
import util.MyRandom;
import view.sprites.AvatarSprite;
import view.subviews.PortraitSubView;

import java.awt.*;

public class ShopSupplier {
    private final GameCharacter avatar;
    private final Item item;
    private final int number;
    private final int goldAmount;
    private final boolean activated;
    private boolean dealMade = false;

    public ShopSupplier(boolean activated, Item supplied) {
        this.activated = activated;
        this.item = supplied;
        this.number = MyRandom.randInt(8, 14);
        this.goldAmount = (int)(10 * Math.ceil((1.0 / (number + 7)) * item.getCost() * number));

        CharacterAppearance randApp = PortraitSubView.makeRandomPortrait(Classes.ART);
        this.avatar = new GameCharacter("", "", randApp.getRace(),
                Classes.ART, randApp, Classes.NO_OTHER_CLASSES);
    }

    public void drawYourself(Model model, Point p) {
        AvatarSprite avatarSprite = avatar.getAvatarSprite();
        avatarSprite.synch();
        Point pos = new Point(p.x + 4, p.y);
        model.getScreenHandler().register(avatarSprite.getName(), pos, avatarSprite);
    }

    public Item getItem() {
        return item;
    }

    public boolean isDealMade() {
        return dealMade;
    }

    public void setDealMade(boolean b) {
        dealMade = b;
    }

    public int getNumber() {
        return number;
    }

    public int getGoldAmount() {
        return goldAmount;
    }

    public boolean isActivated() {
        return activated;
    }
}
