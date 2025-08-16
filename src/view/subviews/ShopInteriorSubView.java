package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.races.Race;
import model.ruins.objects.DungeonChest;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.LodgingNode;
import model.states.dailyaction.ShopInteriorState;
import model.states.dailyaction.tavern.TavernDailyActionState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.Random;

public class ShopInteriorSubView extends RoomDailyActionSubView {
    private static final Sprite MERCHANT = new Sprite32x32("merchant", "world_foreground.png", 0x65,
            MyColors.BLACK, MyColors.GOLD, Race.NORTHERN_HUMAN.getColor(), MyColors.RED);

    private static final Sprite DOOR = new Sprite32x32("door", "world_foreground.png", 0x34,
            MyColors.DARK_GRAY, MyColors.PINK, MyColors.TAN, MyColors.DARK_RED);
    public static final Sprite OPEN_DOOR = new Sprite32x32("door", "world_foreground.png", 0x6E,
            MyColors.DARK_GRAY, MyColors.PINK, MyColors.TAN, MyColors.DARK_RED);

    public static final Sprite LOWER_WALL = new Sprite32x32("lowerwall", "world_foreground.png", 0x24,
            MyColors.DARK_GRAY, MyColors.LIGHT_YELLOW, MyColors.TAN);

    public ShopInteriorSubView(AdvancedDailyActionState state,
                               SteppingMatrix<DailyActionNode> matrix) {
        super(state, matrix);
    }

    @Override
    protected String getPlaceType() {
        return "SHOP INTERIOR";
    }

    @Override
    protected void drawBackgroundRoom(Model model, Random random) {
        super.drawSmallRoom(model, LOWER_WALL);
        Point p = convertToScreen(new Point(5, 1));
        model.getScreenHandler().put(p.x, p.y, LodgingNode.SPRITE);
    }

    @Override
    protected void drawParty(Model model) {

    }

    @Override
    protected void specificDrawDecorations(Model model) {
        drawBar(model);
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

    @Override
    protected Sprite getOpenDoorSprite() {
        return TownHallSubView.OPEN_DOOR;
    }

    @Override
    protected Sprite getClosedDoorSprite() {
        return TownHallSubView.DOOR;
    }

    public void addCalloutAtMerchant(int length) {
        addCallout(length, ShopInteriorState.getShopKeeperPosition());
    }
}
