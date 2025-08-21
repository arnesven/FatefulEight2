package model.states.events;

import model.Model;
import model.actions.Loan;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.BrotherhoodCronyEnemy;
import model.enemies.Enemy;
import model.states.DailyEventState;

import java.util.ArrayList;
import java.util.List;

public class BrotherhoodCroniesEvent extends DailyEventState {
    public BrotherhoodCroniesEvent(Model model) {
        super(model);
    }

    @Override
    public boolean exclusiveToOriginalWorld() {
        return true;
    }

    @Override
    protected void doEvent(Model model) {
        println("A group of hooded figures step out in front of the party and stop you.");
        int days = model.getDay() - model.getParty().getLoan().getDay();
        showRandomPortrait(model, Classes.THF, "Brotherhood");
        portraitSay("Hey scumbag! The Brotherhood wants its money back. They sent us to remind you.");

        print("Do you wish to attempt to persuade the cronies to leave you alone (Y) or do you wish to bribe them (N)? ");
        if (yesNoInput()) {
            boolean result = model.getParty().doSoloSkillCheck(model, this, Skill.Persuade, getPersuadeDifficulty(days));
            if (!result) {
                portraitSay("Talk all you want scum, but I think a busted kneecap or two will change your mind.");
                doCombat(model, days);
            } else {
                leaderSay("Come on guys, I'm only a little late with the money.");
                portraitSay("Fine! But you'd better pay up soon.");
            }
        } else {
            leaderSay("Hey fellas, can't we come to some agreement. Why don't we give you a little gold, " +
                    "and you tell the Brotherhood you couldn't find us.");
            int bribeCost = getBribeCost(days);
            portraitSay("Hmm... give us " + bribeCost + " gold and we'll leave you alone...");
            if (bribeCost <= model.getParty().getGold()) {
                print("Pay " + bribeCost + " gold? (Y/N)");
                if (yesNoInput()) {
                    model.getParty().loseGold(bribeCost);
                    println("The party lost " + bribeCost + " gold.");
                    portraitSay("For now...");
                }
            } else {
                leaderSay("Uh, how about " + model.getParty().getGold() + " instead?");
                portraitSay("You really think we're stupid don't you. I think you need to be taught a lesson!");
                doCombat(model, days);
            }
        }

    }


    private void doCombat(Model model, int days) {
        List<Enemy> enemies = new ArrayList<>();
        int num = getPersuadeDifficulty(days) - 3;
        for (int i = 0; i < num; ++i) {
            enemies.add(new BrotherhoodCronyEnemy('A'));
        }
        runCombat(enemies);
    }

    private int getPersuadeDifficulty(int days) {
        if (days < Loan.REPAY_WITHIN_DAYS + 3) {
            return 7;
        } else if (days < Loan.REPAY_WITHIN_DAYS + 7) {
            return 9;
        } else if (days < Loan.REPAY_WITHIN_DAYS + 13) {
            return 11;
        }
        return 13;
    }


    private int getBribeCost(int days) {
        if (days < Loan.REPAY_WITHIN_DAYS + 3) {
            return 10;
        } else if (days < Loan.REPAY_WITHIN_DAYS + 7) {
            return 20;
        } else if (days < Loan.REPAY_WITHIN_DAYS + 13) {
            return 30;
        }
        return 40;
    }
}
