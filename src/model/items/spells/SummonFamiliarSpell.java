package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.characters.preset.LonnieLiebgott;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.combat.Combatant;
import model.items.Equipment;
import model.items.Item;
import model.items.weapons.FamiliarWeapon;
import model.races.Race;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.GreenSpellSprite;
import view.sprites.Sprite;

public class SummonFamiliarSpell extends SummonCombatSpell {
    private static final Sprite SPRITE = new GreenSpellSprite(5, true);

    public SummonFamiliarSpell() {
        super("Summon Familiar", 14, MyColors.GREEN, 7, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new SummonFamiliarSpell();
    }

    @Override
    protected GameCharacter makeSummon(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        return new FamiliarAlly();
    }

    @Override
    public String getDescription() {
        return "Summons a familiar to fight for the caster in combat.";
    }

    private static class FamiliarAlly extends GameCharacter {
        public FamiliarAlly() {
            super("Familiar", "", Race.SOUTHERN_HUMAN, Classes.FAMILIAR,
                    new LonnieLiebgott(), new CharacterClass[0], new Equipment(new FamiliarWeapon()));
        }
    }
}
