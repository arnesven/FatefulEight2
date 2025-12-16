package model.states;

import model.Model;
import model.Party;
import model.SteppingMatrix;
import model.characters.*;
import model.classes.CharacterClass;
import model.headquarters.TransferCharacterHeadquartersAction;
import model.items.HorseStartingItem;
import model.items.Item;
import model.items.spells.Spell;
import model.map.UrbanLocation;
import model.races.Dwarf;
import model.races.ElvenRace;
import model.races.HalfOrc;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import util.MyStrings;
import view.subviews.RecruitSubView;

import java.awt.*;
import java.util.*;
import java.util.List;

public class RecruitState extends GameState {

    private SteppingMatrix<GameCharacter> recruitMatrix;
    private List<GameCharacter> recruitables = new ArrayList<>();
    private MyPair<Integer, String> recruitResult;
    private Map<GameCharacter, Integer> startingGoldMap;
    private Map<GameCharacter, Item> startingItemMap;
    private boolean startingGold = true;
    private RecruitSubView subView;

    public RecruitState(Model model) {
        super(model);
        recruitables.addAll(model.getAllCharacters());
        recruitables.removeAll(model.getParty().getPartyMembers());
        recruitables.removeAll(model.getLingeringRecruitables());
        recruitResult = rollOnRecruitTable(model);
        recruitResult.first = Math.max(0, recruitResult.first - model.getLingeringRecruitables().size());

        Collections.shuffle(recruitables);
        while (recruitables.size() > recruitResult.first) {
            recruitables.remove(0);
        }
        setRandomClasses(recruitables);
        setLevels(recruitables);
        recruitables.addAll(model.getLingeringRecruitables());
        setGoldAndItems(recruitables);
        recruitMatrix = new SteppingMatrix<>(2, 3);
        recruitMatrix.addElements(recruitables);
        model.getParty().setRecruitmentPersistence(recruitables);
    }

    public RecruitState(Model model, List<GameCharacter> preSelectedRecruitables) {
        super(model);
        recruitables.addAll(preSelectedRecruitables);
        model.getParty().setRecruitmentPersistence(recruitables);
        recruitMatrix = new SteppingMatrix<>(2, 3);
        recruitMatrix.addElements(recruitables);
        recruitResult = new MyPair<>(preSelectedRecruitables.size(), "");
        setGoldAndItems(preSelectedRecruitables);
    }

    private void setGoldAndItems(List<GameCharacter> recruitables) {
        startingGoldMap = new HashMap<>();
        startingItemMap = new HashMap<>();
        for (GameCharacter gc : recruitables) {
            int amount = Math.max(0, MyRandom.randInt(gc.getCharClass().getStartingGold()-10,
                                                      gc.getCharClass().getStartingGold()));
            startingGoldMap.put(gc, amount);
            startingItemMap.put(gc, MyRandom.sample(gc.getCharClass().getStartingItems()).copy());
        }
    }

    public void setStartingGold(GameCharacter gc, int amount) {
        startingGoldMap.put(gc, amount);
    }

    private void setLevels(List<GameCharacter> recruitables) {
        double partyLevel = MyLists.doubleAccumulate(getModel().getParty().getPartyMembers(),
                                                     GameCharacter::getLevel);
        partyLevel /= getModel().getParty().size();
        partyLevel = Math.ceil(partyLevel);
        double finalPartyLevel = partyLevel;
        MyLists.forEach(recruitables, (GameCharacter gc) -> gc.setLevel(MyRandom.randInt(1, (int) finalPartyLevel)));
    }

    @Override
    public GameState run(Model model) {
        model.getTutorial().recruit(model);
        if (GameState.partyIsCreepy(model)) {
            println("There are some adventurers here, but they are unwilling to join because 'party to creepy'.");
            return new EveningState(model);
        }

        if (recruitables.size() > 0) {
            recruitFromView(model);
        } else {
            if (recruitResult.second.equals("")) {
                println("You spend some time asking around, but there aren't any adventurers to recruit.");
            } else {
                println("There are some adventurers here, but they are unwilling to join because '" +
                        recruitResult.second + "'.");
            }
        }
        return new EveningState(model);
    }

