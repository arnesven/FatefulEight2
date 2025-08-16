package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.races.Race;
import model.ruins.objects.DungeonChest;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.LodgingNode;
import model.states.dailyaction.ShopInteriorState;
import model.states.dailyaction.shops.ShoppingNode;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.Random;

public class ShopInteriorSubView extends RoomDailyActionSubView {
    private static final Sprite MERCHANT = new Sprite32x32("merchant", "world_foreground.png", 0x65,
            MyColors.BLACK, MyColors.GOLD, Race.NORTHERN_HUMAN.getColor(), MyColors.RED);

    private final String name;
    private final ShoppingNode shoppingNode;

    public ShopInteriorSubView(AdvancedDailyActionState state,
                               SteppingMatrix<DailyActionNode> matrix,
                               ShoppingNode shoppingNode) {
        super(state, matrix);
        this.shoppingNode = shoppingNode;
        this.name = shoppingNode.getName().toUpperCase();
    }

    @Override
    protected String getPlaceType() {
        return name;
    }

    @Override
    protected void drawBackgroundRoom(Model model, Random random) {
        super.drawSmallRoom(model, shoppingNode.getLowerWallSprite(), shoppingNode.getDoorSprite(), 3);
        Point p = convertToScreen(new Point(5, 1));
        model.getScreenHandler().put(p.x, p.y, LodgingNode.SPRITE);
    }

    @Override
    protected void drawParty(Model model) {}

    @Override
    protected Sprite getOverDoorSprite() {
        return shoppingNode.getOverDoorSprite();
    }

    @Override
    protected void specificDrawDecorations(Model model) {
        drawBar(model);
        drawForeground(model, 4, 6, shoppingNode.getBigSignSprite());
        drawForeground(model, 2, 3, shoppingNode.getCounterItemSprites()[0], 1);
        drawForeground(model, 5, 3, shoppingNode.getCounterItemSprites()[1], 1);

        Point p = ShopInteriorState.getShopKeeperPosition();
        drawForeground(model, p.x, p.y, MERCHANT);
        drawForeground(model, 1, 2, DungeonChest.BIG_CHEST_CLOSED);
        drawForeground(model, 2, 1, TownHallSubView.WINDOW);
        drawForeground(model, 4, 1, TownHallSubView.WINDOW);
    }

    @Override
    protected Point getDoorPosition() {
        return ShopInteriorState.getDoorPosition();
    }

    public void addCalloutAtMerchant(int length) {
        addCallout(length, ShopInteriorState.getShopKeeperPosition());
    }
}
