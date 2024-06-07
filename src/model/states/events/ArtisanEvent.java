package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.combat.loot.CombatLoot;
import model.combat.loot.SingleItemCombatLoot;
import model.enemies.ApprenticeEnemy;
import model.enemies.Enemy;
import model.enemies.ServantEnemy;
import model.items.Equipment;
import model.items.Item;
import model.items.clothing.FancyJerkin;
import model.items.weapons.Warhammer;
import model.states.ShopState;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class ArtisanEvent extends GeneralInteractionEvent {
    private final boolean withIntro;
    private ArrayList<Item> itemList;
    private AdvancedAppearance portrait;
    private String subType;

    public ArtisanEvent(Model model, boolean withIntro) {
        super(model, "Trade with", MyRandom.randInt(10, 40));
        this.withIntro = withIntro;
    }

    public ArtisanEvent(Model model) {
        this(model, true);
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        if (withIntro) {// TODO: Can also improve item for a cost. Common items are cheaper to improve!
            print("The party encounters an artisan on the road. ");
        }
        print("This particular artisan is a");
        int roll = MyRandom.rollD10();
        this.itemList = new ArrayList<>();
        if (roll <= 2) {
            subType = "Tailor";
            println(" tailor and offers to sell you some apparel at a discount.");
            itemList.add(model.getItemDeck().getRandomApparel());
        } else if (roll <= 4) {
            subType = "Smith";
            println(" smith and offers to sell you a weapon at a discount.");
            itemList.add(model.getItemDeck().getRandomWeapon());
        } else if (roll <= 6) {
            subType = "Jeweller";
            println(" a jeweller and offers to sell you an accessory at a discount.");
            itemList.add(model.getItemDeck().getRandomJewelry());
        } else if (roll <= 8) {
            subType = "Cobbler";
            println(" a cobbler and offers to sell you a pair of shoes at a discount.");
            itemList.add(model.getItemDeck().getRandomShoes());
        } else if (roll <= 10) {
            subType = "Enchanter";
            println(" an enchanter and offers to sell you a wand at a discount.");
            itemList.add(model.getItemDeck().getRandomWand());
        }
        this.portrait = PortraitSubView.makeRandomPortrait(Classes.ART);
        showExplicitPortrait(model, portrait, subType);
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        ShopState shop = new ShopState(model, "artisan", itemList,
                new int[]{itemList.get(0).getCost()/2});
        shop.setSellingEnabled(false);
        print("Press enter to continue.");
        waitForReturn();
        shop.run(model);
        println("The artisan also offers to educate you in the ways of his trade, ");
        ChangeClassEvent changeClassEvent = new ChangeClassEvent(model, Classes.ART);
        changeClassEvent.areYouInterested(model);

        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, portrait, subType);
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a skilled artisan. I make things from cloth or finer metals.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        GameCharacter gc = new GameCharacter(subType, "", portrait.getRace(), Classes.ART, portrait,
                Classes.NO_OTHER_CLASSES,
                new Equipment(new Warhammer(), new FancyJerkin(), null));
        gc.setLevel(MyRandom.randInt(1, 4));
        return gc;
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        int dieRoll = MyRandom.rollD10();
        if (dieRoll <= 2) {
            return List.of(new ServantEnemy(PortraitSubView.makeRandomPortrait(Classes.None)));
        }
        if (dieRoll <= 4) {
            return List.of(new ServantEnemy(PortraitSubView.makeRandomPortrait(Classes.None)),
                    new ServantEnemy(PortraitSubView.makeRandomPortrait(Classes.None)));
        }
        if (dieRoll <= 6) {
            return List.of(new ApprenticeEnemy(PortraitSubView.makeRandomPortrait(Classes.ART), new Warhammer()));
        }
        if (dieRoll <= 8) {
            return makeBodyGuards(1, 'C');
        }
        return makeBodyGuards(2, 'C');
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.ALWAYS_ESCAPE;
    }

    @Override
    protected List<CombatLoot> getExtraCombatLoot(Model model) {
        List<CombatLoot> loots = new ArrayList<>();
        for (Item it : itemList) {
            loots.add(new SingleItemCombatLoot(it));
        }
        return loots;
    }
}
