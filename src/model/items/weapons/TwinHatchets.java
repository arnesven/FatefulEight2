package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;

public class TwinHatchets extends WeaponPair {

    public TwinHatchets() {
        super(new Hatchet(), new Hatchet());
    }

    @Override
    public Item copy() {
        return new TwinHatchets();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.uncommon;
    }
}
