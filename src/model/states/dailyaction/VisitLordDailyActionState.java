package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import model.Summon;
import model.TimeOfDay;
import model.headquarters.Headquarters;
import model.items.puzzletube.FindPuzzleDestinationTask;
import model.journal.RescueMissionStoryPart;
import model.journal.StoryPart;
import model.map.UrbanLocation;
import model.states.GameState;
import model.states.events.SilentNoEventState;
import model.tasks.MonsterHuntDestinationTask;
import model.tasks.SummonTask;
import util.MyLists;
import view.sprites.Sprite;
import view.subviews.DailyActionSubView;
import view.subviews.TownHallSubView;

import java.util.List;
import java.awt.*;

public abstract class VisitLordDailyActionState extends AdvancedDailyActionState {
    private final Summon summon;
    private final UrbanLocation location;
    private boolean spentNight;

    public VisitLordDailyActionState(Model model, Summon summon, UrbanLocation location, boolean breakIn) {
        super(model);
        this.summon = summon;
        this.location = location;
        spentNight = false;
        if (!breakIn) {
            addNode(4, 3, new TalkToLordNode());
        }
        addNode(3, 7, new ExitLocaleNode("Leave " + location.getLordDwelling(), location.getExitSprite()));
        addNode(1, 1, new LordTreasuryNode(model, location, breakIn));
    }

    @Override
    protected Point getStartingPosition() {
        return new Point(3, 5);
    }

