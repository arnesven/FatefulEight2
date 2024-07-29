package model.headquarters;

import model.Model;
import model.Party;
import model.characters.GameCharacter;
import model.horses.Horse;
import model.items.Item;
import model.map.UrbanLocation;
import view.MyColors;
import view.sprites.SignSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Headquarters implements Serializable {

    protected static final Sprite SIGN_SPRITE = new SignSprite("innisgn", 0x37, MyColors.BLACK, MyColors.WHITE);

    private final String locationName;
    private final int cost;
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
        this.cost = 75 + 50 * size;
    }

    public void drawYourself(Model model, Point p) {
        specificDrawYourself(model, p);
        Point p2 = new Point(p);
        p2.x += 2;
        p2.y += 2;
        model.getScreenHandler().register("objectforeground", p2, SIGN_SPRITE);
    }

    protected abstract void specificDrawYourself(Model model, Point p);

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
        return new MediumHeadquarters(loc);
    }

    public int getCost() {
        return cost;
    }

    public String presentYourself() {
        return "It's a lovely, medium sized house.";
    }
}
