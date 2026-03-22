package model.states.events;

import model.Model;
import model.actions.Loan;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.FacialExpression;
import model.classes.Classes;
import model.classes.Skill;
import model.combat.conditions.CowardlyCondition;
import model.enemies.BrotherhoodCronyEnemy;
import model.enemies.Enemy;
import model.states.DailyEventState;
import util.MyLists;
import util.MyRandom;

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
        showEventCard("A group of hooded figures step out in front of the party and stop you.");
        int days = model.getDay() - model.getParty().getLoan().getDay();
        showRandomPortrait(model, Classes.THF, "Brotherhood Thug");
        portraitSay("Hey scumbag! The Brotherhood wants its money back. They sent us to remind you.");
        randomSayIfPersonality(PersonalityTrait.anxious, List.of(), "Yikes!", FacialExpression.afraid);
        randomSayIfPersonality(PersonalityTrait.aggressive, List.of(), "Now you have. Beat it.", FacialExpression.angry);

        println("What do you do?");
        List<String> options = new ArrayList<>(List.of("Attempt persuade", "Bribe", "Run away"));
        int repayCost = model.getParty().getLoan().repayCost();
        if (model.getParty().getGold() >= repayCost) {
            options.addFirst("Repay loan (" + repayCost + " gold)");
        }
        int selected = multipleOptionArrowMenu(model, 24, 24, options);
        if (options.get(selected).contains("persuade")) {
            boolean result = model.getParty().doSoloSkillCheck(model, this, Skill.Persuade, getPersuadeDifficulty(days));
            if (!result) {
                portraitSay("Talk all you want scum, but I think a busted kneecap or two will change your mind.",
                        FacialExpression.wicked);
                doCombat(model, days);
            } else {
                leaderSay("Come on guys, I'm only a little late with the money.", FacialExpression.relief);
                portraitSay("Fine! But you'd better pay up soon. We've got agents everywhere, don't think you can get away.");
                randomSayIfPersonality(PersonalityTrait.anxious, List.of(), "That sounds serious.");
            }
        } else if (options.get(selected).contains("Bribe")) {
            leaderSay("Hey fellas, can't we come to some agreement. Why don't we give you a little gold, " +
                    "and you tell the Brotherhood you couldn't find us.", FacialExpression.relief);
            int bribeCost = getBribeCost(days);
            portraitSay("Hmm... give us " + bribeCost + " gold and we'll leave you alone...", FacialExpression.disappointed);
            if (bribeCost <= model.getParty().getGold()) {
                print("Pay " + bribeCost + " gold? (Y/N) ");
                if (yesNoInput()) {
                    model.getParty().loseGold(bribeCost);
                    println("The party lost " + bribeCost + " gold.");
                    portraitSay("For now...", FacialExpression.wicked);
                }
            } else {
                leaderSay("Uh, how about " + model.getParty().getGold() + " instead?", FacialExpression.relief);
                portraitSay("You really think we're stupid don't you? I think you need to be taught a lesson!",
                        FacialExpression.disappointed);
                doCombat(model, days);
            }
        } else if (options.get(selected).contains("Repay")) {
            leaderSay("Fine, " + iOrWe() + "'ll pay up.");
            model.getParty().loseGold(repayCost);
            model.getParty().setLoan(null);
            portraitSay("Oh... Huh... I was kind of looking forward to messing you up. Oh well, plenty of deadbeats in the sea.");
            leaderSay("Now please, get lost.", FacialExpression.disappointed);
        } else {
            leaderSay("Uhm, okay here's your money... But wait, what's that behind you?", FacialExpression.surprised);
            portraitSay("Huh? What?", FacialExpression.surprised);
            leaderSay("Quick run!");
            int minSpeed = MyLists.minimum(model.getParty().getPartyMembers(), GameCharacter::getSpeed);
            if (MyRandom.rollD10() <= minSpeed + 1) {
                println("You manage to run away from the thugs.");
                portraitSay("Agh! Gosh darn it!");
                setFledCombat(true);
            } else {
                println("You try to get away but the thugs quickly surround you.");
                portraitSay("Not so fast deadbeat. We'd better teach you a lesson.", FacialExpression.wicked);
                doCombat(model, days);
            }
        }

    }


    private void doCombat(Model model, int days) {
        List<Enemy> enemies = new ArrayList<>();
        int num = getPersuadeDifficulty(days) - 3;
        for (int i = 0; i < num; ++i) {
            Enemy e = new BrotherhoodCronyEnemy('A');
            e.addCondition(new CowardlyCondition(enemies));
            enemies.add(e);
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
