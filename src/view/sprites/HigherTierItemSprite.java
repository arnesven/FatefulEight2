package view.sprites;

import view.MyColors;

public class HigherTierItemSprite extends OverlayItemSprite {

    private static final MyColors[] TIER_COLORS = new MyColors[]{MyColors.LIGHT_BLUE, MyColors.GREEN, MyColors.PURPLE, MyColors.YELLOW};

    public HigherTierItemSprite(Sprite sprite, int tier) {
        super(13, 1, TIER_COLORS[tier-1], MyColors.PINK, MyColors.BEIGE, sprite);
    }
}
