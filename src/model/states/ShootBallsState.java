package model.states;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import util.MyRandom;
import view.subviews.ShootBallsSubView;

import java.awt.*;

public class ShootBallsState extends GameState {
    private static final int DIFFICULTY = 8;
    private final GameCharacter shooter;

    public ShootBallsState(Model model, GameCharacter shooter) {
        super(model);
        this.shooter = shooter;
    }

    @Override
    public GameState run(Model model) {
        ShootBallsSubView subView = new ShootBallsSubView(this, shooter);
        model.setSubView(subView);
        println("Press return when you are ready for the balls to be thrown, fire arrows with SPACE.");
        waitForReturnSilently();
        subView.startAnimation();
        do {
            waitForReturnSilently();
            shoot(model, subView);
        } while (true);

        //return model.getCurrentHex().getDailyActionState(model);
    }

    private void shoot(Model model, ShootBallsSubView subView) {
        subView.stopAnimation();
        SkillCheckResult result = shooter.testSkill(Skill.Bows);
        println(shooter.getName() + " fires an arrow (" + result.asString() + ")!");
        Point aim = subView.getAim();
        int errorMag = Math.max(0, DIFFICULTY - result.getModifiedRoll());
        int xError = 0;
        int yError = 0;
        if (errorMag > 0) {
            xError = MyRandom.randInt(errorMag);
            yError = errorMag - xError;
        }
        Point finalResult = new Point(aim.x + xError, aim.y + yError);
        System.out.println("Aim is  : (" + aim.x + "," + aim.y + ")");
        System.out.println("Error is: (" + xError + "," + yError + ")");
        System.out.println("Result  : (" + finalResult.x + "," + finalResult.y + ")");
        if (subView.checkForHit(finalResult)) {
            println("It hits a ball!");
        } else {
            println("The arrow missed the ball.");
        }

        println("Press enter to continue.");
        waitForReturnSilently();

        subView.startAnimation();
    }
}
