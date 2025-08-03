package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.classes.SkillCheckResult;
import model.classes.SkillChecks;
import model.items.Item;
import model.items.Prevalence;
import model.items.Scroll;
import model.states.GameState;
import util.MyLists;
import view.MyColors;
import view.sprites.ColorlessSpellSprite;
import view.sprites.Sprite;
import view.subviews.ArrowMenuSubView;

import java.util.List;

public class ScrollMakingSpell extends ImmediateSpell {

    private static final Sprite SPRITE = new ColorlessSpellSprite(3, false);
    public static final String SPELL_NAME = "Scroll Writing";
    private Spell selectedSpell;

    public ScrollMakingSpell() {
        super(SPELL_NAME, 12, COLORLESS, 7, 3);
        selectedSpell = null;
    }

    @Override
    public boolean canExistsAsScroll() {
        return false;
    }

    @Override
    protected boolean preCast(Model model, GameState state, GameCharacter caster) {
        model.getTutorial().scrolls(model);
        if (model.getParty().getInventory().getMaterials() == 0) {
            state.println(caster.getName() + " was preparing to cast " + SPELL_NAME + ", but you do not have any materials.");
            return false;
        }
        List<Spell> spellList = MyLists.filter(model.getParty().getSpells(),
                Spell::canExistsAsScroll);
        if (spellList.isEmpty()) {
            state.println(caster.getName() + " was preparing to cast " + SPELL_NAME +
                    " but you do not have any other suitable spells to make scrolls with.");
            return false;
        }

        state.print(caster.getName() + " is casting " + SPELL_NAME +
                ", which spell do you want to make a spell from (costs 1 material)?");
        List<String> options = MyLists.transform(spellList, Item::getName);
        options.add("Cancel");
        final String[] selected = {null};
        model.setSubView(new ArrowMenuSubView(model.getSubView(), options, 24, 36 - options.size()*2, ArrowMenuSubView.NORTH_WEST) {
            @Override
            protected void enterPressed(Model model, int cursorPos) {
                selected[0] = options.get(cursorPos);
                model.setSubView(getPrevious());
            }
        });
        state.waitForReturn();
        if (selected[0].equals("Cancel")) {
            return false;
        }
        for (Spell sp : spellList) {
            if (sp.getName().equals(selected[0])) {
                this.selectedSpell = sp;
            }
        }
        return true;
    }

    @Override
    protected void applyAuxiliaryEffect(Model model, GameState state, GameCharacter caster) {
        boolean castSuccess = selectedSpell.sufferDamageAndDoSkillCheck(model, state, caster);
        model.getParty().getInventory().addToMaterials(-1);
        if (castSuccess) {
            SkillCheckResult secondCastResult = model.getParty().doSkillCheckWithReRoll(model, state, caster,
                    Spell.getSkillForColor(selectedSpell.getColor()), 5 + selectedSpell.getCost() / 7, 0, 0);
            if (secondCastResult.isSuccessful()) {
                Scroll scroll = new Scroll(selectedSpell);
                state.println(caster.getName() + " grafted the effect of the spell onto some parchment. " +
                        "A scroll was created, " + selectedSpell.getName() + " (" + scroll.getName() + "). " +
                        "Used up 1 material.");
                model.getParty().partyMemberSay(model, caster, List.of("Spectacular!",
                        "The smell of this ink... it's intoxicating.", "A new scroll.",
                        "This will come in handy.", "That's done. I'm satisfied."));
                scroll.addYourself(model.getParty().getInventory());
                return;
            }
        }
        state.println(caster.getName() + " failed to make the scroll and wasted 1 material in the process.");
    }

    @Override
    public String getDescription() {
        return "Make scrolls from other spells, costs materials.";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new ScrollMakingSpell();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
