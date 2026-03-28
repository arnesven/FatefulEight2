package model.items.weapons;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.combat.MagicDamage;
import model.items.Item;
import model.items.Prevalence;
import model.items.imbuements.WeaponImbuement;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.*;

public class ChargedRod extends WandWeapon implements PairableWeapon {
    private static final Sprite SPRITE = new ItemSprite(13, 11,
            MyColors.WHITE, MyColors.LIGHT_YELLOW, MyColors.GOLD);

    private static final AvatarItemSprite WAND_SPRITES = new AvatarItemSprite(0x10,
            MyColors.GOLD, MyColors.YELLOW, MyColors.TRANSPARENT, MyColors.LIGHT_YELLOW);

    public ChargedRod() {
        super("Charged Rod", 21, Skill.MagicWhite, new int[]{9, 11, 12, 14});
        setImbuement(new ChargedRodImbuement());
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new ChargedRod();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(0, 16, MyColors.WHITE, MyColors.LIGHT_YELLOW, MyColors.GOLD);
    }

    private static class ChargedRodImbuement extends WeaponImbuement {
        private int charge = 0;
        @Override
        public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter, Combatant target, int damage, int critical) {
            charge++;
            if (charge >= 5) {
                charge = 0;
                combatEvent.println("The charged rod released a huge blast!");
                if (!target.isDead()) {
                    if (damage == 0) {
                        damage = 1;
                    }
                    combatEvent.println(target.getName() + " takes an additional " + damage + " damage!");
                    combatEvent.addSpecialEffect(target, new SmokeBallAnimation());
                    combatEvent.doDamageToEnemyWithAnimation(target, new MagicDamage(damage), gameCharacter);
                }
            }
        }

        @Override
        public String getText() {
            return "Every 5th shot does double damage.";
        }
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return WAND_SPRITES;
    }
}
