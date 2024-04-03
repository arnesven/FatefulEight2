package model.items;

import model.Model;
import model.characters.GameCharacter;
import model.states.AcceptDeliveryEvent;
import model.tasks.DestinationTask;
import model.tasks.TreasureHuntTask;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.awt.*;

public class MysteriousMap extends BookItem {
    private static final Sprite SPRITE = new ItemSprite(6, 13, MyColors.BEIGE, MyColors.DARK_BROWN);
    private final Point location;

    public MysteriousMap(Model model) {
        super("Mysterious Map", 0);
        this.location = AcceptDeliveryEvent.randomPositionWithoutLocation(model);
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
        return "A curious map. Does it lead to treasure?";
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
}
