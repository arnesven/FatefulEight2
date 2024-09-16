package model.items.weapons;

import model.items.Item;

public class TwinSais extends WeaponPair {
    public TwinSais() {
        super(new Sai(), new Sai());
    }

    @Override
    public Item copy() {
        return new TwinSais();
    }
}
