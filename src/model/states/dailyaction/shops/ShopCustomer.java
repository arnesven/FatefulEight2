package model.states.dailyaction.shops;

import model.Model;
import model.achievements.Achievement;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.items.Item;
import util.MyRandom;
import view.sprites.AvatarSprite;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.Arrays;

public class ShopCustomer {
    public static final String ACHIEVEMENT_KEY = "ShopCustomerAchievement";
    private final Item item;
    private final GameCharacter avatar;
    private final int goldOffer;
    private boolean dealMade = false;

    public ShopCustomer(Item item) {
        this.item = item;
        this.goldOffer = MyRandom.randInt(item.getCost(), item.getCost()*2);
        CharacterClass randClass = Classes.None;
        if (MyRandom.randInt(3) > 0) {
            randClass = MyRandom.sample(Arrays.asList(Classes.allClasses));
        }
        CharacterAppearance randApp = PortraitSubView.makeRandomPortrait(randClass);
        avatar = new GameCharacter("", "", randApp.getRace(),
                randClass, randApp, Classes.NO_OTHER_CLASSES);
    }

    public static Achievement.Data getAchievemetnData() {
        return new Achievement.Data(ACHIEVEMENT_KEY, "Shop Customer",
                "A customer in a shop requested an out-of-stock item. You sold it to the customer.");
    }

    public Item getItem() {
        return item;
    }

    public void drawYourself(Model model, Point p) {
        AvatarSprite avatarSprite = avatar.getAvatarSprite();
        avatarSprite.synch();
        Point pos = new Point(p.x - 4, p.y);
        model.getScreenHandler().register(avatarSprite.getName(), pos, avatarSprite);
    }

    public int getGoldOffer() {
        return goldOffer;
    }

    public boolean getGender() {
        return avatar.getGender();
    }

    public void setDealMade(boolean b) {
        dealMade = b;
    }

    public boolean isDealMade() {
        return dealMade;
    }
}
