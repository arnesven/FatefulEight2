package model.items.weapons;

import model.items.Item;

public class TwinNunchucks extends WeaponPair {
    public TwinNunchucks() {
        super(new Nunchuck(), new Nunchuck());
    }

    @Override
    public Item copy() {
        return new TwinNunchucks();
    }
}
