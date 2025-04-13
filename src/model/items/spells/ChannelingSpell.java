package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.items.Item;
import model.states.CombatEvent;
import model.states.GameState;
import view.sprites.ColorlessSpellSprite;
import view.sprites.Sprite;
import view.subviews.ArrowMenuSubView;

import java.util.List;

public class ChannelingSpell extends SkillBoostingSpell {
    public static final String SPELL_NAME = "Channeling";
    private static final Sprite SPRITE =  new ColorlessSpellSprite(5, false);
    private static final int DIFFICULTY = 10;
    private static final int HP_COST = 1;
    private Skill skill = null;

    public ChannelingSpell() {
        super(SPELL_NAME, 24, COLORLESS, DIFFICULTY, HP_COST);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    protected boolean preCast(Model model, GameState state, GameCharacter caster) {
        state.print("What color of magic do you wish to channel?");
        final int[] selected = {0};
        model.setSubView(new ArrowMenuSubView(model.getSubView(), List.of("Red", "Blue", "Green", "Black", "White"),
                24, 25, ArrowMenuSubView.NORTH_WEST) {
            @Override
            protected void enterPressed(Model model, int cursorPos) {
                selected[0] = cursorPos;
                model.setSubView(getPrevious());
            }
        });
        state.waitForReturn();
        skill = new Skill[]{Skill.MagicRed, Skill.MagicBlue, Skill.MagicGreen, Skill.MagicBlack, Skill.MagicWhite}[selected[0]];
        return true;
    }

    @Override
    public Item copy() {
        return new ChannelingSpell();
    }

    @Override
    protected Skill getBoostingSkill() {
        return skill;
    }

    @Override
    protected int getBoostAmount() {
        return 6;
    }

    @Override
    public String getDescription() {
        return "Temporarily raises the caster's rank in the Magic Skill of a chosen color.";
    }

    @Override
    public CombatSpell getCombatSpell() {
        return new CombatSpell("Channeling", 0, COLORLESS, DIFFICULTY, HP_COST) {
            @Override
            public boolean canBeCastOn(Model model, Combatant target) {
                return target instanceof GameCharacter;
            }

            @Override
            public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
                ChannelingSpell.this.preCast(model, combat, performer);
                ChannelingSpell.this.applyAuxiliaryEffect(model, combat, performer);
            }

            @Override
            public String getDescription() {
                return "";
            }

            @Override
            protected Sprite getSprite() {
                return ChannelingSpell.this.getSprite();
            }

            @Override
            public Item copy() {
                throw new IllegalStateException("Should not be copied.");
            }
        };
    }
}
