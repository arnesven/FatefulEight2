package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.items.spells.Spell;
import model.states.QuestState;

import java.io.Serializable;

public interface SpellCallback extends Serializable {
    QuestEdge run(Model model, QuestState state, Spell spell, GameCharacter caster);
}
