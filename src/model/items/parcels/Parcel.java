package model.items.parcels;

import model.Model;
import model.characters.GameCharacter;
import model.items.Inventory;
import model.items.Item;
import model.items.MysteriousMap;
import model.items.UsableItem;
import model.states.events.FindTreasureMapEvent;
import util.MyRandom;

import java.util.List;

public abstract class Parcel extends UsableItem {
    private final double multiplier;

    public Parcel(String name, double multiplier) {
        super(name, 0);
        this.multiplier = multiplier;
    }

    @Override
    public boolean isCraftable() {
        return false;
    }

    @Override
    public boolean isSellable() {
        return false;
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.getParcels().add(this);
    }

    public double getDeliveryGoldMultiplier() {
        return multiplier;
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        model.getParty().addToNotoriety(getNotoriety());
        Item inner = getInnerItem();
        if (inner == null) {
            if (MyRandom.randInt(50) != 0) {
                return "The " + getName().toLowerCase() +
                        " contained nothing but some uninteresting pieces of parchment.";
            }
            MysteriousMap map = new MysteriousMap(model);
            inner = map;
            FindTreasureMapEvent.addDestinationTask(model, map);
        }
        inner.addYourself(model.getParty().getInventory());
        return "The " + getName().toLowerCase() + " contained " + inner.getName() + ".";
    }

    protected abstract int getNotoriety();

    protected abstract Item getInnerItem();

    @Override
    public boolean canBeUsedOn(Model model, GameCharacter target) {
        return model.getParty().getPartyMembers().contains(target);
    }

    @Override
    public String getUsageVerb() {
        return "Open";
    }


    public static Parcel makeRandomParcel() {
        return MyRandom.sample(List.of(new LetterParcel(), new PackageParcel(),
                new SackParcel(), new ChestParcel(), new BagParcel(),
                new BoxParcel()));
    }
}
