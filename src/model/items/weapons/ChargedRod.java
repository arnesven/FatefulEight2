package model.items.weapons;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.items.Item;
import model.items.Prevalence;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.DamageValueEffect;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class ChargedRod extends WandWeapon {
    private static final Sprite SPRITE = new ItemSprite(13, 11,
            MyColors.WHITE, MyColors.LIGHT_YELLOW, MyColors.GOLD);
    private int charge = 0;

    public ChargedRod() {
        super("Charged Rod", 46, Skill.MagicWhite, new int[]{9, 11, 12, 14});
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
    public String getExtraText() {
        return "Every 5th shot does double damage.";
    }

    @Override
    public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter, Combatant target, int damage, int critical) {
        charge++;
        if (charge == 5) {
            charge = 0;
            combatEvent.println(target.getName() + " takes an additional " + (damage * 2) + " damage!");
            combatEvent.doDamageToEnemy(target, damage*2, gameCharacter);
            combatEvent.addFloatyDamage(target, damage*2, DamageValueEffect.MAGICAL_DAMAGE);
        }
    }
}
