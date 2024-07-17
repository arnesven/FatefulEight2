package model.combat.conditions;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Skill;
import model.combat.Combatant;
import model.races.ElvenRace;
import model.races.Race;
import model.states.CombatEvent;
import model.states.GameState;
import util.MyLists;
import view.GameView;
import view.MyColors;
import view.VampireStageProgressionDialog;
import view.help.ConditionHelpDialog;
import view.help.VampirismHelpView;
import view.party.CharacterCreationView;
import view.sprites.CharSprite;
import view.sprites.Sprite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VampirismCondition extends Condition {

    public static final int NO_STAGE = -1;
    public static final int INITIAL_STAGE = 0;
    private static final List<VampireAbility> ALL_ABILITIES = List.of(
            new BatFormVampireAbility(),
            new MesmerizeVampireAbility(),
            new WisdomAbility(),
            new StrengthAbility(),
            new ClawsVampireAbility(),
            new CelerityVampireAbility());

    private static final Sprite SPRITE = CharSprite.make((char)(0xDC), MyColors.WHITE, MyColors.DARK_RED, MyColors.CYAN);
    private static final int MAX_STAGE = 5;
    private static final int HEALTH_PER_STAGE = 3;
    private static final int STAMINA_PER_STAGE = 2;
    private static final int SPEED_PER_STAGE = 2;
    private static final int CARRY_CAP_BONUS_PER_STAGE = 15;
    private static final MyColors PALEST_SKIN_COLOR = MyColors.WHITE;
    private static final MyColors PALEST_LIP_COLOR = MyColors.GRAY;
    private static final int PROGRESS_EVERY_N_DAYS = 10;
    private final int dayAdded;
    private int stage;
    private CharacterAppearance originalAppearance = null;
    private List<VampireAbility> learnedAbilities = new ArrayList<>();

    public VampirismCondition(int initialStage, int dayAdded) {
        super("Vampirism", "VMP");
        this.stage = initialStage;
        this.dayAdded = dayAdded;
    }

    public static void makeVampire(Model model, GameState state, GameCharacter target) {
        target.addCondition(new VampirismCondition(VampirismCondition.INITIAL_STAGE, model.getDay()));
        if (target.hasCondition(VampirismCondition.class)) {
            state.println(target.getName() + " has been infected by vampirism!");
            model.getTutorial().vampires(model);
        }
    }

    @Override
    public void endOfDayTrigger(Model model, GameState state, Combatant comb) {
        if (model.getDay() != dayAdded &&
                ((model.getDay() - dayAdded) % PROGRESS_EVERY_N_DAYS) == 0) {
            progress(model, (GameCharacter) comb);
        }
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

    public void progress(Model model, GameCharacter owner) {
        if (originalAppearance == null) {
            originalAppearance = owner.getAppearance();
        }
        if (this.stage == MAX_STAGE) {
            return;
        }
        this.stage++;
        updateAppearance(owner);
//        model.getLog().addAnimated(owner.getName() + " vampirism progressed to stage " +
//                stage + "."); // TODO: bring this back (hangs game when using key to progress)
        model.getLog().waitForAnimationToFinish();
        VampireStageProgressionDialog stageDialog = new VampireStageProgressionDialog(model, owner, this);
        model.transitionToDialog(stageDialog);
    }

    public void updateAppearance(GameCharacter owner) {
        if (owner.getAppearance() instanceof AdvancedAppearance) {
            if (stage == 0) {
                owner.setAppearance(originalAppearance); // Should never happen...
            } else {
                AdvancedAppearance app = (AdvancedAppearance) owner.getAppearance().copy();
                MyColors skinColor = getSkinColorForRaceAndStage(owner);
                app.setAlternateSkinColor(skinColor);
                app.setEyeballColor(getEyeColorForStage());
                app.setMascaraColor(skinColor);
                if (skinColor == PALEST_SKIN_COLOR) {
                    app.setLipColor(PALEST_LIP_COLOR);
                }
                app.setMouth(CharacterCreationView.mouthSet[0]);
                owner.setAppearance(app);
            }
        }
    }

    @Override
    public void wasRemoved(Combatant combatant) {
        if (combatant instanceof GameCharacter) {
            ((GameCharacter) combatant).setAppearance(originalAppearance);
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
        return MyLists.any(learnedAbilities, (VampireAbility va) -> va instanceof BatFormVampireAbility);
    }

    public List<VampireAbility> getRandomAbilities() {
        List<VampireAbility> result = new ArrayList<>(ALL_ABILITIES);
        for (VampireAbility va : learnedAbilities) {
            result.removeIf((VampireAbility v2) -> v2.getName().equals(va.getName()));
        }
        Collections.shuffle(result);
        return result.subList(0, Math.min(result.size(), 3));
    }

    public void learnAbility(VampireAbility chosen) {
        this.learnedAbilities.add(chosen);
    }

    public List<VampireAbility> getLearnedAbilities() {
        return learnedAbilities;
    }

    public boolean hasMesmerizeAbility() {
        return MyLists.any(learnedAbilities, (VampireAbility va) -> va instanceof MesmerizeVampireAbility);
    }

    @Override
    public int getBonusForSkill(Skill skill) {
        return MyLists.intAccumulate(learnedAbilities, (VampireAbility va) -> va.getBonusForSkill(skill));
    }

    public boolean hasCelerityAbility() {
        return MyLists.any(learnedAbilities, (VampireAbility va) -> va instanceof CelerityVampireAbility);
    }

    public boolean hasClawsAbility() {
        return MyLists.any(learnedAbilities, (VampireAbility va) -> va instanceof ClawsVampireAbility);
    }
}
