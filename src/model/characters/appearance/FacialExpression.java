package model.characters.appearance;

public enum FacialExpression { angry, surprised, afraid, sad, questioning, relief, wicked, disappointed, excited, none;

    public static final int END_OF_CALLOUT = 1;
    public static final int PERMANENT = 2;

    public int getEyeSpriteIndex() {
        return switch (this) {
            case questioning -> 1;
            case relief, sad -> 2;
            case wicked, disappointed, excited -> 0;
            default -> ordinal();
        };
    }

    public boolean hasBigEyes() {
        return switch (this) {
            case angry, surprised, afraid, excited -> true;
            default -> false;
        };
    }

    public int getDefaultMouth() {
        switch (this) {
            case angry -> {
                return 0x01;
            }
            case surprised -> {
                return 0x02;
            }
            case afraid -> {
                return 0x0B;
            }
            case sad, disappointed -> {
                return 0x0A;
            }
            case questioning -> {
                return 0x08;
            }
            case wicked, relief, excited -> {
                return 0x0C;
            }
        }
        return 0x00;
    }


    public int getTuskMouth() {
        switch (this) {
            case angry -> {
                return 0x11;
            }
            case surprised -> {
                return 0x12;
            }
            case afraid -> {
                return 0x1B;
            }
            case sad, disappointed -> {
                return 0x1A;
            }
            case questioning -> {
                return 0x18;
            }
            case wicked, relief, excited -> {
                return 0x1C;
            }
        }
        return 0x10;
    }

    public int getVampireMouth() {
        switch (this) {
            case angry -> {
                return 0x41;
            }
            case surprised -> {
                return 0x42;
            }
            case afraid -> {
                return 0x0B;
            }
            case sad, disappointed -> {
                return 0x0A;
            }
            case questioning -> {
                return 0x48;
            }
            case wicked, relief, excited -> {
                return 0x0C;
            }
        }
        return 0x00;
    }
}
