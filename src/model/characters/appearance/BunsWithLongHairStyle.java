package model.characters.appearance;

public class BunsWithLongHairStyle extends BunsHairStyle {

    private final HairStyle femaleLongHairStyle;

    public BunsWithLongHairStyle(HairStyle inner, boolean longInBack, String description) {
        super(true, longInBack, inner.getBackHairOnly());
        this.femaleLongHairStyle = inner;
        setDescription(description);
    }

    @Override
    public void addHairInBack(AdvancedAppearance advancedAppearance) {
        super.addHairInBack(advancedAppearance);
        femaleLongHairStyle.addHairInBack(advancedAppearance);
    }

    @Override
    public void apply(AdvancedAppearance appearance) {
        super.apply(appearance);
        femaleLongHairStyle.apply(appearance);
    }
}
