package model.map.locations;

import model.Model;
import model.SteppingMatrix;
import model.journal.PartSixStoryPart;
import model.journal.StoryPart;
import model.mainstory.GainSupportOfHonorableWarriorsTask;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.EasternPalaceDailyActionState;
import model.states.dailyaction.PirateHavenDailyActionState;
import model.states.dailyaction.shops.EasternPalaceShopNode;
import model.states.dailyaction.shops.GeneralShopNode;
import util.MyLists;
import view.GameView;
import view.MyColors;
import view.combat.CombatTheme;
import view.combat.GrassCombatTheme;
import view.help.HelpDialog;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;
import view.subviews.DailyActionSubView;
import view.subviews.EasternPalaceSubView;
import view.subviews.ImageSubView;
import view.subviews.SubView;

import java.awt.*;
import java.util.List;

public class EasternPalaceLocation extends TownishLocation {

    private static final String DESCRIPTION = "The stronghold of the Honorable Warriors.";
    private final ImageSubView subView;

    public EasternPalaceLocation() {
        super("Eastern Palace");
        subView = new ImageSubView("palace", "EASTERN PALACE",
                "You are at the Eastern Palace.", true);
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new GrassCombatTheme();
    }

    @Override
    public SubView getImageSubView() {
        return subView;
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("easternpalaceupper", 0x1AA, MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.DARK_BROWN);
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("easternpalacelower", 0x1BA, MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.DARK_BROWN);
    }

    @Override
    public HelpDialog getHelpDialog(GameView view) {
        return new EasternPalaceHelpDialog(view);
    }

    @Override
    public String getPlaceName() {
        return "the Eastern Palace";
    }

    @Override
    public GameState getDailyActionState(Model model) {
        return new EasternPalaceDailyActionState(model, this);
    }

    @Override
    public GameState getEveningState(Model model, boolean freeLodge, boolean freeRations) {
        return new EasternPalaceDailyActionState(model, this);
    }

    @Override
    public DailyEventState generateEvent(Model model) {
        GainSupportOfHonorableWarriorsTask task = getHonorableWarriorsTask(model);
        if (task != null && !task.isCompleted()) {
            DailyEventState event = task.generateEvent(model);
            if (event != null) {
                return event;
            }
        }
        return super.generateEvent(model);
    }

    private GainSupportOfHonorableWarriorsTask getHonorableWarriorsTask(Model model) {
        StoryPart part = MyLists.last(model.getMainStory().getStoryParts());
        if (part instanceof PartSixStoryPart) {
            PartSixStoryPart partSix = (PartSixStoryPart) part;
            if (partSix.getRemotePeopleTask() instanceof GainSupportOfHonorableWarriorsTask) {
                return (GainSupportOfHonorableWarriorsTask) partSix.getRemotePeopleTask();
            }
        }
        return null;
    }

    @Override
    public Point getTavernPosition() {
        return new Point(6, 5);
    }

    @Override
    public boolean noBoat() {
        return true;
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new EasternPalaceShopNode(model, 1, 5));
    }

    @Override
    public DailyActionSubView makeActionSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
        return new EasternPalaceSubView(advancedDailyActionState, matrix, true, getName());
    }

    @Override
    public int charterBoatEveryNDays() {
        return 0;
    }

    @Override
    public boolean bothBoatAndCarriage() {
        return false;
    }

    @Override
    public String getGeographicalDescription() {
        return DESCRIPTION;
    }

    private static class EasternPalaceHelpDialog extends HelpDialog {
        public EasternPalaceHelpDialog(GameView view) {
            super(view, "Eastern Palace", DESCRIPTION);
        }
    }
}
