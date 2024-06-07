package model.items.weapons;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.combat.conditions.TimedParalysisCondition;
import model.items.Item;
import model.items.Prevalence;
import model.states.CombatEvent;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class IceRod extends WandWeapon {
    private static final Sprite SPRITE = new ItemSprite(13, 11,
            MyColors.LIGHT_PINK, MyColors.CYAN, MyColors.LIGHT_BLUE);

    public IceRod() {
        super("Ice Rod", 44, Skill.MagicBlue, new int[]{9, 11, 12, 14});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new IceRod();
    }

    @Override
    public String getExtraText() {
        return "20% Chance to apply Freeze";
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter, Combatant target, int damage, int critical) {
        if (MyRandom.rollD10() >= 9 && !target.hasCondition(TimedParalysisCondition.class)) {
            target.addCondition(new FreezeCondition());
            combatEvent.println(target.getName() + " is paralyzed by the freezing cold!");
        }
    }

    private static class FreezeCondition extends TimedParalysisCondition {
        public FreezeCondition() {
            super(1);
        }
    }
}
