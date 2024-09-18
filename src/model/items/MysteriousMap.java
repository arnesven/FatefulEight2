package model.items;

import model.Model;
import model.characters.GameCharacter;
import model.map.Direction;
import model.map.WorldHex;
import model.states.AcceptDeliveryEvent;
import model.tasks.DestinationTask;
import model.tasks.TreasureHuntTask;
import util.MyRandom;
import view.GameView;
import view.InventoryView;
import view.MyColors;
import view.TreasureMapView;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.util.List;

public class MysteriousMap extends ReadableItem {
    private static final Sprite SPRITE = new ItemSprite(6, 13, MyColors.BEIGE, MyColors.DARK_BROWN);
    private final Point location;
    private final List<Point> path;

    public MysteriousMap(Model model) {
        super("Mysterious Map", MyRandom.randInt(20, 60));
        this.location = AcceptDeliveryEvent.randomPositionWithoutLocation(model);
        model.getWorld().dijkstrasByLand(location, false);
        this.path = model.getWorld().shortestPathToNearestTownOrCastle(1);
        while (path.size() < 10) {
            addInversion(model, path);
        }
    }

    @Override
    public boolean isCraftable() {
        return false;
    }

    @Override
    public boolean isSellable() {
        return false;
    }

    private void addInversion(Model model, List<Point> path) {
        int index = MyRandom.randInt(path.size()-1);
        Point first = path.get(index);
        Point second = path.get(index+1);

        List<Point> firstDirections = Direction.getDxDyDirections(first);
        List<Point> secondDirections = Direction.getDxDyDirections(second);

        for (int attempt = 0; attempt < 200; ++attempt) {
            Point temporary1 = new Point(first.x, first.y);
            Point temporary2 = new Point(second.x, second.y);
            Point dxDy1 = MyRandom.sample(firstDirections);
            model.getWorld().move(temporary1, dxDy1.x, dxDy1.y);
            Point dxDy2 = MyRandom.sample(secondDirections);
            model.getWorld().move(temporary2, dxDy2.x, dxDy2.y);
            if (temporary1.equals(temporary2) && !path.contains(temporary1)) {
                WorldHex hex = model.getWorld().getHex(temporary1);
                if (hex.canTravelTo(model)) {
                    System.out.println("Found inversion point at " + temporary1 + "!");
                    path.add(index + 1, temporary1);
                    return;
                }
            }
        }
        System.out.println("Failed while trying to find inversion.");
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.add(this);
    }

    @Override
    public String getShoppingDetails() {
        return ", A curious map. Does it lead to treasure?";
    }

    @Override
    public Item copy() {
        throw new IllegalStateException("Should not be copied!");
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {

        return gc.getFirstName() + " inspects the map...";
    }

    @Override
    public boolean canBeUsedOn(Model model, GameCharacter target) {
        return model.getParty().getPartyMembers().contains(target);
    }

    public DestinationTask getDestinationTask() {
        return new TreasureHuntTask(this);
    }

    public Point getLocation() {
        return location;
    }

    @Override
    public boolean removeAfterUse() {
        return false;
    }

    public List<Point> getPath() {
        return path;
    }

    @Override
    public boolean opensViewFromInventoryMenu() {
        return true;
    }

    @Override
    public GameView getViewFromInventoryMenu(Model model, InventoryView inventoryView, Item itemToEquip) {
        return new TreasureMapView(inventoryView, (MysteriousMap) itemToEquip, model);
    }
}
