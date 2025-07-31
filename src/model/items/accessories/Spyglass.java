package model.items.accessories;

import model.characters.GameCharacter;
import model.classes.Skill;
import model.enemies.Enemy;
import model.items.Item;
import model.items.PirateItem;
import model.items.Prevalence;
import model.states.CombatEvent;
import util.MyPair;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class Spyglass extends ShieldItem implements PirateItem {
    private static final Sprite SPRITE = new ItemSprite(8, 16, MyColors.BROWN, MyColors.PEACH, MyColors.CYAN);

    private static final Sprite[] SHIELD_SPRITES = makeShiftedSpriteSet(
            new AvatarItemSprite(0x64, MyColors.BROWN, MyColors.PEACH, MyColors.PINK, MyColors.BEIGE));

    public Spyglass() {
        super("Spyglass", 15, false, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Perception, 2));
    }

    @Override
    public int getWeight() {
        return 1000;
    }

    @Override
    public Item copy() {
        return new Spyglass();
    }

    @Override
    public String getSound() {
        return "click";
    }

    @Override
    public int getAP() {
        return 0;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public String getExtraText() {
        return "Can be used to peer into nearby hexes from hills and mountains";
    }

    @Override
    public Sprite getOnAvatarSprite(GameCharacter gameCharacter) {
        return SHIELD_SPRITES[gameCharacter.getCharClass().getWeaponShift(gameCharacter) + 1];
    }

    // TODO: Feature
//    @Override
//    public void wielderWasAttackedBy(Enemy enemy, CombatEvent combatEvent) {
//        // has a chance to break spyglass
//    }
}
