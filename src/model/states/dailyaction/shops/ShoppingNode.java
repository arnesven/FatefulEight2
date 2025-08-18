package model.states.dailyaction.shops;

import model.GameStatistics;
import model.Model;
import model.TimeOfDay;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.Item;
import model.map.HexLocation;
import model.map.UrbanLocation;
import model.states.EveningState;
import model.states.GameState;
import model.states.ShopState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.ShopInteriorState;
import model.states.events.GeneralInteractionEvent;
import util.MyLists;
import util.MyRandom;
import util.MyUnaryIntFunction;
import view.MyColors;
import view.sprites.MiniItemSprite;
import view.sprites.SignSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class ShoppingNode extends DailyActionNode {
    private static final Sprite SPRITE = new Sprite32x32("shopping", "world_foreground.png", 0x22,
            MyColors.YELLOW, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);
    private static final Sprite SIGN = new SignSprite("generalsign", 0x06, MyColors.BLUE, MyColors.WHITE);
    private static final Sprite OUT_OF_BUSINESS_SIGN = new SignSprite("outofbusinesssign", 0x36, MyColors.RED, MyColors.WHITE);
    private static final String OUT_OF_BUSINESS_FLAG_SUFFIX = "out-of-business";

    private List<Item> shopInventory;
    private boolean triedBreakIn = false;
    private boolean[] haggleFlag = new boolean[]{true};
    private final ShopCustomer customer;
    private final ShopSupplier supplier;

    public ShoppingNode(Model model, String name) {
        super(name);
        shopInventory = makeInventory(model);
        Item customerItem = makeCustomerItem(model);
        if (customerItem != null && MyRandom.rollD6() > 4) {
            this.customer = new ShopCustomer(customerItem);
        } else {
            this.customer = null;
        }
        this.supplier = new ShopSupplier(MyRandom.rollD10() >= 9, MyRandom.sample(makeInventory(model)));
    }

    private Item makeCustomerItem(Model model) {
        for (int tries = 0 ; tries < 3; ++tries) {
            List<Item> candidates = makeInventory(model);
            candidates.removeIf(it -> MyLists.any(shopInventory,
                    it2 -> it2.getName().equals(it.getName())));
            if (!candidates.isEmpty()) {
                return MyRandom.sample(candidates);
            }
        }
        System.err.println("Could generate item for customer after 3 tries.");
        return null;
    }

    protected abstract List<Item> makeInventory(Model model);

    public abstract Sprite getLowerWallSprite();

    public abstract Sprite getDoorSprite();

    public abstract Sprite getOverDoorSprite();

    public abstract Sprite getBigSignSprite();

    public abstract Sprite[] getCounterItemSprites();

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        if (state.isEvening() && supportsBreakIn()) {
            state.print("The shop is closed. Do you want to try to break in? (Y/N) ");
            if (state.yesNoInput()) {
                breakIntoShop(model, state);
            }
            triedBreakIn = true;
            return state;
        }
        return new ShopInteriorState(model, this);
    }

    private void breakIntoShop(Model model, AdvancedDailyActionState state) {
        List<GameCharacter> groupB = null;
        if (model.getParty().size() > 1) {
            state.print("Which party members should participate in the burglary (group B)? ");
            List<GameCharacter> groupA = new ArrayList<>(model.getParty().getPartyMembers());
            groupB = new ArrayList<>();
            SplitPartySubView split = new SplitPartySubView(model.getSubView(), groupA, groupB);
            model.setSubView(split);
            model.getTutorial().burglary(model);
            state.waitForReturnSilently();
            if (groupB.isEmpty()) {
                state.println("Burglary cancelled.");
                return;
            }
            model.getParty().benchPartyMembers(groupA);
        } else {
            model.getTutorial().burglary(model);
            groupB = new ArrayList<>(model.getParty().getPartyMembers());
        }
        boolean result = model.getParty().doSoloLockpickCheck(model, state, getShopSecurity());
        int weightLimit = MyLists.intAccumulate(groupB,
                character -> character.getRace().getCarryingCapacity()*1000 - character.getEquipment().getTotalWeight());
        int accumulatedWeight = 0;
        if (result) {
            state.leaderSay("Okay, we're inside. Now let's gather up the booty!");
            if (shopInventory.isEmpty()) {
                state.partyMemberSay(groupB.get(0), "What, there's nothing here? Aaw... what a waste of time. Let's get out of here.");
                model.getParty().unbenchAll();
                return;
            }
            SubView oldSubView = model.getSubView();
            StealingSubView newSubView = new StealingSubView(shopInventory, weightLimit);
            CollapsingTransition.transition(model, newSubView);
            int bounty = 0;
            while (true) {
                state.waitForReturnSilently();
                if (newSubView.getTopIndex() == 0 || shopInventory.isEmpty()) {
                    break;
                }
                if (accumulatedWeight >= weightLimit) {
                    state.println("You cannot carry any more loot!");
                } else {
                    Item it = newSubView.getSelectedItem();
                    state.println("You stole " + it.getName() + ".");
                    GameStatistics.incrementItemsStolen(1);
                    shopInventory.remove(it);
                    it.addYourself(model.getParty().getInventory());
                    newSubView.removeItem(it);
                    bounty++;
                    accumulatedWeight += it.getWeight();
                    newSubView.setBountyAndWeight(bounty, accumulatedWeight);
                }
            }
            state.partyMemberSay(groupB.get(0), "Now let's try not to be spotted on our way out.");
            result = model.getParty().doCollectiveSkillCheck(model, state, Skill.Sneak, Math.max(1, bounty/2));
            if (!result) {
                state.printAlert("Your crime has been witnessed.");
                GeneralInteractionEvent.addToNotoriety(model, state, bounty * 10);
            }
            CollapsingTransition.transition(model, oldSubView);
            if (MyRandom.rollD10() < bounty - 1) {
                state.println("The " + getName() + " has gone out of business!");
                setOutOfBusiness(model);
            }
        } else {
            result = model.getParty().doCollectiveSkillCheck(model, state, Skill.Sneak, 2);
            if (!result) {
                state.printAlert("Your crime has been witnessed.");
                GeneralInteractionEvent.addToNotoriety(model, state, 10);
            }
        }
        model.getParty().unbenchAll();
    }

    protected boolean supportsBreakIn() {
        return true;
    }

    protected abstract int getShopSecurity();

    @Override
    public boolean returnNextState() {
        return triedBreakIn;
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

    public List<Item> getInventory() {
        return shopInventory;
    }

    public boolean[] getHaggleFlag() {
        return haggleFlag;
    }

    public ShopCustomer getCustomer() {
        return customer;
    }

    public ShopSupplier getSupplier() {
        return supplier;
    }
}
