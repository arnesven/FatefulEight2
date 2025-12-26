package model.combat.conditions;

import model.Model;
import model.actions.RegenerationCondition;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.combat.Combatant;
import model.items.spells.WerewolfFormSpell;
import model.states.GameState;
import view.GameView;
import view.MyColors;
import view.ScreenHandler;
import view.help.ConditionHelpDialog;
import view.sprites.AvatarSprite;
import view.sprites.CharSprite;
import view.sprites.Sprite;

import java.awt.*;

public class WerewolfFormCondition extends Condition {

    private static final Sprite SPRITE = CharSprite.make((char)(0xD7), MyColors.PURPLE, MyColors.BLACK, MyColors.CYAN);
    private final RegenerationCondition regenCondition;
    private AvatarSprite avatar;
    // TODO: This condition does not increase damage of the character
    public WerewolfFormCondition(GameCharacter basedOn, int regen) {
        super("Werewolf Form", "WWF");
        setDuration(WerewolfFormSpell.TURNS);
        this.regenCondition = new RegenerationCondition(999, regen);
        if (basedOn != null) {
            this.avatar = new AvatarSprite(basedOn.getRace(), 0x2E0,
                    MyColors.DARK_GRAY, basedOn.getRace().getColor(), MyColors.LIGHT_GRAY,
                    CharacterAppearance.noHair(), CharacterAppearance.noHair());
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
    public boolean hasAlternateAvatar() {
        return true;
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, int xpos, int ypos) {
        screenHandler.register("wwavatarfor", new Point(xpos, ypos), getAvatar());
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
    public void wasRemoved(Combatant combatant) { }

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
