package model.characters;

import model.Model;
import model.items.spells.MasterySpell;
import util.MyPair;

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
}
