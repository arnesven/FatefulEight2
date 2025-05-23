package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.headquarters.*;
import model.states.GameState;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyLists;
import util.MyRandom;
import view.subviews.CollapsingTransition;
import view.subviews.HeadquartersSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HeadquartersDailyActionState extends GameState {
    public static final int LABOR_RANKS_REQUIRED_FOR_EXPAND = 5;
    public static final int LEADERSHIP_REQUIRED_RANKS_FOR_ASSIGNMENTS = 3;
    private final GameState previousState;
    private static final Point MENU_LOCATION = new Point(24, 19);
    private final boolean fromSpell;

    public HeadquartersDailyActionState(Model model, GameState state, boolean fromSpell) {
        super(model);
        this.previousState = state;
        this.fromSpell = fromSpell;
    }

    @Override
    public GameState run(Model model) {
        HeadquartersSubView subView = new HeadquartersSubView(model);
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.festiveSong);
        CollapsingTransition.transition(model, subView);
        print("You step into your headquarters. ");
        if (headquartersHasCharacters(model)) {
            println(MyRandom.sample(model.getParty().getHeadquarters().getCharacters()).getFirstName() + " welcomes you back.");
        } else {
            println("It's empty.");
        }

        return runHeadquartersMenu(model, subView);
    }

    private GameState runHeadquartersMenu(Model model, HeadquartersSubView subView) {
        do {
            List<HeadquartersAction> actions = new ArrayList<>(List.of(
                    new TransferResourceAction(model, MENU_LOCATION),
                    new TransferItemHeadquartersAction(model, subView),
                    new ReadLogHeadquartersAction(model),
                    new HeadquartersAction(model, "Leave HQ") {
                        @Override
                        public GameState run(Model model) {
                            if (checkForRations(model, "leave headquarters")) {
                                return previousState;
                            }
                            return null;
                        }
                    }
            ));

            if (model.getTimeOfDay() != TimeOfDay.MORNING && !fromSpell) {
                actions.add(new RestAtHeadquartersAction(model));
            }
            if (canDoExpand(model)) {
                actions.add(2, new ExpandHeadquartersAction(model));
            }
            if (canGiveAssignments(model)) {
                actions.add(2, new GiveAssignmentsHeadquartersAction(model, MENU_LOCATION, subView));
            }
            if (model.getParty().getHorseHandler().size() > 1 || headquartersHasHorses(model)) {
                actions.add(2, new TransferHorsesHeadquartersAction(model, subView));
            }
            if (model.getParty().size() > 1 || headquartersHasCharacters(model)) {
                actions.add(2, new TransferCharacterHeadquartersAction(model, subView));
            }

            List<String> options = MyLists.transform(actions, HeadquartersAction::getName);

            int choice = multipleOptionArrowMenu(model, MENU_LOCATION.x, MENU_LOCATION.y, options);
            GameState next = actions.get(choice).run(model);
            if (next != null) {
                return next;
            }
        } while (true);
    }

    private boolean canGiveAssignments(Model model) {
        return headquartersHasCharacters(model) &&
                model.getParty().getLeader().getUnmodifiedRankForSkill(Skill.Leadership) >= LEADERSHIP_REQUIRED_RANKS_FOR_ASSIGNMENTS;
    }

    private boolean canDoExpand(Model model) {
        return MyLists.any(model.getParty().getPartyMembers(),
                (GameCharacter gc) -> gc.getUnmodifiedRankForSkill(Skill.Labor) >= LABOR_RANKS_REQUIRED_FOR_EXPAND) &&
                model.getParty().getHeadquarters().getSize() < Headquarters.MAJESTIC_SIZE;
    }

    private boolean headquartersHasHorses(Model model) {
        return model.getParty().getHeadquarters().getHorses().size() > 0;
    }

    private boolean headquartersHasCharacters(Model model) {
        return !model.getParty().getHeadquarters().getCharacters().isEmpty();
    }
}
