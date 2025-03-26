package model.states.dailyaction.town;

import model.Model;
import model.TimeOfDay;
import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.states.GameState;
import model.states.dailyaction.AdvancedDailyActionState;
import util.MyPair;
import view.subviews.CollapsingTransition;
import view.subviews.SubView;

import java.util.HashMap;
import java.util.Map;

public class CareerCoachNode extends CareerOfficePersonNode {

    public CareerCoachNode() {
        super("Career Coach");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new CareerCoachState(model);
    }

    private static class CareerCoachState extends GameState {
        public CareerCoachState(Model model) {
            super(model);
        }

        @Override
        public GameState run(Model model) {
            coachSay("Welcome to the career office! We are a teaching and learning center funded by the leaders of this town.");
            coachSay("Our ambition is to help anyone and everyone who is set on making a career change.");
            leaderSay("Interesting, what services do you offer?");
            coachSay("If you want to know more about different classes, please speak to my colleague in the yellow hat.");
            coachSay("If you are interested in what learning session we are hosting today, please speak to my colleague in the green hat.");
            checkForSeminar(model);
            return model.getCurrentHex().getDailyActionState(model);
        }

        private void checkForSeminar(Model model) {
            if (model.getParty().isSeminarHeld()) {
                return;
            }
            coachSay("As for me, well I'm actually a recruiter. I'm looking for a group of instructors...");
            MyPair<CharacterClass, Integer> most = findMostClasses(model);
            String className = most.first.getFullName().toLowerCase();
            if (most.second == 1) {
                coachSay("... but you don't seem to fit the criteria.");
            } else if (most.second == 2) {
                coachSay("Say, you have two " + className +
                        "s in your group. If you could find one more, you would be perfect for hosting a seminar on that line of work.");
                leaderSay("I'm not sure we have time for that.");
                coachSay("We would pay you of course. Such seminars are well visited, people come from all " +
                        "around the country to listen and learn. Your share of the entry fees would be 120 gold.");
                leaderSay("Really?");
                coachSay("Yes, but it would be one time thing, naturally. Once held, people aren't too keen to come and " +
                        "take the same course again.");
                leaderSay("Of course. Well if we recruit another " + className + " we may come back.");
                coachSay("I'm looking forward to it!");
            } else {
                coachSay("Wait a minute... you're a whole group of " + className + "s! " +
                        "Would you consider hosting a seminar on that line of work?");
                leaderSay("What's in it for us?");
                coachSay("We would pay you 120 gold!");
                print("Do you agree to host a seminar on being a " + className + "? (Y/N) ");
                if (yesNoInput()) {
                    leaderSay("Alright, let's do it.");
                    if (model.getTimeOfDay() != TimeOfDay.MORNING) {
                        coachSay("Great! But it's too late in the day now. We won't have time to get the word out to people. " +
                                "Come see us first thing in the morning. That way we'll have time to spread the word about the seminar.");
                        leaderSay("Fair enough.");
                    } else {
                        coachSay("Fabulous! I'll spread the word. You guys get ready to teach everybody everything there " +
                                "is to know about being a " + className + "!");
                        model.getParty().setSeminarHeld(true);
                        model.getLog().waitForAnimationToFinish();
                        SubView subView = model.getSubView();
                        setCurrentTerrainSubview(model);
                        println("You hastily prepare a seminar about life as a " + className +
                                ". There are indeed many people who are interested and the amphitheatre " +
                                "behind the career office is packed with participants as the event begins.");
                        println("The time passes quickly and you are surprised to find that many have questions and seem " +
                                "eager to learn about being a " + className + ". However, toward the end, you can feel the " +
                                "fatigue setting in from constantly talking.");
                        for (GameCharacter gc : model.getParty().getPartyMembers()) {
                            if (gc.getCharClass().getShortName().equals(most.first.getShortName())) {
                                if (gc.getSP() > 0) {
                                    println(gc.getName() + " exhausts 1 Stamina Point.");
                                    gc.addToSP(-1);
                                }
                            }
                        }
                        println("When it's all over, the career coach approaches you.");
                        coachSay("That was splendid! I think everybody got a real eye opener! Here's your wages.");
                        model.getParty().addToGold(120);
                        println("You receive 120 gold.");
                        model.setTimeOfDay(TimeOfDay.EVENING);
                        model.getLog().waitForAnimationToFinish();
                        CollapsingTransition.transition(model, subView);
                    }
                } else {
                    leaderSay("Sorry, we just don't have time for this sort of thing.");
                    coachSay("What a shame! Well, please come back and see us if you change your mind.");
                }
            }
        }

        private MyPair<CharacterClass, Integer> findMostClasses(Model model) {
            Map<String, Integer> map = new HashMap<>();
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                if (!map.containsKey(gc.getCharClass().getShortName())) {
                    map.put(gc.getCharClass().getShortName(), 0);
                }
                map.put(gc.getCharClass().getShortName(), map.get(gc.getCharClass().getShortName()) + 1);
            }

            int most = 0;
            String mostClassStr = null;
            for (String cls : map.keySet()) {
                if (map.get(cls) > most) {
                    most = map.get(cls);
                    mostClassStr = cls;
                }
            }

            CharacterClass mostClass = null;
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                if (gc.getCharClass().getShortName().equals(mostClassStr)) {
                    mostClass = gc.getCharClass();
                }
            }
            return new MyPair<>(mostClass, most);
        }

        private void coachSay(String s) {
            printQuote("Career Coach", s);
        }
    }
}
