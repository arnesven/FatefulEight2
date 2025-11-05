package model.states;

import model.Model;
import model.items.Inventory;
import model.items.Item;
import model.items.special.MagicBroom;
import model.map.Direction;
import model.map.UrbanLocation;
import model.states.events.RiverEvent;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyLists;
import view.sprites.FlyingWitchSprite;
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

        boolean flying = checkForFlying(model);
        if (!checkForOverEncumberance(model)) {
            println("Travel canceled.");
            return model.getCurrentHex().getDailyActionState(model);
        }

        boolean riding = false;
        if (!flying) {
            riding = checkForRiding(model);
        }
        if (flying) {
            ClientSoundManager.playBackgroundMusic(BackgroundMusic.happyMandolin);
            spriteToUse = new FlyingWitchSprite(model.getParty().getLeader().getRace().getColor());
            model.getWorld().setAlternativeAvatar(spriteToUse);
        } else if (riding) {
            ClientSoundManager.playBackgroundMusic(BackgroundMusic.ridingSong);
            spriteToUse = new RidingSprite(model.getParty().getLeader(), model.getParty().getHorseHandler().get(0));
            model.getWorld().setAlternativeAvatar(spriteToUse);
        } else {
            ClientSoundManager.playBackgroundMusic(BackgroundMusic.mainSong);
            spriteToUse = model.getParty().getLeader().getAvatarSprite();
        }

        GameState state = travelOneStep(model, mapSubView, true, flying);
        if (state != null) {
            return state;
        }
        if (riding || flying) {
            mapSubView = new MapSubView(model);
            CollapsingTransition.transition(model, mapSubView);
            state = travelOneStep(model, mapSubView, false, flying);
            model.getWorld().setAlternativeAvatar(null);
            if (state != null) {
                return state;
            }
        }
        return nextState(model);
    }

    protected boolean checkForOverEncumberance(Model model) {
        while (model.getParty().getEncumbrance() > model.getParty().getCarryingCapacity()) {
            model.getTutorial().carryingCapacity(model);
            println("Your party is currently carrying to much to be able to travel. You must abandon " +
                    "items, food or other resources before you can travel.");
            if (showThrowAwayMenu(model)) {
                break;
            }
        }
        return model.getParty().getEncumbrance() <= model.getParty().getCarryingCapacity();
    }

    private boolean showThrowAwayMenu(Model model) {
        List<String> options = new ArrayList<>();
        int chosen;
        do {
            options.clear();
            int food = model.getParty().getInventory().getFood();
            if (food > 0) {
                options.add("Food (" + (Inventory.WEIGHT_OF_FOOD * food) / 1000.0 + ")");
            }
            int ingredients = model.getParty().getInventory().getIngredients();
            if (ingredients > 0) {
                options.add("Ingredients (" + (Inventory.WEIGHT_OF_INGREDIENTS * ingredients) / 1000.0 + ")");
            }
            int materials = model.getParty().getInventory().getMaterials();
            if (materials > 0) {
                options.add("Materials (" + (Inventory.WEIGHT_OF_MATERIALS * materials) / 1000.0 + ")");
            }
            int lockpicks = model.getParty().getInventory().getLockpicks();
            if (lockpicks > 0) {
                options.add("Lockpicks (" + (Inventory.WEIGHT_OF_LOCKPICKS*lockpicks) / 1000.0 + ")");
            }
            for (Item it : model.getParty().getInventory().getAllItems()) {
                options.add(it.getName() + " (" + (it.getWeight() / 1000.0) + ")");
            }
            options.add("Cancel");
            chosen = multipleOptionArrowMenu(model, 24, 4, options);
            if (options.get(chosen).contains("Food")) {
                int x = howManyToThrowAway(model, model.getParty().getFood());
                println("You threw away " + x + " ration" + (x == 1 ? "":"s") + ".");
                model.getParty().addToFood(-x);
            } else if (options.get(chosen).contains("Ingredients")) {
                int x = howManyToThrowAway(model, model.getParty().getInventory().getIngredients());
                println("You threw away " + x + " ingredient" + (x == 1 ? "":"s") + ".");
                model.getParty().getInventory().addToIngredients(-x);
            } else if (options.get(chosen).contains("Materials")) {
                int x = howManyToThrowAway(model, model.getParty().getInventory().getMaterials());
                println("You threw away " + x + " ingredient" + (x == 1 ? "" : "s") + ".");
                model.getParty().getInventory().addToMaterials(-x);
            } else if (options.get(chosen).contains("Lockpicks")) {
                int x = howManyToThrowAway(model, model.getParty().getInventory().getLockpicks());
                println("You threw away " + x + " lockpick" + (x == 1 ? "" : "s") + ".");
                model.getParty().getInventory().addToLockpicks(-x);
            } else if (chosen == options.size()-1) {
                return true;
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
                    model.getParty().removeFromInventory(itemToThrowAway);
                    println("You threw away " + itemToThrowAway.getName() + ".");
                }
            }
        } while (options.size() != 1);
        return false;
    }

    private int howManyToThrowAway(Model model, int food) {
        int x = 0;
        do {
            print("How many do you want to throw away? ");
            try {
                String line = lineInput();
                x = Integer.parseInt(line);
                if (x >= 0 && x <= food) {
                    break;
                } else {
                    println("Enter an integer between 0 and " + food + ".");
                }
            } catch (NumberFormatException nfe) {
                println("Enter an integer between 0 and " + food + ".");
            }
        } while (true);
        return x;
    }

    protected boolean checkForFlying(Model model) {
        List<Item> brooms = MyLists.transform(model.getParty().getPartyMembers(), pm -> pm.getEquipment().getWeapon());
        brooms.addAll(model.getParty().getInventory().getAllItems());
        brooms = MyLists.filter(brooms, it -> it instanceof MagicBroom);
        String broomStr = brooms.size() == 1 ? "a magic broom" : "magic brooms";
        if (!brooms.isEmpty()) {
            if (brooms.size() < model.getParty().size()) {
                println("You have " + broomStr + " but your whole party cannot fly.");
                return false;
            }
            String extra = "";
            if (model.getParty().hasHorses()) {
                extra = "(You will have to leave your horses behind)";
            }
            print("You have " + broomStr + ", do you wish to attempt to fly? " + extra + "(Y/N) ");
            boolean choice = yesNoInput();
            if (choice) {
                model.getParty().getHorseHandler().abandonHorses(model);
            }
            return choice;
        }
        return false;
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

    private GameState travelOneStep(Model model, MapSubView mapSubView, boolean cancelEnabled, boolean flying) {
        Point selectedDir = selectDirection(model, mapSubView);
        if (selectedDir.x == 0 && selectedDir.y == 0) {
            if (cancelEnabled) {
                println("Travel canceled.");
                return model.getCurrentHex().getDailyActionState(model);
            } else {
                setCurrentTerrainSubview(model);
                return null;
            }
        }
        Point newPosition = new Point(model.getParty().getPosition());
        model.getWorld().move(newPosition, selectedDir.x, selectedDir.y);

        if (!flying && checkRiverCrossing(model, mapSubView)) {
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
        mapSubView.removeMovementAnimation();
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
        print("Please select an adjacent hex to travel to: ");
        waitForReturn();
        selectedDir = mapSubView.getSelectedDirection();
        return selectedDir;
    }
}
