package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.items.ItemDeck;
import model.items.spells.Spell;
import model.items.books.BookItem;
import model.map.UrbanLocation;
import model.ruins.*;
import model.ruins.objects.*;
import model.ruins.themes.DungeonTheme;
import model.states.DailyEventState;
import model.states.ExploreRuinsState;
import util.MyLists;
import util.MyRandom;
import util.MyStrings;
import view.MyColors;
import view.sprites.HermitSprite;
import view.sprites.Sprite;
import view.subviews.ArrowMenuSubView;
import view.combat.CombatTheme;
import view.subviews.DungeonDrawer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TallSpireEvent extends DailyEventState {
    public TallSpireEvent(Model model) {
        super(model);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Go to tall spire",
                "There is " + getDistantDescription() + " nearby. It is said a recluse live at the top");
    }

    @Override
    public String getDistantDescription() {
        return "a tall spire";
    }

    @Override
    protected void doEvent(Model model) {
        print("You come upon a very high spire. Do you want to enter? (Y/N) ");
        if (yesNoInput()) {
            RuinsDungeon dungeon = new RuinsDungeon(DungeonMaker.makeTallSpireDungeon(model));
            DungeonRoom entryRoom = new TallSpireEntryRoom();
            FinalDungeonLevel finalLevel = (FinalDungeonLevel) dungeon.getLevel(dungeon.getNumberOfLevels()-1);
            finalLevel.setFinalRoom(entryRoom);

            DungeonRoom finalRoom = new RiddleRoom();
            FinalDungeonLevel firstLevel = (FinalDungeonLevel)dungeon.getLevel(0);
            firstLevel.setFinalRoom(finalRoom, false);

            ExploreRuinsState explore = new ExploreTallSpireState(model, dungeon);
            explore.run(model);
        }

        model.getParty().unbenchAll();
    }

    private static class ExploreTallSpireState extends ExploreRuinsState {
        public ExploreTallSpireState(Model model, RuinsDungeon dungeon) {
            super(model, dungeon, "Tall Spire");
            super.setCurrentLevelAndPosition(dungeon.getNumberOfLevels()-1,
                    dungeon.getLevel(dungeon.getNumberOfLevels()-1).getStartingPoint());
        }

        @Override
        public CombatTheme getCombatTheme() {
            return new view.combat.DungeonTheme();
        }

        public String getCurrentRoomInfo() {
            int floor = getDungeon().getNumberOfLevels() - getCurrentLevel() - 1;
            if (floor == 0) {
                return "Ground Floor";
            }
            if (getCurrentLevel() == 0) {
                return "";
            }
            return "Floor " + floor + ", Room " + (getPartyPosition().x+1) + "-" + (getPartyPosition().y+1);
        }
    }

    private static class TallSpireEntryRoom extends DungeonRoom {
        public TallSpireEntryRoom() {
            super(5, 5);
            addObject(new TallSpireExit());
            addDecoration(new LargeWindow(4, 0, true));
            for (int x = 0; x < 8; ++x) {
                addDecoration(new GrassBackground(x, 7, true));
            }
        }

        @Override
        public void drawYourself(DungeonDrawer drawer, Point position, boolean connectLeft, boolean connectRight, boolean leftCorner, boolean rightCorner, boolean corridorLeft, boolean corridorRight, DungeonTheme theme) {
            super.drawYourself(drawer, position, connectLeft, connectRight, leftCorner, rightCorner,
                    corridorLeft, corridorRight, theme);
            int xStart = position.x;
            int yStart = position.y;
            drawBottomRow(drawer, xStart, yStart, getWidth(), getHeight(), connectLeft, connectRight, theme);
        }

        @Override
        public Point getRelativeAvatarPosition() {
            return new Point(4, 4);
        }
    }


    private static class TallSpireExit extends DungeonExit {
        private final OpenDoor inner;

        public TallSpireExit() {
            setInternalPosition(new Point(5, 6));
            this.inner = new OpenDoor(new Point(0, 0), true, "unused");
        }

        @Override
        public void drawYourself(DungeonDrawer drawer, int xPos, int yPos, DungeonTheme theme) {
            inner.drawYourself(drawer, xPos, yPos, theme);
        }
    }

    private static class RiddleRoom extends DungeonRoom {
        public RiddleRoom() {
            super(5, 5);
            addObject(new RiddlerObject());
            addDecoration(new LargeWindow(3, 0));
            addDecoration(new LargeWindow(4, 0));
            addDecoration(new LargeWindow(5, 0));

            for (int y = 0; y < 7; ++y) {
                addDecoration(new SkyBackground(0, y, true));
                addDecoration(new SkyBackground(6, y, false));
            }
            for (int x = 0; x < 8; ++x) {
                if (x == 1) {
                    addDecoration(new StairsRondel(x, -1, true));
                } else if (x == 2) {
                    addDecoration(new StairsRondel(x, -1, false));
                } else {
                    addDecoration(new SkyBackground(x, -1, true));
                }
                if (0 < x && x < 7) {
                    addDecoration(new FullWallDecoration(x, 7));
                } else {
                    addDecoration(new GrassBackground(x, 7, true));
                }
            }
        }

        @Override
        public Point getRelativeAvatarPosition() {
            return new Point(2, 2);
        }
    }


    private static class RiddlerObject extends CenterDungeonObject {

        private Sprite SPRITE = new HermitSprite();

        public RiddlerObject() {
            setInternalPosition(new Point(3, 5));
        }

        @Override
        public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
            exploreRuinsState.printQuote("Recluse", "Ah, at last, someone has managed to climb this tall tower and find me.");
            exploreRuinsState.leaderSay("Who are you?");
            exploreRuinsState.printQuote("Recluse", "I am a recluse. A hermit who has taken residence in this tower, " +
                    "all alone, with only my pigeons to keep me company. I await " +
                    "the one who is most brave, strong and above all, wise.");
            exploreRuinsState.randomSayIfPersonality(PersonalityTrait.jovial, new ArrayList<>(),
                    "Then you won't be interested in meeting us!");
            exploreRuinsState.leaderSay("Well, now we're here. What now?");
            exploreRuinsState.printQuote("Recluse", "Now you must be tested.");
            exploreRuinsState.leaderSay("Tested? In what way?");
            exploreRuinsState.printQuote("Recluse", "With a question of course. If you can answer it correctly " +
                    "I will grant you the thing you seek most of all.");
            exploreRuinsState.leaderSay("Fine.");
            exploreRuinsState.printQuote("Recluse", "But I must warn you. Should you answer incorrectly, " +
                    "you will be punished severely. You will be cast down to the bottom of the tower!");
            exploreRuinsState.leaderSay("That seems rather harsh. Let me think about this.");
            exploreRuinsState.print("Do you accept the hermit's challenge? (Y/N) ");
            if (exploreRuinsState.yesNoInput()) {
                exploreRuinsState.leaderSay("Alright, I will answer your question.");
                if (generateRiddle(model, exploreRuinsState)) {
                    exploreRuinsState.printQuote("Recluse", "Yes, you are correct!");
                    exploreRuinsState.printQuote("Recluse", "What a joyous day this is. I shall send my pigeons far " +
                            "and wide to tell of your deed this day!");
                    exploreRuinsState.println("The party gains 1 reputation!");
                    exploreRuinsState.leaderSay("That's it? That was kind of easy.");
                    exploreRuinsState.printQuote("Recluse", "All questions are easy when one knows the answer.");
                    exploreRuinsState.leaderSay("That's true I guess. I think we'll be going now. Thanks for everything!");
                    exploreRuinsState.printQuote("Recluse", "Farewell.");
                    model.getParty().addToReputation(1);
                } else {
                    exploreRuinsState.printQuote("Recluse", "Alas. You are not correct. Now you must be punished!");
                    exploreRuinsState.println("A mysterious force violently grips hold of the party and pushes it out from an open " +
                            "tower window. Fortunately the branches of some nearby trees brake what would have otherwise surely been " +
                            "a lethal fall.");
                    for (GameCharacter gc : model.getParty().getPartyMembers()) {
                        if (!model.getParty().getBench().contains(gc)) {
                            gc.addToHP(2 - gc.getHP());
                            gc.addToSP( -gc.getSP());
                            exploreRuinsState.partyMemberSay(gc, MyRandom.sample(List.of("Ouch", "Ugh", "Ow!", "That hurt!", "Darn it!")));
                        }
                    }
                }
            }

            exploreRuinsState.setDungeonExited(true);
            exploreRuinsState.getDungeon().setCompleted(true);
        }

        private boolean generateRiddle(Model model, ExploreRuinsState exploreRuinsState) {
            int dieRol = MyRandom.rollD6();
            if (dieRol < 3) {
                return generateLordQuestion(model, exploreRuinsState);
            }
            if (dieRol < 5) {
                return generateSpellQuestion(model, exploreRuinsState);
            }
            // FEATURE: Add potion question
            return generateBookQuestion(model, exploreRuinsState);
        }

        private boolean generateBookQuestion(Model model, ExploreRuinsState exploreRuinsState) {
            List<BookItem> books = new ArrayList<>(ItemDeck.allBooks());
            books.removeIf((BookItem b) -> b.getAuthor().length() > 30);
            int target = MyRandom.randInt(books.size());
            exploreRuinsState.printQuote("Recluse", "Who wrote '" + books.get(target).getTitle() + "'?");
            List<String> optionList = MyLists.transform(books, (BookItem::getAuthor));
            return target == askQuestion(model, exploreRuinsState, optionList);
        }

        private boolean generateSpellQuestion(Model model, ExploreRuinsState exploreRuinsState) {
            Spell targetSpell = MyRandom.sample(ItemDeck.allSpells());
            List<MyColors> spellColors = new ArrayList<>(List.of(MyColors.WHITE, MyColors.BLUE, MyColors.GREEN,
                    MyColors.RED, MyColors.BLACK, Spell.COLORLESS));
            int target = spellColors.indexOf(targetSpell.getColor());
            exploreRuinsState.printQuote("Recluse", "What color is the spell " + targetSpell.getName() + "?");
            List<String> optionList = MyLists.transform(spellColors, (MyColors c) -> MyStrings.capitalize(c.toString()));
            return target == askQuestion(model, exploreRuinsState, optionList);
        }

        private boolean generateLordQuestion(Model model, ExploreRuinsState exploreRuinsState) {
            List<UrbanLocation> lordLocations = new ArrayList<>(model.getWorld().getLordLocations());
            Collections.shuffle(lordLocations);
            lordLocations = lordLocations.subList(0, 4);
            int target = MyRandom.randInt(4);
            exploreRuinsState.printQuote("Recluse", "Who is the lord in " + lordLocations.get(target).getPlaceName() + "?");
            List<String> optionList = MyLists.transform(lordLocations, UrbanLocation::getLordName);
            return target == askQuestion(model, exploreRuinsState, optionList);
        }

        private int askQuestion(Model model, ExploreRuinsState exploreRuinsState, List<String> optionList) {
            int[] selectedAction = new int[1];
            model.setSubView(new ArrowMenuSubView(model.getSubView(),
                    optionList, 24, 24, ArrowMenuSubView.NORTH_WEST) {
                @Override
                protected void enterPressed(Model model, int cursorPos) {
                    selectedAction[0] = cursorPos;
                    model.setSubView(getPrevious());
                }
            });
            exploreRuinsState.waitForReturnSilently();
            exploreRuinsState.leaderSay(
                    MyRandom.sample(List.of("Uhm, I think it's ", "It must be ",
                            "I am sure it is ", "I'm positive it is ")) +
                            optionList.get(selectedAction[0]) + ".");
            return selectedAction[0];
        }

        @Override
        protected Sprite getSprite(DungeonTheme theme) {
            return SPRITE;
        }

        @Override
        public void drawYourself(DungeonDrawer drawer, int xPos, int yPos, DungeonTheme theme) {
            drawer.register(SPRITE.getName(), new Point(xPos, yPos), SPRITE);
        }

        @Override
        public String getDescription() {
            return "An old hermit";
        }
    }
}
