package model.items.spells;

import model.Model;
import model.achievements.Achievement;
import model.characters.GameCharacter;
import model.items.Item;
import model.journal.StoryPart;
import model.journal.ZeppelinStoryPart;
import model.map.*;
import model.states.GameState;
import model.states.TravelBySeaState;
import model.states.events.TravelByCharteredBoat;
import util.MyLists;
import util.MyRandom;
import view.sprites.ColorlessSpellSprite;
import view.sprites.Sprite;
import view.subviews.ArrowMenuSubView;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class SummonShipSpell extends ImmediateSpell {

    private static final Sprite SPRITE = new ColorlessSpellSprite(8, false);

    private static final int CHARTER_COST = 35;
    private boolean summonZep = false;

    public SummonShipSpell() {
        super("Summon Ship", 26, COLORLESS, 8, 4);
    }

    public static Achievement.Data getAchievementData() {
        return new Achievement.Data(SummonShipSpell.class.getCanonicalName(), "Summon Zeppelin",
                "You used the Summon Ship Spell to summon the Zeppelin");
    }

    @Override
    protected boolean preCast(Model model, GameState state, GameCharacter caster) {
        // FEATURE: Ask if you want to summon zeppelin
        if (canSummonZeppelin(model)) {
            state.print("Do you want to summon the Zeppelin? (Y/N) ");
            if (state.yesNoInput()) {
                summonZep = true;
                return true;
            }
        }
        if (!hexEmpty(model) || model.getCurrentHex().getRivers() == 0) {
            state.println(caster.getName() + " was attempting to cast Summon Ship, " +
                    "but the spell cannot be cast in the party's current location.");
            return false;
        }
        return true;
    }

    private boolean hexEmpty(Model model) {
        return model.getCurrentHex().getLocation() == null ||
                model.getCurrentHex().getLocation().isDecoration();
    }

    @Override
    protected void applyAuxiliaryEffect(Model model, GameState state, GameCharacter caster) {
        if (summonZep) {
            summonZeppelin(model, state, caster);
        } else {
            summonShip(model, state, caster);
        }
    }

    private void summonShip(Model model, GameState state, GameCharacter caster) {
        state.println("A little while after chanting the spell, a boat emerges on the water in front of the party. " +
                "The captain of the ship seems quite confused to suddenly be in this location.");
        state.printQuote("Captain", "I was there... and now I'm here... Where am I really?");
        model.getWorld().dijkstrasByLand(model.getParty().getPosition());
        List<Point> path = model.getWorld().shortestPathToNearestTownOrCastle();
        WorldHex hex = model.getWorld().getHex(path.getLast());
        UrbanLocation urb = (UrbanLocation) hex.getLocation();
        int cost = MyRandom.randInt(1, 3) * model.getParty().size();
        List<String> options = new ArrayList<>();
        state.println("You explain to the captain that you are not far from " + urb.getPlaceName() + ". The captain " +
                "offers to take your party there for " + cost + " gold.");
        if (model.getParty().getGold() < cost) {
            state.println("Unfortunately you cannot afford it.");
        } else {
            String place = urb instanceof TownLocation ? ((TownLocation) urb).getTownName() :
                    urb.getPlaceName();
            options.add("Sail to " + place);
        }
        state.println("The captain is also willing to let you charter the boat for " + CHARTER_COST + " gold.");
        if (model.getParty().getGold() < CHARTER_COST) {
            state.println("But you cannot afford it.");
        } else {
            options.add("Charter ship");
        }
        if (options.isEmpty()) {
            state.println("Annoyed, the captain sails off with the ship.");
            return;
        }
        String chosenOption;
        if (options.size() == 1) {
            state.println("Do you " + options.get(0) + "? (Y/N) ");
            if (state.yesNoInput()) {
                chosenOption = options.get(0);
            } else {
                state.println("Annoyed, the captain sails off with the ship.");
                return;
            }
        } else {
            options.add("Cancel");
            int[] selectedAction = new int[1];
            model.setSubView(new ArrowMenuSubView(model.getSubView(),
                    options, 24, 24, ArrowMenuSubView.NORTH_WEST) {
                @Override
                protected void enterPressed(Model model, int cursorPos) {
                    selectedAction[0] = cursorPos;
                    model.setSubView(getPrevious());
                }
            });
            state.waitForReturnSilently(true);
            chosenOption = options.get(selectedAction[0]);
        }

        if (chosenOption.contains("Sail to")) {
            TravelBySeaState.travelBySea(model, hex, state, TravelBySeaState.SHIP_AVATAR,
                    urb.getPlaceName(), false, true);
        } else if (chosenOption.contains("Charter")) {
            state.println("You pay the captain " + CHARTER_COST + " gold to charter the boat.");
            state.printQuote("Captain", "My ship is yours to command. Where do you want to go?");
            model.getParty().spendGold(CHARTER_COST);
            new TravelByCharteredBoat(model).run(model);
        } else {
            state.println("Annoyed, the captain sails off with the ship.");
        }
    }

    private void summonZeppelin(Model model, GameState state, GameCharacter caster) {
        state.println("A little while after chanting the spell, the zeppelin appears on the horizon. " +
                "It speeds towards the party as an impressive speed, then gracefully lands " +
                "on the open space in front of them.");
        state.leaderSay(MyRandom.sample(List.of("That's a very convenient spell.", "I love magic.", "All aboard!",
                        "Now if only that spell could propel the thing while we're on it.")));
        ZeppelinStoryPart part = (ZeppelinStoryPart)MyLists.find(model.getMainStory().getStoryParts(),
                p -> p instanceof ZeppelinStoryPart);
        part.setZeppelinPosition(model.getParty().getPosition());
        state.completeAchievement(SummonShipSpell.class.getCanonicalName());
    }

    @Override
    public String getDescription() {
        return "Summons a ship to the party's current location.";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new SummonShipSpell();
    }

    private boolean canSummonZeppelin(Model model) {
        if (model.getCurrentHex().getLocation() instanceof UrbanLocation ||
            model.getCurrentHex().getLocation() instanceof InnLocation ||
            model.getCurrentHex().getLocation() instanceof TempleLocation) {
            return false;
        }
        StoryPart part = MyLists.find(model.getMainStory().getStoryParts(),
                p -> p instanceof ZeppelinStoryPart);
        if (part == null) {
            return false;
        }
        ZeppelinStoryPart zPart = (ZeppelinStoryPart) part;
        if (!zPart.isZeppelinBought()) {
            return false;
        }

        return model.isInOriginalWorld() &&
                !model.partyIsInOverworldPosition(zPart.getZeppelinPosition());
    }
}
