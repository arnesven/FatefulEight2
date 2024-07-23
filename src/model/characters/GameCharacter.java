package model.characters;

import model.actions.*;
import model.characters.appearance.PortraitClothing;
import model.characters.appearance.SkeletonAppearance;
import model.combat.*;
import model.Model;
import model.Party;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.conditions.*;
import model.enemies.Enemy;
import model.items.*;
import model.items.accessories.Accessory;
import model.items.accessories.ShieldItem;
import model.items.clothing.Clothing;
import model.items.clothing.JustClothes;
import model.items.spells.QuickenedCondition;
import model.items.spells.QuickeningSpell;
import model.items.weapons.NaturalWeapon;
import model.items.weapons.UnarmedCombatWeapon;
import model.items.weapons.VampireClawsWeapon;
import model.items.weapons.Weapon;
import model.races.ColoredRace;
import model.races.Race;
import model.states.CombatEvent;
import model.states.events.RareBirdEvent;
import sound.SoundEffects;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import view.BorderFrame;
import view.LogView;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.AvatarSprite;
import view.sprites.DamageValueEffect;
import view.sprites.DieRollAnimation;
import view.sprites.Sprite;
import view.subviews.CombatSubView;
import view.widget.HealthBar;

import java.awt.Point;
import java.util.*;

public class GameCharacter extends Combatant {
    private static final MyColors[] xpColors = new MyColors[]{MyColors.LIGHT_PINK, MyColors.CYAN, MyColors.WHITE, MyColors.LIGHT_YELLOW, MyColors.LIGHT_GREEN,
                                                        MyColors.LIGHT_BLUE, MyColors.BEIGE, MyColors.WHITE, MyColors.LIGHT_PINK, MyColors.LIGHT_YELLOW};
    private static final MyColors DEFAULT_TEXT_COLOR = MyColors.LIGHT_GRAY;
    private static final int MAX_ATTITUDE = 40;
    private static final int MIN_ATTITUDE = -40;
    private static final int[] XP_LEVELS = new int[]{0, 100, 250, 450, 700, 1000, 1400,
                                                    2000, 2500, 3000, 3500, 4000, 4500, 5000};

    private final String firstName;
    private final String lastName;
    private final Race race;
    private final CharacterClass[] classes;
    private AvatarSprite avatarSprite;
    private CharacterClass charClass;
    private CharacterAppearance appearance;
    private SkeletonAppearance deadAppearance;
    private int level;
    private Equipment equipment;
    private final Map<Skill, SkillBonus> temporarySkillBonuses = new HashMap<>();
    private int currentSp = 1;
    private int currentXp = 0;
    private Party party;
    private int xpGivenCounter = 0;
    private final Map<GameCharacter, Integer> attitudes = new HashMap<>();
    private final Personality personality;
    private final SpellMasteries spellMasteries = new SpellMasteries();

