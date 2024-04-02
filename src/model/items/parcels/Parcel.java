package model.items.parcels;

import model.items.Inventory;
import model.items.Item;
import util.MyRandom;

import java.util.List;

public abstract class Parcel extends Item {
    private final double multiplier;

    public Parcel(String name, double multiplier) {
        super(name, 0);
        this.multiplier = multiplier;
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.getParcels().add(this);
    }

    public static Parcel makeRandomParcel() {
        return MyRandom.sample(List.of(new LetterParcel(), new PackageParcel(), new SackParcel())); // TODO: Add chest, bag, sack
    }

    public double getDeliveryGoldMultiplier() {
        return multiplier;
    }
}
