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
import view.subviews.ArrowMenuSubView;
import view.subviews.RecruitSubView;
import view.subviews.SubView;

import java.awt.*;
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
        setLevels(recruitables);
        recruitMatrix = new SteppingMatrix<>(2, 3);
        recruitMatrix.addElements(recruitables);
        model.getParty().setRecruitmentPersistence(recruitables);
    }

    private void setLevels(List<GameCharacter> recruitables) {
        double partyLevel = 0.0;
        for (GameCharacter gc : getModel().getParty().getPartyMembers()) {
            partyLevel += gc.getLevel();
        }
        partyLevel /= getModel().getParty().size();
        partyLevel = Math.ceil(partyLevel);
        for (GameCharacter gc : recruitables) {
            gc.setLevel(MyRandom.randInt(1, (int)partyLevel));
        }
    }

    public RecruitState(Model model, List<GameCharacter> preSelectedRecruitables) {
        super(model);
        recruitables.addAll(preSelectedRecruitables);
        model.getParty().setRecruitmentPersistence(recruitables);
        recruitMatrix = new SteppingMatrix<>(2, 3);
        recruitMatrix.addElements(recruitables);
        recruitResult = new MyPair<>(preSelectedRecruitables.size(), "");
    }

    @Override
    public GameState run(Model model) {
        model.getTutorial().recruit(model);

        if (recruitables.size() > 0) {
            RecruitSubView subView = new RecruitSubView(this, recruitMatrix);
            model.setSubView(subView);

            do {
                print("There " + (recruitables.size() == 1 ? "is " : "are ") + noOfRecruitables() +
                        " adventurer" + (recruitables.size() > 0 ? "s" : "") + " interested in joining your party, " +
                        recruitableNames() + ".");
                waitForReturn();

                int topCommand = subView.getTopIndex();
                if (topCommand == 2) { // Exit
                    break;
                }
                if (topCommand == 1) { // Dismiss
                    if (model.getParty().size() > 1){
                        dismiss(model);
                    } else {
                        println("You cannot dismiss your last party member.");
                    }
                } else if (topCommand == -1){ // Selecting in matrix
                    recruitSelectedCharacter(model);
                    if (recruitables.size() == 0) {
                        break;
                    }
                }
            } while (true);
        } else {
            if (recruitResult.second.equals("")) {
                println("You spend some time asking around, but there aren't any adventurers to recruit.");
            } else {
                println("There are some adventurers here, but they are unwilling to join because '" + recruitResult.second + "'.");
            }
        }
        return new EveningState(model);
    }

    private void recruitSelectedCharacter(Model model) {
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
            model.getTutorial().leader(model);
        }
    }

    private void dismiss(Model model) {
        print("Which party member do you wish to dismiss? ");
        GameCharacter toDismiss = model.getParty().partyMemberInput(model, this, null);
        int goldLost =
                toDismiss.getCharClass().getStartingGold() + toDismiss.getLevel()*5 + 5;
        if (goldLost > model.getParty().getGold()) {
            print("Since you do not have " + goldLost + " to pay, " + toDismiss.getFullName() + " will keep all equipment." +
                    " Are you sure you want to dismiss " + toDismiss.getFirstName() + " (Y/N)? ");
            if (yesNoInput()) {
                model.getParty().remove(toDismiss, false, false, 0);
                println(toDismiss.getFullName() + " left the party.");
            }
        } else {
            print(toDismiss.getFullName() + " will return all equipment and claim " + goldLost + " from the party's purse." +
                    " Are you sure you want to dismiss " + toDismiss.getFirstName() + " (Y/N)? ");
            if (yesNoInput()) {
                model.getParty().remove(toDismiss, true, true, goldLost);
                println(toDismiss.getFullName() + " left the party.");
            }
        }
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

        double averageLevel = calculateAverageLevel(model);
        if (averageLevel < 2.0) {
            sum -= 1;
            texts.add("party too inexperienced");
        }

        if (model.getDay() - model.getParty().getLastSuccessfulRecruitDay() < 7) {
            sum -= 2;
            texts.add("");
        }
        if (texts.isEmpty()) {
            texts.add("");
        }
        return new MyPair<>(sum, MyRandom.sample(texts));
    }

    public static double calculateAverageLevel(Model model) {
        double averageLevel = 0.0;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            averageLevel += gc.getLevel();
        }
        averageLevel /= model.getParty().size();
        return averageLevel;
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

    public boolean isStartingGoldEnabled() {
        return this.startingGold;
    }
}
