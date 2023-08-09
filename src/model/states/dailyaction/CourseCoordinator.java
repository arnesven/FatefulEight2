package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.states.GameState;
import model.states.events.ChangeClassEvent;
import util.MyRandom;
import view.subviews.CollapsingTransition;
import view.subviews.SubView;

import java.util.List;

public class CourseCoordinator extends CareerOfficePersonNode {
    public CourseCoordinator() {
        super("Course Coordinator");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new CourseCoordinatorState(model);
    }

    private static class CourseCoordinatorState extends GameState {
        public CourseCoordinatorState(Model model) {
            super(model);
        }

        @Override
        public GameState run(Model model) {
            CharacterClass cls = Classes.allClasses[model.getDay() % Classes.allClasses.length];
            if (cls == Classes.None) {
                coordinatorSay("We normally hold courses here every day. Unfortunately, today, " +
                        "the instructor could not come and we have been forced to cancel.");
                leaderSay("That's a shame. What was the topic of the course?");
                coordinatorSay(MyRandom.sample(List.of("Culinary masterpieces.", "Astrology.",
                        "The finer points of democracy.", "Supposed telepathy between twins.",
                        "Hairstyles for cats.", "The gamblers fallacy.", "Kobold conspiracy theories.")));
                leaderSay("...");
            } else {
                coordinatorSay("Today we're offering a special course!");
                leaderSay("Oh really, what is it?");
                coordinatorSay("It's all about how to become a " + cls.getFullName().toLowerCase() +
                        ". Are you interested in taking the course?");
                leaderSay("I could be. What's the entry fee?");
                coordinatorSay("For 15 gold, your entire party can attend. It's in the amphitheatre behind our building.");
                int cost = 15;
                if (model.getParty().getGold() < cost) {
                    leaderSay("I'm sad to say, we can't afford it.");
                    coordinatorSay("Oh... I see. How about 5 gold instead?");
                    cost = 5;
                }
                if (model.getParty().getGold() < cost) {
                    leaderSay("No... it's rather embarrassing, but even that would be too much.");
                    coordinatorSay("I'm sorry. I can't really go any lower, we have to pay our instructors you know.");
                    leaderSay("I understand. We'll come back some other time when we've earned a little more gold.");
                    return model.getCurrentHex().getDailyActionState(model);
                }
                print("Do you attend the course? (Y/N) ");
                if (yesNoInput()) {
                    model.getParty().addToGold(-cost);
                    SubView subView = model.getSubView();
                    setCurrentTerrainSubview(model);
                    print("You attend the course, which is held by a group of " + cls.getFullName().toLowerCase() + "s. " +
                            "You actually learn quite a lot. You now have the opportunity to become one yourself, ");
                    ChangeClassEvent change = new ChangeClassEvent(model, cls);
                    change.areYouInterested(model);
                    model.setTimeOfDay(TimeOfDay.EVENING);
                    CollapsingTransition.transition(model, subView);
                }
            }
            return model.getCurrentHex().getDailyActionState(model);
        }

        private void coordinatorSay(String s) {
            println("Course Coordinator: \"" + s + "\"");
        }
    }
}
