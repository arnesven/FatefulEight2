package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.classes.CharacterClass;
import model.combat.conditions.BlessedCondition;
import model.combat.conditions.PoisonCondition;
import model.enemies.ElfEnemy;
import model.enemies.Enemy;
import model.enemies.FaeryEnemy;
import model.items.Equipment;
import model.items.HigherTierClothing;
import model.items.HigherTierItem;
import model.items.Item;
import model.items.accessories.Accessory;
import model.items.accessories.HigherTierAccessory;
import model.items.clothing.Clothing;
import model.items.clothing.JustClothes;
import model.items.weapons.HigherTierWeapon;
import model.items.weapons.NaturalWeapon;
import model.items.weapons.Weapon;
import model.races.Race;
import model.ruins.DungeonMaker;
import model.ruins.DungeonRoom;
import model.ruins.FinalDungeonLevel;
import model.ruins.RuinsDungeon;
import model.ruins.objects.CenterDungeonObject;
import model.ruins.objects.DungeonChest;
import model.ruins.objects.DungeonMonster;
import model.ruins.objects.DungeonObject;
import model.states.DailyEventState;
import model.states.ExploreRuinsState;
import model.states.GameState;
import model.states.RecruitState;
import util.MyLists;
import util.MyRandom;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;
import view.subviews.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SecretGardenEvent extends DailyEventState {
    public SecretGardenEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The party comes to a neat hedge.");
        leaderSay("Hey, these bushes look like they've been planted...");
        leaderSay("And here's a gate.");
        println("The trees in the garden have grown so densely that light has trouble getting through. " +
                "You wonder what mysteries lurks within.");
        print("Do you enter the secret garden? (Y/N) ");
        if (yesNoInput()) {
            RuinsDungeon dungeon = new RuinsDungeon(DungeonMaker.makeGardenDungeon(8));
            FinalGardenRoom finalRoom = new FinalGardenRoom();
            FinalDungeonLevel finalLevel = (FinalDungeonLevel) dungeon.getLevel(dungeon.getNumberOfLevels()-1);
            finalLevel.setFinalRoom(finalRoom);

            ExploreRuinsState explore = new ExploreSecretGardenState(model, dungeon, "Secret Garden");
            explore.run(model);
            println("You left the secret garden.");
        } else {
            leaderSay("Perhaps it's better not to trespass here.");
        }
    }


    private Item selectItem(GameCharacter gc) {
        List<Item> options = new ArrayList<>();
        if (!(gc.getEquipment().getWeapon() instanceof NaturalWeapon)) {
            options.add(gc.getEquipment().getWeapon());
        }
        if (!(gc.getEquipment().getClothing() instanceof JustClothes)) {
            options.add(gc.getEquipment().getClothing());
        }
        if (gc.getEquipment().getAccessory() != null) {
            options.add(gc.getEquipment().getAccessory());
        }
        List<String> optStrings = MyLists.transform(options, Item::getName);
        optStrings.add("Cancel");
        int selected = multipleOptionArrowMenu(getModel(), 24, 24, optStrings);
        if (selected == options.size()) {
            return null;
        }
        return options.get(selected);
    }

    private static class ExploreSecretGardenState extends ExploreRuinsState {
        public ExploreSecretGardenState(Model model, RuinsDungeon dungeon, String ruinsType) {
            super(model, dungeon, ruinsType);
        }

        @Override
        public CombatTheme getCombatTheme() {
            if (getCurrentLevel() == 1) {
                return new DungeonTheme();
            }
            return new GrassCombatTheme(); // TODO: Dark Woods combat theme.
        }

        @Override
        public String getCurrentRoomInfo() {
            return "";
        }
    }

    private class FinalGardenRoom extends DungeonRoom {

        public FinalGardenRoom() {
            super(3, 3);
            int roll = MyRandom.rollD6();
            if (roll < 3) {
                addObject(new ChestOfIngredients());
            } else if (roll < 5) {
                addObject(new GrandFaery());
            } else if (roll < 6) {
                addObject(new LostElf());
            } else {
                addObject(new MagicSpring());
            }

        }

        @Override
        public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
            exploreRuinsState.leaderSay("What's this, a secret room beneath the garden?");
            super.entryTrigger(model, exploreRuinsState);
            exploreRuinsState.print("Press enter to continue.");
            exploreRuinsState.waitForReturn();
            exploreRuinsState.setDungeonExited(true);
            exploreRuinsState.getDungeon().setCompleted(true);
        }
    }

    private static class ChestOfIngredients extends DungeonChest {
        public ChestOfIngredients() {
            super(new Random());
        }

        @Override
        public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
            exploreRuinsState.leaderSay("A chest!");
            exploreRuinsState.println("The party gains 50 ingredients!");
            model.getParty().getInventory().addToIngredients(50);
        }
    }


    private static class LostElf extends DungeonMonster {
        public LostElf() {
            super(List.of(new ElfEnemy('A')));
        }

        @Override
        public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
            exploreRuinsState.leaderSay("An elf? Who might you be?");
            exploreRuinsState.printQuote("Elf", "Thank goodness. You have no idea how long I've been trapped here. " +
                    "I've been forced to live off roots and insects!");
            exploreRuinsState.leaderSay("How did you even get down here in the first place?");
            exploreRuinsState.printQuote("Elf", "My ancestors planted this garden many centuries ago. I come " +
                    "here every so often to check on things. I never imagined I would lose my way in here however. " +
                    "I really would like to get out of here now. You don't suppose I could tag along with you for a while?");
            List<GameCharacter> list = model.getAvailableCharactersOfRace(Race.WOOD_ELF);
            if (list.isEmpty()) {
                list.add(GameState.makeRandomCharacter(4, Race.WOOD_ELF));
            }
            GameCharacter chara = MyRandom.sample(list);
            chara.setLevel(4);
            chara.setRandomStartingClass();
            SubView subView = model.getSubView();
            RecruitState recruitState = new RecruitState(model, List.of(chara));
            recruitState.setStartingGoldEnabled(true);
            recruitState.run(model);
            model.setSubView(subView);
        }
    }


    private class GrandFaery extends DungeonMonster {
        private static final String GRAND_FAERY_NAME = "Grand Faery";

        public GrandFaery() {
            super(List.of(new FaeryEnemy('A')));
        }

        @Override
        public void entryTrigger(Model model, ExploreRuinsState state) {
            state.leaderSay("Oh no, not more faeries...");
            String race = model.getParty().getLeader().getRace().getName().toLowerCase().replace("_", " ");
            state.printQuote(GRAND_FAERY_NAME, "Don't fret " + race + ". I'm not like my wild sisters.");
            state.leaderSay("Ah... I see, and you can talk. How interesting. Sorry if we chased away " +
                    "your sisters.");
            state.printQuote(GRAND_FAERY_NAME, "Yes. I've been watching you, and I'm grateful to you " +
                    "for disposing of those wild beasts in the garden.");
            state.leaderSay("You are welcome.");
            state.randomSayIfPersonality(PersonalityTrait.greedy, List.of(), "Is there some kind of reward?");
            state.printQuote(GRAND_FAERY_NAME, "I will grant you three boons. Present me with items and " +
                    "I shall improve them for you.");
            for (int i = 0; i < 3; ) {
                state.print("Who's item do you want to improve?");
                GameCharacter gc = model.getParty().partyMemberInput(model, state, model.getParty().getPartyMember(0));

                state.println("Which item do you want to improve?");

                Item it = SecretGardenEvent.this.selectItem(gc);
                if (it != null) {
                    partyMemberSay(gc, "Can you improve my " + it.getName().toLowerCase() + "?");
                    if (it.supportsHigherTier() && !(it instanceof HigherTierItem)) {
                        state.printQuote(GRAND_FAERY_NAME, "Certainly. Let me see that for a second.");
                        state.println("The " + GRAND_FAERY_NAME.toLowerCase() + " gently caresses the " + it.getName().toLowerCase() + " with her hand.");
                        model.getLog().waitForAnimationToFinish();
                        improve(gc, it);
                        state.printQuote(GRAND_FAERY_NAME, "There you go. Your item has now been imbued with my magic.");
                        i++;
                    } else {
                        state.printQuote(GRAND_FAERY_NAME, "I'm sorry, I can't improve that.");
                    }
                }

                if (i < 2) {
                    state.print("Do you want to improve more items? (Y/N) ");
                    if (!state.yesNoInput()) {
                        break;
                    }
                }
            }
            state.leaderSay("Thank you. We'll be on our way now.");
            printQuote(GRAND_FAERY_NAME, "Farewell " + race + ".");
        }

        private void improve(GameCharacter gc, Item it) {
            Equipment eq = gc.getEquipment();
            if (eq.getWeapon() == it) {
                eq.setWeapon(new HigherTierWeapon((Weapon) it, 1));
            } else if (eq.getClothing() == it) {
                eq.setClothing(new HigherTierClothing((Clothing) it, 1));
            } else if (eq.getAccessory() == it) {
                eq.setAccessory(new HigherTierAccessory((Accessory)it, 1));
            }
        }
    }
    private static final Sprite MAGIC_SPRING = new MagicSpringSprite();

    private class MagicSpring extends CenterDungeonObject {

        @Override
        protected Sprite getSprite(model.ruins.themes.DungeonTheme theme) {
            return MAGIC_SPRING;
        }

        @Override
        public void drawYourself(Model model, int xPos, int yPos, model.ruins.themes.DungeonTheme theme) {
            model.getScreenHandler().register(getSprite(theme).getName(), new Point(xPos, yPos), getSprite(theme));
        }

        @Override
        public String getDescription() {
            return "A fountain of water";
        }

        @Override
        public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
            exploreRuinsState.leaderSay("What, a spring?");
            print("Do you drink from the spring? (Y/N) ");
            if (yesNoInput()) {
                if (MyRandom.rollD6() == 1) {
                    println("The spring water poisons each party member!");
                    for (GameCharacter gc : model.getParty().getPartyMembers()) {
                        gc.addCondition(new PoisonCondition());
                    }
                    leaderSay("Ugh... what an aftertaste. Let's get out of here.");
                } else {
                    println("The spring water invigorates each party member!");
                    for (GameCharacter gc : model.getParty().getPartyMembers()) {
                        if (gc.hasCondition(BlessedCondition.class)) {
                            gc.removeCondition(BlessedCondition.class);
                        }
                        gc.addCondition(new BlessedCondition(model.getDay() + 10));
                        gc.addToSP(1000);
                        gc.addToHP(1000);
                    }
                    leaderSay("So refreshing. I feel fit for fight!");
                }
            }
        }
    }

    private static class MagicSpringSprite extends LoopingSprite {
        public MagicSpringSprite() {
            super("magicspring", "dungeon.png", 0xF4, 32, 32);
            setColor1(MyColors.DARK_GRAY);
            setColor2(MyColors.WHITE);
            setColor3(MyColors.GRAY);
            setColor4(MyColors.LIGHT_BLUE);
            setFrames(2);
        }
    }
}
