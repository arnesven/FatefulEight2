package model.states.events;

import model.Model;
import model.classes.Classes;
import model.classes.Skill;
import view.subviews.TownCombatTheme;
import model.enemies.AssassinEnemy;
import model.states.DailyEventState;

import java.util.List;

public class AssassinEvent extends DailyEventState {
    public AssassinEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        if (model.getParty().partyStrength() < 15) {
            new NoEventState(model).run(model);
            return;
        }
        println("You hear a rumor that someone has hired an assassin to liquidate one of the party members.");
        leaderSay("If we ask around a bit maybe we can intercept the assassin.");
        boolean result = model.getParty().doCollaborativeSkillCheck(model, this, Skill.SeekInfo, 7);
        if (result) {
            showRandomPortrait(model, Classes.ASN, "Assassin");
            print("You track down the assassin and follow her back to a seedy club. It doesn't take long before you " +
                    "realize that this is the hideout of an assassins' guild. Surprisingly these fellows are quite" +
                    " friendly and offer to show you a few tricks, ");
            model.getLog().waitForAnimationToFinish();
            ChangeClassEvent change = new ChangeClassEvent(model, Classes.ASN);
            change.areYouInterested(model);
        } else {
            runCombat(List.of(new AssassinEnemy('A')), new TownCombatTheme(), true);
        }
    }
}
