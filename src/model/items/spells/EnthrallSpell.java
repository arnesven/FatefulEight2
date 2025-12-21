package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.ThrallCondition;
import model.items.Item;
import model.items.Prevalence;
import model.states.GameState;
import model.states.events.CheckForVampireEvent;
import util.MyLists;
import view.MyColors;
import view.sprites.BlackSpellSprite;
import view.sprites.Sprite;

public class EnthrallSpell extends ImmediateSpell {

    private static final Sprite SPRITE = new BlackSpellSprite(9, true);
    private GameCharacter target;

    public EnthrallSpell() {
        super("Enthrall", 100, MyColors.BLACK, 7, 4);
    }

    @Override
    protected boolean preCast(Model model, GameState state, GameCharacter caster) {
        state.println("Who should be the target of " + getName() + "?");
        this.target = model.getParty().partyMemberInput(model, state, caster);
        if (CheckForVampireEvent.isVampire(target)) {
            state.println("Vampires cannot be targeted by " + getName() + ".");
            return false;
        }
        if (isThrall(target)) {
            state.println(target.getName() + " has already been enthralled.");
            return false;
        }
        state.println(caster.getName() + " waits until " + GameState.heOrShe(caster.getGender()) +
                " can get a moment alone with " + target.getName() + ".");
        model.getLog().waitForAnimationToFinish();
        model.getParty().benchPartyMembers(MyLists.filter(model.getParty().getPartyMembers(),
                gc -> gc != caster && gc != target));
        return true;
    }

    @Override
    public String tryCastSpell(Model model, GameCharacter gc) {
        if (!CheckForVampireEvent.isVampire(gc)) {
            return "Only vampires can cast " + getName() + ".";
        }
        return super.tryCastSpell(model, gc);
    }

    public static boolean isThrall(GameCharacter target) {
        return target.hasCondition(ThrallCondition.class);
    }

    @Override
    protected void applyAuxiliaryEffect(Model model, GameState state, GameCharacter caster) {
        target.addCondition(new ThrallCondition(caster));
        state.partyMemberSay(caster, target.getName() + ", look into my eyes. Hear my voice.");
        state.partyMemberSay(target, "Yes... yes...");
        state.partyMemberSay(caster, target.getName() + ", you are now completely under my spell.");
        state.partyMemberSay(target, "The spell... I am...");
        state.partyMemberSay(caster, target.getName() + ", I make you my thrall. You MUST obey me.");
        state.partyMemberSay(target, "Yes master...");
        state.println(target.getName() + " has become a vampiric Thrall to " + caster.getName() + ".");
        target.addToAttitude(caster, 20);
        caster.addToAttitude(target, 20);
        model.getLog().waitForAnimationToFinish();
        model.getParty().unbenchAll();
    }

    @Override
    public String getDescription() {
        return "Turns a party member into a thrall.";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new EnthrallSpell();
    }

//    @Override
//    public Prevalence getPrevalence() { // TODO: bring this back (breaks save)
//        return Prevalence.veryRare;
//    }
}
