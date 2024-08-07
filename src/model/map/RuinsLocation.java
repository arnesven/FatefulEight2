package model.map;

import model.Model;
import model.actions.DailyAction;
import model.actions.ExploreRuinsDailyAction;
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

public class RuinsLocation extends HexLocation {

    private SubView subView;

    public RuinsLocation(String ruinsName) {
        super("Ruins of " + ruinsName);
        subView = new ImageSubView("ruins", "RUINS", "Ruins of " + ruinsName, true);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("ruinsupper", 0x01, MyColors.BLACK, MyColors.WHITE, MyColors.RED);
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("ruinslower", 0x11, MyColors.BLACK, MyColors.WHITE, MyColors.RED);
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
    public void travelTo(Model model) {
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.dungeonSong);
    }

    @Override
    public void travelFrom(Model model) {
        ClientSoundManager.playPreviousBackgroundMusic();
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
    public List<DailyAction> getDailyActions(Model model) {
        return List.of(new ExploreRuinsDailyAction(model, getName()));
    }

    @Override
    public boolean inhibitOnRoadSubview() {
        return true;
    }

    @Override
    public HelpDialog getHelpDialog(GameView view) {
        return new TutorialDungeons(view);
    }
}
