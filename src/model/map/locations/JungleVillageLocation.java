package model.map.locations;

import model.Model;
import model.SteppingMatrix;
import model.map.HexLocation;
import model.states.GameState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.JungleVillageDailyActionState;
import model.states.dailyaction.shops.GeneralShopNode;
import view.GameView;
import view.MyColors;
import view.help.HelpDialog;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;
import view.subviews.DailyActionSubView;
import view.subviews.JungleVillageSubView;

import java.awt.*;
import java.util.List;

public class JungleVillageLocation extends TownishLocation {
    private static final String DESCRIPTION = "A Village inhabited by the Jungle Tribe.";

    public JungleVillageLocation() {
        super("Jungle Village");
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("junglevillageupper", 0x18C, MyColors.BLACK, MyColors.BROWN, MyColors.GRAY, MyColors.DARK_GREEN);
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("junglegilalgelower", 0x19C, MyColors.BLACK, MyColors.BROWN, MyColors.GRAY, MyColors.DARK_GREEN);
    }

    @Override
    public HelpDialog getHelpDialog(GameView view) {
        return new JungleVilalgeHelpDialog(view);
    }

    @Override
    public String getPlaceName() {
        return "Jungle Village";
    }

    @Override
    public Point getTavernPosition() {
        return new Point(4, 4);
    }

    @Override
    public boolean noBoat() {
        return true;
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of();
    }

    @Override
    public GameState getDailyActionState(Model model) {
        return new JungleVillageDailyActionState(model, this);
    }

    @Override
    public DailyActionSubView makeActionSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
        return new JungleVillageSubView(advancedDailyActionState, matrix, true, getName());
    }

    @Override
    public int charterBoatEveryNDays() {
        return 0;
    }

    @Override
    public boolean bothBoatAndCarriage() {
        return false;
    }

    @Override
    public String getGeographicalDescription() {
        return DESCRIPTION;
    }

    private static class JungleVilalgeHelpDialog extends HelpDialog {
        public JungleVilalgeHelpDialog(GameView view) {
            super(view, "Jungle Village", DESCRIPTION);
        }
    }
}
