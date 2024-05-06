package model.characters.appearance;

public class HeartHairStyle extends HairStyle {
    private final HairStyle innerHairStyle;
    private final int avatarSprite;
    private final int avatarBack;
    private final int avatarFullBack;
    private final int avatarHalfBack;

    public HeartHairStyle(HairStyle innerHairStyle, boolean hairInBack, boolean longInBack, String description) {
        super(false, true, hairInBack, longInBack, description);
        this.innerHairStyle = innerHairStyle;
        this.avatarSprite = longInBack ? 0x05 : 0x15;
        this.avatarBack = longInBack ? 0x02 : 0x12;
        this.avatarFullBack = innerHairStyle.getFullBackHair();
        this.avatarHalfBack = innerHairStyle.getHalfBackHair();
    }

    @Override
    public void apply(AdvancedAppearance appearance) {
        super.apply(appearance);
        innerHairStyle.apply(appearance);
    }

    @Override
    public void addHairInBack(AdvancedAppearance advancedAppearance) {
        super.addHairInBack(advancedAppearance);
        innerHairStyle.addHairInBack(advancedAppearance);
    }

    @Override
    public int getForeheadLeft() {
        return 0x1FA;
    }

    @Override
    public int getForeheadCenter() {
        return 0x1FB;
    }

    @Override
    public int getForeheadRight() {
        return 0x1FC;
    }

    @Override
    public int getHeadTopLeft() {
        return 0x09;
    }

    @Override
    public int getHeadTop() {
        return 0x0A;
    }

    @Override
    public int getHeadTopRight() {
        return 0x0B;
    }

    @Override
    public int getNormalHair() {
        return avatarSprite;
    }

    @Override
    public int getBackHairOnly() {
        return avatarBack;
    }

    @Override
    public int getFullBackHair() {
        return avatarFullBack;
    }

    @Override
    public int getHalfBackHair() {
        return avatarHalfBack;
    }
}
