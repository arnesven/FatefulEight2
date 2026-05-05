package model.characters.appearance;

public class GoateeBeard extends Beard {
    public GoateeBeard() {
        super(5, 0x41);
    }

    @Override
    public boolean meetsSideburns() {
        return false;
    }
}
