package model.states;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.spells.TelekinesisSpell;
import model.items.weapons.BowWeapon;
import model.items.weapons.TrainingBow;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyRandom;
import view.GameView;
import view.SimpleMessageView;
import view.sprites.AnimationManager;
import view.sprites.Sprite;
import view.subviews.CollapsingTransition;
import view.subviews.ShootBallsSubView;

import java.awt.*;

public class ShootBallsState extends GameState {
    private static final int DIFFICULTY = 8;
    private final GameCharacter shooter;
    private final String titleText;
    private Sprite arrowSprite;
    private BowWeapon bowToUse;
    private ShootBallsSubView subView;
    private Sprite fletching = null;

    public ShootBallsState(Model model, GameCharacter shooter, BowWeapon bowToUse, String titleText) {
        super(model);
        this.shooter = shooter;
        this.bowToUse = bowToUse;
        this.titleText = titleText;
    }

    @Override
    public GameState run(Model model) {
        BackgroundMusic previous = ClientSoundManager.getCurrentBackgroundMusic();
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.jumpyBlip);
        this.subView = new ShootBallsSubView(this, shooter, bowToUse);
        CollapsingTransition.transition(model, subView);
        println("Press enter when you are ready for the balls to be thrown, fire with enter.");
        enableTelekinesis(model);
        waitForReturnSilently();
        subView.startAnimation();
        if (fletching == null) {
            arrowSprite = ShootBallsSubView.makeArrowSprite();
        } else {
            arrowSprite = fletching;
        }
        do {
            try {
                waitForReturnSilently(true);
                if (!subView.stillBallsToShoot()) {
                    break;
                }
                if (subView.shotReady()) {
                    shoot(model, subView);
                }
            } catch (SpellCastException sce) {
                if (sce.getSpell() instanceof TelekinesisSpell &&
                    sce.getSpell().castYourself(model, this, sce.getCaster())) {
                    model.transitionToDialog(new TelekinesisEnabledDialog(model, sce.getCaster()));
                    subView.enableTelekinesis();
                    disableTelekinesis(model);
                }
            }
        } while (true);
        disableTelekinesis(model);
        AnimationManager.unregister(this.subView);
        println("Game over. You got " + subView.getScore() + " out of " + ShootBallsSubView.MAX_BALLS + " balls.");
        ClientSoundManager.playBackgroundMusic(previous);
        return model.getCurrentHex().getDailyActionState(model);
    }

    private void enableTelekinesis(Model model) {
        model.getSpellHandler().acceptSpell(new TelekinesisSpell().getName());
    }

    private void disableTelekinesis(Model model) {
        model.getSpellHandler().unacceptSpell(new TelekinesisSpell().getName());
    }

    private void shoot(Model model, ShootBallsSubView subView) {
        subView.stopAnimation();
        SkillCheckResult result = shooter.testSkill(model, Skill.Bows);
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
        model.getLog().waitForAnimationToFinish();
        if (subView.shootArrow(finalResult, arrowSprite)) {
            println("It hits a ball!");
        } else {
            println("The arrow missed the ball.");
        }


        println("Press enter to continue.");
        waitForReturnSilently();
        subView.clearArrows();

        subView.startAnimation();
    }

    public int getPoints() {
        return subView.getScore();
    }

    public void useFletching(Sprite sprite) {
        this.fletching = sprite;
    }

    public static BowWeapon getCharactersBowOrDefault(GameCharacter shooter) {
        BowWeapon bowToUse = new TrainingBow();
        if (shooter.getEquipment().getWeapon() instanceof BowWeapon) {
            bowToUse = (BowWeapon) shooter.getEquipment().getWeapon();
        }
        return bowToUse;
    }

    public String getSubViewTitleText() {
        return titleText;
    }

    private static class TelekinesisEnabledDialog extends SimpleMessageView {
        public TelekinesisEnabledDialog(Model model, GameCharacter caster) {
            super(model.getView(), caster.getName() + " is now suspending the balls in mid-air with Telekinesis!");
        }
    }
}
