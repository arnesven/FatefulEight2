package model.states.events;

import model.Model;
import model.classes.Classes;
import model.enemies.TempleGuardEnemy;
import model.states.DailyEventState;

import java.util.List;

public class TempleGuardsEvent extends DailyEventState {
    private final boolean withIntro;
    private boolean gotBounced;

    public TempleGuardsEvent(Model model, boolean withIntro) {
        super(model);
        this.withIntro = withIntro;
    }

    @Override
    protected void doEvent(Model model) {
        if (withIntro) {
            println("A few temple guards - armed monks approach you.");
            showRandomPortrait(model, Classes.TEMPLE_GUARD, "Temple Guards");
            println("The guards appraise you.");
            int align = getPartyAlignment(model, this);
            if (align < -1) {
                bounce(model);
            } else {
                portraitSay(model, "Please enjoy your stay at our temple!");
                model.getParty().randomPartyMemberSay(model, List.of("Everybody is so polite here."));

            }
        } else {
            bounce(model);
        }
    }

    private void bounce(Model model) {
        gotBounced = true;
        portraitSay(model, "You scruffy fellows don't belong here. Kindly get out.");
        print("Do you obey the guards and leave the temple? (Y/N) ");
        if (!yesNoInput()) {
            runCombat(List.of(new TempleGuardEnemy('A'), new TempleGuardEnemy('A'), new TempleGuardEnemy('A')));
            String templeName = model.getCurrentHex().getLocation().getName();
            if (!model.getParty().isBannedFromTemple(templeName)) {
                println("You have been banned from " + templeName + ".");
                model.getParty().banFromTemple(templeName);
            }
        }
        println("You are forced off the temple guards.");
    }

    @Override
    public boolean haveFledCombat() {
        return gotBounced;
    }
}
