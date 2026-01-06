package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import model.characters.GameCharacter;
import model.mainstory.vikings.GainSupportOfVikingsTask;
import model.mainstory.vikings.RaidSixthOrderMonastaryEvent;
import model.map.locations.VikingVillageLocation;
import model.states.GameState;
import model.states.dailyaction.town.GoTheDocksNode;
import view.sprites.Sprite;

import java.awt.*;

public class LongboatNode extends GoTheDocksNode {
    private boolean travelled = false;

    public LongboatNode(Model model) {
        super(model);
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new GoOnRaidState(model);
    }

    @Override
    public void drawYourself(Model model, Point p) {
        Point shifted = new Point(p);
        shifted.y -= 2;
        Sprite ship = getBackgroundSprite();
        model.getScreenHandler().register(ship.getName(), shifted, ship);
    }

    @Override
    public boolean returnNextState() {
        return travelled;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    private class GoOnRaidState extends GameState {
        public GoOnRaidState(Model model) {
            super(model);
        }

        @Override
        public GameState run(Model model) {
            GainSupportOfVikingsTask vikingTask = VikingVillageLocation.getVikingTask(model);
            if (vikingTask == null || !vikingTask.isWrestlingContestDone() ||
                    vikingTask.isMonksWarned() ||
                    vikingTask.isCompleted() ||
                    model.getTimeOfDay() == TimeOfDay.EVENING) {
                        println("The ship is moored at the dock, but there is nobody aboard or near it.");
                return model.getCurrentHex().getDailyActionState(model);
            }

            println("The vikings are packing this ship for the upcoming raid on the Sixth Order monastary. If you want you can board " +
                    "the ship to go with Chieftain Loki and the vikings on the raid now.");
            if (model.getParty().size() > 1) {
                GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                partyMemberSay(other, "Are we really going through with this? " +
                        "Those monks are probably completely defenseless.");
                leaderSay("Yes. I feel quite ambivalent myself about it.");
                partyMemberSay(other, "And these Vikings... I know we need their support, " +
                        "but they're not much more than thugs.");
                leaderSay("Rude thugs, yes.");
            } else {
                leaderSay("I don't know how I feel about this. Those monks are probably completely defenseless, " +
                        "and these Vikings are not much more than rude thugs.");
            }
            print("Board the ship and go on the raid? (Y/N) ");
            if (yesNoInput()) {
                travelled = true;
                return new RaidSixthOrderMonastaryEvent(model, vikingTask);
            }
            return model.getCurrentHex().getDailyActionState(model);
        }
    }
}
