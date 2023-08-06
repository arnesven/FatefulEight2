package model.map;

import model.Model;
import model.SteppingMatrix;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.GeneralShopNode;
import view.sprites.Sprite;
import view.subviews.DailyActionSubView;

import java.awt.Point;
import java.util.List;

public interface UrbanLocation {
    String getLordName();
    String getPlaceName();
    Point getTavernPosition();
    boolean noBoat();
    List<GeneralShopNode> getShops(Model model);
    DailyActionSubView makeActionSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix);
    Point getTravelNodePosition();
    String getLocationType();
    String getLordDwelling();
    String getLordTitle();
    boolean getLordGender();
    Sprite getExitSprite();

    int charterBoatEveryNDays();
}
