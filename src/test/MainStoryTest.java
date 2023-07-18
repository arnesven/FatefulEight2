package test;

import model.MainStory;
import model.Model;
import model.characters.GameCharacter;
import model.characters.KruskTalandro;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.journal.JournalEntry;
import model.journal.RuinsEntry;
import model.quests.FrogmenProblemQuest;
import model.quests.SpecialDeliveryQuest;
import model.races.Race;
import util.MyPair;
import util.MyUnitTesting;

import java.util.ArrayList;
import java.util.List;

public class MainStoryTest {

    private abstract static class Parameter {
        String name;
        public Parameter(String name) {
            this.name = name;
        }
        public abstract void stimuli(Model model);
        public abstract void updateExpected(Model model, List<MyPair<String, String>> expected);
    }

    private static final Parameter[] PARAMETERS = new Parameter[] {
            new Parameter("01 - setupStory") {
                @Override
                public void stimuli(Model model) { }

                @Override
                public void updateExpected(Model model, List<MyPair<String, String>> expected) {
                    expected.add(new MyPair<>("Dummy's Uncle", "needs a capable group of adventurers"));
                }
            },
            new Parameter("02 - InitialStoryPart, progress once") {
                @Override
                public void stimuli(Model model) {
                    model.getMainStory().getStoryParts().get(0).progress();      // Visit uncle, get Frogmen Problem quest
                }

                @Override
                public void updateExpected(Model model, List<MyPair<String, String>> expected) {
                    expected.add(new MyPair<>("Dummy's Uncle", "Complete the '" + FrogmenProblemQuest.QUEST_NAME));
                }
            },
            new Parameter("03 - InitialStoryPart, progress twice") {
                @Override
                public void stimuli(Model model) {
                    model.getMainStory().getStoryParts().get(0).progress();      // Visit uncle, get Frogmen Problem quest
                    model.getMainStory().getStoryParts().get(0).progress();      // Completes frogmen problem quest
                }

                @Override
                public void updateExpected(Model model, List<MyPair<String, String>> expected) {
                    expected.add(new MyPair<>("Dummy's Uncle", "Return to Dummy's uncle in the"));
                }
            },
            new Parameter("04 - InitialStoryPart, progress three times") {
                @Override
                public void stimuli(Model model) {
                    model.getMainStory().getStoryParts().get(0).progress();     // Visit uncle, get Frogmen Problem quest
                    model.getMainStory().getStoryParts().get(0).progress();     // Completes frogmen problem quest
                    model.getMainStory().getStoryParts().get(0).progress();     // Returns to uncle, get visit Everix task
                    model.getMainStory().getStoryParts().get(0).transitionStep(model, 0); // Gets "Reward at ... Castle" Task
                }

                @Override
                public void updateExpected(Model model, List<MyPair<String, String>> expected) {
                    expected.add(new MyPair<>("Dummy's Uncle", "Ask Everix in the"));
                    expected.add(new MyPair<>("Reward at", "gold, your reward for dealing with the frogmen problem"));
                }
            },
            new Parameter("05 - InitialStoryPart, progress to completion") {
                @Override
                public void stimuli(Model model) {
                    progressInitialPartToCompletion(model);
                }

                @Override
                public void updateExpected(Model model, List<MyPair<String, String>> expected) {
                    expected.add(new MyPair<>("Dummy's Uncle", "You helped Dummy's uncle and the"));
                    expected.add(new MyPair<>("Reward at", "gold, your reward for dealing with the frogmen problem"));
                    expected.add(new MyPair<>("The Witch in the Woods", "Find Everix's acquaintance, the witch, to ask about the crimson pearl."));
                }
            },
            new Parameter("06 - WitchPart, progress once") {
                @Override
                public void stimuli(Model model) {
                    progressInitialPartToCompletion(model);
                    model.getMainStory().getStoryParts().get(2).progress();
                }

                @Override
                public void updateExpected(Model model, List<MyPair<String, String>> expected) {
                    expected.add(new MyPair<>("Dummy's Uncle", "You helped Dummy's uncle and the"));
                    expected.add(new MyPair<>("Reward at", "gold, your reward for dealing with the frogmen problem"));
                    expected.add(new MyPair<>("The Witch in the Woods", "Complete the '" + SpecialDeliveryQuest.QUEST_NAME));
                }
            },
            new Parameter("07 - WitchPart, progress twice") {
                @Override
                public void stimuli(Model model) {
                    progressInitialPartToCompletion(model);
                    model.getMainStory().getStoryParts().get(2).progress();
                    model.getMainStory().getStoryParts().get(2).progress();
                }

                @Override
                public void updateExpected(Model model, List<MyPair<String, String>> expected) {
                    expected.add(new MyPair<>("Dummy's Uncle", "You helped Dummy's uncle and the"));
                    expected.add(new MyPair<>("Reward at", "gold, your reward for dealing with the frogmen problem"));
                    expected.add(new MyPair<>("The Witch in the Woods", "Return to the witch to get information about the Crimson Pearl"));
                }
            },
            new Parameter("08 - WitchPart, progress three times") {
                @Override
                public void stimuli(Model model) {
                    progressInitialPartToCompletion(model);
                    model.getMainStory().getStoryParts().get(2).progress();
                    model.getMainStory().getStoryParts().get(2).progress();
                    model.getMainStory().getStoryParts().get(2).progress();
                }

                @Override
                public void updateExpected(Model model, List<MyPair<String, String>> expected) {
                    expected.add(new MyPair<>("Dummy's Uncle", "You helped Dummy's uncle and the"));
                    expected.add(new MyPair<>("Reward at", "gold, your reward for dealing with the frogmen problem"));
                    expected.add(new MyPair<>("The Witch in the Woods", "You helped the witch in the wood deliver a special potion to her client"));
                }
            }
    };

