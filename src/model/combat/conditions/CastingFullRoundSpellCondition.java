package model.combat.conditions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.items.spells.FullRoundSpell;
import model.states.CombatEvent;
import model.states.GameState;
import view.GameView;
import view.MyColors;
import view.ScreenHandler;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.awt.*;

public class CastingFullRoundSpellCondition extends Condition {
    private final FullRoundSpell spell;
    private final Combatant target;
    private static final Sprite SPRITE = CharSprite.make((char)(0xC0), MyColors.LIGHT_GREEN, MyColors.BLACK, MyColors.CYAN);
    private final int castRound;
    private final GameCharacter caster;
    private LoopingSprite spinRing = new SpinRingAnimation();

    public CastingFullRoundSpellCondition(FullRoundSpell fullRoundSpell, GameCharacter caster, Combatant target, int castRound) {
        super("Casting Spell", "CAS");
        this.spell = fullRoundSpell;
        this.target = target;
        this.castRound = castRound;
        this.caster = caster;
    }

    @Override
    protected boolean noCombatTurn() {
        return true;
    }

    @Override
    public void endOfCombatRoundTrigger(Model model, GameState state, Combatant comb) {
        if (((CombatEvent)state).getCurrentRound() == castRound + spell.getCastTime() + 1) {
            spell.castingComplete(model, state, comb, target);
        }
    }

    @Override
    public void wasAttackedBy(GameCharacter subject, Enemy enemy, int damage) {
        subject.removeCondition(CastingFullRoundSpellCondition.class);
    }

    @Override
    public boolean removeAtEndOfCombat() {
        return true;
    }

    @Override
    public Sprite getSymbol() {
        return SPRITE;
    }

    @Override
    public boolean hasAlternateAvatar() {
        return true;
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, int xpos, int ypos) {
        caster.drawAvatar(screenHandler, xpos, ypos);
        screenHandler.register(spinRing.getName(), new Point(xpos, ypos), spinRing);
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this,
                "A condition indicating that this character is currently in the process of casting a spell. " +
                        "Such casting processes are normally interrupted if the character is attacked.");
    }

    private static class SpinRingAnimation extends LoopingSprite {
        public SpinRingAnimation() {
            super("castingring", "combat.png", 0x90, 32, 32);
            setFrames(8);
            setColor1(MyColors.WHITE);
            setColor2(MyColors.LIGHT_YELLOW);
            setColor3(MyColors.LIGHT_PINK);
            setColor4(MyColors.CYAN);
            setDelay(4);
        }
    }
}
