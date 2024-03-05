package model.characters.appearance;

public class BunsHairStyle extends HairStyle5x2 {
    public BunsHairStyle(boolean inBack, boolean longInBack, int avatarback) {
        super(0x10A, inBack, longInBack,
                0x48 + + (inBack?0x10:0) + (longInBack?0x10:0), avatarback, "Buns");
    }

    public BunsHairStyle() {
        this(false, false, 0);
    }
}
