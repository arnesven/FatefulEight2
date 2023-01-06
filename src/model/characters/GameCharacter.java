package model.characters;

import model.combat.Combatant;
import model.Model;
import model.Party;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.Enemy;
import model.items.*;
import model.items.accessories.Accessory;
import model.items.clothing.Clothing;
import model.items.clothing.JustClothes;
import model.items.weapons.UnarmedCombatWeapon;
import model.items.weapons.Weapon;
import model.races.Race;
import model.states.CombatEvent;
import util.MyRandom;
import view.BorderFrame;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.AvatarSprite;
import view.sprites.Sprite;

import java.awt.Point;
import java.util.*;

public class GameCharacter extends Combatant {
    private static final MyColors DEFAULT_TEXT_COLOR = MyColors.LIGHT_GRAY;
    private static final int MAX_SP = 2;
    private static final int[] XP_LEVELS = new int[]{0, 100, 250, 450, 700, 1000};

    private final String firstName;
    private final String lastName;
    private final Race race;
    private final CharacterClass[] classes;
    private AvatarSprite avatarSprite;
    private CharacterClass charClass;
    private final CharacterAppearance appearance;
    private int level;
    private Equipment equipment;

    private int currentSp = 1;
    private int currentXp = 0;
    private Party party;
    private String status = "OK";


