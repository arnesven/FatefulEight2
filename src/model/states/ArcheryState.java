package model.states;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.weapons.CrossbowWeapon;
import model.items.weapons.Weapon;
import util.MyRandom;
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

    private static final int[] SHOT_DIFFICULTIES = new int[]{8, 10, 12, 14, 16, 18};
    private static final int[] TARGET_POINTS = new int[]{25, 15, 10, 7, 4, 1};
    private static final int POWER_FACTOR = -4;
    private final int targetDistance;
    private final GameCharacter shooter;
    private final List<GameCharacter> npcShooters = new ArrayList<>();
    private final Weapon bow;
    private final Point wind;
    private ArcheryTargetSubView targetSubView;
    private Map<GameCharacter, Integer> points = new HashMap<>();

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
        targetSubView.setCursorEnabled(true);
        print("Aim at the target with the arrow keys. Use space to change the power of the shot. Press enter to fire.");
        waitForReturn();
        targetSubView.setCursorEnabled(false);
        int skillResult = fireArrowSkillCheck(shooter);
        int error = 2 * Math.max(0, SHOT_DIFFICULTIES[targetDistance-1] - skillResult);
        int xError = MyRandom.randInt(error);
        int yError = error - xError;
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
        Point newWind = new Point(wind.x*2, wind.y*2);
        System.out.println("Wind:  (" + newWind.x + "," + newWind.y + ")");
        System.out.println("-----------------------------------------");
        Point result = new Point(aim.x + newWind.x + xError, aim.y + newWind.y + yError + power);
        System.out.println("SHOT-> (" + result.x + "," + result.y + ")");
        points.put(shooter, shootArrow(result));
        waitForReturnSilently();
    }

    private int shootArrow(Point result) {
        int shotResult = targetSubView.getResultForShot(result);
        if (shotResult >= 0) {
            print("The arrow hit the target! ");
            if (shotResult == 0) {
                println("It's a bullseye - " + TARGET_POINTS[shotResult] + " points!");
            } else {
                println(TARGET_POINTS[shotResult] + " point" + (TARGET_POINTS[shotResult]>1?"s.":"."));
            }
            targetSubView.addArrow(result);
            return TARGET_POINTS[shotResult];
        }

        if (shotResult == ArcheryTargetSubView.ON_LEG) {
            println("The arrow hit the target's wooden stands.");
            targetSubView.addArrow(result);
        } else if (shotResult == ArcheryTargetSubView.OVER_TARGET) {
            println("The arrow flew over the target.");
        } else {
            println("The arrow missed the target and landed in the grass.");
        }
        return 0;
    }


    private int fireArrowSkillCheck(GameCharacter shooter) {
        print(shooter.getFirstName() + " fires the arrow! ");
        SkillCheckResult skillCheckResult;
        do {
            skillCheckResult = shooter.testSkill(Skill.Bows);
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
            Weapon bow = npc.getEquipment().getWeapon();
            print(npc.getName() + " takes a shot with " + hisOrHer(npc.getGender()) + " "
                    + bow.getName() + "... Press enter to continue.");
            waitForReturn();
            int skillRoll = npc.testSkill(Skill.Bows).getModifiedRoll();

            int power = 0;
            int weaponPower = npc.getEquipment().getWeapon().getDamageTable().length;
            if (npc.getEquipment().getWeapon() instanceof CrossbowWeapon || weaponPower < targetDistance) {
                power = POWER_FACTOR * (weaponPower - targetDistance);
            }
            int randBoxSize = ArcheryTargetSubView.TARGET_DIAMETER + ArcheryTargetSubView.TARGET_DIAMETER/2
                    - skillRoll*2 + targetDistance*2;
            System.out.println(npc.getName() + " taking a shot...");
            System.out.println("  Skill roll: " + skillRoll);
            System.out.println("  Power error: " + power);
            System.out.println("  Randboxsize: " + randBoxSize);
            Point shot = new Point(MyRandom.randInt(-randBoxSize/2, randBoxSize/2),
                            MyRandom.randInt(-randBoxSize/2, randBoxSize/2) + power);
            System.out.println("    Shot: (" + shot.x + "," + shot.y + ")");
            points.put(npc, shootArrow(shot));
        }
    }

    public void addNPCShooters(List<GameCharacter> marksmen) {
        npcShooters.addAll(marksmen);
    }

    public Map<GameCharacter, Integer> getPoints() {
        return points;
    }
}
