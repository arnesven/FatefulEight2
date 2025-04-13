package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.items.Item;
import model.races.Race;
import util.MyRandom;
import view.MyColors;
import view.sprites.AvatarSprite;

import java.util.ArrayList;
import java.util.List;

public class NoClass extends CharacterClass {
    protected NoClass() {
        super("", "NONE", 3, 3, false, 0,
                new WeightedSkill[]{});
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnLooseShirt(characterAppearance, MyColors.BEIGE);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x220, MyColors.BEIGE, race.getColor(), appearance.getNormalHair(), appearance.getFullBackHair());
    }

    @Override
    public Equipment getDefaultEquipment() {
        return new Equipment();
    }

    @Override
    public boolean isBackRowCombatant() {
        return true;
    }

    @Override
    protected MyColors getIconColor() {
        return MyColors.BEIGE;
    }

    @Override
    protected int getIconNumber() {
        return 0x1F;
    }

    @Override
    public String getDescription() {
        return "No Class - Classless characters cannot " +
                "gain Experience Points but will be elevated to the average party level " +
                "once they change to anther class.";
    }

    @Override
    public List<Item> getStartingItems() {
        return new ArrayList<>();
    }

    @Override
    public String getHowToLearn() {
        return "Unused";
    }
}