    private static void progressInitialPartToCompletion(Model model) {
        model.getMainStory().getStoryParts().get(0).progress();     // Visit uncle, get Frogmen Problem quest
        model.getMainStory().getStoryParts().get(0).progress();     // Completes frogmen problem quest
        model.getMainStory().getStoryParts().get(0).progress();     // Returns to uncle, get visit Everix task
        model.getMainStory().getStoryParts().get(0).transitionStep(model, 0); // Gets "Reward at ... Castle" Task
        model.getMainStory().getStoryParts().get(0).progress();     // Visits Everix
        model.getMainStory().getStoryParts().get(0).transitionStep(model, 1); // Gets "Find Witch" Task
    }

    public static void testSuit(Model model) {
        int count = 0;
        for (Parameter param : PARAMETERS) {
            test(model, param);
            count++;
        }
        System.out.println(count + " TESTS PASSED");
    }

    private static void precondition(Model model) {
        model.resetMainStory();
        GameCharacter dummy = new GameCharacter("Dummy", "Delacroix", Race.HALF_ORC, Classes.WIT,
                new KruskTalandro(), new CharacterClass[]{Classes.WIT, Classes.DRU, Classes.MAG, Classes.SOR});
        model.getMainStory().setupStory(dummy);                // Get task "visit uncle"
    }

    private static List<MyPair<String, String>> makeExpected(Model model) {
        List<MyPair<String, String>> expected = new ArrayList<>();
        expected.add(new MyPair<>("Visit a Town", "Visit a town."));
        expected.add(new MyPair<>("Visit a Castle", "Visit a castle."));
        expected.add(new MyPair<>("Visit a Temple", "Visit a temple."));
        RuinsEntry ruins = new RuinsEntry(model);
        expected.add(new MyPair<>(ruins.getName(), ruins.getText()));
        return expected;
    }

    private static void verify(List<MyPair<String, String>> expected, List<JournalEntry> actual, String name) {
        for (JournalEntry entry : actual) {
            int size = expected.size();
            expected.removeIf((MyPair<String, String> pair) -> (entry.getName().contains(pair.first) && entry.getText().contains(pair.second)));
            MyUnitTesting.assertTrue(size > expected.size(), "In Test " + name + ":\n Could not match " + entry);
        }
        MyUnitTesting.assertTrue(expected.isEmpty(), "In Test " + name + ":\n Expected has unmatched entries " + expected);
    }

    public static void test(Model model, Parameter param) {
        precondition(model);
        param.stimuli(model);
//        storyParts.get(0).progress();     // Visit uncle, get Frogmen Problem quest
//        storyParts.get(0).progress();     // Completes frogmen problem quest
//        storyParts.get(0).progress();     // Returns to uncle, get visit Everix task
//        storyParts.get(0).transitionStep(model, 0); // Gets "Reward at ... Castle" Task
//        storyParts.get(0).progress();     // Visits Everix
//        storyParts.get(0).transitionStep(model, 1); // Gets "Find Witch" Task
//
//        storyParts.get(1).progress();     // Visits lord of castle, Gets "Rescue mission" quests
//        storyParts.get(1).progress();     // Completes "Rescue mission quest"
//        storyParts.get(1).progress();     // Visits lord again -> Completed
//
//        storyParts.get(2).progress();     // Visits Witch, Gets "Special Delivery" Quests

        List<MyPair<String, String>> expectedJournalEntries = makeExpected(model);
        param.updateExpected(model, expectedJournalEntries);

        verify(expectedJournalEntries, model.getMainStory().getMainStoryTasks(model), param.name);
    }
}
