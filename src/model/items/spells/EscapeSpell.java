package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.items.Item;
import model.states.CombatEvent;
import model.states.GameState;
import view.MyColors;
import view.sprites.BlueSpellSprite;
import view.sprites.Sprite;

import java.awt.*;

public class EscapeSpell extends CombatSpell {

    private static final Sprite SPRITE = new BlueSpellSprite(4, true);

    public EscapeSpell() {
        super("Escape", 24, MyColors.BLUE, 7, 2, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new EscapeSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof GameCharacter;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        combat.println(performer.getFirstName() + " teleports the party out of combat!");
        combat.setTimeLimit(combat.getCurrentRound() - 1);
        combat.setUsedEscapeSpell(true);
    }

    @Override
    public boolean canBeUsedWithMass() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Teleports the party out of combat and back home.";
    }

    public static GameState makeTeleportHomeEvent(Model model) {
        Point p = model.getParty().getHomePosition(model);
        return new GameState(model) {
            @Override
            public GameState run(Model model) {
                TeleportSpell.teleportPartyToPosition(model, this, p, false);
                return model.getCurrentHex().getEveningState(model, false, false);
            }
        };
    }
}
