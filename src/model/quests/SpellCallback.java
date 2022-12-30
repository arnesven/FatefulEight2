package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.items.spells.Spell;
import model.states.QuestState;

public interface SpellCallback {
    QuestEdge run(Model model, QuestState state, Spell spell, GameCharacter caster);
}
