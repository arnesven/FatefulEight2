package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.characters.preset.LonnieLiebgott;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.combat.Combatant;
import model.items.Equipment;
import model.items.Item;
import model.items.accessories.GreatHelm;
import model.items.accessories.SkullCap;
import model.items.clothing.ChainMail;
import model.items.clothing.LeatherArmor;
import model.items.clothing.OutlawArmor;
import model.items.weapons.BastardSword;
import model.items.weapons.DaiKatana;
import model.items.weapons.Longsword;
import model.races.Race;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.*;

public class RaiseBoneWalkerSpell extends SummonCombatSpell {
    public static final String SPELL_NAME = "Raise Bone Walker";
    private static final Sprite SPRITE = new CombatSpellSprite(9, 8, MyColors.BROWN, MyColors.GRAY, MyColors.RED);

    public RaiseBoneWalkerSpell() {
        super(SPELL_NAME, 34, MyColors.BLACK, 10, 3);
    }

    public static String getMagicExpertTips() {
        return "When you attain levels of mastery in Raise Bone Walker, " +
                "your animated servants will be both tougher and more deadly.";
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
    protected GameCharacter makeSummon(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        GameCharacter boneWalker = new BoneWalkerAlly();
        boneWalker.addToHP(-boneWalker.getMaxHP());
        int mastery = getMasteryLevel(performer);
        boneWalker.addToHP(7 + mastery * 4);
        switch (mastery) {
            case 2:
                boneWalker.getEquipment().setWeapon(new BastardSword());
            case 1:
                boneWalker.getEquipment().setClothing(new LeatherArmor());
                break;
            case 4:
                boneWalker.getEquipment().setWeapon(new DaiKatana());
                boneWalker.getEquipment().setAccessory(new GreatHelm());
            case 3:
                boneWalker.getEquipment().setClothing(new ChainMail());
            default:
        }
        return boneWalker;
    }

    @Override
    public boolean masteriesEnabled() {
        return true;
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
