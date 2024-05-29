package model.states;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.weapons.BowWeapon;
import model.items.weapons.CrossbowWeapon;
import model.items.weapons.ShortBow;
import model.items.weapons.Weapon;
import sound.ClientSoundManager;
import sound.SoundEffects;
import util.MyRandom;
import view.sprites.Sprite;
import view.subviews.AimingSubView;
import view.subviews.ArcheryTargetSubView;
import view.subviews.CollapsingTransition;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArcheryState extends GameState {
    public  static final int VERY_FAR_DISTANCE = 4;
    public static final int FAR_DISTANCE = 3;
    public static final int MEDIUM_DISTANCE = 2;
    public static final int SHORT_DISTANCE = 1;
    public static final int VERY_SHORT_DISTANCE = 0;

    private static final int[] SHOT_DIFFICULTIES = new int[]{10, 11, 12, 13, 14, 15};
    private static final int[] TARGET_POINTS = new int[]{25, 15, 10, 7, 4, 1};
    private static final int POWER_FACTOR = -4;
    private final int targetDistance;
    private final GameCharacter shooter;
    private final List<GameCharacter> npcShooters = new ArrayList<>();
    private final Weapon bow;
    private final Point wind;
    private ArcheryTargetSubView targetSubView;
    private Map<GameCharacter, Integer> points = new HashMap<>();
    private List<Integer> detailedResults = new ArrayList<>();
    private int shotsPerShooter = 1;
    private Map<GameCharacter, Sprite> fletchings = null;

    public ArcheryState(Model model, GameCharacter shooter, Weapon bowToUse, int targetDistance) {
        super(model);
        this.shooter = shooter;
        this.bow = bowToUse;
        this.targetDistance = targetDistance;
        this.wind = new Point(MyRandom.randInt(-5, 5), MyRandom.randInt(-5, 5));
    }

    @Override
    public GameState run(Model model) {
        this.targetSubView = new ArcheryTargetSubView(bow, wind, targetDistance);
        targetSubView.setCursorEnabled(false);
        if (bow instanceof CrossbowWeapon) {
            targetSubView.setPowerLocked(true);
        }
        CollapsingTransition.transition(model, targetSubView);
        List<GameCharacter> beforeShooters = new ArrayList<>(npcShooters.subList(0, npcShooters.size()/2));
        npcsShoot(beforeShooters);
        characterTakesShot(model);
        List<GameCharacter> afterShooters = new ArrayList<>(npcShooters);
        afterShooters.removeAll(beforeShooters);
        npcsShoot(afterShooters);
        return model.getCurrentHex().getDailyActionState(model);
    }

    private void characterTakesShot(Model model) {
        for (int i = 0; i < shotsPerShooter; ++i) {
            targetSubView.setCursorEnabled(true);
            if (i == 0) {
                print("Aim at the target with the arrow keys. Use space to change the power of the shot. Press enter to fire.");
            } else {
                print("You have " + (shotsPerShooter - i) + " shots remaining. Press enter to fire.");
            }
            waitForReturn();
            targetSubView.setCursorEnabled(false);
            int skillResult = fireArrowSkillCheck(model, shooter);
            int error = 2 * Math.max(0, SHOT_DIFFICULTIES[targetDistance - 1] - skillResult);
            int xError = 0;
            int yError = 0;
            if (error > 0) {
                xError = MyRandom.randInt(error);
                yError = error - xError;
            }
            if (MyRandom.flipCoin()) {
                xError = -xError;
            }
            if (MyRandom.flipCoin()) {
                yError = -yError;
            }
            System.out.println(" ");
            System.out.println("Error: (" + xError + "," + yError + ")");
            Point aim = targetSubView.getAim();
            int power = POWER_FACTOR * (targetSubView.getSelectedPower() - targetDistance);
            System.out.println("Power: (0," + power + ")");
            System.out.println("Aim:   (" + aim.x + "," + aim.y + ")");
            Point newWind = new Point(wind.x * 2, wind.y * 2);
            System.out.println("Wind:  (" + newWind.x + "," + newWind.y + ")");
            System.out.println("-----------------------------------------");
            Point result = new Point(aim.x + newWind.x + xError, aim.y + newWind.y + yError + power);
            System.out.println("SHOT-> (" + result.x + "," + result.y + ")");
            if (!points.containsKey(shooter)) {
                points.put(shooter, 0);
            }
            int shotResult = shootArrow(shooter, result);
            points.put(shooter, points.get(shooter) + shotResult);
            detailedResults.add(shotResult);
        }
        print("Press enter to continue.");
        waitForReturn();
    }

    private int shootArrow(GameCharacter shooter, Point result) {
        int shotResult = targetSubView.getResultForShot(result);
        if (shotResult >= 0) {
            print("The arrow hit the target! ");
            SoundEffects.playTargetHit();
            if (shotResult == 0) {
                println("It's a bullseye - " + TARGET_POINTS[shotResult] + " points!");
            } else {
                println(TARGET_POINTS[shotResult] + " point" + (TARGET_POINTS[shotResult]>1?"s.":"."));
            }
            Sprite sprite = AimingSubView.makeArrowSprite();
            if (fletchings != null) {
                if (fletchings.get(shooter) != null) {
                    sprite = fletchings.get(shooter);
                }
            }
            targetSubView.addArrow(result, sprite);
            return TARGET_POINTS[shotResult];
        }

        if (shotResult == ArcheryTargetSubView.ON_LEG) {
            println("The arrow hit the target's wooden stands.");
            targetSubView.addArrow(result);
            SoundEffects.playHitWood();
        } else if (shotResult == ArcheryTargetSubView.OVER_TARGET) {
            println("The arrow flew over the target.");
            SoundEffects.playMiss();
        } else {
            println("The arrow missed the target and landed in the grass.");
            SoundEffects.playGrass();
        }
        return 0;
    }


    private int fireArrowSkillCheck(Model model, GameCharacter shooter) {
        print(shooter.getFirstName() + " fires the arrow! ");
        SkillCheckResult skillCheckResult;
        do {
            skillCheckResult = shooter.testSkill(model, Skill.Bows);
            println("Roll of " + skillCheckResult.asString() + ".");
            if (shooter.getSP() == 0) {
                break;
            } else {
                print("Use 1 Stamina Point to re-roll? (Y/N) ");
                if (!yesNoInput()) {
                    break;
                }
                shooter.addToSP(-1);
            }
        } while (true);
        return skillCheckResult.getModifiedRoll();
    }


    private void npcsShoot(List<GameCharacter> shooters) {
        for (GameCharacter npc : shooters) {
            for (int i = 0; i < shotsPerShooter; ++i) {
                Weapon bow = npc.getEquipment().getWeapon();
                if (i == 0) {
                    print(npc.getName() + " takes a shot with " + hisOrHer(npc.getGender()) + " "
                            + bow.getName().toLowerCase() + ". ");
                } else {
                    print(npc.getName() + " takes another shot. ");
                }
                getModel().getLog().waitForAnimationToFinish();
                int skillRoll = npc.testSkillHidden(Skill.Bows, SkillCheckResult.NO_DIFFICULTY, 0).getModifiedRoll();

                int power = 0;
                int weaponPower = npc.getEquipment().getWeapon().getDamageTable().length;
                if (npc.getEquipment().getWeapon() instanceof CrossbowWeapon || weaponPower < targetDistance) {
                    power = POWER_FACTOR * (weaponPower - targetDistance);
                }
                int randBoxSize = ArcheryTargetSubView.TARGET_DIAMETER + ArcheryTargetSubView.TARGET_DIAMETER / 2
                        - skillRoll * 2 + targetDistance * 2;
                System.out.println(npc.getName() + " taking a shot...");
                System.out.println("  Skill roll: " + skillRoll);
                System.out.println("  Power error: " + power);
                System.out.println("  Randboxsize: " + randBoxSize);
                Point shot;
                if (randBoxSize < 2) {
                    shot = new Point(0, power);
                } else {
                    shot = new Point(MyRandom.randInt(-randBoxSize / 2, randBoxSize / 2),
                            MyRandom.randInt(-randBoxSize / 2, randBoxSize / 2) + power);
                }
                System.out.println("    Shot: (" + shot.x + "," + shot.y + ")");
                if (!points.containsKey(npc)) {
                    points.put(npc, 0);
                }
                points.put(npc, points.get(npc) + shootArrow(npc, shot));
            }
        }
    }

    public void addNPCShooters(List<GameCharacter> marksmen) {
        npcShooters.addAll(marksmen);
    }

    public Map<GameCharacter, Integer> getPoints() {
        return points;
    }

    public void setShots(int i) {
        this.shotsPerShooter = i;
    }

    public void useFletchings(Map<GameCharacter, Sprite> fletchings) {
        this.fletchings = fletchings;
    }

    public static int getPointsForBullseye() {
        return TARGET_POINTS[0];
    }

    public List<Integer> getDetailedResults() {
        return detailedResults;
    }

    public static BowWeapon getCharactersBowOrDefault(GameCharacter shooter) {
        BowWeapon bowToUse = new ShortBow();
        if (shooter.getEquipment().getWeapon() instanceof BowWeapon) {
            bowToUse = (BowWeapon) shooter.getEquipment().getWeapon();
        }
        return bowToUse;
    }
}
