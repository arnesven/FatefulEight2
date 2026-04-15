package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import view.MyColors;
import view.combat.CombatTheme;
import view.sprites.CampfireSprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PartyManagementEveningSubView extends DailyActionSubView {

    private final static Sprite32x32 TENT_LEFT = new PartyManagementTentSprite(false);
    private final static Sprite32x32 TENT_RIGHT = new PartyManagementTentSprite(true);

    public static final Point CAMP_FIRE_POS = new Point(4, 5);
    private static final List<Point> AROUND_CAMP_FIRE = List.of(
            new Point(CAMP_FIRE_POS.x - 1, CAMP_FIRE_POS.y - 1),
            new Point(CAMP_FIRE_POS.x + 1, CAMP_FIRE_POS.y - 1),
            new Point(CAMP_FIRE_POS.x - 1, CAMP_FIRE_POS.y),
            new Point(CAMP_FIRE_POS.x + 1, CAMP_FIRE_POS.y),
            new Point(CAMP_FIRE_POS.x - 1, CAMP_FIRE_POS.y + 1),
            new Point(CAMP_FIRE_POS.x, CAMP_FIRE_POS.y + 1),
            new Point(CAMP_FIRE_POS.x +1, CAMP_FIRE_POS.y + 1));
    private final ArrayList<Point> campfirePositions;

    public PartyManagementEveningSubView(AdvancedDailyActionState advancedDailyActionState,
                                         SteppingMatrix<DailyActionNode> matrix) {
        super(advancedDailyActionState, matrix, DailyActionSubView.DIRECT_MOVEMENT);
        this.campfirePositions = new ArrayList<>(AROUND_CAMP_FIRE);
        Collections.shuffle(campfirePositions);
    }

    @Override
    protected void drawBackground(Model model) {
        CombatTheme theme = model.getCurrentHex().getNightTimeCombatTheme();
        theme.drawBackground(model, X_OFFSET, Y_OFFSET);
        drawExtraTents(model);
        drawPartyArea(model, campfirePositions);
    }

    private void drawExtraTents(Model model) {
        if (model.getParty().getInventory().getTentSize() > 6) {
            Point p = convertToScreen(new Point(2, 2));
            model.getScreenHandler().register(TENT_LEFT.getName(), p, TENT_LEFT);
        }
        if (model.getParty().getInventory().getTentSize() > 4) {
            Point p = convertToScreen(new Point(5, 2));
            model.getScreenHandler().register(TENT_RIGHT.getName(), p, TENT_RIGHT);
        }

    }

    @Override
    protected String getPlaceType() {
        return "CAMP";
    }

    private static class PartyManagementTentSprite extends Sprite32x32 {
        public PartyManagementTentSprite(boolean flipped) {
            super("tent" + (flipped ? "left" : "right"), "world_foreground.png",
                    0xEA, MyColors.TRANSPARENT, MyColors.BLACK, MyColors.DARK_GRAY, MyColors.YELLOW);
            setFlipHorizontal(flipped);
        }
    }
}
