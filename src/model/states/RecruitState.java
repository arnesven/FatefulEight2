package model.states;

import model.Model;
import model.Party;
import model.SteppingMatrix;
import model.characters.*;
import model.races.Dwarf;
import model.races.ElvenRace;
import model.races.HalfOrc;
import model.tutorial.TutorialHandler;
import util.MyPair;
import util.MyRandom;
import view.subviews.RecruitSubView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class RecruitState extends GameState {

    private SteppingMatrix<GameCharacter> recruitMatrix;
    private List<GameCharacter> recruitables = new ArrayList<>();
    private MyPair<Integer, String> recruitResult;
    private boolean startingGold = true;

    public RecruitState(Model model) {
        super(model);
        recruitables.addAll(model.getAllCharacters());
        recruitables.removeAll(model.getParty().getPartyMembers());

        recruitResult = rollOnRecruitTable(model);
        Collections.shuffle(recruitables);
        while (recruitables.size() > recruitResult.first) {
            recruitables.remove(0);
        }
        setRandomClasses(recruitables);
        // TODO: Set levels on recruitables
        recruitMatrix = new SteppingMatrix<>(2, 3);
        recruitMatrix.addElements(recruitables);
    }

    public RecruitState(Model model, List<GameCharacter> preSelectedRecruitables) {
        super(model);
        recruitables.addAll(preSelectedRecruitables);
        recruitMatrix = new SteppingMatrix<>(2, 3);
        recruitMatrix.addElements(recruitables);
    }

    @Override
    public GameState run(Model model) {
        model.getTutorial().recruit(model);

        if (recruitables.size() > 0) {
            model.setSubView(new RecruitSubView(recruitMatrix));
            String dismiss = "Dismiss (D) ";
            if (model.getParty().size() == 1) {
                dismiss = "";
            }
            print("There " + (recruitables.size() == 1 ? "is " : "are ") + noOfRecruitables() +
                    " adventurer" + (recruitables.size() > 0 ? "s" : "") + " interested in joining your party, " +
                    recruitableNames() + ". " +
                    "Do you want to recruit (R), " + dismiss + "or are you done (Q)? ");
            do {
                char selectedAction = lineInput().toUpperCase().charAt(0);
                if (selectedAction == 'R') {
                    if (model.getParty().isFull()) {
                        println("Your party is currently full. You must dismiss party members to recruit new ones.");
                    } else {
                        GameCharacter gc = recruitMatrix.getSelectedElement();
                        recruitMatrix.remove(gc);
                        recruitables.remove(gc);
                        println("You recruited " + gc.getFullName() + "!");
                        model.getParty().recruit(gc, model.getDay());
                        model.getAllCharacters().remove(gc);
                        int amount = gc.getCharClass().getStartingGold();
                        if (startingGold && amount != 0) {
                            model.getParty().addToGold(amount);
                            println(gc.getName() + " contributed " + amount + " gold to the party's collective purse.");
                        }
                        if (recruitables.size() == 0) {
                            break;
                        }
                    }
                } else if (selectedAction == 'D' && model.getParty().size() > 1) {
                    print("Which party member do you wish to dismiss? ");
                    GameCharacter toDismiss = model.getParty().partyMemberInput(model, this, null);
                    int goldLost = Math.min(model.getParty().getGold(),
                            toDismiss.getCharClass().getStartingGold() + toDismiss.getLevel()*5);
                    print(toDismiss.getFullName() + " will return all equipment and claim " + goldLost + " from the party's purse." +
                            " Are you sure you want to dismiss " + toDismiss.getFirstName() + " (Y/N)?");
                    if (yesNoInput()) {
                        model.getParty().remove(toDismiss, true, true, goldLost);
                        println(toDismiss.getFullName() + " left the party.");
                    }
                } else if (selectedAction == 'Q') {
                    break;
                }
                print("There " + (recruitables.size() == 1 ? "is " : "are ") + "still " + noOfRecruitables() +
                        " adventurer" + (recruitables.size() > 0 ? "s" : "") + " interested in joining your party. " +
                        "Do you want to recruit (R), dismiss (D) or are you done (Q)? ");
            } while (true);
        } else {
            if (recruitResult.second.equals("")) {
                println("You spend some time asking around, but there aren't any adventurers to recruit.");
            } else {
                println("There are some adventurers here, but they are unwilling to join because \"" + recruitResult.second + "\".");
            }
        }
        return new EveningState(model);
    }

    private void setRandomClasses(List<GameCharacter> recruitables) {
        for (GameCharacter gc : recruitables) {
            gc.setRandomStartingClass();
        }
    }

    private MyPair<Integer, String> rollOnRecruitTable(Model model) {
        int d10Roll = MyRandom.rollD10();
        MyPair<Integer, String> modifier = getModifiers(model);
        int result = d10Roll + modifier.first;
        if (result >= 5) {
            return new MyPair<>(Math.min(result-4, 6), "");
        }
        if (result == 4) {
            if (partyHasAnElf(model.getParty())) {
                return new MyPair<>(0, "party has too many elves");
            }
            return new MyPair<>(1, "");
        }
        if (result == 3) {
            if (partyHasADwarf(model.getParty())) {
                return new MyPair<>(0, "party has too many dwarves");
            }
            return new MyPair<>(1, "");
        }
        if (result > 1) {
            if (partyHasAHalfOrc(model.getParty())) {
                return new MyPair<>(0, "party has too many half-orcs");
            }
            return new MyPair<>(1, "");
        }
        return new MyPair<>(0, modifier.second);

    }

    private boolean partyHasAHalfOrc(Party party) {
        return party.getPartyMembers().stream().anyMatch(
                (Predicate<GameCharacter>) gameCharacter -> gameCharacter.getRace() instanceof HalfOrc);
    }

    private boolean partyHasADwarf(Party party) {
        return party.getPartyMembers().stream().anyMatch(
                (Predicate<GameCharacter>) gameCharacter -> gameCharacter.getRace() instanceof Dwarf);
    }

    private boolean partyHasAnElf(Party party) {
        return party.getPartyMembers().stream().anyMatch(
                (Predicate<GameCharacter>) gameCharacter -> gameCharacter.getRace() instanceof ElvenRace);
    }

    private MyPair<Integer, String> getModifiers(Model model) {
        List<String> texts = new ArrayList<>();
        int sum = 0;
        if (model.getParty().size() < 2) {
            sum -= 1;
            texts.add("party not big enough");
        }
        if (model.getParty().size() < 3) {
            sum -= 1;
        }

        if (model.getParty().size() > 7) {
            sum -= 1;
        }
        if (model.getParty().size() > 6) {
            sum -= 1;
            texts.add("party already too big");
        }

        double averageLevel = 0.0;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            averageLevel += gc.getLevel();
        }
        averageLevel /= model.getParty().size();
        if (averageLevel < 2.0) {
            sum -= 1;
            texts.add("party too inexperienced");
        }

        if (model.getDay() - model.getParty().getLastSuccessfulRecruitDay() < 7) {
            sum -= 1;
            texts.add("");
        }
        return new MyPair<>(sum, MyRandom.sample(texts));
    }

    private String recruitableNames() {
          StringBuilder bldr = new StringBuilder();
          int i = 0;
          for (GameCharacter gc : recruitables) {
              bldr.append(gc.getFullName() + (i+2==recruitables.size()?" and ": ", "));
              i++;
          }
          return bldr.substring(0, bldr.length()-2);
    }

    private String noOfRecruitables() {
        String[] words = new String[]{"zero", "one", "two", "three", "four", "five", "six"};
        return words[recruitables.size()];
    }

    public void setStartingGoldEnabled(boolean b) {
        this.startingGold = b;
    }
}
