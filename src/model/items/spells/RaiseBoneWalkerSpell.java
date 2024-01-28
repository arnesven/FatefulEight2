package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.characters.preset.LonnieLiebgott;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.combat.Combatant;
import model.combat.SummonCondition;
import model.items.Equipment;
import model.items.Item;
import model.items.accessories.SkullCap;
import model.items.clothing.OutlawArmor;
import model.items.weapons.Longsword;
import model.races.Race;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.*;

import java.util.List;

public class RaiseBoneWalkerSpell extends CombatSpell {
    private static final Sprite SPRITE = new CombatSpellSprite(9, 8, MyColors.BROWN, MyColors.GRAY, MyColors.RED);

    public RaiseBoneWalkerSpell() {
        super("Raise Bone Walker", 34, MyColors.BLACK, 10, 3);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new RaiseBoneWalkerSpell();
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
        }
        GameCharacter boneWalker = new BoneWalkerAlly();
        combat.addAllies(List.of(boneWalker));
        AnimationManager.synchAnimations();
        performer.addCondition(new SummonCondition(boneWalker));
        combat.addSpecialEffect(boneWalker, new SmokeBallAnimation());
    }

    @Override
    public String getDescription() {
        return "Summons a bone walker to fight for the caster in combat.";
    }

    private static class BoneWalkerAlly extends GameCharacter {
        public BoneWalkerAlly() {
            super("Bone Walker", "", Race.DARK_ELF, Classes.BONE_WALKER,
                    new LonnieLiebgott(), new CharacterClass[0], new Equipment(new Longsword(), new OutlawArmor(), new SkullCap()));
        }
    }

}
