package model.items.potions;

public class FineWine extends WinePotion {
    @Override
    public String getName() {
        return "Fine Wine";
    }

    @Override
    public int getCost() {
        return 4;
    }
}
