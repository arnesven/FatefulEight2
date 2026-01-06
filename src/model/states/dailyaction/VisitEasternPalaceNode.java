package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import model.characters.GameCharacter;
import model.mainstory.honorable.GainSupportOfHonorableWarriorsTask;
import model.map.locations.EasternPalaceLocation;
import model.states.GameState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;

public class VisitEasternPalaceNode extends DailyActionNode {

    private static final Sprite[][] SPRITES = makePalaceSprites();

    public VisitEasternPalaceNode() {
        super("Visit Palace");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new EasternPalaceMajordomoEvent(model);
    }

    @Override
    public void drawYourself(Model model, Point p) {
        super.drawYourself(model, p);
        for (int row = 0; row < SPRITES[0].length; ++row) {
            for (int col = 0; col < SPRITES.length; ++col) {
                if (col != 1 || row != 2) {
                    Sprite spriteToUse = SPRITES[col][row];
                    Point p2 = new Point(p.x + 4 * (col - 1), p.y + 4 * (row - 2));
                    model.getScreenHandler().register(spriteToUse.getName(), p2, spriteToUse);
                }
            }
        }
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITES[1][2];
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        if (model.getTimeOfDay() == TimeOfDay.EVENING) {
            state.println("The palace is closed at this time. Try again tomorrow.");
            return false;
        }
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
        model.setTimeOfDay(TimeOfDay.EVENING);
    }

    public static Sprite[][] makePalaceSprites() {
        MyColors[] color1 = new MyColors[]{MyColors.WHITE, MyColors.WHITE, MyColors.WHITE, MyColors.DARK_GRAY};
        MyColors[] color3 = new MyColors[]{MyColors.RED, MyColors.RED, MyColors.BROWN, MyColors.DARK_GREEN};
        MyColors[] color2 = new MyColors[]{MyColors.DARK_GRAY, MyColors.DARK_GRAY, MyColors.DARK_GRAY, MyColors.LIGHT_GRAY};
        MyColors[] color4 = new MyColors[]{MyColors.DARK_RED, MyColors.DARK_RED, MyColors.DARK_RED, MyColors.TAN};
        Sprite[][] result = new Sprite32x32[3][4];
        for (int row = 0; row < result[0].length; ++row) {
            for (int col = 0; col < result.length; ++col) {
                result[col][row] = new Sprite32x32("palace" + col + "-"+ row, "world_foreground.png",
                        0x10 * row + col + 12, color1[row], color2[row], color3[row], color4[row]);
            }
        }
        return result;
    }

    private static class EasternPalaceMajordomoEvent extends GameState {
        public EasternPalaceMajordomoEvent(Model model) {
            super(model);
        }

        @Override
        public GameState run(Model model) {
            GainSupportOfHonorableWarriorsTask task = EasternPalaceLocation.getHonorableWarriorsTask(model);
            if (task == null) {
                printQuote("Palace Majordomo", "Our Lord is busy meditating. Please return at another time.");
                return null;
            }
            if (task.hasDoneMikosTask()) {
                printQuote("Palace Majordomo", "Outsider... wait, I've heard of you, you're the one who's been " +
                        "helping around in town.");
                leaderSay("Yes. We need to see Lord Shingen.");
                printQuote("Palace Majordomo", "Hmm... there seems to be a narrow slot in Lord Shingen's busy schedule. " +
                        "Please follow me.");
                task.makeLordShingenEvent(model).doTheEvent(model);
            } else {
                printQuote("Palace Majordomo", "You are an outsider. You have no business here.");
                leaderSay("Actually, we have urgent business. We need to see Lord Shingen.");
                printQuote("Palace Majordomo", "Lord Shingen is wary of outsiders, especially from the west. Now begone!");
                leaderSay("Rude.");
                if (model.getParty().size() > 1) {
                    GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                    partyMemberSay(other, "What do we do now?");
                    leaderSay("We'll figure something out. I'm sure.");
                }
            }
            return null;
        }
    }
}
