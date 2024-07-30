package model.headquarters;

import model.Model;
import model.characters.GameCharacter;
import model.horses.Horse;
import model.items.Item;
import model.map.UrbanLocation;
import util.MyLists;
import util.MyRandom;
import view.MyColors;
import view.sprites.SignSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Headquarters implements Serializable {

    public static final int SMALL_SIZE = 1;
    public static final int MEDIUM_SIZE = 2;
    public static final int LARGE_SIZE = 3;
    public static final int GRAND_SIZE = 4;
    public static final int MAJESTIC_SIZE = 5;
    protected static final Sprite SIGN_SPRITE = new SignSprite("innisgn", 0x37, MyColors.BLACK, MyColors.WHITE);

    private final String locationName;
    private final int cost;
    private HeadquarterAppearance appearance;
    private int food = 0;
    private int gold = 0;
    private int materials = 0;
    private int ingredients = 0;
    private final List<GameCharacter> characters = new ArrayList<>();
    private final List<Horse> horses = new ArrayList<>();
    private final List<Item> items = new ArrayList<>();
    private final int maxCharacters;
    private int maxHorses;

    public Headquarters(UrbanLocation location, int size) {
        this.locationName = location.getPlaceName();
        maxCharacters = size * 2;
        maxHorses = maxCharacters + 2;
        this.cost = 75 + 25 * size;

        appearance = HeadquarterAppearance.createAppearance(size);
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
        return maxCharacters;
    }

    public List<Horse> getHorses() {
        return horses;
    }

    public int getMaxHorses() {
        return maxHorses;
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
        return cost;
    }
}
