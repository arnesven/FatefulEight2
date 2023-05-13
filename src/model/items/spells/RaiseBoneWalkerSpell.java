package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.characters.LonnieLiebgott;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.combat.BurningWeaponCondition;
import model.combat.Combatant;
import model.combat.Condition;
import model.items.Equipment;
import model.items.Item;
import model.items.accessories.SkullCap;
import model.items.clothing.LeatherArmor;
import model.items.clothing.OutlawArmor;
import model.items.weapons.Longsword;
import model.races.Race;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.SmokeBallAnimation;
import view.sprites.Sprite;

import java.util.List;

public class RaiseBoneWalkerSpell extends CombatSpell {
    private static final Sprite SPRITE = new ItemSprite(9, 8, MyColors.BROWN, MyColors.GRAY, MyColors.RED);

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

        return target instanceof GameCharacter && !target.hasCondition(SummonCondition.class);
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        GameCharacter boneWalker = new BoneWalkerAlly();
        combat.addAllies(List.of(boneWalker));
        performer.addCondition(new SummonCondition());
        combat.addSpecialEffect(boneWalker, new SmokeBallAnimation());
    }

    @Override
    public String getDescription() {
        return "Summons a bone walker which will fight for you during combat.";
    }

    private static class BoneWalkerAlly extends GameCharacter {
        public BoneWalkerAlly() {
            super("Bone Walker", "", Race.DARK_ELF, Classes.BONE_WALKER,
                    new LonnieLiebgott(), new CharacterClass[0], new Equipment(new Longsword(), new OutlawArmor(), new SkullCap()));
        }
    }

    private static class SummonCondition extends Condition {
        public SummonCondition() {
            super("Summon", "SMN");
        }

        @Override
        protected boolean noCombatTurn() {
            return false;
        }

        @Override
        public boolean removeAtEndOfCombat() {
            return true;
        }

        @Override
        public Sprite getSymbol() {
            return BurningWeaponCondition.CONDITION_SPRITE;
        }
    }
}
