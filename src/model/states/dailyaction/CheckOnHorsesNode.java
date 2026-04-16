package model.states.dailyaction;

import model.Model;
import model.horses.Horse;
import model.horses.HorseHandler;
import model.map.*;
import model.states.GameState;
import view.sprites.Animation;
import view.sprites.RidingSprite;
import view.sprites.Sprite;

import java.awt.*;

public class CheckOnHorsesNode extends DailyActionNode {
    private static final Point SHIFT = new Point(-4, -6);
    private final Sprite sprite;

    public CheckOnHorsesNode(HorseHandler horseHandler) {
        super("Check on horses");
        this.sprite = new RidingSprite(horseHandler.getFirst());
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new CheckOnHorsesState(model);
    }

    @Override
    public void drawYourself(Model model, Point p) {
        if (sprite instanceof Animation) {
            ((Animation) sprite).synch();
        }
        Point p2 = new Point(p.x - 4, p.y - 4);
        model.getScreenHandler().register(getBackgroundSprite().getName(), p2, getBackgroundSprite());
    }

    @Override
    public Sprite getBackgroundSprite() {
        return sprite;
    }

    @Override
    public Point getCursorShift() {
        return SHIFT;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }

    private static class CheckOnHorsesState extends GameState {
        public CheckOnHorsesState(Model model) {
            super(model);
        }

        @Override
        public GameState run(Model model) {
            boolean moreThanOneHorse = model.getParty().getHorseHandler().size() > 1;
            String itOrThem = moreThanOneHorse ? "them":"it";
            String itIsOrTheyAre = moreThanOneHorse ? "they are":"it is";
            String singleS = moreThanOneHorse ? "" : "s";

            String subjectAndVerb = model.getParty().getHorseHandler().getFirst().getName();
            switch (model.getCurrentHex().getResourcePrevalences().ingredients) {
                case ResourcePrevalence.NON_EXISTENT:
                    subjectAndVerb += " stare";
                    if (moreThanOneHorse) {
                        subjectAndVerb = "horses stare";
                    }
                    println("Your " + subjectAndVerb + " at you with hollow, desperate eyes, silently urging, " +
                            "'Get us out of here. There\'s nothing for us to eat'.");
                    break;

                case ResourcePrevalence.POOR:
                    subjectAndVerb += " trot";
                    if (moreThanOneHorse) {
                        subjectAndVerb = "horses trots";
                    }
                    println("Your " + subjectAndVerb + " around slowly, there are a few plants for " + itOrThem + " to eat.");
                    break;

                case ResourcePrevalence.FAIR:
                    subjectAndVerb += " looks";
                    if (moreThanOneHorse) {
                        subjectAndVerb = "horses look";
                    }
                    println("Your " + subjectAndVerb + " briefly at you, then return" + singleS + " to grazing.");

                default: // GOOD
                    subjectAndVerb += " ignore";
                    if (moreThanOneHorse) {
                        subjectAndVerb = "horses ignores";
                    }
                    println("Your " + subjectAndVerb + " you as " + itIsOrTheyAre + " busy grazing on the lush vegetation.");
            }
            return null;
        }
    }
}
