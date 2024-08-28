package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;

public class TwinDaggers extends WeaponPair {

    public TwinDaggers() {
        super(new Dagger(), new Dagger());
    }

    @Override
    public Item copy() {
        return new TwinDaggers();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.uncommon;
    }
}
