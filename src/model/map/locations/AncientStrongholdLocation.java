package model.map.locations;

import model.Model;
import model.actions.DailyAction;
import model.map.HexLocation;
import model.states.DailyEventState;
import util.MyPair;
import view.GameView;
import view.MyColors;
import view.ScreenHandler;
import view.help.HelpDialog;
import view.sprites.HexLocationSprite;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;
import view.subviews.DailyActionMenu;
import view.subviews.ImageSubView;

import java.awt.*;
import java.util.List;

public class AncientStrongholdLocation extends HexLocation {
    private static final Sprite TOWER_TOP = new AncientStrongholdUpperSprite();
    private final int direction;
    private static final ImageSubView subView = new ImageSubView("stronghold", "ANCIENT STRONGHOLD", "An ancient stronghold.", true);


    public AncientStrongholdLocation(int expandDirection) {
        super("Ancient Stronghold");
        this.direction = expandDirection;
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("astrongholdupper", 0x137, MyColors.BLACK, MyColors.DARK_GRAY, MyColors.DARK_RED, MyColors.YELLOW);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("astrongholdupper", 0x127, MyColors.BLACK, MyColors.DARK_GRAY, MyColors.DARK_RED, MyColors.YELLOW);
    }

    @Override
    public ImageSubView getImageSubView(Model model) {
        return subView;
    }

    @Override
    public void drawUpperHalf(ScreenHandler screenHandler, int x, int y, int flag) {
        super.drawUpperHalf(screenHandler, x, y, flag);
        screenHandler.register(TOWER_TOP.getName(), new Point(x, y-4), TOWER_TOP);
    }

    @Override
    public MyPair<Point, Integer> getDailyActionMenuAnchor() {
        return DailyActionMenu.UPPER_LEFT_CORNER;
    }

    @Override
    public HelpDialog getHelpDialog(GameView view) {
        return new AncientStrongholdHelpDialog(view);
    }

    @Override
    public boolean hasDailyActions() {
        return true;
    }

    @Override
    public List<DailyAction> getDailyActions(Model model) {
        return List.of(new DailyAction("Visit Stronghold", new VisitAncientStrongholdEvent(model)));
    }

    @Override
    public boolean isDecoration() {
        return false;
    }

    private static class AncientStrongholdUpperSprite extends LoopingSprite {
        public AncientStrongholdUpperSprite() {
            super("ancientupper", "world_foreground.png", 0x98, 32, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_GRAY);
            setColor3(MyColors.DARK_RED);
            setColor4(MyColors.YELLOW);
            setFrames(4);
        }
    }

    private class VisitAncientStrongholdEvent extends DailyEventState {
        public VisitAncientStrongholdEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            println("The ancient stronghold is ominous and enormous.");
            if (model.getMainStory().getExpandDirection() == direction) {
                println("The door to the stronghold is open.");
                leaderSay("We should rest up here before we take on the Quad's stronghold.");
                if (model.getParty().size() > 1) {
                    partyMemberSay(model.getParty().getRandomPartyMember(model.getParty().getLeader()),
                            "Sounds wise. I don't want to go in there, but I guess we have no choice.");
                }
            } else {
                println("The door to the stronghold is shut and tightly locked. " +
                        "It seems like no pick could pick this lock.");
                leaderSay("We're never getting in there...");
            }
        }
    }

    private class AncientStrongholdHelpDialog extends HelpDialog {
        public AncientStrongholdHelpDialog(GameView view) {
            super(view, "Ancient Stronghold", "A strange, ancient fortress, filled with adversaries, riddles " +
                    "and mystery.");
        }
    }
}