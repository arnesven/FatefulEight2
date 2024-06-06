package model.states.fatue;

import model.Model;
import model.characters.GameCharacter;
import model.items.special.FatueKeyItem;
import model.states.dailyaction.AdvancedDailyActionState;
import view.MyColors;

public abstract class KeyRequiredFatueDungeonNode extends FatueDungeonNode {
    private final FatueKeyItem requiredKey;

    public KeyRequiredFatueDungeonNode(String name, boolean isDownward, MyColors colorOfRequiredKey, String promptMessage) {
        super(name, isDownward, promptMessage);
        this.requiredKey = new FatueKeyItem(colorOfRequiredKey);
    }

    @Override
    protected boolean runPreHook(Model model, AdvancedDailyActionState state) {
        state.println("You start down the hallway but soon encounter a locked door.");
        if (checkForKey(model)) {
            printKeyUsed(state);
            return true;
        }
        if (model.getParty().size() > 1) {
            state.leaderSay("Any chance to pick this lock?");
            GameCharacter rando = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            state.partyMemberSay(rando, "No can do. That's a masterpiece lock. The only " +
                    "thing that will open it is the proper key.");
        }
        printRequiredKeys(state);
        return false;
    }

    protected void printRequiredKeys(AdvancedDailyActionState state) {
        state.println("You need the " + requiredKey.getName() + " to open the door.");
    }

    protected boolean checkForKey(Model model) {
        return FatueKeyItem.hasKey(model, requiredKey.getColor());
    }

    protected void printKeyUsed(AdvancedDailyActionState state) {
        state.println("You use the " + requiredKey.getName() + " to unlock the door.");
    }
}
