package model;

import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.states.RecruitState;
import util.MyLists;
import util.MyRandom;
import util.MyStrings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum RecruitInfo {
    none,
    name,
    profession,
    qualifications,
    goldAndGear,
    all;

    public boolean doRecruitTalk(Model model, RecruitState state, RecruitableCharacter rgc) {
        GameCharacter selected = rgc.getCharacter();
        if (this == none) {
            state.leaderSay("Who are you?");
            state.candidateSay(rgc, "I'm " + selected.getName() + ".");
            return true;
        }

        if (this == name) {
            state.leaderSay("What is your profession?");
            state.candidateSay(rgc, "I'm a level " + MyStrings.numberWord(selected.getLevel()) + " " +
                    selected.getCharClass().getFullName() + ".");
            return true;
        }
        if (this == profession) {
            state.leaderSay("What are your qualifications?");
            List<CharacterClass> classList = new ArrayList<>(Arrays.asList(selected.getClasses()));
            classList.remove(selected.getCharClass());
            classList.removeIf(cls -> cls == Classes.None);
            if (classList.isEmpty()) {
                state.candidateSay(rgc, "I've devoted my entire career to being " +
                        MyStrings.aOrAn(selected.getCharClass().getFullName()) + " " +
                        selected.getCharClass().getFullName() + ". I haven't tried any other fields of work.");
            } else {
                state.candidateSay(rgc, "I have previously worked as " + MyLists.commaAndJoin(classList,
                        cls -> MyStrings.aOrAn(cls.getFullName()) + " " + cls.getFullName()) + ".");
            }
            return true;
        } else if (this == qualifications) {
            state.leaderSay("Would you contribute any gold or equipment to the party if you joined?");
            String conjunc = "";
            String firstPart = "";
            if (rgc.getStartingGold() > 0) {
                conjunc = "And";
                if (selected.hasPersonality(PersonalityTrait.generous)) {
                    firstPart = "Of course, " + rgc.getStartingGold() + " gold. " +
                            "That's all I have.";
                } else {
                    firstPart = "Yes, " + rgc.getStartingGold() + " gold.";
                }
            } else {
                conjunc = "But";
                if (selected.hasPersonality(PersonalityTrait.stingy)) {
                    firstPart = "No way. You're not getting my money!";
                } else {
                    firstPart = "No. Sorry.";
                }
            }
            if (rgc.getStartingItem() != null) {
                String startItemString = RecruitState.makeStartingItemString(rgc.getStartingItem());
                state.candidateSay(rgc, firstPart + " " + conjunc +
                        " I have " + MyStrings.aOrAn(startItemString) + " " + startItemString + ".");
            } else {
                state.candidateSay(rgc, firstPart + ".");
            }
            return true;
        }
        state.leaderSay(MyRandom.sample(List.of("Are you ready to be an adventurer?",
                "So you think you're up for this?", "Are you a dependable party member?",
                "Are you a team player?", "Do you get along well with others?",
                "Do you do well in stressful situations?", "Are you ambitious?",
                "Are you a good fighter?", "You think you can contribute to this party?",
                "Can you follow orders?")));
        state.candidateSay(rgc, MyRandom.sample(List.of("Definitely.", "I think so.",
                "Absolutely.", "Of course.", "That's me.", "Yes.", "No doubt about it.")));
        return false;
    }

    public String getFormattedString(GameCharacter gc, int startingGold) {
        int knownInfo = this.ordinal();
        if (knownInfo == 0) {
            return "???";
        }
        if (knownInfo == 1) {
            return String.format("%s", gc.getFullName());
        }
        if (knownInfo == 2) {
            return String.format("%s, %s %d", gc.getFullName(),
                    gc.getCharClass().getShortName(), gc.getLevel());
        }
        if (knownInfo == 3) {
            return String.format("%s, %s %d, %s", gc.getFullName(),
                    gc.getCharClass().getShortName(), gc.getLevel(), gc.getOtherClasses());
        }
        return String.format("%s, %s, %s %d, %s, %d gold", gc.getFullName(), gc.getRace().getName(),
                gc.getCharClass().getShortName(), gc.getLevel(), gc.getOtherClasses(), startingGold);
    }
}
