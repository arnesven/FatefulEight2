package model.states.duel;

import model.Model;
import view.sprites.CastingEffectSprite;
import view.sprites.MiscastEffectSprite;
import view.sprites.ShieldDuelAnimation;

public class ShieldMagicDuelAction extends MagicDuelAction {
    private final int level;
    private boolean success;
    private boolean showShieldAnimation = false;

    public ShieldMagicDuelAction(int level) {
        this.level = level;
    }

    @Override
    protected boolean isReactive() {
        return true;
    }

    @Override
    protected void specificPrepare(Model model, MagicDuelEvent state, MagicDuelist performer) {
        this.success = performer.testMagicSkill(model, state, BASE_DIFFICULTY + level);
        if (!success) {
            getPerformer().setAnimation(new MiscastEffectSprite());
        } else {
            getPerformer().setAnimation(new CastingEffectSprite());
        }
    }

    @Override
    protected int getPowerCost() {
        return level;
    }

    @Override
    protected boolean avertsAttack(Model model, MagicDuelEvent state,
                                   AttackMagicDuelAction attackMagicDuelAction, MagicDuelist opponent) {
        if (success) {
            this.showShieldAnimation = true;
            if (attackMagicDuelAction.getPowerLevel() > level) {
                state.println(opponent.getName() + "'s attack is too powerful for " +
                        getPerformer().getName() + "'s shield, it broke though!");
                return false;
            }
            state.println(getPerformer().getName() + " successfully shielded against " +
                    opponent.getName() + "'s attack.");
            return true;
        }
        return false;
    }

    @Override
    public void wrapUp(Model model, MagicDuelEvent magicDuelEvent, MagicDuelist opponent) {
        if (showShieldAnimation) {
            getPerformer().setAnimation(new ShieldDuelAnimation());
        }
    }
}
