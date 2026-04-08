package model.states.mine;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.weapons.GrandMaul;
import model.items.weapons.Pickaxe;
import model.items.weapons.Weapon;
import model.states.DailyEventState;
import model.states.SpellCastException;
import sound.SoundEffects;
import util.MyLists;
import view.sprites.BreakingRockAnimation;
import view.subviews.*;

import java.util.List;

import java.awt.*;

public class AdvancedMineEvent extends DailyEventState {
    private boolean playerHasQuit = false;
    private int currentLevel = 1;
    private int stepsLeft = 99;
    private LogicalMine mine;
    private MineSubView mineSubView;

    public AdvancedMineEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        this.mine = new LogicalMine();
        this.mineSubView = new MineSubView(this, mine);
        CollapsingTransition.transition(model, mineSubView);

        do {
            try {
                waitUntil(mineSubView, FreeMoveAvatarSubView::hasMovesToHandle, true);
                if (!mineSubView.handleMove(model)) {
                    askToExit(model);
                }
            } catch (SpellCastException sce) {

            }
        } while (!playerHasQuit);
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getMovesLeft() {
        return stepsLeft;
    }

    public void askToExit(Model model) {
        print("Do you want to exit the mine? (Y/N) ");
        if (yesNoInput()) {
            playerHasQuit = true;
        }
    }

    public void decrementMoves() {
        stepsLeft--;
    }

    public Point moveToRoom(Model model, AdvancedMineEvent state, MineDirection direction) {
        MineRoomLocation loc = mine.getCurrentLocation().copy();
        loc.moveInDirection(direction);
        if (mine.roomIsDiscovered(loc)) {
            mine.moveToRoom(direction);
        } else {
            if (direction == MineDirection.up || direction == MineDirection.down) {
                CollapsingTransition.transition(model, mineSubView, () -> mine.moveToRoom(direction));
            } else {
                SwipingTransition.transition(model, mineSubView, direction, () -> mine.moveToRoom(direction));
            }
        }
        return mine.getStartingPoint();
    }

    public MineRoomLocation getCurrentLocation() {
        return mine.getCurrentLocation();
    }

    public boolean askToMineObject(Model model, MineableObject mineObject) {
        List<GameCharacter> nonBenchers = MyLists.filter(model.getParty().getPartyMembers(),
                gc -> !model.getParty().getBench().contains(gc));
        if (!MyLists.any(nonBenchers, gc -> gc.getSP() > 0)) {
            println("You cannot mine the rock since nobody in your party has any Stamina Points.");
            return false;
        }
        if (!hasSuitableMiningGear(model, nonBenchers)) {
            println("You cannot mine the rock since none of your present characters have any suitable mining gear.");
            return false;
        }
        print("Do you want to mine the " + mineObject.getName() + "? (Y/N) ");
        if (!yesNoInput()) {
            return false;
        }
        GameCharacter chosen;
        if (nonBenchers.size() == 1) {
            chosen = nonBenchers.getFirst();
        } else {
            GameCharacter best = MyLists.maximumValue(nonBenchers, gc -> gc.getRankForSkill(Skill.Labor));
            do {
                print("Which party member will mine?");
                chosen = model.getParty().partyMemberInput(model, this, best);
                if (model.getParty().getBench().contains(chosen)) {
                    println(chosen.getName() + " is not here right now.");
                } else if (chosen.getSP() == 0) {
                    println(chosen.getName() + " is too exhausted to mine the rock.");
                } else {
                    break;
                }
            } while (true);
        }
        println(chosen.getName() + " hacks at the " + mineObject.getName() + "...");
        SkillCheckResult result = chosen.testSkill(model, Skill.Labor, mineObject.getDifficulty());
        if (result.isSuccessful()) {
            println("And the rock breaks apart! (Labor " + result.asString() + ")");
            model.getLog().waitForAnimationToFinish();
            mineObject.giveReward(model, this);
            mineSubView.addAnimation(mine.getPositionFor(mineObject), new BreakingRockAnimation(mineObject.getAnimationColor()));
            mine.destroyRock(model, mineObject);
            SoundEffects.playRockBreak();
            return true;
        }
        println("But the rock does not even crack. (Labor " + result.asString() + ")");
        if (chosen.testSkillHidden(Skill.Endurance, mineObject.getDifficulty()/2, 0).isFailure()) {
            println(chosen.getFirstName() + " is worn out from the physical labor and exhausts 1 Stamina Point.");
            chosen.addToSP(-1);
        }
        return false;
    }

    private boolean hasSuitableMiningGear(Model model, List<GameCharacter> availableChars) {
        return MyLists.any(availableChars, this::hasSuitableMiningGear) ||
                MyLists.any(model.getParty().getInventory().getWeapons(), this::isMiningGear);
    }

    private boolean hasSuitableMiningGear(GameCharacter chosen) {
        return isMiningGear(chosen.getEquipment().getWeapon());
    }

    private boolean isMiningGear(Weapon weapon) {
        return weapon.isOfType(Pickaxe.class) || weapon.isOfType(GrandMaul.class);
    }
}
