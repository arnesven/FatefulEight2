package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;

public class PointyStick extends WoodenSpear {
    @Override
    public String getName() {
        return "Pointy Stick";
    }

    @Override
    public Item copy() {
        return new PointyStick();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.veryRare;
    }
}
