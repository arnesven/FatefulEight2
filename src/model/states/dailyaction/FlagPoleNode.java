package model.states.dailyaction;

import model.Model;
import model.Summon;
import model.map.HexLocation;
import model.map.UrbanLocation;
import model.states.GameState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;

public class FlagPoleNode extends DailyActionNode {
    private static final Sprite32x32 POLE_LOWER = new Sprite32x32("polelower", "world_foreground.png", 0x8A,
            MyColors.BLACK, MyColors.YELLOW, MyColors.PINK, MyColors.BEIGE);
    private static final Sprite32x32 POLE_UPPER = new Sprite32x32("poleupper", "world_foreground.png", 0x7A,
            MyColors.BLACK, MyColors.YELLOW, MyColors.PINK, MyColors.BEIGE);
    private static final Sprite32x32 SUCCESS_FLAG = new Sprite32x32("succflag", "world_foreground.png", 0x7B,
            MyColors.BLACK, MyColors.WHITE, MyColors.GREEN, MyColors.BEIGE);
    private static final Sprite32x32 FAIL_FLAG = new Sprite32x32("failflag", "world_foreground.png", 0x8B,
            MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BEIGE);


    public FlagPoleNode() {
        super("Flag Pole");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return null;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().register(POLE_LOWER.getName(), p, POLE_LOWER);
        Point p2 = new Point(p);
        p2.y -= 4;
        model.getScreenHandler().register(POLE_UPPER.getName(), p2, POLE_UPPER);
        if (model.getCurrentHex().getLocation() instanceof UrbanLocation) {
            if (showSuccessFlag(model, ((UrbanLocation) model.getCurrentHex().getLocation()))) {
                model.getScreenHandler().register(POLE_UPPER.getName(), p2, SUCCESS_FLAG);
            }
        }
    }

    public static boolean showSuccessFlag(Model model, UrbanLocation location) {
        if (model.getParty().getSummons().containsKey(location.getPlaceName())) {
            return model.getParty().getSummons().get(location.getPlaceName()).getStep() == Summon.COMPLETE;
        }
        return false;
    }

    @Override
    public Sprite getBackgroundSprite() {
        return null;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        if (showSuccessFlag(model, ((UrbanLocation)model.getCurrentHex().getLocation()))) {
            state.println("The flag is raised.");
        } else {
            state.println("Just a flagpole...");
        }
        return false;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {

    }
}