    @Override
    protected abstract DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix);

    private class TalkToLordNode extends DailyActionNode {
        public TalkToLordNode() {
            super("Talk to " + location.getLordTitle());
        }

        @Override
        public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
            if (FindPuzzleDestinationTask.hasTaskAtCurrentLocation(model)) {
                return new MakeDealForPuzzleTubeDailyAction(model, location);
            }
            if (MonsterHuntDestinationTask.hasTaskAtCurrentLocation(model)) {
                return new GetPaidForMonstersDailyAction(model, location);
            }
            return new AnswerSummonDailyAction(model);
        }

        @Override
        public Sprite getBackgroundSprite() {
            return TownHallSubView.RUG;
        }

        @Override
        public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
            if (FindPuzzleDestinationTask.hasTaskAtCurrentLocation(model) ||
                    MonsterHuntDestinationTask.hasTaskAtCurrentLocation(model)) {
                return true;
            }
            if (summon == null) {
                printQuote(location.getLordName(), "Excuse me, what are you doing in here? " +
                        "I've told my servants I'm not to be disturbed with the trifles of commoners.");
                return false;
            }
            if (state.isEvening() && summon.getStep() != Summon.COMPLETE) {
                printQuote(location.getLordName(), "I'm sorry but it's too late in the day now. Please come back tomorrow.");
                return false;
            }
            return true;
        }

        @Override
        public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
            if (!spentNight) {
                if (state.isMorning()) {
                    model.setTimeOfDay(TimeOfDay.MIDDAY);
                } else if (!state.isEvening()) {
                    model.setTimeOfDay(TimeOfDay.EVENING);
                }
            }
        }

        @Override
        public boolean returnNextState() {
            return spentNight;
        }
    }

    private class AnswerSummonDailyAction extends GameState {

        public AnswerSummonDailyAction(Model model) {
            super(model);
        }

        @Override
        public GameState run(Model model) {
            new InnerPortraitEvent(model).run(model);
            if (spentNight) {
                return new LodgingState(model, true).run(model);
            }
            return new SilentNoEventState(model);
        }

        private class InnerPortraitEvent extends SummonTask {
            public InnerPortraitEvent(Model model) {
                super(model);
            }

            @Override
            public String getJournalDescription() {
                return "unused";
            }

            @Override
            protected void doEvent(Model model) {
                String lord = location.getLordName();
                showExplicitPortrait(model, model.getLordPortrait(location), location.getLordName());
                if (summon.getStep() == Summon.ACCEPTED) {
                    if (hasMetLordThroughMainStory(model)) {
                        String leaderName = model.getParty().getLeader().getName();
                        portraitSay("Hello there. " + leaderName + "! Good to see you.");
                        leaderSay("You summoned me.");
                        portraitSay("Yes, there was another task I was wondering if you might be able to help me with.");
                    } else {
                        String leaderName = model.getParty().getLeader().getName();
                        portraitSay("Hello there. " + leaderName + ", I presume? I've been expecting you.");
                        leaderSay("Yes, that's me. Who are you?");
                        portraitSay("I'm " + lord + ". I'm in charge here. First of all, " +
                                "let me formally welcome you to " + location.getPlaceName() +
                                ", I hope you like our " + location.getLocationType() + ".");
                        model.getParty().randomPartyMemberSay(model,
                                List.of("Enough with the formalities. What is it you want?",
                                        "Get on with it, I haven't got all day!", "It's pleasant enough I suppose.",
                                        "Sure, it's great. Now what do you want?", "Thanks. Can I help you?", "How do you know my name?"));
                        portraitSay("Your reputation has preceded you and I was wondering if you might be able to help me with a problem of mine.");
                    }
                    summon.increaseStep();
                }
                if (summon.getStep() != Summon.COMPLETE) {
                    SummonTask task = summon.getTask(model, location);
                    task.setPortraitSubView(this);
                    task.doTask(model);
                    if (summon.getStep() == Summon.COMPLETE) {
                        summon.setCompletedOnDay(model.getDay());
                    }
                }

                if (summon.getStep() == Summon.COMPLETE) {
                    portraitSay("Thanks again for helping me with my problem.");

                    checkForHeadquarterPurchase(model);

                    if (!model.getParty().hasHeadquartersIn(VisitLordDailyActionState.this.location)) {
                        portraitSay("Please, stay for supper and spend the night, there's room for everyone.");
                        print("Do you wish to spend the night here? (Y/N): ");
                        if (yesNoInput()) {
                            spentNight = true;
                        }
                    }
                }
            }

            private void checkForHeadquarterPurchase(Model model) {
                if (model.getParty().hasHeadquartersIn(location)) {
                    return;
                }
                portraitSay("Say, you seem like a stand up citizen. We need more of your ilk residing in our town! " +
                        "It just so happens we have some real estate for sale at the moment. Would you be interested?");
                leaderSay("I could be. What are the details?");
                Headquarters hq = location.getRealEstate();
                portraitSay(hq.presentYourself() + " The current owner is willing to let it go for " +
                        hq.getCost() + " gold. Why don't you buy it? You could make it the " +
                        "headquarters for your adventuring party.");
                if (hq.getCost() > model.getParty().getGold()) {
                    leaderSay("I'm afraid it's a little over our budget.");
                    portraitSay("Oh, I see. Well, the offer lasts as long as nobody else buys it.");
                } else {
                    print("Buy the house? (Y/N) ");
                    if (yesNoInput()) {
                        if (model.getParty().getHeadquarters() == null) {
                            leaderSay("Okay. We'll take it.");
                            buyHome(model, location, hq);
                        } else {
                            boolean answer = offerTransfer(model, hq, location, "buy it");
                            if (answer) {
                                buyHome(model, location, hq);
                            } else {
                                leaderSay("No, I think I've changed my mind.");
                                portraitSay("Alright then. Come back if you change it again.");
                            }
                        }
                    } else {
                        leaderSay("We'll think about it.");
                        portraitSay("Oh, I see. Well, the offer lasts as long as nobody else buys it.");
                    }
                }
            }

            private void buyHome(Model model, UrbanLocation location, Headquarters hq) {
                portraitSay("Splendid. I can hand you the keys right now.");
                println("You paid " + hq.getCost() + " gold to the " + location.getLordTitle() + ".");
                model.getParty().spendGold(hq.getCost());
                model.getParty().setHeadquarters(model, hq);
            }
        }
    }


    public static boolean hasMetLordThroughMainStory(Model model) {
        StoryPart part = MyLists.find(model.getMainStory().getStoryParts(), (StoryPart p) -> p instanceof RescueMissionStoryPart);
        if (part != null && part.getJournalEntries().get(0).isComplete()) {
            return true;
        }
        return model.getSettings().getMiscFlags().containsKey("MAIN_STORY_LORD_MET");
    }
}
