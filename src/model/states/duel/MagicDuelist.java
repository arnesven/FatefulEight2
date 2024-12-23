package model.states.duel;

import model.Model;
import model.characters.GameCharacter;
import model.classes.SkillCheckResult;
import model.items.spells.Spell;
import util.MyRandom;
import view.MyColors;
import view.sprites.RunOnceAnimationSprite;
import view.sprites.StrikeEffectSprite;

public class MagicDuelist {
    private final GameCharacter character;
    private final MyColors magicColor;
    private final PowerGauge gauge;
    private int hitsTaken = 0;
    private RunOnceAnimationSprite reactionAnimation;

    public MagicDuelist(GameCharacter chara, MyColors magicColor, PowerGauge gauge) {
        this.character = chara;
        this.magicColor = magicColor;
        this.gauge = gauge;
    }

    public GameCharacter getCharacter() {
        return character;
    }

    public MyColors getMagicColor() {
        return magicColor;
    }

    public PowerGauge getGauge() {
        return gauge;
    }

    public boolean testMagicSkill(Model model, MagicDuelEvent state, int difficulty) {
        SkillCheckResult result = character.testSkill(model,
                Spell.getSkillForColor(magicColor), difficulty, 0);
        if (result.isFailure()) {
            state.println(character.getName() + " miscasts! (" + result.asString() + ").");
        }
        return result.isSuccessful();
    }

    public String getName() {
        return character.getName();
    }

    private void takeHits(int i) {
        this.hitsTaken += i;
    }

    public void setAnimation(RunOnceAnimationSprite animationSprite) {
        this.reactionAnimation = animationSprite;
    }

    public boolean hasAnimation() {
        return this.reactionAnimation != null;
    }

    public RunOnceAnimationSprite getAnimation() {
        return this.reactionAnimation;
    }

    public void generatePower() {
        int amount = MyRandom.rollD6() + MyRandom.rollD6() + MyRandom.rollD6();
        gauge.addToLevel(amount);
    }

    public void addToPower(int amount) {
        gauge.addToLevel(amount);
    }

    public int getPower() {
        return gauge.getCurrentLevel();
    }

    public void takeDamage(int amount) {
        takeHits(amount);
        setAnimation(new StrikeEffectSprite());
    }

    public int getHitsTaken() {
        return hitsTaken;
    }

    public int getMaxHits() {
        return 5;
    }

    public boolean canDoSpecialAttack() {
        return gauge.canDoSpecialAttack();
    }

    public boolean isKnockedOut() {
        return hitsTaken >= MagicDuelEvent.MAX_HITS;
    }
}
