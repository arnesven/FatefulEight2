package model.states;

import model.*;
import model.characters.*;
import model.headquarters.TransferCharacterHeadquartersAction;
import model.items.InventoryDummyItem;
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

    private final SteppingMatrix<RecruitableCharacter> recruitMatrix;
    private final List<RecruitableCharacter> recruitables = new ArrayList<>();
    private final MyPair<Integer, String> recruitResult;
    private RecruitSubView subView;

    public RecruitState(Model model) {
        super(model);
        List<GameCharacter> candidates = new ArrayList<>(model.getAllCharacters());
        candidates.removeAll(model.getParty().getPartyMembers());
        candidates.removeIf(gc -> MyLists.any(model.getLingeringRecruitables(), rgc -> rgc.getCharacter() == gc));
        recruitables.addAll(MyLists.transform(candidates, RecruitableCharacter::new));

        recruitResult = rollOnRecruitTable(model);
        recruitResult.first = Math.max(0, recruitResult.first - model.getLingeringRecruitables().size());
        Collections.shuffle(recruitables);
        while (recruitables.size() > recruitResult.first) {
            recruitables.removeFirst();
        }

        setRandomLevels(model, recruitables);
        recruitables.addAll(model.getLingeringRecruitables());
        recruitMatrix = new SteppingMatrix<>(2, 3);
        recruitMatrix.addElements(recruitables);
        model.getParty().setRecruitmentPersistence(recruitables);
        assertNoDuplicates(model);
    }

    public RecruitState(Model model, List<RecruitableCharacter> preSelectedRecruitables) {
        super(model);
        recruitables.addAll(preSelectedRecruitables);
        model.getParty().setRecruitmentPersistence(recruitables);
        recruitMatrix = new SteppingMatrix<>(2, 3);
        recruitMatrix.addElements(recruitables);
        recruitResult = new MyPair<>(preSelectedRecruitables.size(), "");
        assertNoDuplicates(model);
    }

    private void assertNoDuplicates(Model model) {
        if (MyLists.any(recruitables, rgc -> model.getParty().getPartyMembers().contains(rgc.getCharacter()))) {
            System.err.println("Lingering recruitables:");
            for (RecruitableCharacter rgc : model.getLingeringRecruitables()) {
                System.err.println(rgc.getCharacter().getName());
            }
            System.err.println("Character Persistance:");
            for (RecruitableCharacter rgc : model.getParty().getRecruitmentPersistence()) {
                System.err.println(rgc.getCharacter().getName());
            }
            System.err.println("All Characters:");
            for (GameCharacter gc : model.getAllCharacters()) {
                System.err.println(gc.getName());
            }
            throw new IllegalStateException("Party member appeared as recruitable!");
        }
    }

    public static void setRandomLevels(Model model, List<RecruitableCharacter> recruitables) {
        double partyLevel = MyLists.doubleAccumulate(model.getParty().getPartyMembers(),
                                                     GameCharacter::getLevel);
        partyLevel /= model.getParty().size();
        partyLevel = Math.ceil(partyLevel);
        double finalPartyLevel = partyLevel;
        MyLists.forEach(recruitables, (RecruitableCharacter rgc) -> rgc.setLevel(MyRandom.randInt(1, (int) finalPartyLevel)));
    }

    @Override
    public GameState run(Model model) {
        model.getTutorial().recruit(model);
        if (!recruitables.isEmpty()) {
            recruitFromView(model);
        } else {
            if (recruitResult.second.isEmpty()) {
                println("You spend some time asking around, but there aren't any adventurers to recruit.");
            } else {
                println("There are some adventurers here, but they are unwilling to join because '" +
                        recruitResult.second + "'.");
            }
        }
        return new EveningState(model);
    }

    private void recruitFromView(Model model) {
        this.subView = new RecruitSubView(recruitMatrix);
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
                    if (recruitables.isEmpty()) {
                        break;
                    }
                }
            }
        } while (true);
        model.getLingeringRecruitables().clear();
        for (RecruitableCharacter rgc : recruitMatrix.getElementList()) {
            if (!model.getLingeringRecruitables().contains(rgc)) {
                model.getLingeringRecruitables().add(rgc);
            }
        }
    }

    private void talkToCharacter(Model model) {
        RecruitableCharacter current = recruitMatrix.getSelectedElement();
        current.talkTo(model, this);
        for (RecruitableCharacter rgc : recruitables) {
            if (rgc != current) {
                rgc.increaseAnnoyance();
            }
        }
        for (RecruitableCharacter rgc : recruitables) {
            if (rgc != current && rgc.noLongerWantsToJoin()) {
                candidateSay(rgc, MyRandom.sample(List.of("This is taking forever... I'm out of here.",
                        "I've got better things to do. See ya.", "That's it. I'm out.",
                        "You know what? I've changed my mind.", "Too much talk, not enough action.")));
                model.getLog().waitForAnimationToFinish();
                println(rgc.getCharacter().getName() + " no longer wants to join the party.");
                subView.clearAnimationsFor(rgc.getCharacter());
                recruitables.remove(rgc);
                recruitMatrix.remove(rgc);
                return;
            }
        }
        for (RecruitableCharacter rgc : recruitables) {
            if (rgc != current && rgc.isGettingImpatient()) {
                candidateSay(rgc, MyRandom.sample(List.of("This is taking forever...",
                        "Bla bla bla...", "Come on, just hire me already.",
                        "Hey. What about me?.", "Too much talk, when can we go?.")));
                return;
            }
        }
    }

    private void recruitSelectedCharacter(Model model) {
        if (model.getParty().isFull()) {
            println("Your party is currently full. You must dismiss party members to recruit new ones.");
        } else {
            RecruitableCharacter rgc = recruitMatrix.getSelectedElement();
            GameCharacter gc = rgc.getCharacter();
            String name = gc.getName();
            String firstName = gc.getFirstName();
            if (rgc.getInfo() == RecruitInfo.none) {
                name = "... uhm, who ever you are";
                firstName = MyRandom.sample(List.of("buddy", "friend", "stranger"));
            } else {
                System.out.println("rgc.getInfo() = " + rgc.getInfo().name());
            }
            leaderSay(MyRandom.sample(List.of("Want to join the party " + name + "?",
                    "Welcome aboard " + name + "! If you're still interested.",
                    "Are you in, " + firstName + "?",
                    "The jobs yours " + firstName + ". Do you want it?")));
            if (rgc.getInfo() == RecruitInfo.none) {
                candidateSay(rgc, "I do. And my name is " + rgc.getCharacter().getName() + ".");
            } else {
                newPartyMemberComment(rgc);
            }
            model.getLog().waitForAnimationToFinish();
            subView.clearAnimationsFor(rgc.getCharacter());
            recruitMatrix.remove(rgc);
            recruitables.remove(rgc);
            println("You recruited " + gc.getFullName() + "!");
            model.getParty().recruit(gc, model.getDay());
            model.getAllCharacters().remove(gc);
            int amount = rgc.getStartingGold();
            if (amount != 0) {
                model.getParty().goldTransaction(amount);
                println(gc.getName() + " contributed " + amount + " gold to the party's collective purse.");
            }
            if (!gc.getCharClass().getStartingItems().isEmpty()) {
                Item it = rgc.getStartingItem();
                ChooseStartingCharacterState.addSelectedItem(model, gc, it);
                if (!gc.getEquipment().contains(it)) {
                    println(gc.getName() + " contributed " + makeStartingItemString(it) + " to the party.");
                }
            }
            model.getTutorial().leader(model);
        }
    }

    public static String makeStartingItemString(Item it) {
        if (it instanceof InventoryDummyItem) {
            return ((InventoryDummyItem)it).getDescription();
        }
        return Item.getNameWithArticle(it);
    }

    private void newPartyMemberComment(RecruitableCharacter rgc) {
        GameCharacter gc = rgc.getCharacter();
        if (gc.hasPersonality(PersonalityTrait.narcissistic) || gc.hasPersonality(PersonalityTrait.snobby)) {
            candidateSay(rgc, "You should be honored to have me. Okay.");
        } else if (gc.hasPersonality(PersonalityTrait.intellectual)) {
            candidateSay(rgc, "Of course. It will be a good learning experience for me.");
        } else if (gc.hasPersonality(PersonalityTrait.critical)) {
            candidateSay(rgc, "Hmm... Yes, for now.");
        } else if (gc.hasPersonality(PersonalityTrait.encouraging) || gc.hasPersonality(PersonalityTrait.brave)) {
            candidateSay(rgc, "Yes, this is so exciting!");
        } else if (gc.hasPersonality(PersonalityTrait.diplomatic) || gc.hasPersonality(PersonalityTrait.calm)) {
            candidateSay(rgc, "Yes, it is a good deal for both of us.");
        } else if (gc.hasPersonality(PersonalityTrait.friendly) || gc.hasPersonality(PersonalityTrait.benevolent)) {
            candidateSay(rgc, "Absolutely. It's good to meet you.");
        } else if (gc.hasPersonality(PersonalityTrait.greedy) || gc.hasPersonality(PersonalityTrait.stingy)) {
            candidateSay(rgc, "Okay. When do I get my wages?");
        } else if (gc.hasPersonality(PersonalityTrait.unkind) || gc.hasPersonality(PersonalityTrait.rude)) {
            candidateSay(rgc, "Reluctantly, yes.");
        } else if (gc.hasPersonality(PersonalityTrait.playful) || gc.hasPersonality(PersonalityTrait.jovial)) {
            candidateSay(rgc, "Hooray, I finally get to go into the wilderness!");
        } else {
            candidateSay(rgc, MyRandom.sample(List.of("Yes.", "I shall.", "I accept.")));
        }
    }

    public void candidateSay(RecruitableCharacter gc, String s) {
        getModel().getLog().waitForAnimationToFinish();
        subView.characterSay(gc, s);
        printQuote(gc.getCharacter().getName(), s);
    }

    private void dismiss(Model model) {
        model.setInCombat(true);
        print("Which party member do you wish to dismiss? ");
        model.getTutorial().dismiss(model);
        GameCharacter toDismiss = model.getParty().partyMemberInput(model, this, null);
        if (model.getParty().getHeadquarters() != null && TransferCharacterHeadquartersAction.canDoDropOff(model) && model.isInOriginalWorld()) {
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
        if (GameState.partyIsCreepy(model)) {
            sum -= 3;
            texts.add("party too creepy");
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

    public void goToDismiss(Model model) {
        dismiss(model);
    }

    public List<RecruitableCharacter> getRecruitables() {
        return recruitables;
    }
}