    public GameCharacter(String firstName, String lastName, Race race, CharacterClass charClass, CharacterAppearance appearance,
                         CharacterClass[] classes, Equipment equipment) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.race = race;
        this.appearance = appearance;
        this.level = 1;
        this.classes = classes;
        this.equipment = equipment;
        setClass(charClass);
        super.setCurrentHp(getMaxHP());
    }

    public GameCharacter(String firstName, String lastName, Race race,
                         CharacterClass charClass, CharacterAppearance appearance, CharacterClass[] classes) {
        this(firstName, lastName, race, charClass, appearance, classes, charClass.getStartingEquipment());
    }

    public static int getXPForNextLevel(int level) {
        return XP_LEVELS[level];
    }

    private AvatarSprite makeAvatarSprite() {
        return charClass.getAvatar(race);
    }

    public void drawYourself(ScreenHandler screenHandler, int col, int row, MyColors color) {
        String nameString = this.getFullName();
        if (this.getFullName().length() > BorderFrame.CHARACTER_WINDOW_COLUMNS) {
            nameString = getFirstName().charAt(0) + ". " + getLastName();
        }
        BorderFrame.drawString(screenHandler, nameString, col, row, color);

        String raceAndClassString = this.getRace().getName() + " " + this.getGameClass() + " Lvl " + this.getLevel();
        BorderFrame.drawString(screenHandler, raceAndClassString, col, row+1, DEFAULT_TEXT_COLOR);

        drawAppearance(screenHandler, col, row+3);

        BorderFrame.drawString(screenHandler, String.format("%5d XP  %1d SP", this.getXP(), this.getSP()), col+8, row+2, DEFAULT_TEXT_COLOR);
        BorderFrame.drawString(screenHandler, String.format("%2d/%2d HP %2d AP", this.getHP(), this.getMaxHP(), this.getAP()), col+8, row+3, DEFAULT_TEXT_COLOR);
        BorderFrame.drawString(screenHandler, String.format("SPEED %2d %s", this.getSpeed(), isLeader() ? "LEADER" : ""), col+8, row+4, DEFAULT_TEXT_COLOR);
        BorderFrame.drawString(screenHandler, String.format("STATUS %s", this.getStatus()), col+8, row+5, DEFAULT_TEXT_COLOR);

        equipment.drawYourself(screenHandler, col, row);
    }

    public void drawAppearance(ScreenHandler screenHandler, int col, int row) {
        appearance.drawYourself(screenHandler, col, row);
    }

    public boolean isLeader() {
        if (party == null) {
            return false;
        }
        return party.getLeader() == this;
    }

    public int getSpeed() {
        return charClass.getSpeed() + race.getSpeedModifier() + equipment.getSpeedModifiers();
    }

    @Override
    public void addToHP(int i) {
        super.addToHP(i);
        if (getHP() == 0) {
            setStatus("DEAD");
        }
    }

    private void setStatus(String stat) {
        this.status = stat;
    }

    @Override
    public void takeCombatTurn(Model model, CombatEvent combatEvent) {
        if (!getsCombatTurn()) {
            return;
        }
        if (party.getBackRow().contains(this) && !mayAttackFromBackRow()) {
            combatEvent.println(getFirstName() + " can't attack from back row!");
            return;
        }
        combatEvent.print(getFirstName() + "'s turn. ");
        char selectedAction;
        while (true) {
            String options = "Attack (A), Item (I), Magic (M), Pass (P)";
            if (isLeader() && combatEvent.fleeingEnabled()) {
                options += ", Flee (F)";
            }
            combatEvent.print("Select combat action; " + options + ": ");
            selectedAction = combatEvent.lineInput().toUpperCase().charAt(0);
            if (selectedAction == 'A') {
                Combatant target = combatEvent.getSelectedEnemy();
                if (target instanceof GameCharacter) {
                    combatEvent.println("You cannot attack a party member.");
                } else {
                    performAttack(model, combatEvent, target);
                    break;
                }
            } else if (isLeader() && selectedAction == 'F' && combatEvent.fleeingEnabled()) {
                performFleeFromBattle(model, combatEvent);
                break;
            } else if (selectedAction == 'I') {
                Set<UsableItem> usableItems = new HashSet<>();
                usableItems.addAll(model.getParty().getInventory().getPotions());
                if (usableItems.size() > 0) {
                    boolean used = performUseItem(model, combatEvent, usableItems);
                    if (used) {
                        break;
                    }
                } else {
                    combatEvent.println("You have no usable items.");
                }
            } else if (selectedAction == 'P') {
                break;
            }
        }

    }

    private void performAttack(Model model, CombatEvent combatEvent, Combatant target) {
        for (int i = 0; i < equipment.getWeapon().getNumberOfAttacks(); i++) {
            SkillCheckResult result = testSkill(equipment.getWeapon().getSkillToUse(this));
            int damage = equipment.getWeapon().getDamage(result.getModifiedRoll(), this);
            String extraInfo = " (" + result.asString() + " on [" + equipment.getWeapon().getDamageTableAsString() + "]";
            if (result.isCritical() && equipment.getWeapon().allowsCriticalHits()) {
                damage *= 2;
                extraInfo += " x2 Critical Hit";
            }
            extraInfo += ")";
            combatEvent.println(getFirstName() + " attacks " + target.getName() + ", dealing " + damage + " damage." + extraInfo);
            combatEvent.addStrikeEffect(target, model, damage, result.isCritical() && equipment.getWeapon().allowsCriticalHits());
            combatEvent.doDamageToEnemy(target, damage, this);
        }
    }

    private void performFleeFromBattle(Model model, CombatEvent combatEvent) {
        if (model.getParty().size() > 1) {
            SkillCheckResult result = testSkill(Skill.Leadership, 3 + model.getParty().size());
            combatEvent.println("Trying to escape from combat (Leadership " + result.asString() + ").");
            if (result.isSuccessful()) {
                combatEvent.setPartyFled(true);
            }
        } else {
            int d10 = MyRandom.rollD10();
            combatEvent.print("Trying to escape from combat (D10 roll=" + d10);
            if (d10 >= 5) {
                combatEvent.println(" >=5, SUCCESS.");
                combatEvent.setPartyFled(true);
            } else {
                combatEvent.println(" <5 FAIL. Can't get away!");
            }
        }
    }


    private boolean performUseItem(Model model, CombatEvent combatEvent, Set<UsableItem> usableItems) {
        List<UsableItem> items = new ArrayList<>(usableItems);
        combatEvent.print("Which item would you like to use? ");
        for (int i = 0; i < items.size(); ++i) {
            combatEvent.print(items.get(i).getName() + " (" + (i+1) + "), ");
        }
        combatEvent.print("or cancel (Q)? ");
        try {
            int selected = Integer.parseInt(combatEvent.lineInput());
            if (0 < selected && selected <= items.size()) {
                UsableItem item = items.get(selected-1);
                combatEvent.print("On whom do you wish to use the " + item.getName() + "?");
                GameCharacter target = model.getParty().partyMemberInput(model, combatEvent, this);
                if (item.canBeUsedOn(model, target)) {
                    String message = item.useYourself(model, target);
                    combatEvent.println(message);
                    return true;
                } else {
                    combatEvent.println(item.getName() + " cannot be used on " + target.getFirstName() + ".");
                }
            }
        } catch (NumberFormatException nfe) {

        }
        return false;
    }

    private boolean mayAttackFromBackRow() {
        return equipment.getWeapon().isRangedAttack();
    }

    private boolean getsCombatTurn() {
        return !isDead();
    }

    public int getRankForSkill(Skill skill) {
        if (skill == Skill.MagicAny) {
            int best = 0;
            for (Skill s : Skill.values()) {
                if (s.isMagic() && charClass.getWeightForSkill(s) > best) {
                    best = charClass.getWeightForSkill(s);
                    skill = s;
                }
            }
        }
        return Skill.getRankForSkill(charClass.getWeightForSkill(skill), getLevel())
                + race.getBonusForSkill(skill) + equipment.getBonusForSkill(skill);
    }

    @Override
    public String getName() {
        return getFullName();
    }

    public String getStatus() {
        return status;
    }

    public int getSP() {
        return currentSp;
    }

    public int getAP() {
        return equipment.getTotalAP();
    }

    public int getXP() {
        return currentXp;
    }

    public int getMaxHP() {
        return charClass.getHP() + race.getHPModifier() + equipment.getHealthBonus() + level;
    }

    public int getLevel() {
        return level;
    }

    private String getGameClass() {
        return charClass.getShortName();
    }

    public Race getRace() {
        return this.race;
    }

    public String getFullName() {
        return this.getFirstName() + " " + this.getLastName();
    }

    private String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void addToSP(int i) {
        currentSp = Math.max(0, Math.min(currentSp + i, MAX_SP));
    }

    public void drawAvatar(ScreenHandler screenHandler, int xpos, int ypos) {
        if (isDead()) {
            screenHandler.register("avatarfor" + getFullName() + "dead", new Point(xpos, ypos), avatarSprite.getDead());
        } else {
            screenHandler.register("avatarfor" + getFullName(), new Point(xpos, ypos), avatarSprite);
        }
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, int xpos, int ypos, Sprite initiativeSymbol) {
        drawAvatar(screenHandler, xpos, ypos);
        screenHandler.register(getName() + "inittoken", new Point(xpos+3, ypos+3), initiativeSymbol);
    }

    @Override
    public Sprite getCombatCursor(Model model) {
        return party.getCursorForPartyMember(this);
    }

    @Override
    public int getWidth() {
        return 1;
    }

    public AvatarSprite getAvatarSprite() {
        return avatarSprite;
    }

    public boolean isDead() {
        return getHP() <= 0;
    }

    public CharacterClass getCharClass() {
        return charClass;
    }

    public SkillCheckResult testSkill(Skill skill, int difficulty, int bonus) {
        return new SkillCheckResult(getRankForSkill(skill), difficulty, bonus);
    }

    public SkillCheckResult testSkill(Skill skill, int difficulty) {
        return new SkillCheckResult(getRankForSkill(skill), difficulty);
    }

    public SkillCheckResult testSkill(Skill skill) {
        return new SkillCheckResult(getRankForSkill(skill));
    }

    public int getMaxSP() {
        return 2;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public double calcAverageDamage() {
        double sum = 0.0;
        for (int roll=1; roll <=10; roll++) {
            int modified = roll + getRankForSkill(equipment.getWeapon().getSkillToUse(this));
            if (roll == 10) {
                sum += equipment.getWeapon().getDamage(modified, this) * 2;
            } else {
                sum += equipment.getWeapon().getDamage(modified, this);
            }
        }
        return sum / 10.0;
    }

    public void unequipWeapon() {
        if (!(equipment.getWeapon() instanceof UnarmedCombatWeapon)) {
            party.getInventory().add(equipment.getWeapon());
        }
        equipment.setWeapon(new UnarmedCombatWeapon());
    }

    public void equipWeaponFromInventory(Weapon weapon) {
        unequipWeapon();
        party.getInventory().remove(weapon);
        equipment.setWeapon(weapon);
    }

    public void unequipArmor() {
        if (!(equipment.getClothing() instanceof JustClothes)) {
            party.getInventory().add(equipment.getClothing());
        }
        equipment.setClothing(new JustClothes());
    }

    public void equipClothingFromInventory(Clothing clothing) {
        unequipArmor();
        party.getInventory().remove(clothing);
        equipment.setClothing(clothing);
    }

    public void unequipAccessory() {
        if (equipment.getAccessory() != null) {
            party.getInventory().add(equipment.getAccessory());
        }
        equipment.setAccessory(null);
    }

    public void equipAccessoryFromInventory(Accessory item) {
        unequipAccessory();
        party.getInventory().remove(item);
        equipment.setAccessory(item);
    }

    public Set<Skill> getSkillSet() {
        Set<Skill> skillSet = new TreeSet<>();
        skillSet.addAll(getCharClass().getSkills());
        skillSet.addAll(race.getSkills());
        return skillSet;
    }

    public String getOtherClasses() {
        StringBuilder bldr = new StringBuilder();
        for (CharacterClass cls : classes) {
            if (cls != charClass) {
                bldr.append(cls.getShortName());
                bldr.append(", ");
            }
        }
        return bldr.substring(0, bldr.length()-2);
    }

    public CharacterClass[] getClasses() {
        return classes;
    }

    public void setClass(CharacterClass newClass) {
        if (charClass != null) {
            charClass.takeClothesOff(appearance);
        }
        this.charClass = newClass;
        this.appearance.reset();
        this.appearance.setClass(charClass);
        this.avatarSprite = makeAvatarSprite();
        if (!newClass.canUseHeavyArmor()) {
            if (equipment.getClothing().isHeavy()) {
                unequipArmor();
            }
            if (equipment.getAccessory() != null && equipment.getAccessory().isHeavy()) {
                unequipAccessory();
            }
        }
    }

    public GameCharacter copy() {
        return new GameCharacter(firstName, lastName, race, charClass, appearance.copy(), classes, equipment);
    }

    public void addToXP(int toAdd) {
        if (level == 0) {
            return;
        }
        currentXp += toAdd;
        for (int i = 0; i < XP_LEVELS.length; ++i) {
            if (currentXp < XP_LEVELS[i]) {
                break;
            }
            level = i+1;
        }
    }

    public int getXpToNextLevel() {
        if (level == 6) {
            return -1;
        }
        return XP_LEVELS[level] - currentXp;
    }

    public void setLevel(int i) {
        this.level = i;
        if (i > 0) {
            this.currentXp = XP_LEVELS[i - 1];
        }
    }

    public void setRandomStartingClass() {
        setClass(MyRandom.sample(Arrays.asList(classes)));
        this.equipment = charClass.getStartingEquipment();
        super.setCurrentHp(getMaxHP());
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public boolean getGender() {
        return appearance.isFemale();
    }

    public void transferEquipmentToParty(Party party) {
        equipment.transferToParty(party.getInventory());
    }

    public void getAttackedBy(Enemy enemy, Model model, CombatEvent combatEvent) {
        int damage = enemy.getDamage();
        int reduction = Math.min(damage, Math.max(0, getAP() - MyRandom.rollD10() + 1));
        String reductionString = "";
        if (getAP() > 0) {
            reductionString = " (reduced by " + reduction + ")";
        }
        damage = damage - reduction;
        addToHP(-1 * damage);
        combatEvent.println(enemy.getName() + " deals " + damage + " damage to " + getName() + reductionString + ".");
        combatEvent.addStrikeEffect(this, model, damage, false);
        equipment.wielderWasAttackedBy(enemy, combatEvent);
    }
}
