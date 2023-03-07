package model.states;

import model.Model;
import model.actions.DailyAction;
import util.MyPair;
import view.DrawingArea;
import view.subviews.DailyActionMenu;
import view.subviews.OnTheRoadSubView;
import view.subviews.SubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DailyActionState extends GameState {

    private Point menuPos;
    private int menuAnchor;

    public DailyActionState(Model model) {
        super(model);
    }

    public GameState run(Model model) {
        if (!model.getCurrentHex().hasLodging() && model.getParty().isOnRoad()) {
            model.setSubView(OnTheRoadSubView.instance);
            menuPos = new Point(SubView.X_OFFSET, SubView.Y_OFFSET);
            menuAnchor = DailyActionMenu.NORTH_WEST;
        } else {
            model.setSubView(model.getCurrentHex().getImageSubView());
            MyPair<Point, Integer> pointAndAnchor = model.getCurrentHex().getDailyActionMenuPositionAndAnchor();
            menuPos = pointAndAnchor.first;
            menuAnchor = pointAndAnchor.second;
        }
        String place = model.getCurrentHex().getPlaceName();
        print("You are " + place + ". ");
        model.getLog().waitForAnimationToFinish();
        print("Please select your daily action.\n");
        DailyAction selectedAction = selectDailyAction(model);
        selectedAction.runPreHook(model);
        return selectedAction.getState();
    }

    private DailyAction selectDailyAction(Model model) {
        List<DailyAction> actions = model.getDailyActions();
        List<String> actionNames = new ArrayList<>();
        for (DailyAction da : actions) {
            actionNames.add(da.getName());
        }
        DailyActionMenu menu = new DailyActionMenu(model.getSubView(), actions, actionNames,
                menuPos.x, menuPos.y, menuAnchor);
        model.setSubView(menu);
        waitForReturn();
        return menu.getSelectedAction();

//        DailyAction selectedAction = null;
//        String dailyActionString = printDailyActions();
//        do {
//            print(dailyActionString);
//            char input = lineInput().charAt(0);
//            char lower = ("" + input).toUpperCase().charAt(0);
//
//            for (DailyAction da : getDailyActions()) {
//                if (da.getShortKey() == input || da.getShortKey() == lower) {
//                    selectedAction = da;
//                }
//            }
//            if (selectedAction == null) {
//                println("That is not a valid input.");
//            } else {
//                break;
//            }
//        } while (true);
//        return selectedAction;
    }

//    private String printDailyActions() {
//        List<DailyAction> actions = getDailyActions();
//        StringBuilder bldr = new StringBuilder();
//        for (DailyAction da : actions) {
//            bldr.append(da.getName());
//            bldr.append(" (");
//            bldr.append(da.getShortKey());
//            bldr.append("), ");
//        }
//        bldr.append(": ");
//        return bldr.substring(0, bldr.length()-2);
//    }
}
