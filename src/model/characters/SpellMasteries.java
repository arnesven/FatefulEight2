package model.characters;

import model.Model;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.Item;
import model.items.spells.MasterySpell;
import model.items.spells.Spell;
import model.states.GameState;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import util.MyStrings;
import view.subviews.ArrowMenuSubView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellMasteries implements Serializable {

    private final Map<String, MyPair<Integer, Integer>> masteries = new HashMap<>();

    public List<String> getAbilityList() {
        List<String> result = new ArrayList<>();
        for (String key : masteries.keySet()) {
            if (masteries.get(key).first > 0) {
                result.add(key + " " + masteries.get(key).first);
            }
        }
        return result;
    }

    public boolean registerSuccessfullCast(Model model, MasterySpell masterySpell) {
        if (!masteries.containsKey(masterySpell.getName())) {
            masteries.put(masterySpell.getName(), new MyPair<>(0, 0));
        }
        MyPair<Integer, Integer> pair = masteries.get(masterySpell.getName());
        pair.second++;
        if (pair.first >= masterySpell.getThresholds().length) { // Max mastery level.
            return false;
        }
        if (pair.second.equals(masterySpell.getThresholds()[pair.first])) {
            pair.first++;
            return true;
        }
        return false;
    }

    public int getMasteryLevel(String spellName) {
        if (masteries.containsKey(spellName)) {
            return masteries.get(spellName).first;
        }
        return 0;
    }

    public int getMasteryLevel(MasterySpell masterySpell) {
        return getMasteryLevel(masterySpell.getName());
    }

    public int getCastCountPercentage(MasterySpell masterSpell) {
        if (!masteries.containsKey(masterSpell.getName())) {
            return 0;
        }
        MyPair<Integer, Integer> pair = masteries.get(masterSpell.getName());
        if (pair.first >= masterSpell.getThresholds().length) {
            return 100;
        }
        int prev = 0;
        if (pair.first > 0) {
            prev = masterSpell.getThresholds()[pair.first - 1];
        }

        int currentThresh = masterSpell.getThresholds()[pair.first];
        return (int)Math.round((pair.second - prev) * 100.0 /
                (currentThresh - prev));
    }


    public static void offersToTutorSpells(Model model, GameState state, String tutorer, List<Skill> magicColors) {
        List<Spell> tutorSpells = findTutoringSpells(model, magicColors);
        if (!tutorSpells.isEmpty()) {
            state.println(MyStrings.capitalize(tutorer) + " offers to tutor any one of your party members in the following spell masteries:");
            List<String> options = MyLists.transform(tutorSpells, Item::getName);
            options.add("Cancel");
            Spell[] selected = new Spell[]{null};
            model.setSubView(new ArrowMenuSubView(model.getSubView(), options, 24, 36 - options.size() * 2, ArrowMenuSubView.NORTH_WEST) {
                @Override
                protected void enterPressed(Model model, int cursorPos) {
                    if (cursorPos < tutorSpells.size()) {
                        selected[0] = tutorSpells.get(cursorPos);
                        model.setSubView(getPrevious());
                    }
                }
            });
            state.waitForReturn();
            if (selected[0] != null) {
                GameCharacter student = model.getParty().getPartyMember(0);
                if (model.getParty().size() > 1) {
                    state.println("Who should be tutored?");
                    student = model.getParty().partyMemberInput(model, state, model.getParty().getPartyMember(0));
                }
                MasterySpell masterySpell = (MasterySpell) selected[0];
                state.println(student.getName() + " is being tutored by " + tutorer + ". " +
                        MyStrings.capitalize(GameState.hisOrHer(student.getGender())) + " best to learn...");
                SkillCheckResult result = student.testSkill(model, Spell.getSkillForColor(masterySpell.getColor()));
                state.println(student.getName() + " tests " + Spell.getSkillForColor(masterySpell.getColor()).getName() + " (" + result.asString() + ").");
                int masteryTimes = result.getModifiedRoll() / 5;
                if (masteryTimes == 0 || result.getUnmodifiedRoll() == 1) {
                    state.println("... but fails to learn anything from " + tutorer + ".");
                    state.partyMemberSay(student, "I just don't get this.");
                } else {
                    state.println(student.getName() + " spell mastery for " + masterySpell.getName() + " was boosted!");
                    for (int i = 0; i < masteryTimes; ++i) {
                        masterySpell.incrementMastery(model, student);
                    }
                    state.partyMemberSay(student, MyRandom.sample(List.of("I got it!", "Eureka!", "I've figured this out.",
                            "I understand now.", "What finesses... I'm impressed by myself.", "I love magic.")));
                }
            }

        } else {
            state.println(tutorer + " was also offering to tutor your party in spell masteries, " +
                    "but you did not know any suitable spells.");
        }
    }

    private static List<Spell> findTutoringSpells(Model model, List<Skill> magicColors) {
        return MyLists.filter(model.getParty().getSpells(),
                sp -> magicColors.contains(Spell.getSkillForColor(sp.getColor())) &&
                sp instanceof MasterySpell && ((MasterySpell) sp).masteriesEnabled());
    }
}
