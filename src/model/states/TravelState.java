package model.states;

import model.Model;
import model.items.Inventory;
import model.items.Item;
import model.map.Direction;
import model.map.UrbanLocation;
import model.states.events.RiverEvent;
import view.sprites.RidingSprite;
import view.sprites.Sprite;
import view.subviews.EmptySubView;
import view.subviews.MapSubView;
import view.subviews.CollapsingTransition;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class TravelState extends GameState {

    private Sprite spriteToUse;

    public TravelState(Model model) {
        super(model);
    }

    @Override
    public final GameState run(Model model) {
        MapSubView mapSubView = new MapSubView(model);
        CollapsingTransition.transition(model, mapSubView);
        model.getTutorial().travel(model);

        checkForOverEncumberance(model);

        boolean riding = checkForRiding(model);
        if (riding) {
            spriteToUse = new RidingSprite(model.getParty().getLeader(), model.getParty().getHorseHandler().get(0));
            model.getWorld().setAlternativeAvatar(spriteToUse);
        } else {
            spriteToUse = model.getParty().getLeader().getAvatarSprite();
        }

        GameState state = travelOneStep(model, mapSubView);
        if (state != null) {
            return state;
        }
        if (riding) {
            mapSubView = new MapSubView(model);
            CollapsingTransition.transition(model, mapSubView);
            state = travelOneStep(model, mapSubView);
            model.getWorld().setAlternativeAvatar(null);
            if (state != null) {
                return state;
            }
        }
        return nextState(model);
    }

    private void checkForOverEncumberance(Model model) {
        while (model.getParty().getInventory().getTotalWeight() >= model.getParty().getCarryingCapacity()) {
            println("Your party is currently carrying to much to be able to travel. You must abandon " +
                    "items, food or other resources before you can travel.");
            List<String> options = new ArrayList<>();
            int chosen;
            do {
                options.clear();
                if (model.getParty().getInventory().getFood() > 0) {
                    options.add("Food (" + Inventory.WEIGHT_OF_FOOD / 1000.0 + ")");
                }
                if (model.getParty().getInventory().getIngredients() > 0) {
                    options.add("Ingredients (" + Inventory.WEIGHT_OF_INGREDIENTS / 1000.0 + ")");
                }
                if (model.getParty().getInventory().getMaterials() > 0) {
                    options.add("Materials (" + Inventory.WEIGHT_OF_MATERIALS / 1000.0 + ")");
                }
                for (Item it : model.getParty().getInventory().getAllItems()) {
                    options.add(it.getName() + " (" + (it.getWeight() / 1000.0) + ")");
                }
                options.add("Cancel");
                chosen = multipleOptionArrowMenu(model, 24, 4, options);
                if (options.get(chosen).contains("Food")) {
                    println("You threw away 1 ration.");
                    model.getParty().addToFood(-1);
                } else if (options.get(chosen).contains("Ingredients")) {
                    println("You threw away 1 ingredient.");
                    model.getParty().getInventory().addToIngredients(-1);
                } else if (options.get(chosen).contains("Materials")) {
                    println("You threw away 1 material.");
                    model.getParty().getInventory().addToMaterials(-1);
                } else if (chosen == options.size()-1) {
                    break;
                } else {
                    Item itemToThrowAway = null;
                    for (Item it : model.getParty().getInventory().getAllItems()) {
                        if (options.get(chosen).contains(it.getName())) {
                            itemToThrowAway = it;
                        }
                    }
                    if (itemToThrowAway == null) {
                        throw new IllegalStateException("Could not find item to throw away.");
                    } else {
                        model.getParty().getInventory().remove(itemToThrowAway);
                        println("You threw away " + itemToThrowAway.getName() + ".");
                    }
                }
            } while (options.size() == 1);
        }
    }

    protected boolean checkForRiding(Model model) {
        if (!model.getParty().hasHorses()) {
            return false;
        }
        if (model.getParty().canRide()) {
            if (model.getSettings().alwaysRide()) {
                return true;
            }
            print("You have enough horses for your party to ride. Do you want to? (Y/N) ");
            return yesNoInput();
        } else {
            println("Your whole party cannot ride because at least one party member does not have a suitable mount.");
        }
        return false;
    }

    private GameState travelOneStep(Model model, MapSubView mapSubView) {
        Point selectedDir = selectDirection(model, mapSubView);
        Point newPosition = new Point(model.getParty().getPosition());
        model.getWorld().move(newPosition, selectedDir.x, selectedDir.y);

        if (checkRiverCrossing(model, mapSubView)) {
            println("The party comes to a river.");
            CollapsingTransition.transition(model, RiverEvent.subView);
            RiverEvent river = model.getCurrentHex().generateRiverEvent(model);
            GameState state = river.run(model);
            if (state instanceof GameOverState) {
                return state;
            } if (river.eventPreventsCrossing(model)) {
                setCurrentTerrainSubview(model);
                return state;
            }
            CollapsingTransition.transition(model, mapSubView);
        }

        mapSubView.drawAvatarEnabled(false);
        mapSubView.addMovementAnimation(spriteToUse,
                model.getWorld().translateToScreen(model.getParty().getPosition(), model.getParty().getPosition(), MapSubView.MAP_WIDTH_HEXES, MapSubView.MAP_HEIGHT_HEXES),
                model.getWorld().translateToScreen(newPosition, model.getParty().getPosition(), MapSubView.MAP_WIDTH_HEXES, MapSubView.MAP_HEIGHT_HEXES));
        mapSubView.waitForAnimation();
        CollapsingTransition.transition(model, new EmptySubView());

        moveToHex(model, selectedDir, mapSubView);
        return null;
    }

    private void moveToHex(Model model, Point selectedDir, MapSubView mapSubView) {
        model.getCurrentHex().travelFrom(model);
        model.getParty().move(model, selectedDir.x, selectedDir.y);
        if (model.getParty().getPreviousPosition().getLocation() instanceof UrbanLocation &&
                model.getCurrentHex().hasRoad()) {
            model.getParty().setOnRoad(true);
        } else if (partyNoLongerOnRoad(model, mapSubView)) {
            model.getParty().setOnRoad(false);
        }
        setCurrentTerrainSubview(model);
        model.getCurrentHex().travelTo(model);
    }

    protected boolean partyNoLongerOnRoad(Model model, MapSubView mapSubView) {
        return model.getParty().isOnRoad() &&
                !model.getWorld().travelingAlongRoad(model.getParty().getPosition(),
                        model.getParty().getPreviousPosition(),
                        Direction.getDirectionForDxDy(model.getParty().getPreviousPosition(),
                                mapSubView.getSelectedDirection()));
    }

    protected GameState nextState(Model model) {
        return model.getCurrentHex().generateEvent(model);
    }

    protected boolean checkRiverCrossing(Model model, MapSubView mapSubView) {
        return !model.isInCaveSystem() &&
                !model.getCurrentHex().getRoadInDirection(Direction.getDirectionForDxDy(model.getParty().getPosition(), mapSubView.getSelectedDirection())) &&
                model.getWorld().crossesRiver(model.getParty().getPosition(),
                    Direction.getDirectionForDxDy(model.getParty().getPosition(), mapSubView.getSelectedDirection()));
    }

    protected Point selectDirection(Model model, MapSubView mapSubView) {
        Point selectedDir;
        do {
            print("Please select an adjacent hex to travel to: ");
            waitForReturn();
            selectedDir = mapSubView.getSelectedDirection();
        } while (selectedDir.x == 0 && selectedDir.y == 0);
        return selectedDir;
    }
}
