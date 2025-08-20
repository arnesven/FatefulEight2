package model.achievements;

import model.Model;
import model.items.ItemDeck;
import model.items.spells.Spell;
import util.MyLists;
import util.MyStrings;
import view.MyColors;

import java.util.List;

public class CollectAllSpellsOfColorAchievement extends PassiveAchievement {
    private final MyColors color;

    public CollectAllSpellsOfColorAchievement(MyColors color) {
        super(Spell.nameForColor(color) + " Spell Collector",
                "You collected all " + MyStrings.numberWord(allSpellsOfColor(color).size()) +
                        " " + Spell.nameForColor(color) + " Spells.");
        this.color = color;
    }

    private static List<Spell> allSpellsOfColor(MyColors color) {
        return MyLists.filter(ItemDeck.allSpells(), sp -> sp.getColor() == color);
    }

    @Override
    public boolean condition(Model model) {
        return MyLists.all(allSpellsOfColor(color),
                sp -> MyLists.any(model.getParty().getSpells(),
                        sp2 -> sp2.getName().equals(sp.getName())));
    }
}
