package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.characters.preset.LonnieLiebgott;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import model.combat.Combatant;
import model.items.Equipment;
import model.items.Item;
import model.items.Prevalence;
import model.items.accessories.GreatHelm;
import model.items.accessories.HeraldicShield;
import model.items.clothing.ChainMail;
import model.items.clothing.LeatherArmor;
import model.items.clothing.PlateMailArmor;
import model.items.weapons.*;
import model.races.Race;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.CombatSpellSprite;
import view.sprites.Sprite;

public class SummonTreeHerderSpell extends SummonCombatSpell {
    private static final Sprite SPRITE = new CombatSpellSprite(3, 8, MyColors.BEIGE, MyColors.GREEN, MyColors.WHITE);

    public SummonTreeHerderSpell() {
        super("Summon Tree Herder", 46, MyColors.GREEN, 10, 3); // 10
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new SummonTreeHerderSpell();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public String getDescription() {
        return "Summons a Tree Herder to fight for the caster in combat.";
    }

    @Override
    protected GameCharacter makeSummon(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        GameCharacter herder = new TreeHerderAlly();
        int mastery = getMasteryLevel(performer);
        herder.setLevel(mastery+2);
        herder.addToHP(herder.getMaxHP() - herder.getHP());
        return herder;
    }

    private static class TreeHerderAlly extends GameCharacter {
        public TreeHerderAlly() {
            super("Tree Herder", "", Race.WOOD_ELF, Classes.TREE_HERDER,
                    new LonnieLiebgott(), Classes.NO_OTHER_CLASSES, new Equipment(new BigBranchArm(), new LeatherArmor(), null));
        }

        private static class BigBranchArm extends NaturalWeapon {
            public BigBranchArm() {
                super("Big Branch Arm", 0, Skill.BluntWeapons, new int[]{7,9,12,13,15});
            }

            @Override
            public int getWeight() {
                return 0;
            }

            @Override
            public Item copy() {
                return new BigBranchArm();
            }
        }
    }
}
