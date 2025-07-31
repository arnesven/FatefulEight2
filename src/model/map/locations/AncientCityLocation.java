package model.map.locations;

import model.Model;
import model.SteppingMatrix;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.shops.GeneralShopNode;
import view.GameView;
import view.MyColors;
import view.help.HelpDialog;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;
import view.subviews.DailyActionSubView;

import java.awt.*;
import java.util.List;

public class AncientCityLocation extends TownishLocation {
    private final String cityName;

    public AncientCityLocation(String name) {
        super("City of " + name);
        this.cityName = name;
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("acityupper", 0x10C, MyColors.BLACK, MyColors.BEIGE, MyColors.GRAY_RED);
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("acityupper", 0x11C, MyColors.BLACK, MyColors.BEIGE, MyColors.GRAY_RED);
    }

    @Override
    public HelpDialog getHelpDialog(GameView view) {
        return null;
    }

    @Override
    public String getPlaceName() {
        return "the City of " + cityName;
    }

    @Override
    public Point getTavernPosition() {
        return new Point(3, 3);
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
    public DailyActionSubView makeActionSubView(Model model, AdvancedDailyActionState advancedDailyActionState,
                                                SteppingMatrix<DailyActionNode> matrix) {
        return null;
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
        return "";
    }
}
