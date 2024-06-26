package model.ruins;

import model.Model;
import model.ruins.objects.DungeonObject;
import model.ruins.themes.DungeonTheme;
import model.states.ExploreRuinsState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class ExitDungeonIcon extends DungeonObject {

    private static final Sprite32x32 SPRITE = new Sprite32x32("exiticon", "dungeon.png", 0x40,
            MyColors.BLACK, MyColors.WHITE, MyColors.BLUE, MyColors.DARK_GRAY);

    public ExitDungeonIcon() {
        super(0, 0);
    }

    @Override
    protected Sprite getSprite(DungeonTheme theme) {
        return SPRITE;
    }

    @Override
    public String getDescription() {
        return "Backtrack and exit dungeon.";
    }

    @Override
    public void doAction(Model model, ExploreRuinsState state) {
        state.print("Are you sure you want to exit the " + state.getDungeonType().toLowerCase() + "? (Y/N) ");
        if (state.yesNoInput()) {
            state.setDungeonExited(true);
        }
    }
}
