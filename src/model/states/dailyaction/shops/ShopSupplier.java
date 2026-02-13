package model.states.dailyaction.shops;

import model.Model;
import model.achievements.Achievement;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.items.Item;
import util.MyRandom;
import view.sprites.AvatarSprite;
import view.subviews.PortraitSubView;

import java.awt.*;

public class ShopSupplier {
    public static final String ACHIEVEMENT_KEY = "ShopSupplierAchievement";
    private static final String DEAL_ON_DAY_KEY = "SUPPLIER_DEAL_ON_DAY";
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
                Classes.ART, randApp);
    }

    public static int getDealDay(Model model) {
        if (model.getSettings().getMiscCounters().containsKey(DEAL_ON_DAY_KEY)) {
            return model.getSettings().getMiscCounters().get(DEAL_ON_DAY_KEY);
        }
        return -100;
    }

    public static void setDealOnDay(Model model) {
        model.getSettings().getMiscCounters().put(DEAL_ON_DAY_KEY, model.getDay());
    }

    public static Achievement.Data getAchievementData() {
        return new Achievement.Data(ACHIEVEMENT_KEY, "Shop Supplier",
                "You helped a flustered shop supplier by buying the shipment, " +
                        "albeit angering some shopkeepers in the process.");
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
