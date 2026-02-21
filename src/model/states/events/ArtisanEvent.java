package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.SpecificArtisanClass;
import model.combat.loot.CombatLoot;
import model.combat.loot.SingleItemCombatLoot;
import model.enemies.ApprenticeEnemy;
import model.enemies.Enemy;
import model.enemies.ServantEnemy;
import model.items.*;
import model.items.accessories.*;
import model.items.clothing.Clothing;
import model.items.clothing.FancyJerkin;
import model.items.clothing.JustClothes;
import model.items.weapons.*;
import model.races.AllRaces;
import model.races.Race;
import model.states.ShopState;
import util.MyRandom;
import view.MyColors;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class ArtisanEvent extends GeneralInteractionEvent {
    private final boolean withIntro;
    private final Race race;
    private ArrayList<Item> itemList;
    private AdvancedAppearance portrait;
    private ArtisanType subType;
    private int improvedTimes = 0;

    public ArtisanEvent(Model model, boolean withIntro, ArtisanType subType, Race race) {
        super(model, "Trade with", MyRandom.randInt(10, 40));
        this.withIntro = withIntro;
        this.subType = subType;
        this.race = race;
    }

    public ArtisanEvent(Model model, boolean withIntro, ArtisanType subType) {
        this(model, withIntro, subType, MyRandom.sample(AllRaces.getAllRaces()));
    }

    public ArtisanEvent(Model model, boolean withIntro, Race race) {
        this(model, withIntro, randomSubType(), race);
    }

    private static ArtisanType randomSubType() {
        return MyRandom.sample(List.of(
                new Tailor(),
                new Tailor(),
                new Armorer(),
                new Armorer(),
                new Smith(),
                new Smith(),
                new Fletcher(),
                new Jeweller(),
                new Hatter(),
                new Cobbler(),
                new Carpenter(),
                new Enchanter()));
    }

    public ArtisanEvent(Model model) {
        this(model, true, MyRandom.sample(Race.getAllRaces()));
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit " + subType.getName(), "I know a very skilled " + subType.getName() + " in town");
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        String cardText = "";
        if (withIntro) {
            cardText = "The party encounters an artisan. ";
        }
        cardText += "This particular artisan is a";
        this.itemList = new ArrayList<>();


        showEventCard(cardText + " an " + subType.getName().toLowerCase() + " and offers to sell you " +
                subType.getItemType() + " at a discount.");


        itemList.add(subType.getItem(model));
        this.portrait = PortraitSubView.makeRandomPortrait(
                subType.makeArtisanSubClass(), race);
        showExplicitPortrait(model, portrait, subType.getName());
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        ShopState shop = new ShopState(model, subType.getName(), itemList,
                new int[]{itemList.get(0).getCost()/2}, new boolean[]{true});
        shop.setSellingEnabled(false);
        portraitSay("This is a finely crafted item. I can't let it go for less.");
        print("Press enter to continue.");
        waitForReturn();
        shop.run(model);
        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, portrait, subType.getName());
        offerImprove(model);

        portraitSay("You know... If you're interested I could give you some pointers...");
        println("The " + subType.getName() + " is offering to educate you in the ways of his trade, ");
        ChangeClassEvent changeClassEvent = new ChangeClassEvent(model, Classes.ART);
        changeClassEvent.areYouInterested(model);

        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, portrait, subType.getName());
        return true;
    }

    private void offerImprove(Model model) {
        portraitSay("If you want, I could apply my trade to some of your equipment. Interested?");
        print("The " + subType.getName() + " is offering to upgrade " + subType.getItemType() + " for your party members. Are you interested (Y/N) ");
        while (yesNoInput()) {
            print("For whom do you want to upgrade an item?");
            GameCharacter gc = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
            if (subType.didImprove(model, this, gc, itemList)) {
                partyMemberSay(gc, MyRandom.sample(List.of("Thank you!", "Much obliged.", "It looks great!", "Beautiful!")));
                improvedTimes++;
            }
            if (improvedTimes < 3) {
                print("Do you want to upgrade " + subType.getItemType() + " for another party members? (Y/N) ");
            } else {
                portraitSay("Unfortunately I don't have enough materials to upgrade anything more.");
                break;
            }
        }
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a skilled artisan. I make things from cloth or finer metals.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        GameCharacter gc = new GameCharacter(subType.getName(), "", portrait.getRace(), subType.makeArtisanSubClass(), portrait,
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

    private abstract static class ArtisanType {
        private final String name;
        private final String itemType;
        private final MyColors shirtColor;
        private final MyColors apronColor;
        private final ArrayList<Item> alreadyUpgraded;

        public ArtisanType(String name, String itemType, MyColors shirtColor, MyColors apronColor) {
            this.name = name;
            this.itemType = itemType;
            this.shirtColor = shirtColor;
            this.apronColor = apronColor;
            this.alreadyUpgraded = new ArrayList<>();
        }

        public abstract Item getItem(Model model);

        protected Item getItemForUpgrade(Equipment equipment) {
            Item it = getApplicableItem(equipment);
            if (it == null || alreadyUpgraded.contains(it) || !it.isCraftable() || !it.supportsHigherTier()) {
                return null;
            }
            return it;
        }

        protected abstract Item getApplicableItem(Equipment equipment);

        public String getName() {
            return name;
        }

        public String getItemType() {
            return itemType;
        }

        public boolean didImprove(Model model, ArtisanEvent artisanEvent, GameCharacter gc,
                                  List<Item> sellItems) {
            Item it = getItemForUpgrade(gc.getEquipment());
            if (it == null) {
                artisanEvent.println("That character does not have an item which the " + getName() + " can upgrade.");
                return false;
            }
            artisanEvent.portraitSay("Do you want me to upgrade something? Let's see...");
            if (sellItems.contains(it)) {
                artisanEvent.portraitSay("You mean the item I just sold you? Sorry, I made that as good as I can already!");
                return false;
            }
            int cost = 0;
            if (it.getPrevalence() == Prevalence.common || it instanceof StartingItem) {
                artisanEvent.portraitSay("Oh, it's a " + it.getName() + ". I've seen countless of these. Upgrading it should be easy.");
                cost = it.getCost() / 2;
            } else if (it.getPrevalence() == Prevalence.uncommon) {
                artisanEvent.portraitSay("Hmmm, it's a " + it.getName() + ". I've seen some of these before. I can upgrade it.");
                cost = it.getCost();
            } else if (it.getPrevalence() == Prevalence.rare) {
                artisanEvent.portraitSay("It's a " + it.getName() + " you say? I think I've seen something like this before. " +
                        "Upgrading it will be difficult.");
                cost = it.getCost() * 2;
            } else {
                artisanEvent.portraitSay("It's a " + it.getName() + " you say? I've never seen anything like it. " +
                        "I'm sorry, but I don't think I can upgrade it for you.");
                return false;
            }
            artisanEvent.print("The " + getName() + " will charge you " + cost + " gold to upgrade the " +
                    it.getName());
            if (cost > model.getParty().getGold()) {
                artisanEvent.println(". Unfortunately, that is more than you have.");
                return false;
            }
            artisanEvent.print(" is that acceptable to you? (Y/N) ");
            if (!artisanEvent.yesNoInput()) {
                return false;
            }
            artisanEvent.println("You hand over " + cost + " gold and the " + getName() + " starts working at once.");
            model.getParty().spendGold(cost);
            artisanEvent.println("A while later the " + getName() + " returns to you and hands you your item.");
            artisanEvent.portraitSay("Here you go. I'm quite pleased with the result myself.");

            Item upgraded;
            if (it instanceof HigherTierItem) {
                HigherTierItem higherItem = (HigherTierItem)it;
                upgraded = higherItem.getInnerItem().makeHigherTierCopy(higherItem.getTier()+1);
            } else {
                upgraded = it.makeHigherTierCopy(1);
            }
            alreadyUpgraded.add(upgraded);
            artisanEvent.println("The " + it.getName() + " was upgrade to " + upgraded.getName() + "!");
            if (gc.getEquipment().getWeapon() == it) {
                gc.getEquipment().setWeapon((Weapon) upgraded);
            } else if (gc.getEquipment().getClothing() == it) {
                gc.getEquipment().setClothing((Clothing) upgraded);
            } else if (gc.getEquipment().getAccessory() == it) {
                gc.getEquipment().setAccessory((Accessory) upgraded);
            }
            return true;
        }

        public CharacterClass makeArtisanSubClass() {
            return new SpecificArtisanClass(shirtColor, apronColor);
        }
    }

    private static class Tailor extends ArtisanType {
        public Tailor() {
            super("Tailor", "some apparel", MyColors.BLUE, MyColors.BROWN);
        }

        @Override
        public Item getItem(Model model) {
            return model.getItemDeck().getRandomApparel();
        }

        @Override
        protected Item getApplicableItem(Equipment equipment) {
            if (equipment.getClothing() instanceof JustClothes) {
                return null;
            }
            return equipment.getClothing();
        }
    }

    public static class Smith extends ArtisanType {
        public Smith() {
            super("Smith", "a weapon", MyColors.DARK_GRAY, MyColors.BROWN);
        }

        @Override
        public Item getItem(Model model) {
            return model.getItemDeck().getRandomWeapon();
        }

        @Override
        protected Item getApplicableItem(Equipment equipment) {
            if (equipment.getWeapon() instanceof NaturalWeapon ||
                    equipment.getWeapon() instanceof WandWeapon) {
                return null;
            }
            return equipment.getWeapon();
        }
    }

    private static class Jeweller extends ArtisanType {
        public Jeweller() {
            super("Jeweller", "an accessory", MyColors.LIGHT_BLUE, MyColors.DARK_GRAY);
        }

        @Override
        public Item getItem(Model model) {
            return model.getItemDeck().getRandomJewelry();
        }

        @Override
        protected Item getApplicableItem(Equipment equipment) {
            if (equipment.getAccessory() instanceof JewelryItem ||
                    (equipment.getAccessory() instanceof HigherTierAccessory &&
                    ((HigherTierAccessory) equipment.getAccessory()).getInnerItem() instanceof JewelryItem)) {
                return equipment.getAccessory();
            }
            return null;
        }
    }

    private static class Cobbler extends ArtisanType {
        public Cobbler() {
            super("Cobbler", "a pair of shoes", MyColors.BROWN, MyColors.DARK_GRAY);
        }

        @Override
        public Item getItem(Model model) {
            return model.getItemDeck().getRandomShoes();
        }

        @Override
        protected Item getApplicableItem(Equipment equipment) {
            if (equipment.getAccessory() instanceof ShoesItem ||
                    (equipment.getAccessory() instanceof HigherTierAccessory &&
                    ((HigherTierAccessory) equipment.getAccessory()).getInnerItem() instanceof ShoesItem)) {
                return equipment.getAccessory();
            }
            return null;
        }
    }

    public static class Enchanter extends ArtisanType {
        public Enchanter() {
            super("Enchanter", "a wand or staff", MyColors.PURPLE, MyColors.DARK_RED);
        }

        @Override
        public Item getItem(Model model) {
            return model.getItemDeck().getRandomWand();
        }

        @Override
        protected Item getApplicableItem(Equipment equipment) {
            if (equipment.getWeapon().isOfType(WandWeapon.class) ||
                    equipment.getWeapon().isOfType(StaffWeapon.class)) {
                return equipment.getWeapon();
            }
            return null;
        }
    }

    public static class Armorer extends ArtisanType {
        public Armorer() {
            super("Armorer", "a set of armor", MyColors.DARK_RED, MyColors.BROWN);
        }

        @Override
        public Item getItem(Model model) {
            do {
                Clothing clothing = model.getItemDeck().getRandomApparel();
                if (clothing.getAP() > 1) {
                    return clothing;
                }
            } while (true);
        }

        @Override
        protected Item getApplicableItem(Equipment equipment) {
            if (equipment.getClothing().getAP() < 2) {
                return null;
            }
            return equipment.getClothing();
        }
    }

    private static class Fletcher extends ArtisanType {
        public Fletcher() {
            super("Fletcher", "a bow", MyColors.LIGHT_GREEN, MyColors.BROWN);
        }

        @Override
        public Item getItem(Model model) {
            do {
                Weapon weapon = model.getItemDeck().getRandomWeapon();
                if (weapon instanceof BowWeapon) {
                    return weapon;
                }
            } while (true);
        }

        @Override
        protected Item getApplicableItem(Equipment equipment) {
            if (equipment.getWeapon().isOfType(BowWeapon.class)) {
                return equipment.getWeapon();
            }
            return null;
        }
    }

    private static class Hatter extends ArtisanType {
        public Hatter() {
            super("Hatter", "some head gear", MyColors.LIGHT_RED, MyColors.BROWN);
        }

        @Override
        public Item getItem(Model model) {
            return model.getItemDeck().getRandomHeadgear();
        }

        @Override
        protected Item getApplicableItem(Equipment equipment) {
            if (equipment.getAccessory() instanceof HeadGearItem ||
                    (equipment.getAccessory() instanceof HigherTierAccessory &&
                    ((HigherTierAccessory) equipment.getAccessory()).getInnerItem() instanceof HeadGearItem)) {
                return equipment.getAccessory();
            }
            return equipment.getAccessory();
        }
    }

    private static class Carpenter extends ArtisanType {
        public Carpenter() {
            super("carpenter", "a shield", MyColors.LIGHT_YELLOW, MyColors.BROWN);
        }

        @Override
        public Item getItem(Model model) {
            return model.getItemDeck().getRandomShield();
        }

        @Override
        protected Item getApplicableItem(Equipment equipment) {
            if (equipment.getAccessory() instanceof ShieldItem ||
                    (equipment.getAccessory() instanceof HigherTierAccessory &&
                    ((HigherTierAccessory) equipment.getAccessory()).getInnerItem() instanceof ShieldItem)) {
                return equipment.getAccessory();
            }
            return null;
        }
    }
}
