package model.states.dailyaction.town;

import model.Model;
import model.SteppingMatrix;
import model.states.dailyaction.*;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.CareerOfficeSubView;
import view.subviews.DailyActionSubView;

import java.awt.*;

public class CareerOfficeState extends AdvancedDailyActionState {
    private static final Sprite DOOR = new Sprite32x32("door", "world_foreground.png", 0x34,
            MyColors.DARK_GRAY, MyColors.PINK, MyColors.TAN, MyColors.DARK_RED);

    public CareerOfficeState(Model model) {
        super(model);
        addNode(3, 3, new CareerCoachNode());
        addNode(1, 3, new ClassesExpert());
        addNode(5, 3, new CourseCoordinator());
        addNode(3, 6, new ExitLocaleNode("Leave career office", DOOR));
    }

    @Override
    protected Point getStartingPosition() {
        return new Point(3, 4);
    }

    @Override
    protected DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
        return new CareerOfficeSubView(advancedDailyActionState, matrix);
    }

}
