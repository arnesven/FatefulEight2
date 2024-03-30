package model.states.dailyaction;

import control.FatefulEight;
import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.map.HexLocation;
import model.map.UrbanLocation;
import model.states.DailyEventState;
import model.states.GameState;
import model.travellers.Traveller;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.PortraitSubView;
import view.subviews.TavernSubView;

import java.util.List;
import java.awt.*;

public class TravellerNode extends DailyActionNode {
    private final Sprite32x32 sprite;
    private final Traveller traveller;

    public TravellerNode(Model model) {
        super("Traveller");
        this.traveller = makeRandomTraveller(model, 5);
        int spriteNum = 0xAB;
        if (traveller.getRace().isShort()) {
            spriteNum = 0xBB;
        }
        this.sprite = new Sprite32x32("traveller", "world_foreground.png", spriteNum,
                MyColors.BLACK, MyColors.DARK_RED, traveller.getRace().getColor(), MyColors.BROWN);


    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new AcceptTravellerState(model, state, traveller);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return TavernSubView.FLOOR;
    }


    @Override
    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().register(sprite.getName(), new Point(p.x, p.y-4), sprite);
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        if (!FatefulEight.inDebugMode()) { // TODO: Remove
            if (model.getParty().size() == 1 && DailyEventState.calculateAverageLevel(model) < 2.0) {
                traveller.refuseLowLevel(state);
                return false;
            }
            if (model.getParty().getNotoriety() > 10) {
                traveller.refuseNotoriety(state);
                return false;
            }
        }
        if (model.getParty().getActiveTravellers().contains(traveller)) {
            traveller.printReady(state);
            return false;
        }
        return true;
    }

    public static Traveller makeRandomTraveller(Model model, int howManyNearest) {
        AdvancedAppearance appearance = PortraitSubView.makeRandomPortrait(Classes.None);
        int rank = MyRandom.randInt(0, howManyNearest);
        model.getWorld().dijkstrasByLand(model.getParty().getPosition());
        List<Point> points = model.getWorld().shortestPathToNearestTownOrCastle(rank);
        HexLocation loc = model.getWorld().getHex(points.get(points.size()-1)).getLocation();
        int time = MyRandom.randInt(points.size(), points.size()*2);
        int reward = MyRandom.randInt(points.size()/2, points.size()*2) +
                MyRandom.randInt(points.size()/2, points.size()*2);
        return new Traveller("Traveller", appearance, loc, time, reward);
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }
}
