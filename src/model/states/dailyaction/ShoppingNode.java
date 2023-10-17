package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import model.classes.Skill;
import model.items.Item;
import model.map.HexLocation;
import model.map.UrbanLocation;
import model.states.GameState;
import model.states.ShopState;
import util.MyRandom;
import view.MyColors;
import view.sprites.SignSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;

import java.awt.*;
import java.util.List;

public abstract class ShoppingNode extends DailyActionNode {
    private static final Sprite SPRITE = new Sprite32x32("shopping", "world_foreground.png", 0x22,
            MyColors.YELLOW, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);
    private static final Sprite SIGN = new SignSprite("generalsign", 0x06, MyColors.BLUE, MyColors.WHITE);
    private static final Sprite OUT_OF_BUSINESS_SIGN = new SignSprite("outofbusinesssign", 0x36, MyColors.RED, MyColors.WHITE);
    private static final String OUT_OF_BUSINESS_FLAG_SUFFIX = "out-of-business";
    private List<Item> shopInventory;
    private boolean triedBreakIn = false;

    public ShoppingNode(Model model, String name) {
        super(name);
        shopInventory = makeInventory(model);
    }

    protected abstract List<Item> makeInventory(Model model);

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        if (state.isEvening()) {
            state.print("The shop is closed. Do you want to try to break in? (Y/N) ");
            if (state.yesNoInput()) {
                boolean result = model.getParty().doSoloLockpickCheck(model, state, getShopSecurity());
                if (result) {
                    state.leaderSay("Okay, we're inside. Now let's gather up the booty!");
                    int bounty = 0;
                    for (Item it : shopInventory) {
                        state.println("You stole " + it.getName() + ".");
                        it.addYourself(model.getParty().getInventory());
                        bounty++;
                    }
                    setOutOfBusiness(model);
                    state.leaderSay("Now let's try not to be spotted on our way out.");
                    result = model.getParty().doCollectiveSkillCheck(model, state, Skill.Sneak, bounty/2);
                    if (!result) {
                        state.println("!Your crime has been witnessed, your notoriety has increased!");
                        model.getParty().addToNotoriety(bounty * 10);
                    }
                }
            }
            triedBreakIn = true;
            return model.getCurrentHex().getEveningState(model, false, false);
        }
        return new ShopState(model, getName(), shopInventory, getSpecialPrices(shopInventory));
    }

    protected abstract int getShopSecurity();

    @Override
    public boolean returnNextState() {
        return triedBreakIn;
    }

    protected int[] getSpecialPrices(List<Item> inventory) {
        return null;
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }

    @Override
    public Sprite getForegroundSprite() {
        return SIGN;
    }

    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().register(getBackgroundSprite().getName(), new Point(p), getBackgroundSprite());
        Sprite fg = getForegroundSprite();
        if (isOutOfBusiness(model)) {
            fg = OUT_OF_BUSINESS_SIGN;
        }
        if (fg != null) {
            p.x += 2;
            p.y += 2;
            model.getScreenHandler().register("objectforeground", p, fg, 1);
        }
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState townDailyActionState, Model model) {
        if (isOutOfBusiness(model)) {
            townDailyActionState.println("This shop has been robbed and has since gone out of business.");
            return false;
        }
        return true;
    }

    private boolean isOutOfBusiness(Model model) {
        HexLocation location = model.getCurrentHex().getLocation();
        if (!(location instanceof UrbanLocation)) {
            return false;
        }
        Boolean outOfBusiness = model.getSettings().getMiscFlags().get(
                ((UrbanLocation) location).getPlaceName() + getName() + OUT_OF_BUSINESS_FLAG_SUFFIX);
        return outOfBusiness != null && outOfBusiness;
    }

    private void setOutOfBusiness(Model model) {
        HexLocation location = model.getCurrentHex().getLocation();
        if (!(location instanceof UrbanLocation)) {
            return;
        }
        model.getSettings().getMiscFlags().put(
                ((UrbanLocation) location).getPlaceName() + getName() + OUT_OF_BUSINESS_FLAG_SUFFIX,
                true);
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
        if (state.isMorning()) {
            model.setTimeOfDay(TimeOfDay.MIDDAY);
        }
    }
}
