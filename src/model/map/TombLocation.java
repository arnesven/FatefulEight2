package model.map;

import model.Model;
import model.actions.DailyAction;
import model.actions.ExploreRuinsDailyAction;
import model.states.ExploreTombState;
import model.states.GameState;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyPair;
import view.GameView;
import view.MyColors;
import view.help.HelpDialog;
import view.help.TutorialDungeons;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;
import view.subviews.DailyActionMenu;
import view.subviews.ImageSubView;
import view.subviews.SubView;

import java.awt.*;
import java.util.List;

public class TombLocation extends HexLocation {

    private final SubView subView;

    public TombLocation(String tombName) {
        super("Tomb of " + tombName);
        subView = new ImageSubView("ruins", "TOMB", tombName + "'s Tomb", true);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("tombupper", 0x182, MyColors.BLACK, MyColors.WHITE, MyColors.RED);
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("tomblower", 0x192, MyColors.BLACK, MyColors.WHITE, MyColors.RED);
    }

    @Override
    public void travelTo(Model model) {
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.mysticSong);
    }

    @Override
    public void travelFrom(Model model) {
        model.playMainSong();
    }

    @Override
    public MyPair<Point, Integer> getDailyActionMenuAnchor() {
        return DailyActionMenu.UPPER_RIGHT_CORNER;
    }

    @Override
    public boolean hasDailyActions() {
        return true;
    }

    @Override
    public boolean inhibitOnRoadSubview() {
        return true;
    }

    @Override
    public boolean isDecoration() {
        return false;
    }

    @Override
    public HelpDialog getHelpDialog(GameView view) {
        return new TutorialDungeons(view);
    }

    @Override
    public SubView getImageSubView() {
        return subView;
    }

    @Override
    public List<DailyAction> getDailyActions(Model model) {
        return List.of(new ExploreTombDailyAction(model, getName()));
    }

    private static class ExploreTombDailyAction extends DailyAction {
        public ExploreTombDailyAction(Model model, String name) {
            super("Explore Tomb", new ExploreTombState(model, name));
        }
    }
}
