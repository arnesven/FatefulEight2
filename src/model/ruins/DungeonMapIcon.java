package model.ruins;

import model.Model;
import model.ruins.objects.DungeonObject;
import model.ruins.themes.DungeonTheme;
import model.states.ExploreRuinsState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class DungeonMapIcon extends DungeonObject {
    private static final Sprite32x32 SPRITE = new Sprite32x32("mapiconn", "dungeon.png", 0x50,
            MyColors.BLACK, MyColors.WHITE, MyColors.BLUE, MyColors.DARK_GRAY);

    public DungeonMapIcon() {
        super(0, 0);
    }

    @Override
    protected Sprite getSprite(DungeonTheme theme) {
        return SPRITE;
    }

    @Override
    public String getDescription() {
        return "Look at map.";
    }

    @Override
    public void doAction(Model model, ExploreRuinsState state) {
        state.setMapView(true);
    }
}
