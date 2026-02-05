package model.map;

import model.Model;
import model.achievements.Achievement;
import model.actions.DailyAction;
import model.map.locations.AncientCityLocation;
import model.states.ExploreTombState;
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

    public static final String HAARFAGRE_TOMB = "Haarfagre";
    public static final String VORHUNDREN_TOMB = "Vorhundren";
    public static final String KZINRIC_TOMB = "Kzinric";
    public static final String MIRON_TOMB = "Miron";
    public static final String SHAKMA_TOMB = "Shakma";
    public static final String CATRIONA_TOMB = "Catriona";
    public static final String UZOCTL_TOMB = "Uzoctl";
    public static final String XALARDIUM_TOMB = "Xalardium";

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
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.dungeonSong);
    }

    @Override
    public void travelFrom(Model model) { }

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
    public SubView getImageSubView(Model model) {
        return subView;
    }

    @Override
    public List<DailyAction> getDailyActions(Model model) {
        return List.of(new ExploreTombDailyAction(model, getName()));
    }

    public Achievement.Data getAchievementData() {
        return new Achievement.Data(getName(), getName(),
                "You successfully cleared the dungeon under the " + getName() +
                        " and vanquished the evil that dwelt there.");
    }

    private static class ExploreTombDailyAction extends DailyAction {
        public ExploreTombDailyAction(Model model, String name) {
            super("Explore Tomb", new ExploreTombState(model, name));
        }
    }

    @Override
    public HexLocation makePastSelf() {
        return new AncientCityLocation(getName().replace("Tomb of ", ""));
    }
}
