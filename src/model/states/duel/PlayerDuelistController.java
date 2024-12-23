package model.states.duel;

import model.Model;
import model.states.GameState;
import util.MyRandom;
import view.subviews.ArrowMenuSubView;

import java.awt.*;
import java.util.List;

public class PlayerDuelistController implements DuelistController {

    private static final Point MENU_POS = new Point(23, 33);

    private final MagicDuelist duelist;

    public PlayerDuelistController(MagicDuelist magicDuelist) {
        this.duelist = magicDuelist;
    }

    @Override
    public MagicDuelAction selectNormalTurnAction(Model model, MagicDuelEvent state) {
        MagicDuelAction selectedAction = null;
        while (selectedAction == null) {
            int choice = multipleOptionArrowMenu(model, state, MENU_POS.x, MENU_POS.y, List.of("Attack", "Shield", "Absorb"));
            if (choice == 0) {
                selectedAction = attackMenu(model, state);

            } else if (choice == 1) {
                selectedAction = shieldMenu(model, state);
            } else {
                selectedAction = new AbsorbMagicDuelAction();
            }
        }
        return selectedAction;
    }

    @Override
    public BeamOptions selectBeamTurnAction(Model model, MagicDuelEvent magicDuelEvent) {
        BeamOptions selectedAction;
        int choice = multipleOptionArrowMenu(model, magicDuelEvent, MENU_POS.x, MENU_POS.y,
                List.of("Hold on", "Power-Up", "Release"));
        if (choice == 0) {
            selectedAction = BeamOptions.HoldOn;
        } else if (choice == 1) {
            selectedAction = BeamOptions.IncreasePower;
        } else {
            selectedAction = BeamOptions.Release;
        }
        return selectedAction;
    }

    private MagicDuelAction attackMenu(Model model, MagicDuelEvent state) {
        int choice = multipleOptionArrowMenu(model, state, MENU_POS.x, MENU_POS.y, List.of("Normal", "Special", "Back"));
        if (choice == 0) {
            return new NormalAttackDuelAction();
        }
        if (choice == 1) {
            if (duelist.canDoSpecialAttack()) {
                return new SpecialAttackDuelAction();
            }
            state.println("You do not have enough power to do a special attack!");
        }
        return null;
    }

    private MagicDuelAction shieldMenu(Model model, MagicDuelEvent state) {
        int choice = multipleOptionArrowMenu(model, state, MENU_POS.x, MENU_POS.y - 4,
                List.of("Standard", "Level 1", "Level 2", "Level 3", "Back"));
        if (choice == 4) {
            return null;
        }
        return new ShieldMagicDuelAction(choice);
    }

    protected int multipleOptionArrowMenu(Model model, GameState state, int x, int y, List<String> optionList) {
        int[] selectedAction = new int[1];
        model.setSubView(new ArrowMenuSubView(model.getSubView(),
                optionList, x, y, ArrowMenuSubView.NORTH_WEST) {
            @Override
            protected void enterPressed(Model model, int cursorPos) {
                selectedAction[0] = cursorPos;
                model.setSubView(getPrevious());
            }
        });
        state.waitForReturnSilently();
        return selectedAction[0];
    }
}
