package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.characters.preset.LonnieLiebgott;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.combat.Combatant;
import model.combat.conditions.SummonCondition;
import model.items.Equipment;
import model.items.Item;
import model.items.weapons.FamiliarWeapon;
import model.races.Race;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.CombatSpellSprite;
import view.sprites.SmokeBallAnimation;
import view.sprites.Sprite;

import java.util.List;

public class SummonFamiliarSpell extends CombatSpell {
    private static final Sprite SPRITE = new CombatSpellSprite(11, 8, MyColors.BEIGE, MyColors.GREEN, MyColors.WHITE);

    public SummonFamiliarSpell() {
        super("Summon Familiar", 14, MyColors.GREEN, 7, 1, false);
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
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof GameCharacter;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        if (performer.hasCondition(SummonCondition.class)) {
            SummonCondition sumCond = (SummonCondition) performer.getCondition(SummonCondition.class);
            GameCharacter gc = sumCond.getSummon();
            combat.removeAlly(gc);
            performer.removeCondition(SummonCondition.class);
            combat.println("Your summon has been replaced.");
        }
        GameCharacter familiar = new FamiliarAlly();
        combat.addAllies(List.of(familiar));
        performer.addCondition(new SummonCondition(familiar));
        combat.addSpecialEffect(familiar, new SmokeBallAnimation());
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
