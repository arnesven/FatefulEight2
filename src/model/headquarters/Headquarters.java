package model.headquarters;

import model.Model;
import model.Party;
import model.characters.GameCharacter;
import model.combat.conditions.VampirismCondition;
import model.horses.Horse;
import model.items.Item;
import model.items.books.BookItem;
import model.map.UrbanLocation;
import model.states.events.InvestInShopEvent;
import util.MyLists;
import util.MyRandom;
import view.MyColors;
import view.sprites.SignSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Headquarters implements Serializable {

    public static final int SMALL_SIZE = 1;
    public static final int MEDIUM_SIZE = 2;
    public static final int LARGE_SIZE = 3;
    public static final int GRAND_SIZE = 4;
    public static final int MAJESTIC_SIZE = 5;
    protected static final Sprite SIGN_SPRITE = new SignSprite("innisgn", 0x37, MyColors.BLACK, MyColors.WHITE);

    private final String locationName;
    private int size;
    private HeadquarterAppearance appearance;
    private int food = 0;
    private int gold = 0;
    private int materials = 0;
    private int ingredients = 0;
    private final List<GameCharacter> characters = new ArrayList<>();
    private final TownWorkingAssignees townWorkers = new TownWorkingAssignees();
    private final ShoppingAssignees shoppers;
    private final SubPartyAssignees subParty = new SubPartyAssignees();
    private final List<Horse> horses = new ArrayList<>();
    private final List<Item> items = new ArrayList<>();
    private final HeadquartersLogBook logBook = new HeadquartersLogBook();

    public Headquarters(UrbanLocation location, int size) {
        this.locationName = location.getPlaceName();
        this.size = size;

        appearance = HeadquarterAppearance.createAppearance(size);
        shoppers = new ShoppingAssignees(getMaxCharacters());
    }

    public static int calcCostFor(int size) {
        return 75 + 25 * size;
    }

    public void drawYourself(Model model, Point p) {
        appearance.drawYourself(model, p);
        Point p2 = new Point(p);
        p2.x += 2;
        p2.y += 2;
        model.getScreenHandler().register("objectforeground", p2, SIGN_SPRITE);
    }

    public String presentYourself() {
        return appearance.getDescription();
    }

    public String getLocationName() {
        return locationName;
    }

    public int getFood() {
        return food;
    }

    public int getGold() {
        return gold;
    }

    public int getMaterials() {
        return materials;
    }

    public int getIngredients() {
        return ingredients;
    }

    public void addToFood(int i) {
        this.food = Math.max(0, this.food + i);
    }

    public void addToGold(int i) {
        this.gold = Math.max(0, this.gold + i);
    }

    public void addToMaterials(int i) {
        this.materials = Math.max(0, this.materials + i);
    }

    public void addToIngredients(int i) {
        this.ingredients = Math.max(0, this.ingredients + i);
    }

    public List<GameCharacter> getCharacters() {
        return characters;
    }

    public int getMaxCharacters() {
        return size * 2 + 2;
    }

    public List<Horse> getHorses() {
        return horses;
    }

    public int getMaxHorses() {
        return getMaxCharacters() + 2;
    }

    public List<Item> getItems() {
        return items;
    }

    public static Headquarters makeRandomHeadquarters(UrbanLocation loc) {
        return MyRandom.sample(List.of(
                new Headquarters(loc, SMALL_SIZE),
                new Headquarters(loc, MEDIUM_SIZE),
                new Headquarters(loc, LARGE_SIZE),
                new Headquarters(loc, GRAND_SIZE),
                new Headquarters(loc, MAJESTIC_SIZE)));
    }

    public int getCost() {
        return calcCostFor(size);
    }

    public void endOfDayUpdate(Model model) {
        StringBuilder logEntry = new StringBuilder();
        if (logBook.isEmpty()) {
            logEntry.append("We established these headquarters here in ").append(locationName).append(".\n");
        }
        performAssignments(model, logEntry);
        consumeRations(model, logEntry);
        if (logEntry.length() > 0) {
            logBook.makeDayEntry(model.getDay(), logEntry.toString());
        }
        int investProfit = InvestInShopEvent.updateHeadquarters(model, locationName);
        if (investProfit > 0) {
            logEntry.append("A courier from a shop in town came by and deposited ").append(investProfit).append(" gold.\n");
            addToGold(investProfit);
        }
    }

    private void performAssignments(Model model, StringBuilder logEntry) {
        shoppers.performAssignments(model, this, logEntry);
        townWorkers.performAssignments(model, this, logEntry);
        subParty.performAssignments(model, this, logEntry);
    }

    private void consumeRations(Model model, StringBuilder logEntry) {
        List<GameCharacter> peopleAtHeadquarters = new ArrayList<>(characters);
        peopleAtHeadquarters.removeAll(subParty.getAwayCharacters());
        if (!peopleAtHeadquarters.isEmpty()) {
            if (food >= peopleAtHeadquarters.size()) {
                food -= peopleAtHeadquarters.size();
                logEntry.append(MyLists.commaAndJoin(peopleAtHeadquarters, GameCharacter::getName));
                logEntry.append(" ate rations (").append(peopleAtHeadquarters.size()).append(").\n");
                recover(peopleAtHeadquarters);
            } else if (food == 0) {
                logEntry.append("The rations have run out. ");
                logEntry.append(MyLists.commaAndJoin(peopleAtHeadquarters, GameCharacter::getName));
                logEntry.append(" starved.\n");
                checkIfLeaves(model, new ArrayList<>(peopleAtHeadquarters), logEntry);
            } else {
                logEntry.append("There were not enough rations for everybody to eat. ");
                List<GameCharacter> candidates = new ArrayList<>(peopleAtHeadquarters);
                Collections.shuffle(candidates);
                List<GameCharacter> eaters = candidates.subList(0, food);
                List<GameCharacter> starvers = new ArrayList<>(candidates);
                starvers.removeAll(eaters);
                logEntry.append(MyLists.commaAndJoin(eaters, GameCharacter::getName));
                logEntry.append(" ate rations (").append(food).append("). ");
                logEntry.append(MyLists.commaAndJoin(starvers, GameCharacter::getName));
                logEntry.append(" starved.\n");
                recover(eaters);
                checkIfLeaves(model, starvers, logEntry);
                food = 0;
            }
        }
    }

    private void recover(List<GameCharacter> candidates) {
        for (GameCharacter gc : candidates) {
            gc.addToHP(2);
            if (!gc.hasCondition(VampirismCondition.class)) {
                gc.addToSP(1);
            }
        }
    }

    private void checkIfLeaves(Model model, List<GameCharacter> candidates, StringBuilder logEntry) {
        List<GameCharacter> leavers = new ArrayList<>();
        for (GameCharacter gc : candidates) {
            if (MyRandom.randInt(3) == 0) {
                assignRnR(gc);
                characters.remove(gc);
                leavers.add(gc);
            }
        }
        if (leavers.size() > 0) {
            logEntry.append(MyLists.commaAndJoin(leavers, GameCharacter::getName));
            logEntry.append(" left permanently.\n");
        }
    }

    public BookItem getLogBook() {
        return logBook;
    }

    public int getSize() {
        return size;
    }

    public void incrementSize() {
        size = Math.min(size + 1, MAJESTIC_SIZE);
        appearance = HeadquarterAppearance.createAppearance(size);
    }

    public List<GameCharacter> getTownWorkers() {
        return townWorkers;
    }

    public void assignRnR(GameCharacter selected) {
        townWorkers.remove(selected);
        shoppers.remove(selected);
        subParty.remove(selected);
    }

    public void assignTownWork(GameCharacter selected) {
        assignRnR(selected);
        townWorkers.add(selected);
    }

    public void assignShopping(GameCharacter selected) {
        assignRnR(selected);
        shoppers.add(selected);
    }

    public void assignSubParty(GameCharacter selected) {
        assignRnR(selected);
        subParty.add(selected);
    }

    public List<GameCharacter> getShoppers() {
        return shoppers;
    }

    public List<GameCharacter> getSubParty() {
        return subParty;
    }

    public int getTripLength() {
        return subParty.getTripLength();
    }

    public static String getTripLengthString(int tripLength) {
        switch (tripLength) {
            case 1:
                return "Short";
            case 2:
                return "Medium";
            default:
                return "Long";
        }
    }

    public static int getTripLengthInDays(int tripLength) {
        return tripLength * 2 + 1;
    }

    public int getFoodLimit() {
        return shoppers.getFoodLimit();
    }

    public void setFoodLimit(int amount) {
        shoppers.setFoodLimit(amount);
    }

    public void setTripLength(int i) {
        subParty.setTripLength(i);
    }

    public int getSubPartyFoodRequired() {
        return getTripLengthInDays(subParty.getTripLength()) * subParty.size();
    }

    public void pickUpCharacter(GameCharacter selected, Party party) {
        assignRnR(selected);
        characters.remove(selected);
        party.add(selected, false);
    }

    public boolean isAway(GameCharacter selected) {
        return subParty.isAway(selected);
    }

    public int getSubPartyETA() {
        return subParty.getETA();
    }

    public void transferTo(Headquarters newHQ) {
        while (newHQ.getCharacters().size() <= newHQ.getMaxCharacters() && !characters.isEmpty()) {
            newHQ.getCharacters().add(characters.remove(0));
        }
        characters.clear();
        while (newHQ.getHorses().size() <= newHQ.getMaxHorses() && !horses.isEmpty()) {
            newHQ.getHorses().add(horses.remove(0));
        }
        horses.clear();
        newHQ.addToGold(gold);
        gold = 0;
        newHQ.addToMaterials(materials);
        materials = 0;
        newHQ.addToFood(food);
        food = 0;
        newHQ.addToIngredients(ingredients);
        ingredients = 0;
        newHQ.getItems().addAll(getItems());
        getItems().clear();
    }
}
