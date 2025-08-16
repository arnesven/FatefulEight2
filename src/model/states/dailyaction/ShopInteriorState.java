package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import model.items.Item;
import model.states.GameState;
import model.states.dailyaction.shops.ShopKeeperNode;
import model.states.dailyaction.town.CourseCoordinator;
import view.subviews.CareerOfficeSubView;
import view.subviews.DailyActionSubView;
import view.subviews.ShopInteriorSubView;

import java.awt.*;
import java.util.List;

public class ShopInteriorState extends AdvancedDailyActionState {
    private static final Point DOOR_POS = new Point(3, 6);
    private static final Point SHOP_KEEPER_POS = new Point(3, 2);

    public ShopInteriorState(Model model, String name, List<Item> shopInventory, int[] specialPrices, boolean[] haggleFlag) {
        super(model);
        addNode(getShopKeeperPosition().x, getShopKeeperPosition().y+1,
                new ShopKeeperNode(name, shopInventory, specialPrices, haggleFlag));
        addNode(DOOR_POS.x, DOOR_POS.y + 1, new ExitLocaleNode("Leave shop"));
    }

    public static Point getShopKeeperPosition() {
        return SHOP_KEEPER_POS;
    }

    public static Point getDoorPosition() {
        return DOOR_POS;
    }

    @Override
    protected Point getStartingPosition() {
        return new Point(3, 4);
    }

    @Override
    protected DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
        return new ShopInteriorSubView(advancedDailyActionState, matrix);
    }
}
