package model.characters;

import model.actions.AbilityCombatAction;
import model.actions.BasicCombatAction;
import model.actions.DefendCombatAction;
import model.actions.RiposteCombatAction;
import model.characters.appearance.PortraitClothing;
import model.characters.appearance.SkeletonAppearance;
import model.combat.*;
import model.Model;
import model.Party;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.Enemy;
import model.items.*;
import model.items.accessories.Accessory;
import model.items.accessories.ShieldItem;
import model.items.clothing.Clothing;
import model.items.clothing.JustClothes;
import model.items.spells.CombatSpell;
import model.items.spells.QuickenedCondition;
import model.items.spells.QuickeningSpell;
import model.items.weapons.NaturalWeapon;
import model.items.weapons.UnarmedCombatWeapon;
import model.items.weapons.Weapon;
import model.races.Race;
import model.states.CombatEvent;
import sound.SoundEffects;
import sound.SoundManager;
import sprites.CombatSpeechBubble;
import util.MyPair;
import util.MyRandom;
import view.BorderFrame;
import view.LogView;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.AvatarSprite;
import view.sprites.DamageValueEffect;
import view.sprites.Sprite;
import view.subviews.CombatSubView;
import view.widget.HealthBar;

import java.awt.Point;
import java.util.*;

public class GameCharacter extends Combatant {
    private static MyColors[] xpColors = new MyColors[]{MyColors.LIGHT_PINK, MyColors.CYAN, MyColors.WHITE, MyColors.LIGHT_YELLOW, MyColors.LIGHT_GREEN,
                                                        MyColors.LIGHT_BLUE, MyColors.BEIGE, MyColors.WHITE, MyColors.LIGHT_PINK, MyColors.LIGHT_YELLOW};
    private static final MyColors DEFAULT_TEXT_COLOR = MyColors.LIGHT_GRAY;
    private static final int MAX_SP = 2;
    private static final int[] XP_LEVELS = new int[]{0, 100, 250, 450, 700, 1000, 1400,
                                                    2000, 2500, 3000, 3500, 4000, 4500, 5000};
    private static final int NO_DIFFICULTY = Integer.MAX_VALUE;

