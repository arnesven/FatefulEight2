package model.combat.conditions;

import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.characters.special.WitchKingAppearance;
import model.characters.special.WitchKingEyes;
import model.races.ElvenRace;
import model.races.Race;
import model.states.TravelBySeaState;
import util.Arithmetics;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.help.VampirismHelpView;
import view.party.CharacterCreationView;
import view.sprites.CharSprite;
import view.sprites.MouthSprite;
import view.sprites.Sprite;

public class VampirismCondition extends Condition {

    public static final int NO_STAGE = -1;
    public static final int INITIAL_STAGE = 0;

    private static final Sprite SPRITE = CharSprite.make((char)(0xDC), MyColors.WHITE, MyColors.DARK_RED, MyColors.CYAN);
    private static final int MAX_STAGE = 5;
    private static final int HEALTH_PER_STAGE = 3;
    private static final int STAMINA_PER_STAGE = 2;
    private static final int SPEED_PER_STAGE = 2;
    private static final int CARRY_CAP_BONUS_PER_STAGE = 15;
    private static final MyColors PALEST_SKIN_COLOR = MyColors.WHITE;
    private static final MyColors PALEST_LIP_COLOR = MyColors.GRAY;
    private int stage;
    private CharacterAppearance originalAppearance = null;

    public VampirismCondition(int initialStage) {
        super("Vampirism", "VMP");
        this.stage = initialStage;
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public Sprite getSymbol() {
        return SPRITE;
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new VampirismHelpView(view, this);
    }

    public void progress(GameCharacter owner) {
        if (originalAppearance == null) {
            originalAppearance = owner.getAppearance();
        }
        this.stage = Arithmetics.incrementWithWrap(this.stage, MAX_STAGE + 1); // TODO: Stop at max instead of wrap-around
        if (owner.getAppearance() instanceof AdvancedAppearance) {
            if (stage == 0) {
                owner.setAppearance(originalAppearance);
            } else {
                AdvancedAppearance app = (AdvancedAppearance) owner.getAppearance().copy();
                MyColors skinColor = getSkinColorForRaceAndStage(owner);
                app.setAlternateSkinColor(skinColor);
                app.setMascaraColor(skinColor);
                if (skinColor == PALEST_SKIN_COLOR) {
                    app.setLipColor(PALEST_LIP_COLOR);
                }
                app.setMouth(CharacterCreationView.mouthSet[0]);
                app.setEyeballColor(getEyeColorForStage());
                owner.setAppearance(app);
            }
        }
    }

    private MyColors getEyeColorForStage() {
        if (stage >= 4) {
            return MyColors.YELLOW;
        }
        if (stage >= 1) {
            return MyColors.LIGHT_YELLOW;
        }
        return MyColors.WHITE;
    }

    private MyColors getSkinColorForRaceAndStage(GameCharacter owner) {
        if (stage == 0) {
            return owner.getRace().getColor();
        }
        if (stage >= 4) {
            return PALEST_SKIN_COLOR;
        }
        if (owner.getRace() instanceof ElvenRace) {
            return owner.getRace().getColor();
        }
        if (owner.getRace().id() == Race.HALFLING.id()) {
            if (stage >= 2) {
                return MyColors.BEIGE;
            }
        }
        if (owner.getRace().id() == Race.HALF_ORC.id()) {
            if (stage >= 2) {
                return MyColors.LIGHT_GREEN;
            }
        }
        if (owner.getRace().id() == Race.NORTHERN_HUMAN.id() || owner.getRace().id() == Race.DWARF.id()) {
            if (stage >= 2) {
                return MyColors.LIGHT_PINK;
            }
        }
        if (owner.getRace().id() == Race.SOUTHERN_HUMAN.id()) {
            if (stage >= 3) {
                return MyColors.BEIGE;
            }
            if (stage >= 2) {
                return MyColors.PEACH;
            }
        }
        return owner.getRace().getColor();
    }

    public int getStage() {
        return stage;
    }

    public String getStageString() {
        if (stage == -1) {
            return "";
        }
        return "Current stage: " + stage + "\n\n";
    }

    @Override
    public int getHealthBonus() {
        return HEALTH_PER_STAGE * stage;
    }

    @Override
    public int getStaminaBonus() {
        return STAMINA_PER_STAGE * stage;
    }

    @Override
    public int getSpeedBonus() {
        return SPEED_PER_STAGE * stage;
    }

    @Override
    public int getCarryCapBonus() {
        return CARRY_CAP_BONUS_PER_STAGE * stage;
    }

    public boolean hasBatAbility() {
        return true; // TODO: Fix
    }
}
