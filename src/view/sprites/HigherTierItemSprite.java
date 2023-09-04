package view.sprites;

import view.MyColors;

public class HigherTierItemSprite extends OverlayItemSprite {

    private static final MyColors[] TIER_COLORS = new MyColors[]{MyColors.LIGHT_BLUE, MyColors.GREEN, MyColors.PURPLE, MyColors.YELLOW};

    public HigherTierItemSprite(Sprite sprite, int tier) {
        super(13, 1, getTierColor(tier), MyColors.PINK, MyColors.BEIGE, sprite);
    }

    private static MyColors getTierColor(int tier) {
        if (tier > 4) {
            tier = 4;
        }
        return TIER_COLORS[tier-1];
    }
}
