package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.combat.loot.CombatLoot;
import model.combat.loot.SingleItemCombatLoot;
import model.enemies.BodyGuardEnemy;
import model.enemies.Enemy;
import model.enemies.ServantEnemy;
import model.items.Equipment;
import model.items.Item;
import model.items.MysteriousMap;
import model.items.clothing.PilgrimsCloak;
import model.items.weapons.ShortSword;
import model.states.GameState;
import model.states.ShopState;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class MerchantEvent extends GeneralInteractionEvent {
    private final boolean withIntro;
    private final CharacterAppearance apperance;
    private ArrayList<Item> items;
    private MysteriousMap map;

    public MerchantEvent(Model model, boolean withIntro, CharacterAppearance app) {
        super(model, "Trade with", MyRandom.randInt(20, 100));
        this.withIntro = withIntro;
        this.apperance = app;
    }

    public MerchantEvent(Model model) {
        this(model, true,
                PortraitSubView.makeRandomPortrait(Classes.MERCHANT));
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        this.items = new ArrayList<>();
        items.addAll(model.getItemDeck().draw(6));
        if (MyRandom.flipCoin()) {
            this.map = new MysteriousMap(model);
            items.add(map);
        }
        if (withIntro) {
            println("The party encounters a large wagon with tons of wares stacked upon it. " +
                    "Beside it stands a plump character in fancy clothing.");
            showExplicitPortrait(model, apperance, "Merchant");
        }
        return true;
    }

    @Override
    protected GameState doEndOfEventHook(Model model) {
        if (map != null && model.getParty().getInventory().getAllItems().contains(map)) {
            FindTreasureMapEvent.addDestinationTask(model, map);
        }
        return super.doEndOfEventHook(model);
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        portraitSay("Please, I have lots of merchandise and I just know you adventurer types are always in " +
                "need of something. Won't you please have a look?");
        ShopState.pressToEnterShop(this);
        ShopState merchantShop = new ShopState(model, "merchant", items,
                new int[]{items.get(0).getCost()+10, items.get(1).getCost()+10,
                        items.get(2).getCost()-2, items.get(3).getCost()-2,
                        items.get(4).getCost()/2, items.get(5).getCost()/2}, new boolean[]{true});
        merchantShop.run(model);
        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, apperance, "Merchant");
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a merchant. I buy and sell things. It sounds simple, but it can be hard to run a business.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        GameCharacter merchant = new GameCharacter("Merchant", "", apperance.getRace(), Classes.MERCHANT, apperance,
                Classes.NO_OTHER_CLASSES, new Equipment(new ShortSword(), new PilgrimsCloak(), null));
        merchant.setLevel(MyRandom.randInt(1, 4));
        return merchant;
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        List<Enemy> enemies = new ArrayList<>();
        for (int i = MyRandom.randInt(4); i > 0; --i) {
            enemies.add(new ServantEnemy(PortraitSubView.makeRandomPortrait(Classes.None)));
        }
        for (int i = MyRandom.randInt(1, 4); i > 0; --i) {
            enemies.add(new BodyGuardEnemy('C'));
        }
        return enemies;
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_IF_ADVANTAGE;
    }

    @Override
    protected List<CombatLoot> getExtraCombatLoot(Model model) {
        List<CombatLoot> loots = new ArrayList<>();
        for (Item it : items) {
            loots.add(new SingleItemCombatLoot(it));
        }
        return loots;
    }

    private GameCharacter makeCharacter() {
        GameCharacter merchant = new GameCharacter("Merchant", "",
                apperance.getRace(), Classes.MERCHANT, apperance,
                Classes.NO_OTHER_CLASSES, new Equipment(new ShortSword(), new PilgrimsCloak(), null));
        merchant.setLevel(MyRandom.randInt(1, 4));
        return merchant;
    }
}