    private final String firstName;
    private final String lastName;
    private final Race race;
    private final CharacterClass[] classes;
    private AvatarSprite avatarSprite;
    private CharacterClass charClass;
    private final CharacterAppearance appearance;
    private final SkeletonAppearance deadAppearance;
    private int level;
    private Equipment equipment;
    private final Map<Skill, SkillBonus> temporarySkillBonuses = new HashMap<>();
    private int currentSp = 1;
    private int currentXp = 0;
    private Party party;
    private int xpGivenCounter = 0;
    private final Map<GameCharacter, Integer> attitudes = new HashMap<>();

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
        return getSP() == 0 ? MyColors.YELLOW : (getSP() == getMaxSP() ? MyColors.GREEN : DEFAULT_TEXT_COLOR);
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
        int heavyClothing = equipment.getClothing().isHeavy() ? -2 : 0;
        int heavyAccessory = equipment.getAccessory() != null && equipment.getAccessory().isHeavy() ? -1 : 0;
        int quickened = hasCondition(QuickenedCondition.class) ? QuickeningSpell.SPEED_BONUS : 0;
        return charClass.getSpeed() + race.getSpeedModifier() + equipment.getSpeedModifiers() + heavyClothing + heavyAccessory + quickened;
    }

    @Override
    public void addToHP(int i) {
        super.addToHP(i);
    }

    private boolean canAttackInCombat() {
        if (party == null) {
            return true;
        }
        return !party.getBackRow().contains(this) || mayAttackFromBackRow();
    }

    private void performAttack(Model model, CombatEvent combatEvent, Combatant target) {
        model.getTutorial().combatAttacks(model);
        for (int i = 0; i < equipment.getWeapon().getNumberOfAttacks(); i++) {
            if (target.isDead()) {
                return;
            }
            doOneAttack(combatEvent, target, false, 0, equipment.getWeapon().getCriticalTarget());
            if (i < equipment.getWeapon().getNumberOfAttacks() - 1) {
                model.getLog().waitForAnimationToFinish();
            }
        }
    }

    public void doOneAttack(CombatEvent combatEvent, Combatant target, boolean sneakAttack, int extraDamage, int crit) {
        int bonus = getAttackBonusesFromConditions();
        SkillCheckResult result = testSkill(equipment.getWeapon().getSkillToUse(this), NO_DIFFICULTY, bonus);
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
        combatEvent.println(getFirstName() + " attacks " + target.getName() + ", dealing " + damage + " damage." + extraInfo);
        combatEvent.addSpecialEffect(target, equipment.getWeapon().getEffectSprite());
        if (damage > 0) {
            MyColors damageColor = DamageValueEffect.STANDARD_DAMAGE;
            if (result.isCritical(crit) && equipment.getWeapon().allowsCriticalHits()) {
                damageColor = DamageValueEffect.CRITICAL_DAMAGE;
            }
            combatEvent.addFloatyDamage(target, damage, damageColor);
            SoundEffects.playSound(equipment.getWeapon().getAttackSound());
        } else {
            combatEvent.addFloatyText(target, CombatSubView.MISS_TEXT);
        }
        combatEvent.doDamageToEnemy(target, damage, this);
        equipment.getWeapon().didOneAttackWith(combatEvent, this, target, damage, crit);
        combatEvent.blockSneakAttackFor(this);
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
            combatEvent.print("Trying to escape from combat (D10 roll=" + d10 + ")");
            if (d10 >= 5) {
                combatEvent.println(" >=5, SUCCESS.");
                combatEvent.setPartyFled(true);
            } else {
                combatEvent.println(" <5 FAIL. Can't get away!");
            }
        }
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
        return Skill.getRankForSkill(charClass.getWeightForSkill(skill), getLevel())
                + race.getBonusForSkill(skill) + equipment.getBonusForSkill(skill) + tempBonus;
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
        if (hasCondition(ShiningAegisCondition.class)) {
            bonus += ((ShiningAegisCondition)getCondition(ShiningAegisCondition.class)).getArmorBonus();
        }
        return equipment.getTotalAP() + bonus;
    }

    public int getXP() {
        return currentXp;
    }

    public int getMaxHP() {
        int spellBonus = hasCondition(GiantGrowthCondition.class) ? 2 : 0;
        return charClass.getHP() + race.getHPModifier() + equipment.getHealthBonus() + level + spellBonus;
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
                screenHandler.register("avatarweapon" + getFullName(), new Point(xpos, ypos), equipment.getWeapon().getOnAvatarSprite(this));
            }
            if (equipment.getAccessory() instanceof ShieldItem) {
                screenHandler.register("avatarshield" + getFullName(), new Point(xpos, ypos), ((ShieldItem)equipment.getAccessory()).getOnAvatarSprite(this));
            }
        }
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, int xpos, int ypos, Sprite initiativeSymbol) {
        if (!hasCondition(InvisibilityCondition.class)) {
            drawAvatar(screenHandler, xpos, ypos);
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

    public List<CombatAction> getCombatActions(Model model, Combatant target, CombatEvent combatEvent) {
        List<CombatAction> result = new ArrayList<>();
        if (canAttackInCombat() && target.canBeAttackedBy(this)) {
            result.add(new BasicCombatAction("Attack", true) {
                @Override
                protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
                    performer.performAttack(model, combat, target);
                }
            });
        }
        if (isLeader() && combatEvent.fleeingEnabled()) {
            result.add(new BasicCombatAction("Flee", false) {
                @Override
                protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
                    performFleeFromBattle(model, combat);
                }
            });
        }

        if (model.getParty().getPartyMembers().contains(this)) {
            Set<UsableItem> usableItems = new HashSet<>();
            usableItems.addAll(model.getParty().getInventory().getPotions());
            usableItems.addAll(model.getParty().getInventory().getCombatScrolls());
            if (usableItems.size() > 0) {
                result.add(new ItemCombatAction(usableItems, target));
            }

            List<CombatSpell> combatSpells = model.getParty().getInventory().getCombatSpells();
            if (!combatSpells.isEmpty()) {
                result.add(new SpellCombatAction(combatSpells, target));
            }

            AbilityCombatAction abilities = new AbilityCombatAction(this, target);
            if (abilities.getInnerActions(model).size() > 0 ) {
                result.add(abilities);
            }
        }

        if (combatEvent.canDelay(this)) {
            result.add(new BasicCombatAction("Delay", false) {
                @Override
                protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
                    combat.delayCombatant(performer);
                }
            });
        }

        result.add(new BasicCombatAction("Pass", false) {
            @Override
            protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
                combat.println("");
            }
        });
        return result;
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

    public SkillCheckResult testSkill(Skill skill, int difficulty, int bonus) {
        return new SkillCheckResult(getRankAndRemoveTempBonus(skill), difficulty, bonus);
    }

    public SkillCheckResult testSkill(Skill skill, int difficulty) {
        return new SkillCheckResult(getRankAndRemoveTempBonus(skill), difficulty);
    }

    public SkillCheckResult testSkill(Skill skill) {
        return new SkillCheckResult(getRankAndRemoveTempBonus(skill));
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
        return 2 + levelBonus + equipmentBonus;
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
        equipment.setWeapon(new UnarmedCombatWeapon());
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
        if (charClass != null) {
            charClass.takeClothesOff(appearance);
        }
        this.charClass = newClass;
        this.appearance.setClass(charClass);;
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
        if (checkForEvade(enemy)) {
            combatEvent.addFloatyText(this, CombatSubView.EVADE_TEXT);
            combatEvent.println(getFirstName() + " evaded " + enemy.getName() + "'s attack! ");
            model.getTutorial().evading(model);
            RiposteCombatAction.doRiposte(combatEvent, this, enemy);
            return;
        }
        if ((checkForBlock(enemy) && enemy.getAttackBehavior().isPhysicalAttack()) ||
                (!enemy.getAttackBehavior().isPhysicalAttack() && hasCondition(WardCondition.class))) {
            combatEvent.addFloatyText(this, CombatSubView.BLOCK_TEXT);
            combatEvent.println(getFirstName() + " blocked " + enemy.getName() + "'s attack!");
            model.getTutorial().blocking(model);
        } else {
            MyPair<Integer, Boolean> pair = enemy.calculateBaseDamage(model.getParty().getBackRow().contains(this));
            int damage = pair.first;
            boolean critical = pair.second;
            String reductionString = "";
            if (enemy.getAttackBehavior().isPhysicalAttack()) {
                int reduction = Math.min(damage, calculateDamageReduction());
                if (getAP() > 0) {
                    reductionString = " (reduced by " + reduction + ")";
                }
                damage = damage - reduction;
            }
            addToHP(-1 * damage);
            if (pair.second) {
                reductionString = ", " + LogView.WHITE_COLOR + "Critical Hit" + LogView.DEFAULT_COLOR + reductionString;
            }
            combatEvent.println(enemy.getName() + " deals " + damage + " damage to " + getFirstName() + reductionString + ".");
            combatEvent.addFloatyDamage(this, damage, critical ? DamageValueEffect.CRITICAL_DAMAGE : DamageValueEffect.STANDARD_DAMAGE);
            if (party != null) {
                combatEvent.tookDamageTalk(this, damage);
            }
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
        return roll <= speedDiff;
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
        attitudes.put(target, attitudes.get(target) + i);
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
}