    public GameCharacter(String firstName, String lastName, Race race, CharacterClass charClass, CharacterAppearance appearance,
                         CharacterClass[] classes, Equipment equipment) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.race = race;
        this.appearance = appearance;
        this.level = 1;
        this.classes = classes;
        this.equipment = equipment;
        deadAppearance = new SkeletonAppearance(appearance.getShoulders(), appearance.getGender());
        setClass(charClass);
        personality = new Personality();
        super.setCurrentHp(getMaxHP());
    }

    public GameCharacter(String firstName, String lastName, Race race,
                         CharacterClass charClass, CharacterAppearance appearance, CharacterClass[] classes) {
        this(firstName, lastName, race, charClass, appearance, classes, charClass.getStartingEquipment());
    }

    public static int getXPForNextLevel(int level) {
        if (level >= XP_LEVELS.length) {
            return 99999;
        }
        return XP_LEVELS[level];
    }

    private AvatarSprite makeAvatarSprite() {
        if (appearance.hasAlternateSkinColor()) {
            return charClass.getAvatar(new ColoredRace(appearance.getAlternateSkinColor(), race), appearance);
        }
        return charClass.getAvatar(race, appearance);
    }

    public void drawYourself(ScreenHandler screenHandler, int col, int row, MyColors color) {
        String nameString = this.getFullName();
        if (this.getFullName().length() > BorderFrame.CHARACTER_WINDOW_COLUMNS) {
            nameString = getFirstName().charAt(0) + ". " + getLastName();
        }
        BorderFrame.drawString(screenHandler, nameString, col, row, color);

        String raceAndClassString = this.getRace().getName() + " " + this.getGameClass() + " Lvl " + this.getLevel();
        BorderFrame.drawString(screenHandler, raceAndClassString, col, row+1, DEFAULT_TEXT_COLOR);
        HealthBar.drawHealthBar(screenHandler, this, col, row+2);

        if (party != null && party.getBench().contains(this)) {
            BorderFrame.drawString(screenHandler, "ABSENT", col+1, row+5, DEFAULT_TEXT_COLOR);
        } else {
            drawAppearance(screenHandler, col, row + 3);
        }
        MyColors xpColor = DEFAULT_TEXT_COLOR;
        String xPString = String.format("%5d XP", this.getXP());
        if (xpGivenCounter > 0) {
            xpColor = xpColors[--xpGivenCounter];
            xPString = String.format("%5d*XP", this.getXP());
        }
        BorderFrame.drawString(screenHandler, xPString, col+8, row+2, xpColor);
        BorderFrame.drawString(screenHandler, String.format("%2d AP", this.getAP()), col+17, row+2, DEFAULT_TEXT_COLOR);
        BorderFrame.drawString(screenHandler, String.format("%2d/%2d HP", this.getHP(), this.getMaxHP()), col+8, row+3, HealthBar.getHealthColor(this.getHP(), this.getMaxHP()));
        BorderFrame.drawString(screenHandler, String.format("%1d SP", this.getSP()), col+18, row+3, getStaminaColor());
        BorderFrame.drawString(screenHandler, String.format("SPEED %2d", this.getSpeed()), col+8, row+4, DEFAULT_TEXT_COLOR);
        String leaderIcon = new String(new char[]{0xC3, 0xC4, 0xC5, 0xC6});
        BorderFrame.drawString(screenHandler, String.format("%s", isLeader() ? leaderIcon : ""), col+18, row+4, MyColors.WHITE);

        String status = this.getStatus();
        if (status.length() <= 7) {
            BorderFrame.drawString(screenHandler, String.format("STATUS %s", this.getStatus()), col+8, row+5, DEFAULT_TEXT_COLOR);
        } else if (status.length() <= 15) {
            BorderFrame.drawString(screenHandler, String.format("%s", this.getStatus()), col+8, row+5, DEFAULT_TEXT_COLOR);
        } else {
            status = status.substring(0, 3) + "...";
            BorderFrame.drawString(screenHandler, String.format("STATUS %s", this.getStatus()), col+8, row+5, DEFAULT_TEXT_COLOR);
        }

        equipment.drawYourself(screenHandler, col, row);
    }

    private MyColors getStaminaColor() {
        return getSP() == 0 ? MyColors.YELLOW : (getSP() >= getMaxSP() ? MyColors.GREEN : DEFAULT_TEXT_COLOR);
    }

    public void drawAppearance(ScreenHandler screenHandler, int col, int row) {
        if (!isDead()) {
            appearance.drawYourself(screenHandler, col, row);
        } else {
            deadAppearance.drawYourself(screenHandler, col, row);
        }
    }

    public boolean isLeader() {
        if (party == null) {
            return false;
        }
        return party.getLeader() == this;
    }

    public int getSpeed() {
        int levelBonus = Math.max(0, (level-2) / 3);
        int heavyClothing = equipment.getClothing().isHeavy() ? -2 : 0;
        int heavyAccessory = equipment.getAccessory() != null && equipment.getAccessory().isHeavy() ? -1 : 0;
        int conditionBonus = MyLists.intAccumulate(getConditions(), Condition::getSpeedBonus);
        return charClass.getSpeed() + race.getSpeedModifier() + levelBonus +
                equipment.getSpeedModifiers() + heavyClothing +
                heavyAccessory + conditionBonus;
    }

    @Override
    public void addToHP(int i) {
        super.addToHP(i);
    }

    public boolean canAttackInCombat() {
        if (party == null) {
            return true;
        }
        return !party.getBackRow().contains(this) || mayAttackFromBackRow();
    }

    public void performAttack(Model model, CombatEvent combatEvent, Combatant target) {
        model.getTutorial().combatAttacks(model);
        for (int i = 0; i < equipment.getWeapon().getNumberOfAttacks(); i++) {
            if (target.isDead()) {
                return;
            }
            doOneAttack(model, combatEvent, target, false, 0, equipment.getWeapon().getCriticalTarget());
            if (i < equipment.getWeapon().getNumberOfAttacks() - 1) {
                model.getLog().waitForAnimationToFinish();
            }
        }
    }

    public void doOneAttack(Model model, CombatEvent combatEvent, Combatant target, boolean sneakAttack, int extraDamage, int crit) {
        combatEvent.print(getFirstName() + " attacks " + target.getName());
        int bonus = getAttackBonusesFromConditions();
        SkillCheckResult result = testSkill(model, equipment.getWeapon().getSkillToUse(this),
                SkillCheckResult.NO_DIFFICULTY, bonus);
        int damage = equipment.getWeapon().getDamage(result.getModifiedRoll(), this);
        String extraInfo = " (" + result.asString() + " on [" + equipment.getWeapon().getDamageTableAsString() + "]";
        if (extraDamage > 0) {
            damage += extraDamage;
            extraInfo += " +" + extraDamage;
        }
        if (sneakAttack) {
            damage *= 3;
            extraInfo += " x3 Sneak Attack";
        }
        if (result.isCritical(crit) && equipment.getWeapon().allowsCriticalHits()) {
            damage *= 2;
            extraInfo += LogView.YELLOW_COLOR +  " x2 Critical Hit" + LogView.DEFAULT_COLOR;
        }
        extraInfo += ")";
        combatEvent.println(", dealing " + damage + " damage." + extraInfo);
        combatEvent.addSpecialEffect(target, equipment.getWeapon().getEffectSprite());
        if (damage > 0) {
            MyColors damageColor = equipment.getWeapon().isPhysicalDamage() ?
                    DamageValueEffect.STANDARD_DAMAGE : DamageValueEffect.MAGICAL_DAMAGE;
            if (result.isCritical(crit) && equipment.getWeapon().allowsCriticalHits()) {
                damageColor = DamageValueEffect.CRITICAL_DAMAGE;
            }
            combatEvent.addFloatyDamage(target, damage, damageColor);
            SoundEffects.playSound(equipment.getWeapon().getAttackSound());
        } else {
            combatEvent.addFloatyText(target, CombatSubView.MISS_TEXT);
        }
        combatEvent.doDamageToEnemy(target, damage, this);
        equipment.getWeapon().didOneAttackWith(model, combatEvent, this, target, damage, crit);
        combatEvent.blockSneakAttackFor(this);
    }


    private boolean mayAttackFromBackRow() {
        return equipment.getWeapon().isRangedAttack();
    }

    public int getRankForSkill(Skill skill) {
        if (skill.areEqual(Skill.MagicAny)) {
            int best = 0;
            for (Skill s : Skill.values()) {
                if (s.isMagic() && charClass.getWeightForSkill(s) > best) {
                    best = charClass.getWeightForSkill(s);
                    skill = s;
                }
            }
        }
        int tempBonus = 0;
        if (temporarySkillBonuses.containsKey(skill)) {
            tempBonus = temporarySkillBonuses.get(skill).getBonus();
        }
        Skill finalSkill = skill;
        int conditionBonus = MyLists.intAccumulate(getConditions(), (Condition cond) -> cond.getBonusForSkill(finalSkill));
        return Skill.getRankForSkill(charClass.getWeightForSkill(skill), getLevel())
                + race.getBonusForSkill(skill) + equipment.getBonusForSkill(skill) + tempBonus + conditionBonus;
    }

    @Override
    public String getName() {
        return getFullName();
    }

    public int getSP() {
        return currentSp;
    }

    public int getAP() {
        int bonus = 0;
        for (Condition cond : getConditions()) {
            bonus += cond.getArmorBonus();
        }
        return equipment.getTotalAP() + bonus;
    }

    public int getXP() {
        return currentXp;
    }

    public int getMaxHP() {
        int conditionBonus = MyLists.intAccumulate(getConditions(), Condition::getHealthBonus);
        return charClass.getHP() + race.getHPModifier() + equipment.getHealthBonus() + level + conditionBonus;
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
        if (this.getLastName().equals("")) {
            return this.getFirstName();
        }
        return this.getFirstName() + " " + this.getLastName();
    }

    private String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void addToSP(int i) {
        currentSp = Math.max(0, Math.min(currentSp + i, getMaxSP()));
    }

    public void drawAvatar(ScreenHandler screenHandler, int xpos, int ypos) {
        if (isDead()) {
            screenHandler.register("avatarfor" + getFullName() + "dead", new Point(xpos, ypos), avatarSprite.getDead());
        } else {
            screenHandler.register("avatarfor" + getFullName(), new Point(xpos, ypos), avatarSprite);
            if (!(equipment.getWeapon() instanceof NaturalWeapon)) {
                Sprite spr = equipment.getWeapon().getOnAvatarSprite(this);
                if (spr != null) {
                    screenHandler.register("avatarweapon" + getFullName(), new Point(xpos, ypos), spr);
                }
            }
            if (equipment.getAccessory() instanceof ShieldItem) {
                Sprite spr = ((ShieldItem)equipment.getAccessory()).getOnAvatarSprite(this);
                if (spr != null) {
                    screenHandler.register("avatarshield" + getFullName(), new Point(xpos, ypos), spr);
                }
            }
        }
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, int xpos, int ypos, Sprite initiativeSymbol) {
        if (!hasCondition(InvisibilityCondition.class)) {
            Condition cond = MyLists.find(getConditions(), Condition::hasAlternateAvatar);
            if (cond != null) {
                cond.drawYourself(screenHandler, xpos, ypos);
            } else {
                drawAvatar(screenHandler, xpos, ypos);
            }

//            if (hasCondition(WerewolfFormCondition.class)) {
//                AvatarSprite werewolfAvatar = ((WerewolfFormCondition) getCondition(WerewolfFormCondition.class)).getAvatar();
//                if (isDead()) {
//                    screenHandler.register("wwavatarfor" + getFullName() + "dead", new Point(xpos, ypos), werewolfAvatar.getDead());
//                } else {
//                    screenHandler.register("wwavatarfor" + getFullName(), new Point(xpos, ypos), werewolfAvatar);
//                }
//            } else if (hasCondition(BatFormCondition.class) && !isDead()) {
//                Sprite avatar = ((BatFormCondition) getCondition(BatFormCondition.class)).getAvatar();
//                screenHandler.register("batavatar" + getFullName(), new Point(xpos, ypos), avatar);
//            }
        }
        screenHandler.register(getName() + "inittoken", new Point(xpos+3, ypos+3), initiativeSymbol);
        drawConditions(screenHandler, xpos, ypos);
    }

    @Override
    public Sprite getCombatCursor(Model model) {
        return party.getCursorForPartyMember(this);
    }

    @Override
    public int getWidth() {
        return 1;
    }

    @Override
    public String getDeathSound() {
        if (getGender()) {
            return "female_scream";
        }
        return "male_scream";
    }

    public AvatarSprite getAvatarSprite() {
        return avatarSprite;
    }

    public CharacterClass getCharClass() {
        return charClass;
    }

    private int getRankAndRemoveTempBonus(Skill skill) {
        int total = getRankForSkill(skill);
        if (temporarySkillBonuses.containsKey(skill) && !temporarySkillBonuses.get(skill).isPersistent()) {
            temporarySkillBonuses.remove(skill);
        }
        return total;
    }

    public SkillCheckResult testSkillHidden(Skill skill, int difficulty, int bonus) {
        return new SkillCheckResult(getRankAndRemoveTempBonus(skill), difficulty, bonus);
    }

    public SkillCheckResult testSkill(Model model, Skill skill, int difficulty, int bonus) {
        SkillCheckResult result = new SkillCheckResult(getRankAndRemoveTempBonus(skill), difficulty, bonus);
        if (party != null && model.getSettings().animateDieRollsEnabled()) {
            DieRollAnimation die = party.addDieRollAnimation(this, result.getUnmodifiedRoll());
            while (die.blocksGame()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (result.isSuccessful()) {
                SoundEffects.successSkill();
            } else if (result.isFailure()) {
                SoundEffects.failedSkill();
            }
        }
        return result;
    }

    public SkillCheckResult testSkill(Model model, Skill skill, int difficulty) {
        return testSkill(model, skill, difficulty, 0);
    }

    public SkillCheckResult testSkill(Model model, Skill skill) {
        return testSkill(model, skill, SkillCheckResult.NO_DIFFICULTY, 0);
    }

    public int getMaxSP() {
        int levelBonus = 0;
        if (level > 6) {
            levelBonus = (level - 5) / 2;
        }
        int equipmentBonus = 0;
        if (equipment.getAccessory() != null) {
            equipmentBonus = equipment.getAccessory().getSPBonus();
        }
        int conditionBonus = MyLists.intAccumulate(getConditions(), Condition::getStaminaBonus);
        return 2 + levelBonus + equipmentBonus + conditionBonus;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public double calcAverageDamage() {
        double sum = 0.0;
        int rank = getRankForSkill(equipment.getWeapon().getSkillToUse(this));
        for (int i = 0; i < equipment.getWeapon().getNumberOfAttacks(); ++i) {
            for (int roll = 1; roll <= 10; roll++) {
                int modified = roll + rank;
                if (roll >= equipment.getWeapon().getCriticalTarget()) {
                    sum += equipment.getWeapon().getDamage(modified, this) * 2;
                } else {
                    sum += equipment.getWeapon().getDamage(modified, this);
                }
            }
        }
        return sum / 10.0;
    }

    public void unequipWeapon() {
        if (!(equipment.getWeapon() instanceof UnarmedCombatWeapon)) {
            party.getInventory().add(equipment.getWeapon());
        }
        if (ClawsVampireAbility.canDoAbility(this)) {
            equipment.setWeapon(new VampireClawsWeapon());
        } else {
            equipment.setWeapon(new UnarmedCombatWeapon());
        }
    }

    public void equipWeaponFromInventory(Weapon weapon) {
        unequipWeapon();
        party.getInventory().remove(weapon);
        equipment.setWeapon(weapon);
    }

    public void unequipArmor() {
        if (!(equipment.getClothing() instanceof JustClothes)) {
            if (party != null) {
                party.getInventory().add(equipment.getClothing());
            }
        }
        equipment.setClothing(new JustClothes());
    }

    public void equipClothingFromInventory(Clothing clothing) {
        unequipArmor();
        party.getInventory().remove(clothing);
        equipment.setClothing(clothing);
    }

    public void unequipAccessory() {
        if (equipment.getAccessory() != null && party != null) {
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
        if (newClass == charClass) { // No need to change anything.
            return;
        }
        if (charClass != null) {
            charClass.takeClothesOff(appearance);
        }
        this.charClass = newClass;
        this.appearance.reset(); // Fix hair sprites.
        this.appearance.setClass(charClass);
        this.deadAppearance.setClass(charClass);
        this.avatarSprite = makeAvatarSprite();
        if (!newClass.canUseHeavyArmor()) {
            if (equipment.getClothing().isHeavy()) {
                unequipArmor();
            }
            if (equipment.getAccessory() != null && equipment.getAccessory().isHeavy()) {
                unequipAccessory();
            }
        }
        if (getHP() > getMaxHP()) {
            addToHP(getMaxHP() - getHP());
        }
    }

    public GameCharacter copy() {
        GameCharacter clone = new GameCharacter(firstName, lastName, race, charClass, appearance.copy(), classes, equipment);
        clone.setLevel(getLevel());
        return clone;
    }

    public void addToXP(int toAdd) {
        if (level == 0) {
            return;
        }
        currentXp += toAdd;
        xpGivenCounter = xpColors.length;
        for (int i = 0; i < XP_LEVELS.length; ++i) {
            if (currentXp < XP_LEVELS[i]) {
                break;
            }
            level = i+1;
        }
    }

    public int getXpToNextLevel() {
        if (level == XP_LEVELS.length) {
            return 99999;
        }
        return XP_LEVELS[level] - currentXp;
    }

    public void setLevel(int i) {
        this.level = i;
        if (i > 0) {
            this.currentXp = XP_LEVELS[i - 1];
        }
        this.setCurrentHp(getMaxHP());
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
        combatEvent.blockSneakAttackFor(this);
        combatEvent.addSpecialEffect(this, enemy.getStrikeEffect());
        MyPair<Integer, Boolean> pair = enemy.calculateBaseDamage(model.getParty().getBackRow().contains(this));
        int damage = pair.first;
        boolean critical = pair.second;
        if (checkForEvade(enemy)) {
            combatEvent.addFloatyText(this, CombatSubView.EVADE_TEXT);
            combatEvent.println(getFirstName() + " evaded " + enemy.getName() + "'s attack! ");
            model.getTutorial().evading(model);
            RiposteCombatAction.doRiposte(model, combatEvent, this, enemy);
            combatEvent.getStatistics().addToAvoidedDamage(damage);
            return;
        }
        if ((checkForBlock(enemy) && enemy.getAttackBehavior().isPhysicalAttack()) ||
                (!enemy.getAttackBehavior().isPhysicalAttack() && hasCondition(WardCondition.class))) {
            combatEvent.addFloatyText(this, CombatSubView.BLOCK_TEXT);
            combatEvent.println(getFirstName() + " blocked " + enemy.getName() + "'s attack!");
            model.getTutorial().blocking(model);
            combatEvent.getStatistics().addToAvoidedDamage(damage);
        } else {
            String reductionString = "";
            if (enemy.getAttackBehavior().isPhysicalAttack() || equipment.applyArmorToMagicAttacks()) {
                int reduction = Math.min(damage, calculateDamageReduction());
                if (getAP() > 0) {
                    reductionString = " (reduced by " + reduction + ")";
                }
                damage = damage - reduction;
                combatEvent.getStatistics().addToReduced(reduction);
            }
            addToHP(-1 * damage);
            if (pair.second) {
                reductionString = ", " + LogView.YELLOW_COLOR + "Critical Hit" + LogView.DEFAULT_COLOR + reductionString;
            }
            combatEvent.println(enemy.getName() + " deals " + damage + " damage to " + getFirstName() + reductionString + ".");
            combatEvent.addFloatyDamage(this, damage, critical ? DamageValueEffect.CRITICAL_DAMAGE : DamageValueEffect.STANDARD_DAMAGE);
            if (party != null) {
                combatEvent.tookDamageTalk(this, damage);
            }
            combatEvent.getStatistics().addEnemyDamage(damage);
        }
        equipment.wielderWasAttackedBy(enemy, combatEvent);
    }

    private boolean checkForBlock(Enemy enemy) {
        int defendBonus = DefendCombatAction.isDefending(this) ? 2 : 0;
        if (equipment.getAccessory() instanceof ShieldItem) {
            return MyRandom.rollD10() <= ((ShieldItem)equipment.getAccessory()).getBlockChance() + defendBonus;
        }
        return false;
    }

    private boolean checkForEvade(Enemy enemy) {
        int speedDiff = Math.max(getSpeed() - enemy.getSpeed(), 0) / 2;
        speedDiff += RiposteCombatAction.getEvadeBonus(this);
        int roll = MyRandom.rollD10();
        return roll <= speedDiff && roll != 10;
    }

    private int calculateDamageReduction() {
        //                       1  2  3  4  5  6  7   8   9  10
        int[] levels = new int[]{4, 4, 5, 6, 7, 8, 9, 10, 10, 10,
                10, 10, 10, 10, 10, 10, 10, 10, 10, 10}; // assuming 20 AP is absolute maximum.
        int ap = getAP();
        while (ap > 0) {
            int roll = MyRandom.rollD10();
            if (roll >= levels[ap-1]) {
                return ap;
            }
            ap--;
        }
        return 0;
    }

    public boolean canAssumeClass(int id) {
        for (CharacterClass cc : classes) {
            if (cc.id() == id) {
                return true;
            }
        }
        return false;
    }

    public void addTemporaryBonus(Skill skill, int bonus, boolean persistent) {
        temporarySkillBonuses.put(skill, new SkillBonus(bonus, persistent));
    }

    public void removeTemporaryBonus(Skill skill) {
        temporarySkillBonuses.remove(skill);
    }

    public CharacterAppearance getAppearance() {
        return appearance;
    }

    public boolean canChangeClothing() {
        return true;
    }

    public boolean canChangeAccessory() {
        return true;
    }

    public int getAttitude(GameCharacter target) {
        if (!attitudes.containsKey(target)) {
            attitudes.put(target, 0);
        }
        return attitudes.get(target);
    }

    public void addToAttitude(GameCharacter target, int i) {
        if (!attitudes.containsKey(target)) {
            attitudes.put(target, 0);
        }
        int result = Math.max(MIN_ATTITUDE, Math.min(MAX_ATTITUDE, attitudes.get(target) + i));
        attitudes.put(target, result);
    }

    public int getCharacterStrength() {
        return 14 + getLevel();
    }

    public void setSpecificClothing(PortraitClothing clothes) {
        appearance.setSpecificClothing(clothes);
        if (clothes.hasSpecialAvatar()) {
            avatarSprite = clothes.makeAvatar(race, appearance);
        }
    }

    public void removeSpecificClothing() {
        avatarSprite = makeAvatarSprite();
        appearance.setClass(charClass);
    }

    public boolean isSpecialCharacter() {
        return charClass.isSpecialCharacter();
    }

    public boolean hasPersonality(PersonalityTrait trait) {
        return personality.contains(trait);
    }

    protected void addToPersonality(PersonalityTrait trait) {
        personality.add(trait);
    }

    public SpellMasteries getMasteries() {
        return spellMasteries;
    }

    public void setAppearance(CharacterAppearance appearance) {
        appearance.setClass(charClass);
        this.appearance = appearance;
        deadAppearance = new SkeletonAppearance(appearance.getShoulders(), appearance.getGender());
        avatarSprite = makeAvatarSprite();
    }

    @Override
    protected boolean hasConditionImmunity(Condition cond) {
        return equipment.grantsConditionImmunity(cond);
    }

    public int getCarryCap() {
        int conditionBonus = MyLists.intAccumulate(getConditions(), Condition::getCarryCapBonus);
        return getRace().getCarryingCapacity() + conditionBonus;
    }
}
