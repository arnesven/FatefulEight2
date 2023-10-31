package model.states.dailyaction;

import model.Model;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.map.CastleLocation;
import model.map.UrbanLocation;
import model.ruins.DungeonChest;
import model.states.GameState;
import model.states.events.ChestEvent;
import model.states.events.SilentNoEventState;
import view.sprites.Sprite;

import java.awt.*;

public class LordTreasuryNode extends DailyActionNode {

    private static final String OPENED_FLAG_SUFFIX = "TreasuryOpened";
    private final UrbanLocation location;
    private boolean opened;
    private final boolean breakIn;
    private boolean exitsLocale = false;

    public LordTreasuryNode(Model model, UrbanLocation location, boolean breakIn) {
        super("Treasury");
        this.location = location;
        Boolean opened = model.getSettings().getMiscFlags().get(location.getPlaceName() + OPENED_FLAG_SUFFIX);
        this.opened = (opened != null && opened);
        this.breakIn = breakIn;
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new RobLordTreasuryAction(model);
    }

    @Override
    public Sprite getBackgroundSprite() {
        if (opened) {
            if (isBig()) {
                return DungeonChest.BIG_CHEST_OPEN;
            }
            return DungeonChest.BIG_CHEST_CLOSED;
        }
        if (isBig()) {
            return DungeonChest.BIG_CHEST_CLOSED;
        }
        return DungeonChest.CHEST_CLOSED;
    }

    private boolean isBig() {
        return location instanceof CastleLocation;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        Sprite spr = getBackgroundSprite();
        model.getScreenHandler().register(spr.getName(), p, spr);
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        if (opened) {
            state.println("The treasure has already been opened.");
            return false;
        }
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }

    @Override
    public boolean exitsCurrentLocale() { return exitsLocale; }

    @Override
    public boolean returnNextState() { return exitsLocale; }

    private class RobLordTreasuryAction extends GameState {
        public RobLordTreasuryAction(Model model) {
            super(model);
        }

        @Override
        public GameState run(Model model) {
            if (!breakIn) {
                lordIsOutraged(model);
            } else {
                crackSafe(model);
            }
            exitsLocale = true;
            println("You were expelled from " + location.getLordDwelling() + ".");
            print("Press enter to continue.");
            waitForReturn();
            model.getParty().unbenchAll();
            return new SilentNoEventState(model);
        }

        private void lordIsOutraged(Model model) {
            printQuote(location.getLordName(), "Stop! What do you think you're doing?");
            leaderSay("Uhm, I was just looking for the restroom?");
            SkillCheckResult result = model.getParty().getLeader().testSkill(Skill.Persuade, 7);

            println(model.getParty().getLeader().getName() + " attempts to persuade " +
                    location.getLordName() + ", Persuade " + result.asString() + ".");
            if (result.isSuccessful()) {
                printQuote(location.getLordName(), "It's not back there, I assure you. " +
                        "There's an outhouse just around the corner...");
                leaderSay("Thank you. I'll be on my way then...");
            } else {
                printQuote(location.getLordName(), "You don't fool me thief. Now get out.");
                model.getParty().addToNotoriety(10);
                printAlert("Your notoriety has increased!");
            }
        }

        private void crackSafe(Model model) {
            leaderSay("Now's our chance. Let's break into the treasury!");
            boolean success = model.getParty().doSoloLockpickCheck(model, this, 8);
            if (success) {
                ChestEvent.chestOpens(model, this, isBig()?15:7);
                opened = true;
                model.getSettings().getMiscFlags().put(location.getPlaceName() + OPENED_FLAG_SUFFIX, true);
            } else {
                partyMemberSay(model.getParty().getRandomPartyMember(), "That's one tricky lock.");
                leaderSay("Hmm, too bad. I wonder what " + heOrShe(location.getLordGender()) + " is hiding in there.");
            }
            partyMemberSay(model.getParty().getRandomPartyMember(), "Let's get out of here before we're caught!");
            leaderSay("And make sure not to be spotted on our way out.");
            boolean result = model.getParty().doCollectiveSkillCheck(model, this, Skill.Sneak, 4);
            if (!result) {
                model.getParty().addToNotoriety(40);
                printAlert("Your crime has been witnessed, your notoriety has increased!");
            }
        }
    }
}
