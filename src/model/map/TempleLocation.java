package model.map;

import model.Model;
import model.actions.DailyAction;
import model.states.*;
import model.states.dailyaction.BuyRationsState;
import model.states.events.*;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyPair;
import util.MyRandom;
import view.GameView;
import view.MyColors;
import view.help.HelpDialog;
import view.help.TempleHelpDialog;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;
import view.subviews.DailyActionMenu;
import view.subviews.ImageSubView;
import view.subviews.SubView;

import java.awt.*;
import java.util.List;

public class TempleLocation extends HexLocation {
    private final ImageSubView subView;

    public TempleLocation(String templeName) {
        super("Temple of " + templeName);
        subView = new ImageSubView("temple", "TEMPLE", "Temple of " + templeName, true);
    }

    @Override
    public DailyEventState generateEvent(Model model) {
        if (model.getParty().isBannedFromTemple(getName())) {
            return new TempleGuardsEvent(model, false);
        }
        int roll = MyRandom.rollD10();
        if (roll > 2) {
            return MyRandom.sample(List.of(
                    new PriestEvent(model),
                    new PriestEvent(model),
                    new WhiteKnightEvent(model),
                    new CleansingRitual(model),
                    new TranceEvent(model),
                    new HighPriestessEvent(model),
                    new TempleGuardsEvent(model, true),
                    new KitchenDutyEvent(model),
                    new MeditationEvent(model),
                    new GoldenIdolsEvent(model),
                    new FindPuzzleTubeEvent(model)
            ));
        }
        return new NoEventState(model);
    }

    @Override
    public void travelTo(Model model) {
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.templeSong);
    }

    @Override
    public void travelFrom(Model model) { }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("templeupper", 0xE0, MyColors.BLACK, MyColors.WHITE, MyColors.PEACH);
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("templelower", 0xF0, MyColors.BLACK, MyColors.WHITE, MyColors.PEACH);
    }

    @Override
    public SubView getImageSubView() {
        return subView;
    }

    @Override
    public boolean isDecoration() {
        return false;
    }

    @Override
    public boolean hasLodging() {
        return true;
    }

    @Override
    public MyPair<Point, Integer> getDailyActionMenuAnchor() {
        return DailyActionMenu.UPPER_LEFT_CORNER;
    }

    @Override
    public HelpDialog getHelpDialog(GameView view) {
        return new TempleHelpDialog(view);
    }

    @Override
    public GameState getEveningState(Model model, boolean freeLodge, boolean freeRations) {
        return new TempleEveningState(model);
    }

    @Override
    public boolean hasDailyActions() {
        return true;
    }

    @Override
    public List<DailyAction> getDailyActions(Model model) {
        return List.of(new DailyAction("Buy Rations", new BuyRationsState(model)),
                new DailyAction("Training", new TrainingState(model)));
    }
}
