package model.ruins.objects;

import model.Model;
import model.combat.loot.CombinedLoot;
import model.combat.loot.PersonCombatLoot;
import model.ruins.DungeonRoom;
import model.ruins.themes.DungeonTheme;
import model.states.ExploreRuinsState;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.List;

public class CorpseObject extends CenterDungeonObject {
    public static final Sprite SPRITE = new Sprite32x32("dungeoncorpse", "dungeon.png", 0x45,
            MyColors.BLACK, MyColors.BEIGE, MyColors.PINK, MyColors.BEIGE);
    private static final List<String> TALK_LIST = List.of("Poor bugger", "Let's not end up like him",
            "I don't want to end up like her.", "She must have gotten lost down here.",
            "Monster probably killed him.", "Poor sod.", "Another adventurer bites the dust.", "Rest in peace.");
    private boolean looted = false;

    @Override
    protected Sprite getSprite(DungeonTheme theme) {
        return SPRITE;
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos, DungeonTheme theme) {
        model.getScreenHandler().register(getSprite(theme).getName(), new Point(xPos, yPos), getSprite(theme));
    }

    @Override
    public String getDescription() {
        return "A corpse";
    }

    @Override
    public void doAction(Model model, ExploreRuinsState state) {
        if (looted) {
            model.getParty().randomPartyMemberSay(model, TALK_LIST);
        } else {
            looted = true;
            model.getParty().randomPartyMemberSay(model, List.of("Maybe she still has something of value?"));
            int roll = MyRandom.rollD10();
            if (roll < 3) {
                state.println("Just bones and rags...");
            } else if (roll < 7)  {
                CombinedLoot combinedLoot = new CombinedLoot();
                combinedLoot.add(new PersonCombatLoot(model));
                combinedLoot.add(new PersonCombatLoot(model));
                if (!combinedLoot.getText().equals("")) {
                    state.println("You found " + combinedLoot.getText() + ".");
                    combinedLoot.giveYourself(model.getParty());
                } else {
                    state.println("You found nothing of interest.");
                }
            } else {
                String type = state.getDungeonType().toLowerCase();
                if (type.equals("ruins")) {
                    type = "these " + type;
                } else if (type.equals("dungeon")) {
                    type = "this " + type;
                }
                state.println("You found a map of " + type + "!");
                state.mapsFound(state.getCurrentLevel());
                for (int i = 0; i <= state.getCurrentLevel(); ++i) {
                    for (DungeonRoom room : state.getDungeon().getLevel(i).getRoomList()) {
                        room.setRevealedOnMap(true);
                    }
                }
                state.leaderSay("It's not complete, but I bet it will come in handy!");
            }
        }
    }

    @Override
    public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
        super.entryTrigger(model, exploreRuinsState);
        model.getParty().randomPartyMemberSay(model, TALK_LIST);
    }
}
