package model.characters.appearance;

import java.io.Serializable;

public class CharacterEyes implements Serializable {
    // DO NOT CHANGE THE ORDER OF allEyes, IT WILL AFFECT PRESET CHARACTERS
    public static CharacterEyes[] allEyes = new CharacterEyes[]{
            new NormalSmallEyes(),
            new NormalBigEyes(),
            new SmallEyesWithBangs(),
            new BaggyBigEyesWithBangs(),
            new SmallEyesWithSideburns(),
            new BaggySmallEyesWithSideburns(),
            new SmallEyesWithBangsRight(),
            new BaggySmallEyes(),
            new ElfinEyes(),
            new CharacterEyes(0x1A5, 0x1A6, "", 0, 0),
            new CharacterEyes(0x1A7, 0x1A8, "", 2, 3)};

    private final int eye;
    private final boolean symmetric;
    private final int rightEye;
    private final int lookIndex;
    private final int surprisedIndex;

    public CharacterEyes(int eye, String x, int lookIndex, int surprisedIndex) {
        this.eye = eye;
        this.symmetric = true;
        this.rightEye = 0;
        this.lookIndex = lookIndex;
        this.surprisedIndex = surprisedIndex;
    }

    public CharacterEyes(int left, int right, String x, int lookIndex, int surprisedIndex) {
        this.eye = left;
        this.rightEye = right;
        this.symmetric = false;
        this.lookIndex = lookIndex;
        this.surprisedIndex = surprisedIndex;
    }

    public boolean areSymmetrical() {
        return symmetric;
    }

    public int getLeftEye() {
        return 0x20 + eye;
    }

    public int getEye() {
        return 0x20 + eye;
    }

    public int getRightEye() {
        return 0x20 + rightEye;
    }

    public int getLookIndex() {
        return lookIndex;
    }

    public int getSurprisedIndex() {
        return surprisedIndex;
    }
}
