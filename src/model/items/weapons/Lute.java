package model.items.weapons;

import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.items.Item;
import model.items.Prevalence;
import model.states.CombatEvent;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

import java.util.List;

public class Lute extends Weapon {
    public static final Sprite SPRITE = new TwoHandedItemSprite(12, 11,
            MyColors.DARK_BROWN, MyColors.PEACH, MyColors.BROWN);
    protected static final AvatarItemSprite[] ON_AVATAR_SPRITES = makeShiftedSpriteSet(
            new AvatarItemSprite(0x80, MyColors.DARK_BROWN, MyColors.BROWN, MyColors.PEACH, MyColors.GRAY));

    public Lute() {
        super("Lute", 24, Skill.BluntWeapons, new int[]{8, 11});
    }

    public static boolean canDoAbility(GameCharacter performer, Combatant target) {
        return performer.getEquipment().getWeapon().isOfType(Lute.class);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Entertain, 1));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return 1000;
    }

    @Override
    public Item copy() {
        return new Lute();
    }

    @Override
    public boolean isTwoHanded() {
        return true;
    }

    @Override
    public String getSound() {
        return "lute";
    }

    @Override
    protected AvatarItemSprite getOnAvatarSprite(int index) {
        return ON_AVATAR_SPRITES[index];
    }

    @Override
    public String getExtraText() {
        return "Grants Ballad ability";
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public void didOneAttackWith(CombatEvent combatEvent, GameCharacter gameCharacter, Combatant target, int damage, int critical) {
        if (MyRandom.rollD10() > 8) {
            combatEvent.println("The lute is broken!");
            combatEvent.partyMemberSay(gameCharacter, MyRandom.sample(List.of("Darn it!", "That's unfortunate.", "No! My lute!")));
            gameCharacter.unequipWeapon();
        }
    }
}
