package model.characters.appearance;

import java.io.Serializable;

public class CharacterEyes implements Serializable {
    // DO NOT CHANGE THE ORDER OF allEyes, IT WILL AFFECT PRESET CHARACTERS
    public static CharacterEyes[] allEyes = new CharacterEyes[]{
            new CharacterEyes(1), new CharacterEyes(0), new CharacterEyes(2, 3),
            new CharacterEyes(4, 5), new CharacterEyes(6, 7), new CharacterEyes(8, 9),
            new CharacterEyes(0xA, 0xB), new CharacterEyes(0xC, 0xD),
            new CharacterEyes(0xE, 0xF),
            new CharacterEyes(0x1A5, 0x1A6), new CharacterEyes(0x1A7, 0x1A8)};

    private final int eye;
    private final boolean symmetric;
    private final int rightEye;

    public CharacterEyes(int eye) {
        this.eye = eye;
        this.symmetric = true;
        this.rightEye = 0;
    }

    public CharacterEyes(int left, int right) {
        this.eye = left;
        this.rightEye = right;
        this.symmetric = false;
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
}
