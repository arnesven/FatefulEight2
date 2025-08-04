package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.PortraitClothing;
import model.classes.Looks;
import model.combat.Combatant;
import model.items.Item;
import model.items.Prevalence;
import model.races.Race;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.*;

public class BoneArmorSpell extends CombatSpell {
    public static final String SPELL_NAME = "Bone Armor";
    private static final Sprite SPRITE = new BlackSpellSprite(7, true);

    public BoneArmorSpell() {
        super(SPELL_NAME, 34, MyColors.BLACK, 9, 2, false); // 9, 2
    }

    public static String getMagicExpertTips() {
        return "I once knew a powerful Sorcereress who was a master of Bone Armor. " +
                "It provided a powerful protection in combat.";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new BoneArmorSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof GameCharacter;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public boolean masteriesEnabled() {
        return true;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        if (performer.hasCondition(BoneArmorCondition.class)) {
            combat.println(getName() + " has no effect on " + performer.getName());
        } else {
            performer.addCondition(new BoneArmorCondition(getMasteryLevel(performer) + 4));
            performer.setSpecificClothing(new BoneArmorClothing());
        }
    }

    @Override
    public String getDescription() {
        return "Provides the caster with a suit of armor of bone for the remainder of combat.";
    }


    private static class BoneArmorClothing implements PortraitClothing {
        @Override
        public void putClothesOn(CharacterAppearance appearance) {
            putOnHelm(appearance, MyColors.DARK_GRAY, MyColors.GRAY);
            putMaskOn(appearance, MyColors.GRAY);
            Looks.putOnLightArmor(appearance, MyColors.GRAY, MyColors.DARK_GRAY);
        }

        private void putOnHelm(CharacterAppearance appearance, MyColors fill, MyColors line) {
            appearance.removeOuterHair();
            for (int y = 1; y <= 2; ++y) {
                for (int x = 2; x <= 4; ++x) {
                    ClothesSprite spr = new ClothesSprite(0x130 + 0x10 * y + x-2, line, fill);
                    appearance.setSprite(x, y, spr);
                }
            }
        }

        private void putMaskOn(CharacterAppearance appearance, MyColors color) {
            for (int i = 0; i < 3; ++i) {
                Sprite8x8 mask1 = new Sprite8x8("bonemaskupper" + i, "face.png", 0x187+i);
                mask1.setColor1(color);
                appearance.addSpriteOnTop(2+i, 3, mask1);

                Sprite8x8 mask2 = new Sprite8x8("bonemasklower" + i, "face.png", 0x20A+i);
                mask2.setColor1(color);
                appearance.addSpriteOnTop(2+i, 4, mask2);
            }
        }

        @Override
        public void finalizeLook(CharacterAppearance appearance) {

        }

        @Override
        public boolean showFacialHair() {
            return false;
        }

        @Override
        public boolean coversEars() {
            return true;
        }

        @Override
        public boolean hasSpecialAvatar() {
            return false; // FEATURE: Make avatar for bone armor.
        }

        @Override
        public AvatarSprite makeAvatar(Race race, CharacterAppearance appearance) {
            return null;
        }
    }
}
