package model.map.locations;

import model.Model;
import model.actions.DailyAction;
import model.map.HexLocation;
import model.states.DailyEventState;
import model.states.dailyaction.BuyRationsState;
import model.states.events.VisitMonasteryEvent;
import view.GameView;
import view.MyColors;
import view.help.HelpDialog;
import view.help.MonasteryHelpDialog;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;
import view.subviews.ImageSubView;
import view.subviews.SubView;

import java.util.ArrayList;
import java.util.List;

public class MonasteryLocation extends HexLocation {
    private final ImageSubView subView;

    public MonasteryLocation() {
        super("Monastery");
        subView = new ImageSubView("monastery", "MONASTERY", "Monastery under Construction", true);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("monasteryupper", 0x180, MyColors.DARK_GRAY, MyColors.GRAY, MyColors.DARK_GREEN, MyColors.GREEN);
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("monasterylower", 0x190, MyColors.DARK_GRAY, MyColors.GRAY, MyColors.DARK_GREEN, MyColors.GREEN);
    }


    @Override
    public boolean isDecoration() {
        return false;
    }

    @Override
    public HelpDialog getHelpDialog(GameView view) {
        return new MonasteryHelpDialog(view);
    }


    @Override
    public SubView getImageSubView(Model model) {
        return subView;
    }

    @Override
    public DailyEventState generateEvent(Model model) {
        return new VisitMonasteryEvent(model);
    }

    @Override
    public boolean hasDailyActions() {
        return true;
    }

    @Override
    public List<DailyAction> getDailyActions(Model model) {
        List<DailyAction> acts = new ArrayList<>();
        if (VisitMonasteryEvent.hasVisited(model)) {
            acts.add(new DailyAction("Talk to Monks", new VisitMonasteryEvent(model)));
            acts.add(new DailyAction("Buy Rations", new BuyRationsState(model)));
        }
        return acts;
    }
}
