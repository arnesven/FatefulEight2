package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.ReadableItem;
import model.items.MysteriousMap;
import model.states.DailyEventState;
import model.states.GameState;
import util.MyPair;

public class FindTreasureMapEvent extends DailyEventState {
    public FindTreasureMapEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        if ((!hasTreasureMap(model) && spotMap(model))) {
            println("You find a piece of parchment on the ground.");
            leaderSay("A treasure map?");
            getMysteriousMap(model, this);
        } else {
            new NoEventState(model).doTheEvent(model);
        }
    }

    public static void getMysteriousMap(Model model, GameState state) {
        state.println("You got a Mysterious Map!");
        MysteriousMap map = new MysteriousMap(model);
        model.getParty().getInventory().add(map);
        model.getParty().addDestinationTask(map.getDestinationTask());
    }

    private boolean hasTreasureMap(Model model) {
        for (ReadableItem b : model.getParty().getInventory().getBooks()) {
            if (b instanceof MysteriousMap) {
                return true;
            }
        }
        return false;
    }

    private boolean spotMap(Model model) {
        MyPair<SkillCheckResult, GameCharacter> resultAndSpotter = doPassiveSkillCheck(Skill.Perception, 7);
        if (resultAndSpotter.first.isSuccessful()) {
            GameCharacter gc = resultAndSpotter.second;
            println(gc.getName() + " spots something on the ground. (Perception " +
                    resultAndSpotter.first.asString() + ")");
            return true;
        }
        return false;
    }
}
