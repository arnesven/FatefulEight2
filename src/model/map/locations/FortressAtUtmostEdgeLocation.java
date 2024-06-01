package model.map.locations;

import model.Model;
import model.actions.DailyAction;
import model.map.CaveHex;
import model.map.HexLocation;
import model.states.DailyEventState;
import model.states.dailyaction.ExploreFortressAtUtmostEdgeDailyAction;
import model.states.events.FortressAtUtmostEdgeEvent;
import view.GameView;
import view.MyColors;
import view.help.FortressAtUtmostEdgeHelpDialog;
import view.help.HelpDialog;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;
import view.subviews.ImageSubView;
import view.subviews.SubView;

import java.util.ArrayList;
import java.util.List;

public class FortressAtUtmostEdgeLocation extends HexLocation {
    public static final String NAME = "Fortress at the Utmost Edge";
    private static final SubView IMAGE_SUB_VIEW = new ImageSubView("fatue", "FORTRESS AT THE UTMOST EDGE",
            "You have arrived at the Fortress at the Utmost Edge...");
    public static final String HAS_VISITED = "hasVisitedFatue";

    public FortressAtUtmostEdgeLocation() {
        super(NAME);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("utmostfortressupper", 0x184, MyColors.BLACK, MyColors.YELLOW, CaveHex.GROUND_COLOR);
    }
    @Override
    protected Sprite getLowerSprite() {
        return  HexLocationSprite.make("utmostfortresslower", 0x194, MyColors.BLACK, MyColors.YELLOW, CaveHex.GROUND_COLOR);
    }

    @Override
    public boolean isDecoration() {
        return false;
    }

    @Override
    public SubView getImageSubView() {
        return IMAGE_SUB_VIEW;
    }

    @Override
    public HelpDialog getHelpDialog(GameView view) {
        return new FortressAtUtmostEdgeHelpDialog(view);
    }

    @Override
    public DailyEventState generateEvent(Model model) {
        return new FortressAtUtmostEdgeEvent(model);
    }

    @Override
    public List<DailyAction> getDailyActions(Model model) {
        if (model.getSettings().getMiscFlags().get(HAS_VISITED) != null) {
            return List.of(new ExploreFortressAtUtmostEdgeDailyAction(model));
        }
        return new ArrayList<>();
    }

    @Override
    public boolean hasDailyActions() {
        return true;
    }
}
