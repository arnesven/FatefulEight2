package model.horses;

public class OldMaidHorse extends Prancer {

    @Override
    public String getName() {
        return "Old Maid";
    }

    @Override
    public int getCost() {
        return 28;
    }

    @Override
    public Horse copy() {
        return new OldMaidHorse();
    }
}
