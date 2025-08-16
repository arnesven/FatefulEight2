package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import model.items.Item;
import model.states.GameState;
import model.states.dailyaction.shops.ShopKeeperNode;
import model.states.dailyaction.shops.ShoppingNode;
import model.states.dailyaction.town.CourseCoordinator;
import view.sprites.Sprite;
import view.subviews.CareerOfficeSubView;
import view.subviews.DailyActionSubView;
import view.subviews.ShopInteriorSubView;

import java.awt.*;
import java.util.List;

public class ShopInteriorState extends AdvancedDailyActionState {
    private static final Point DOOR_POS = new Point(3, 6);
    private static final Point SHOP_KEEPER_POS = new Point(3, 2);
    private final ShoppingNode node;

    public ShopInteriorState(Model model, ShoppingNode shoppingNode) {
        super(model);
        this.node = shoppingNode;
        addNode(getShopKeeperPosition().x, getShopKeeperPosition().y+1,
                new ShopKeeperNode(node.getName(), node.getInventory(), null, node.getHaggleFlag()));
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
        return new ShopInteriorSubView(advancedDailyActionState, matrix, node);
    }
}
