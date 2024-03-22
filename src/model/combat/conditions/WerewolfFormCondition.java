package model.combat.conditions;

import model.Model;
import model.actions.RegenerationCondition;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.combat.Combatant;
import model.items.spells.WerewolfFormSpell;
import model.races.Race;
import model.states.GameState;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.AvatarSprite;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class WerewolfFormCondition extends Condition {

    private static final Sprite SPRITE = CharSprite.make((char)(0xD7), MyColors.PURPLE, MyColors.BLACK, MyColors.CYAN);
    private final RegenerationCondition regenCondition;
    private AvatarSprite avatar;

    public WerewolfFormCondition(GameCharacter basedOn, int regen) {
        super("Werewolf Form", "WWF");
        setDuration(WerewolfFormSpell.TURNS);
        this.regenCondition = new RegenerationCondition(999, regen);
        if (basedOn != null) {
            this.avatar = new AvatarSprite(basedOn.getRace(), 0xD7,
                    MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, CharacterAppearance.noHair());
        }
    }

    @Override
    public boolean removeAtEndOfCombat() {
        return true;
    }

    @Override
    public void endOfCombatRoundTrigger(Model model, GameState state, Combatant comb) {
        regenCondition.endOfCombatRoundTrigger(model, state, comb);
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public Sprite getSymbol() {
        return SPRITE;
    }

    @Override
    public void wasRemoved(Combatant combatant) {
        if (combatant instanceof GameCharacter) {
            ((GameCharacter) combatant).getEquipment().getWeapon().setBurning(false);
        }
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this,
                "A condition which indicates that the combatant has morphed into werewolf form, " +
                        "with increased damage and regenerating health points each turn.");
    }

    public AvatarSprite getAvatar() {
        return avatar;
    }
}
