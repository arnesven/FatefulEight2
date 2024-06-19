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
import model.items.*;
import model.items.accessories.Accessory;
import model.items.accessories.ShoesItem;
import model.items.clothing.Clothing;
import model.items.clothing.FancyJerkin;
import model.items.clothing.JustClothes;
import model.items.weapons.*;
import model.states.ShopState;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class ArtisanEvent extends GeneralInteractionEvent {
    private final boolean withIntro;
    private ArrayList<Item> itemList;
    private AdvancedAppearance portrait;
    private ArtisanType subType;

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
            print("The party encounters an artisan. ");
        }
        print("This particular artisan is a");
        int roll = MyRandom.rollD10();
        this.itemList = new ArrayList<>();
        if (roll <= 2) {
            subType = new Tailor();
        } else if (roll <= 4) {
            subType = new Smith();
        } else if (roll <= 6) {
            subType = new Jeweller();
        } else if (roll <= 8) {
            subType = new Cobbler();
        } else if (roll <= 10) {
            subType = new Enchanter();
        }
        println(" an " + subType.getName().toLowerCase() + " and offers to sell you " +
                subType.getItemType() + " at a discount.");
        itemList.add(subType.getItem(model));
        this.portrait = PortraitSubView.makeRandomPortrait(Classes.ART);
        showExplicitPortrait(model, portrait, subType.getName());
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        ShopState shop = new ShopState(model, subType.getName().toLowerCase(), itemList,
                new int[]{itemList.get(0).getCost()/2});
        shop.setSellingEnabled(false);
        portraitSay("This is a finely crafted item. I can't let it go for less.");
        print("Press enter to continue.");
        waitForReturn();
        shop.run(model);
        setCurrentTerrainSubview(model);
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
            if (subType.didImprove(model, this, gc)) {
                partyMemberSay(gc, MyRandom.sample(List.of("Thank you!", "Much obliged.", "It looks great!", "Beautiful!")));
            }
            print("Do you want to upgrade " + subType.getItemType() + " for another party members? (Y/N) ");
        }
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a skilled artisan. I make things from cloth or finer metals.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        GameCharacter gc = new GameCharacter(subType.getName(), "", portrait.getRace(), Classes.ART, portrait,
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

    private abstract static class ArtisanType {
        private final String name;
        private final String itemType;

        public ArtisanType(String name, String itemType) {
            this.name = name;
            this.itemType = itemType;
        }

        public abstract Item getItem(Model model);

        protected abstract Item getApplicableItem(Equipment equipment);

        public String getName() {
            return name;
        }

        public String getItemType() {
            return itemType;
        }

        public boolean didImprove(Model model, ArtisanEvent artisanEvent, GameCharacter gc) {
            Item it = getApplicableItem(gc.getEquipment());
            if (it == null) {
                artisanEvent.println("That character does not have an item which the " + getName() + " can upgrade.");
                return false;
            }
            artisanEvent.portraitSay("Do you want me to upgrade something? Let's see...");
            int cost = 0;
            if (it.getPrevalence() == Prevalence.common || it instanceof StartingItem) {
                artisanEvent.portraitSay("Oh, it's a " + it.getName() + ". I've seen countless of these. Upgrading it should be easy.");
                cost = it.getCost() / 2;
            } else if (it.getPrevalence() == Prevalence.uncommon) {
                artisanEvent.portraitSay("Hmmm, it's a " + it.getName() + ". I've seen a some of these before. I can upgrade it.");
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
            model.getParty().addToGold(-cost);
            artisanEvent.println("A while later the " + getName() + " returns to you hand hands you your item.");
            artisanEvent.portraitSay("Here you go. I'm quite pleased with the result myself.");

            Item upgraded;
            if (it instanceof HigherTierItem) {
                HigherTierItem higherItem = (HigherTierItem)it;
                upgraded = higherItem.getInnerItem().makeHigherTierCopy(higherItem.getTier()+1);
            } else {
                upgraded = it.makeHigherTierCopy(1);
            }
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

    }

    private static class Tailor extends ArtisanType {
        public Tailor() {
            super("Tailor", "some apparel");
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

    private static class Smith extends ArtisanType {
        public Smith() {
            super("Smith", "a weapon");
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
            super("Jeweller", "an accessory");
        }

        @Override
        public Item getItem(Model model) {
            return model.getItemDeck().getRandomJewelry();
        }

        @Override
        protected Item getApplicableItem(Equipment equipment) {
            return equipment.getAccessory();
        }
    }

    private static class Cobbler extends ArtisanType {
        public Cobbler() {
            super("Cobbler", "a pair of shoes");
        }

        @Override
        public Item getItem(Model model) {
            return model.getItemDeck().getRandomShoes();
        }

        @Override
        protected Item getApplicableItem(Equipment equipment) {
            if (equipment.getAccessory() instanceof ShoesItem) {
                return equipment.getAccessory();
            }
            return null;
        }
    }

    private static class Enchanter extends ArtisanType {
        public Enchanter() {
            super("Enchanter", "a wand");
        }

        @Override
        public Item getItem(Model model) {
            return model.getItemDeck().getRandomWand();
        }

        @Override
        protected Item getApplicableItem(Equipment equipment) {
            if (equipment.getWeapon() instanceof WandWeapon) {
                return equipment.getWeapon();
            }
            return null;
        }
    }
}
