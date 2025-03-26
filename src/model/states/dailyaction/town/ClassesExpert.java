package model.states.dailyaction.town;

import model.Model;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.states.GameState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.town.CareerOfficePersonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassesExpert extends CareerOfficePersonNode {

    public ClassesExpert() {
        super("Classes Expert");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new ClassesExpertState(model);
    }


    private static class ClassesExpertState extends GameState {
        public ClassesExpertState(Model model) {
            super(model);
        }

        @Override
        public GameState run(Model model) {
            expertSay("I can tell you more about classes. Which class would you like to know more about?");
            List<String> options = new ArrayList<>();
            Map<String, CharacterClass> classMap = new HashMap<>();
            for (CharacterClass cls : Classes.allClasses) {
                if (cls != Classes.None) {
                    options.add(cls.getFullName());
                    classMap.put(cls.getFullName(), cls);
                }
            }
            int selected = multipleOptionArrowMenu(model, 30, 6, options);

            CharacterClass selectedClass = classMap.get(options.get(selected));
            expertSay(selectedClass.getDescription());
            leaderSay("Where or how could one learn to become a " + selectedClass.getFullName().toLowerCase() + "?");
            expertSay("We occasionally hold courses, workshops and seminar here at the career office. " +
                    "When we are able of course. We can only give them when we have some real " +
                    selectedClass.getFullName().toLowerCase() + "s to host them.");
            expertSay("But you can learn how to become this class in other ways too. " + selectedClass.getHowToLearn());

            return model.getCurrentHex().getDailyActionState(model);
        }

        private void expertSay(String s) {
            printQuote("Expert", s);
        }
    }
}