    private void recruitFromView(Model model) {
        this.subView = new RecruitSubView(this, recruitMatrix, startingGoldMap);
        model.setSubView(subView);
        print("There " + (recruitables.size() == 1 ? "is " : "are ") + noOfRecruitables() +
                " adventurer" + (!recruitables.isEmpty() ? "s" : "") + " interested in joining your party.");
        do {
            waitForReturnSilently();
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
            } else if (topCommand == -1) { // Selecting in matrix
                Point p = subView.getCursorPosition();
                int choice = multipleOptionArrowMenu(model, p.x, p.y+5, List.of("Talk", "Recruit", "Cancel"));
                if (choice == 0) {
                    talkToCharacter(model);
                } else if (choice == 1) {
                    recruitSelectedCharacter(model);
                    if (recruitables.size() == 0) {
                        break;
                    }
                }
            }
        } while (true);
        for (GameCharacter gc : recruitMatrix.getElementList()) {
            if (!model.getLingeringRecruitables().contains(gc)) {
                model.getLingeringRecruitables().add(gc);
            }
        }
    }

    private void talkToCharacter(Model model) {
        GameCharacter selected = recruitMatrix.getSelectedElement();
        int knownInfo = subView.getKnownInfo(selected);
        if (knownInfo == 0) {
            leaderSay("Who are you?");
            candidateSay(selected, "I'm " + selected.getName() + ".");
            subView.improveKnownInfo(selected);
        } else if (knownInfo == 1) {
            leaderSay("What is your profession?");
            candidateSay(selected, "I'm a level " + MyStrings.numberWord(selected.getLevel()) + " " +
                    selected.getCharClass().getFullName() + ".");
            subView.improveKnownInfo(selected);
        } else if (knownInfo == 2) {
            leaderSay("What are your qualifications?");
            List<CharacterClass> classList = new ArrayList<>(Arrays.asList(selected.getClasses()));
            classList.remove(selected.getCharClass());
            candidateSay(selected, "I have previously worked as " + MyLists.commaAndJoin(classList,
                    cls -> MyStrings.aOrAn(cls.getFullName()) + " " + cls.getFullName()) + ".");
            subView.improveKnownInfo(selected);
        } else if (knownInfo == 3) {
            leaderSay("Would you contribute any gold or equipment to the party if you joined?");
            String conjunc = "";
            String firstPart = "";
            if (startingGoldMap.containsKey(selected) && startingGoldMap.get(selected) > 0) {
                conjunc = "And";
                if (selected.hasPersonality(PersonalityTrait.generous)) {
                    firstPart = "Of course, " + startingGoldMap.get(selected) + " gold. " +
                            "That's all I have.";
                } else {
                    firstPart = "Yes, " + startingGoldMap.get(selected) + " gold.";
                }
            } else {
                conjunc = "But";
                if (selected.hasPersonality(PersonalityTrait.stingy)) {
                    firstPart = "No way. You're not getting my money!";
                } else {
                    firstPart = "No. Sorry.";
                }
            }
            String startItemString = makeStartingItemString(startingItemMap.get(selected));
            candidateSay(selected, firstPart + " " + conjunc +
                    " I have " + MyStrings.aOrAn(startItemString) + " " + startItemString + ".");
            subView.improveKnownInfo(selected);
        } else {
            leaderSay(MyRandom.sample(List.of("Are you ready to be an adventurer?",
                    "So you think you're up for this?", "Are you a dependable party member?",
                    "Are you a team player?", "Do you get along well with others?",
                    "Do you do well in stressful situations?", "Are you ambitious?",
                    "Are you a good fighter?", "You think you can contribute to this party?",
                    "Can you follow orders?")));
            candidateSay(selected, MyRandom.sample(List.of("Definitely.", "I think so.",
                    "Absolutely.", "Of course.", "That's me.", "Yes.", "No doubt about it.")));
        }
    }

    private void recruitSelectedCharacter(Model model) {
        if (model.getParty().isFull()) {
            println("Your party is currently full. You must dismiss party members to recruit new ones.");
        } else {
            GameCharacter gc = recruitMatrix.getSelectedElement();
            leaderSay("Want to join the party " + gc.getName() + "?");
            newPartyMemberComment(gc);
            model.getLog().waitForAnimationToFinish();
            recruitMatrix.remove(gc);
            recruitables.remove(gc);
            println("You recruited " + gc.getFullName() + "!");
            model.getParty().recruit(gc, model.getDay());
            model.getAllCharacters().remove(gc);
            int amount = startingGoldMap.get(gc);
            if (startingGold && amount != 0) {
                model.getParty().goldTransaction(amount);
                println(gc.getName() + " contributed " + amount + " gold to the party's collective purse.");
            }
            if (!gc.getCharClass().getStartingItems().isEmpty()) {
                Item it = startingItemMap.get(gc);
                ChooseStartingCharacterState.addSelectedItem(model, gc, it);
                if (!gc.getEquipment().contains(it)) {
                    println(gc.getName() + " contributed a " + makeStartingItemString(it) + " to the party.");
                }
            }
            model.getTutorial().leader(model);
        }
    }

    private String makeStartingItemString(Item it) {
        String extra = "";
        if (it instanceof Spell) {
            extra = "spell, ";
        }
        if (it instanceof HorseStartingItem) {
            extra = "horse, ";
        }
        return extra + it.getName();
    }

    private void newPartyMemberComment(GameCharacter gc) {
        if (gc.hasPersonality(PersonalityTrait.narcissistic) || gc.hasPersonality(PersonalityTrait.snobby)) {
            candidateSay(gc, "You should be honored to have me. Okay.");
        } else if (gc.hasPersonality(PersonalityTrait.intellectual)) {
            candidateSay(gc, "Of course. It will be a good learning experience for me.");
        } else if (gc.hasPersonality(PersonalityTrait.critical)) {
            candidateSay(gc, "Hmm... Yes, for now.");
        } else if (gc.hasPersonality(PersonalityTrait.encouraging) || gc.hasPersonality(PersonalityTrait.brave)) {
            candidateSay(gc, "Yes, this is so exciting!");
        } else if (gc.hasPersonality(PersonalityTrait.diplomatic) || gc.hasPersonality(PersonalityTrait.calm)) {
            candidateSay(gc, "Yes, it is a good deal for both of us.");
        } else if (gc.hasPersonality(PersonalityTrait.friendly) || gc.hasPersonality(PersonalityTrait.benevolent)) {
            candidateSay(gc, "Absolutely. It's good to meet you.");
        } else if (gc.hasPersonality(PersonalityTrait.greedy) || gc.hasPersonality(PersonalityTrait.stingy)) {
            candidateSay(gc, "Okay. When do I get my wages?");
        } else if (gc.hasPersonality(PersonalityTrait.unkind) || gc.hasPersonality(PersonalityTrait.rude)) {
            candidateSay(gc, "Reluctantly, yes.");
        } else if (gc.hasPersonality(PersonalityTrait.playful) || gc.hasPersonality(PersonalityTrait.jovial)) {
            candidateSay(gc, "Hooray, I finally get to go into the wilderness!");
        } else {
            candidateSay(gc, MyRandom.sample(List.of("Yes.", "I shall.", "I accept.")));
        }
    }

    private void candidateSay(GameCharacter gc, String s) {
        getModel().getLog().waitForAnimationToFinish();
        subView.characterSay(gc, s);
        printQuote(gc.getName(), s);
    }

    private void dismiss(Model model) {
        model.setInCombat(true);
        print("Which party member do you wish to dismiss? ");
        model.getTutorial().dismiss(model);
        GameCharacter toDismiss = model.getParty().partyMemberInput(model, this, null);
        if (model.getParty().getHeadquarters() != null && TransferCharacterHeadquartersAction.canDoDropOff(model)) {
            print("Do you wish to dismiss " + toDismiss.getName() + " permanently (Y) or send " +
                    himOrHer(toDismiss.getGender()) + " to your headquarters (N) in " + model.getParty().getHeadquarters().getLocationName() + "? ");
            if (!yesNoInput()) {
                leaderSay(toDismiss.getFirstName() + ", I want you to go back to headquarters and stay there until further notice.");
                partyMemberSay(toDismiss, MyRandom.sample(List.of("If you say so.", "If you think that's best", "Okay, it's your call.",
                        "Fair enough.", "I understand.")));
                TransferCharacterHeadquartersAction.dropOffAtHeadquarters(model, this, toDismiss);
                model.setInCombat(false);
                return;
            }
        }

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
        model.setInCombat(false);
    }

    private void setRandomClasses(List<GameCharacter> recruitables) {
        MyLists.forEach(recruitables, GameCharacter::setRandomStartingClass);
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
                gameCharacter -> gameCharacter.getRace() instanceof HalfOrc);
    }

    private boolean partyHasADwarf(Party party) {
        return party.getPartyMembers().stream().anyMatch(
                gameCharacter -> gameCharacter.getRace() instanceof Dwarf);
    }

    private boolean partyHasAnElf(Party party) {
        return party.getPartyMembers().stream().anyMatch(
                gameCharacter -> gameCharacter.getRace() instanceof ElvenRace);
    }

    private MyPair<Integer, String> getModifiers(Model model) {
        List<String> texts = new ArrayList<>();
        int sum = 0;
        int roundIncr = MyRandom.randInt(4);
        if (roundIncr > 0 && model.getParty().getGold() >= roundIncr) {
            print("Offer to buy the next round (costs " + roundIncr + " gold)? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().spendGold(roundIncr);
                sum += roundIncr;
            }
        }
        if (model.getCurrentHex().getLocation() instanceof UrbanLocation) {
            sum += 1;
        }
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

    public void goToDismiss(Model model) {
        dismiss(model);
    }

    public List<GameCharacter> getRecruitables() {
        return recruitables;
    }
}
