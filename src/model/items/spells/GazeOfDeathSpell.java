package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.items.Item;
import model.states.CombatEvent;
import sound.SoundEffects;
import view.MyColors;
import view.sprites.*;
import view.subviews.CombatSubView;

import java.util.List;

public class GazeOfDeathSpell extends CombatSpell {
    private static final Sprite SPRITE = new CombatSpellSprite(3, 8, MyColors.BROWN, MyColors.GRAY, MyColors.RED);

    public GazeOfDeathSpell() {
        super("Gaze of Death", 26, MyColors.BLACK, 8, 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new GazeOfDeathSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof Enemy;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        int difficulty = Math.max(target.getMaxHP(), 9);
        boolean success = false;
        if (model.getSubView() instanceof CombatSubView) {
            success = doWithAnimation(model, combat, performer, target, difficulty, (CombatSubView) model.getSubView());
        } else {
            SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, combat, performer, Skill.SpellCasting, difficulty, 0, 0);
            success = result.isSuccessful();
        }
        if (success) {
            combat.println("Gaze of Death kills " + target.getName() + "!");
            combat.addFloatyDamage(target, target.getHP(), DamageValueEffect.MAGICAL_DAMAGE);
            combat.doDamageToEnemy(target, target.getHP(), performer);
        } else {
            combat.println("Gaze of Death did not affect " + target.getName() + ".");
        }
    }

    private boolean doWithAnimation(Model model, CombatEvent combat, GameCharacter performer,
                                 Combatant target, int difficulty, CombatSubView subView) {
        DarkenCombatAnimation darken = new DarkenCombatAnimation(8);
        subView.addSpecialEffectEverywhere(List.of(performer, target), darken);
        DarkenCombatAnimation castersDarken = new DarkenCombatAnimation(12);
        combat.addSpecialEffect(performer, castersDarken);
        DarkenCombatAnimation targetDarken = new DarkenCombatAnimation(12);
        combat.addSpecialEffect(target, targetDarken);
        combat.waitUntil(darken, RunOnceAndStillAnimation::firstPartDone);
        castersDarken.cancel();

        RunOnceAndStillAnimation eyeOpen = new EyeAnimation(8, 5);
        combat.addSpecialEffect(performer, eyeOpen);
        combat.waitUntil(eyeOpen, RunOnceAndStillAnimation::firstPartDone);
        SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, combat, performer, Skill.SpellCasting, difficulty, 0, 0);
        eyeOpen.cancel();
        if (result.isSuccessful()) {
            SoundEffects.playSound("gazeofdeath");
            RunOnceAnimationSprite eyeBlink = new EyeBlinkAnimation(16, 4);
            combat.addSpecialEffect(performer, eyeBlink);
            combat.waitUntil(eyeBlink, RunOnceAnimationSprite::isDone);
            eyeBlink = new EyeBlinkAnimation(17, 4);
            combat.addSpecialEffect(performer, eyeBlink);
            combat.waitUntil(eyeBlink, RunOnceAnimationSprite::isDone);
        } else {
            RunOnceAndStillAnimation eyeClose = new EyeAnimation(12, 4);
            combat.addSpecialEffect(performer, eyeClose);
            combat.waitUntil(eyeClose, RunOnceAndStillAnimation::firstPartDone);
            eyeClose.cancel();
        }
        darken.cancel();
        targetDarken.cancel();
        return result.isSuccessful();
    }

    @Override
    public String getDescription() {
        return "Has a chance to immediately kill an enemy.";
    }

    private static class DarkenCombatAnimation extends RunOnceAndStillAnimation {
        public DarkenCombatAnimation(int col) {
            super("darkenanimation", "combat.png", col, 14,
                    32, 32, 4, MyColors.BLACK, Integer.MAX_VALUE);
            setAnimationDelay(50);
        }
    }

    private static class EyeAnimation extends RunOnceAndStillAnimation {
        public EyeAnimation(int col, int frames) {
            super("eyeanimation", "combat.png", col, 15,
                    32, 32, frames, MyColors.BLACK, Integer.MAX_VALUE);
            setAnimationDelay(10);
            setColor3(MyColors.DARK_BLUE);
        }
    }

    private static class EyeBlinkAnimation extends RunOnceAnimationSprite {
        public EyeBlinkAnimation(int row, int delay) {
            super("eyeblink", "combat.png", 8, row,
                    32, 32, 7, MyColors.BLACK);
            setAnimationDelay(delay);
            setColor2(MyColors.BEIGE);
            setColor3(MyColors.LIGHT_RED);
            setColor4(MyColors.CYAN);
        }
    }
}
