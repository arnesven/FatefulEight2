package model.map.locations;

import model.Model;
import model.SteppingMatrix;
import model.journal.PartSevenStoryPart;
import model.states.GameState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.AncientCityDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.shops.*;
import view.GameView;
import view.MyColors;
import view.help.HelpDialog;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;
import view.subviews.AncientCitySubView;
import view.subviews.DailyActionSubView;
import view.subviews.ImageSubView;
import view.subviews.SubView;

import java.awt.*;
import java.util.List;

public class AncientCityLocation extends TownishLocation {
    private static final String DESCRIPTION = "An ancient city with tall stone buildings.";
    private final String cityName;
    private final ImageSubView subView;

    public AncientCityLocation(String name) {
        super("City of " + name);
        this.cityName = name;
        subView = new ImageSubView("ancient_city", "ANCIENT CITY",
                "You are in an Ancient City.", true);
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
    public SubView getImageSubView(Model model) {
        return subView;
    }

    @Override
    public GameState getDailyActionState(Model model) {
        boolean isCapital = this == PartSevenStoryPart.getCapitalCity(model);
        return new AncientCityDailyActionState(model, this, isCapital);
    }

    @Override
    public GameState getEveningState(Model model, boolean freeLodge, boolean freeRations) {
        boolean isCapital = this == PartSevenStoryPart.getCapitalCity(model);
        return new AncientCityDailyActionState(model, this, isCapital);
    }

    @Override
    public HelpDialog getHelpDialog(GameView view) {
        return new AncientCityHelpDialog(view);
    }

    @Override
    public String getPlaceName() {
        return "the City of " + cityName;
    }

    @Override
    public Point getTavernPosition() {
        return new Point(2, 4);
    }

    @Override
    public boolean noBoat() {
        return true;
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        if (this == PartSevenStoryPart.getCapitalCity(model)) {
            return List.of(new AncientCityWeaponShop(model, 5, 5), // Weapon Shop
                    new AncientCityArmorShop(model, 4, 5), // Armor Shop
                    new AncientCityShop(model, 5, 3), // General Shop
                    new AncientCityMagicShop(model, 1, 2)); // Magic Shop
        }

        if (this == PartSevenStoryPart.getCityA(model)) {
            return List.of(new AncientCityWeaponShop(model, 2, 5),
                    new AncientCityMagicShop(model, 6, 2),
                    new AncientCityShop(model, 5, 2));
        }
        // City B.
        return List.of(new AncientCityShop(model, 6, 4),
                new AncientCityMagicShop(model, 3, 6),
                new AncientCityArmorShop(model, 4, 6));
    }

    @Override
    public DailyActionSubView makeActionSubView(Model model, AdvancedDailyActionState advancedDailyActionState,
                                                SteppingMatrix<DailyActionNode> matrix) {
        return new AncientCitySubView(advancedDailyActionState, matrix, getName());
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

    private static class AncientCityHelpDialog extends HelpDialog {
        public AncientCityHelpDialog(GameView view) {
            super(view, "Ancient City", DESCRIPTION);
        }
    }
}
