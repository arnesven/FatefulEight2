package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.enemies.Enemy;
import model.items.Equipment;
import model.items.HigherTierClothing;
import model.items.Item;
import model.items.clothing.HeavyArmorClothing;
import model.items.clothing.LeatherArmor;
import model.items.weapons.*;
import model.races.Race;
import model.states.ShopState;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class SmithEvent extends GeneralInteractionEvent {
    private final Race smithRace;
    private AdvancedAppearance portrait;

    public SmithEvent(Model model, Race smithRace) {
        super(model, "Talk to", MyRandom.randInt(40, 60));
        this.smithRace = smithRace;
    }

    public SmithEvent(Model model) {
        this(model, Race.randomRace());
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit Smith", "There's a smith who often manufactures items of great quality");
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        this.portrait = PortraitSubView.makeRandomPortrait(new ArtisanEvent.Smith().makeArtisanSubClass(), smithRace);
        showExplicitPortrait(model, portrait, "Smith");
        println("The smith stands in the heat from the furnace. " + heOrSheCap(portrait.getGender()) + " is banging with a mallet on an " +
                "anvil.");
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        println(heOrSheCap(portrait.getGender()) + " offers you a unique item.");
        List<Item> items = new ArrayList<>();
        do {
            Item it = model.getItemDeck().getRandomItem(0.98);
            if (isSmithyItem(it)) {
                items.add(it);
                break;
            }
        } while (true);

        ShopState shop = new ShopState(model, "Smith", items, new int[]{items.get(0).getCost()/2});
        shop.run(model);
        print("The smith also offers to instruct you in the ways of being an Artisan, ");
        ChangeClassEvent change = new ChangeClassEvent(model, Classes.ART);
        change.areYouInterested(model);
        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, portrait, "Smith");
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a blacksmith. I manufacture or improve things from iron and steel.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        GameCharacter gc = new GameCharacter("Smith", "", portrait.getRace(), Classes.ART, portrait,
                Classes.NO_OTHER_CLASSES, new Equipment(new Warhammer(), new LeatherArmor(), null));
        gc.setLevel(MyRandom.randInt(3, 6));
        return gc;
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        return new ArrayList<>();
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_IF_ADVANTAGE;
    }

    private boolean isSmithyItem(Item it) {
        return it instanceof BladedWeapon || it instanceof BluntWeapon || it instanceof AxeWeapon ||
                (it instanceof HeavyArmorClothing) || it instanceof HigherTierWeapon || it instanceof HigherTierClothing;
    }
}
