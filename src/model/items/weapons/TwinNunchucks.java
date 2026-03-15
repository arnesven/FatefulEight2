package model.items.weapons;

import model.items.Item;

public class TwinNunchucks extends WeaponPair {
    public TwinNunchucks() {
        super(new Nunchaku(), new Nunchaku());
    }

    @Override
    public Item copy() {
        return new TwinNunchucks();
    }
}
