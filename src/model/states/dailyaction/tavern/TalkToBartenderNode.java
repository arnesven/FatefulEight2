package model.states.dailyaction.tavern;

import model.Model;
import model.TimeOfDay;
import model.items.FoodDummyItem;
import model.items.Item;
import model.items.ObolsDummyItem;
import model.items.potions.BeerPotion;
import model.items.potions.WinePotion;
import model.items.special.LargeTentUpgradeItem;
import model.items.special.TentUpgradeItem;
import model.items.weapons.FishingPole;
import model.states.GameState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TalkToBartenderNode extends DailyActionNode {
    private static final Sprite STOOL = new Sprite32x32("barstool", "world_foreground.png", 0x55,
            MyColors.GRAY, MyColors.DARK_RED, MyColors.DARK_GREEN, MyColors.CYAN);
    private final boolean inTown;

    private boolean workDone = false;
    private final List<Item> itemsForSale;

    public TalkToBartenderNode(boolean inTown) {
        super("Talk to bartender");
        this.inTown = inTown;
        itemsForSale = new ArrayList<>(List.of(new ObolsDummyItem(10),
                new FoodDummyItem(5)));
        for (int i = MyRandom.randInt(4); i > 0; --i) {
            itemsForSale.add(new BeerPotion());
        }
        for (int i = MyRandom.randInt(3); i > 0; --i) {
            itemsForSale.add(new WinePotion());
        }
        if (MyRandom.randInt(4) != 0) {
            itemsForSale.add(new TentUpgradeItem());
        }
        if (MyRandom.randInt(4) == 0) {
            itemsForSale.add(new LargeTentUpgradeItem());
        }
        if (MyRandom.randInt(4) == 0) {
            itemsForSale.add(new FishingPole());
        }
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new TalkToBartenderState(this, model);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return STOOL;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().register(STOOL.getName(), p, STOOL);
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
        if (workDone) {
            model.setTimeOfDay(TimeOfDay.EVENING);
        }
    }

    public boolean isInTown() {
        return inTown;
    }

    public void setWorkDone(boolean workDone) {
        this.workDone = workDone;
    }

    public List<Item> getItemsForSale() {
        return itemsForSale;
    }
}
