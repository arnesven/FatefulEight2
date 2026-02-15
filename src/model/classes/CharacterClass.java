package model.classes;

import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.horses.*;
import model.items.Equipment;
import model.items.HorseStartingItem;
import model.items.Item;
import model.races.Race;
import view.MyColors;
import view.sprites.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CharacterClass implements Serializable {

    private static final Map<String, Sprite> icons = new HashMap<>();
    private final String shortname;
    private final WeightedSkill[] skillBonuses;
    private final int startGold;
    private final int id;
    private String name;
    private int hp;
    private int speed;
    private boolean canUseHeavyArmor;
    private static int nextClassId = 0;

    protected CharacterClass(String name, String shortname, int hp, int speed,
                          boolean canUseHeavyArmor, int startGold, WeightedSkill[] skillBonuses) {
        this.name = name;
        this.shortname = shortname;
        this.hp = hp;
        this.speed = speed;
        this.startGold = startGold;
        this.skillBonuses = skillBonuses;
        this.canUseHeavyArmor = canUseHeavyArmor;
        this.id = nextClassId++;
    }

    public String getFullName() {
        return name;
    }

    public String getShortName() {
        return shortname;
    }

    public int getHP() {
        return hp;
    }

    public int getSpeed() {
        return speed;
    }

    public abstract void putClothesOn(CharacterAppearance characterAppearance);

    public void takeClothesOff(CharacterAppearance characterAppearance) { };

    public WeightedSkill getWeightForSkill(Skill skill) {
        for (WeightedSkill ws : skillBonuses) {
            if (ws.getSkill().areEqual(skill)) {
                return ws;
            }
        }
        return new NoSkillBonus();
    }

    public abstract AvatarSprite getAvatar(Race race, CharacterAppearance appearance);

    public abstract Equipment getDefaultEquipment();

    public abstract boolean isBackRowCombatant();

    public List<Skill> getSkills() {
        List<Skill> result = new ArrayList<>();
        for (WeightedSkill ws : skillBonuses) {
            result.add(ws.getSkill());
        }
        return result;
    }

    public boolean canUseHeavyArmor() {
        return canUseHeavyArmor;
    }

    public int getStartingGold() {
        return startGold;
    }

    public void finalizeLook(CharacterAppearance appearance) {

    }

    public boolean showFacialHair() {
        return true;
    }

    public boolean showDetail() {
        return true;
    }

    public int id() {
        return id;
    }

    public void manipulateAvatar(CharacterAppearance appearance, Race race) {

    }

    public boolean coversEars() {
        return false;
    }

    public int getWeaponShift(GameCharacter gameCharacter) {
        if (gameCharacter.getRace().isShort()) {
            return - 1;
        }
        return 0;
    }

    public final Sprite getIconSprite() {
        if (!icons.containsKey(getShortName())) {
            icons.put(getShortName(), new CharClassIconSprite(getIconNumber(), getIconColor()));
        }
        return icons.get(getShortName());
    }

    protected abstract MyColors getIconColor();

    protected abstract int getIconNumber();

    public abstract String getHowToLearn();

    public boolean isSpecialCharacter() {
        return false;
    }

    public boolean showHairInBack() {
        return true;
    }

    public static List<CharacterClass> getSelectableClasses() {
        List<CharacterClass> classes = new ArrayList<>();
        classes.add(Classes.AMZ);
        classes.add(Classes.ART);
        classes.add(Classes.ASN);
        classes.add(Classes.BBN);
        classes.add(Classes.BRD);
        classes.add(Classes.BKN);
        classes.add(Classes.CAP);
        classes.add(Classes.DRU);
        classes.add(Classes.FOR);
        classes.add(Classes.MAG);
        classes.add(Classes.MAR);
        classes.add(Classes.MIN);
        classes.add(Classes.NOB);
        classes.add(Classes.PAL);
        classes.add(Classes.PRI);
        classes.add(Classes.SOR);
        classes.add(Classes.SPY);
        classes.add(Classes.THF);
        classes.add(Classes.WIZ);
        classes.add(Classes.WIT);
        return classes;
    }

    public abstract String getDescription();

    public abstract List<Item> getStartingItems();

    protected static List<Item> horseOrPony() {
        return List.of(new HorseStartingItem(new OldMaidHorse()), new HorseStartingItem(new OldPony()));
    }

    public boolean coversEyebrows() {
        return false;
    }
}
