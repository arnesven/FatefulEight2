package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.items.Item;
import model.states.DailyEventState;
import model.states.GameState;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;
import view.subviews.PortraitSubView;

import java.util.List;

public class FaceDancingSpell extends ImmediateSpell {

    private static final Sprite SPRITE = new ItemSprite(3, 8, MyColors.BROWN, MyColors.BLUE, MyColors.WHITE);
    private static final int DAMAGE = 4;
    private GameCharacter target;
    private int lower;

    public FaceDancingSpell() {
        super("Face Dancing", 48, MyColors.BLUE, 8, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new FaceDancingSpell();
    }

    @Override
    protected boolean preCast(Model model, GameState state, GameCharacter caster) {
        state.print("Change appearance of which party member?");
        this.target = model.getParty().partyMemberInput(model, state, model.getParty().getPartyMember(0));
        lower = model.getParty().getNotoriety() / model.getParty().size();
        state.print("Warning; casting the spell will deal " + DAMAGE + " damage to " +
                target.getFirstName() + ", change " + GameState.hisOrHer(target.getGender()) + " appearance permanently, " +
                "and lower your notoriety by " + lower + ". Do you want to continue casting the spell? (Y/N) ");
        if (!state.yesNoInput()) {
            return false;
        }
        return true;
    }

    @Override
    protected void applyAuxiliaryEffect(Model model, GameState state, GameCharacter caster) {
        target.addToHP(-DAMAGE);
        if (target.isDead()) {
            state.println(target.getFirstName() + " has been killed by the effects of the spell!");
            if (!DailyEventState.didResurrect(model, state, target)) {
                model.getParty().remove(target, true, false, 0);
            }
        } else {
            AdvancedAppearance appearance = PortraitSubView.makeRandomPortrait(target.getCharClass(), target.getRace(), target.getGender());
            target.setAppearance(appearance);
            state.println(target.getFirstName() + "'s appearance has changed!");
            state.partyMemberSay(target, MyRandom.sample(List.of("Ouch", "Hey, that hurt.",
                    "Ooof... wait, what just happened?", "The pain...", "Aaaaah!")));
        }
        state.println("Your notoriety was lowered by " + lower + "!");
        model.getParty().addToNotoriety(-lower);
    }

    @Override
    public String getDescription() {
        return "Changes a character's appearance and lowers notoriety.";
    }
}
