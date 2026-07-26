package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import model.states.GameState;
import model.states.events.FishingState;
import model.states.events.NoEventState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.Point;

public class MiningHoleNode extends DailyActionNode {
    private static final Sprite[] DAY_SPRITES = makeSprites(true);
    private static final Sprite[] NIGHT_SPRITES = makeSprites(false);

    private static Sprite[] makeSprites(boolean day) {
        Sprite[] result = new Sprite[14];
        MyColors[] colors = day ? new MyColors[]{MyColors.DARK_GRAY, MyColors.BROWN, MyColors.DARK_BROWN, MyColors.TAN} :
                new MyColors[]{MyColors.BLACK, MyColors.DARK_BROWN, MyColors.DARK_RED, MyColors.DARK_GRAY};
        for (int x = 0; x < 8; ++x) {
            result[x] = new Sprite32x32("mininghole"+x, "past_foreground.png", 0x20 + 0x10*(x / 4) + x % 4,
                    colors[0], colors[1], colors[2], colors[3]);
        }
        colors = day ? new MyColors[]{MyColors.BROWN, MyColors.BEIGE, MyColors.GOLD, MyColors.BLACK} :
                new MyColors[]{MyColors.DARK_BROWN, MyColors.BROWN, MyColors.GRAY_RED, MyColors.BLACK};
        for (int x = 0; x < 6; ++x) {
            result[8+x] = new Sprite32x32("miningcrane"+x, "past_foreground.png", 0x24 + 0x10*(x / 3) + x % 3,
                    colors[0], colors[1], colors[2], colors[3]);
        }
        return result;
    }

    @Override
    public Point getCursorShift() {
        return new Point(0, -4);
    }

    public MiningHoleNode() {
        super("Mining Hole");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new NoEventState(model);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return DAY_SPRITES[0];
    }

    @Override
    public void drawYourself(Model model, Point p) {
        Sprite[] spritesToUse = model.getTimeOfDay() == TimeOfDay.EVENING || model.getTimeOfDay() == TimeOfDay.NIGHT
                ? NIGHT_SPRITES : DAY_SPRITES;
        for (int x = 0; x < 8; ++x) {
            model.getScreenHandler().put(p.x + 4*(x % 4), p.y + 4*(x/4) - 4, spritesToUse[x]);
        }

        for (int x = 0; x < 6; ++x) {
            Point p2 = new Point(p.x + 10 + 4*(x % 3), p.y - 6 + 4*(x/3));
            model.getScreenHandler().register(spritesToUse[x+8].getName(), p2, spritesToUse[x+8]);
        }
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {

    }
}
