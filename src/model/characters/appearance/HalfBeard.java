package model.characters.appearance;

public class HalfBeard extends Beard {
    public HalfBeard() {
        super(1, 0x40);
    }

    @Override
    public boolean meetsSideburns() {
        return false;
    }
}
