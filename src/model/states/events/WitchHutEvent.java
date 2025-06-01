package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.enemies.Enemy;
import model.items.Equipment;
import model.items.Item;
import model.items.clothing.CultistsRobes;
import model.items.potions.Potion;
import model.items.weapons.SkullWand;
import model.states.ShopState;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class WitchHutEvent extends MagicExpertGeneralInteractionEvent {
    private AdvancedAppearance portrait;

    public WitchHutEvent(Model model) {
        super(model, "Talk to", MyRandom.randInt(10, 30));
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit Witch",
                "When I passed through here last, I encountered a witch in a little hut");
    }

    @Override
    public String getDistantDescription() {
        return "a little hut";
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        this.portrait = PortraitSubView.makeRandomPortrait(Classes.WIT);
        showExplicitPortrait(model, portrait, "Witch");
        println("You find a small hut in a dank grove. Light emanates from " +
                "the window. Inside a witch is stirring a cauldron and " +
                "mumbling strange rhymes.");
        randomSayIfPersonality(PersonalityTrait.rude, new ArrayList<>(),
                "Is that an incantation or she just insane?");
        randomSayIfPersonality(PersonalityTrait.friendly, new ArrayList<>(),
                "Excuse us madam...");
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        println("She beckons you inside and offers to sell you a couple of bottles of the draft.");
        List<Item> itemList = new ArrayList<>();
        Potion pot = model.getItemDeck().getRandomPotion();
        itemList.addAll(List.of(pot.copy(), pot.copy(), pot.copy()));
        ShopState shop = new ShopState(model, "Witch", itemList,
                new int[]{pot.getCost()/2, pot.getCost()/2, pot.getCost()/2}, new boolean[]{true});
        shop.setSellingEnabled(false);
        print("Press enter to continue.");
        waitForReturn();
        shop.run(model);
        println("The witch also offers to reveal the dark secrets of witchcraft, ");
        ChangeClassEvent changeClassEvent = new ChangeClassEvent(model, Classes.WIT);
        changeClassEvent.areYouInterested(model);
        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, portrait, "Witch");
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a witch. I brew potions and handle other 'special' magical requests.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        GameCharacter gc = new GameCharacter("Witch", "", portrait.getRace(), Classes.WIT, portrait,
                Classes.NO_OTHER_CLASSES, new Equipment(new SkullWand(), new CultistsRobes(), model.getItemDeck().getRandomJewelry()));
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
}
