package model.states;

import model.Model;
import model.items.weapons.Wakizashi;
import model.states.swords.LargeSamuraiSword;
import model.states.swords.MediumSamuraiSword;
import model.states.swords.SamuraiSword;
import model.states.swords.SmallSamuraiSword;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.subviews.CollapsingTransition;
import view.subviews.PickSamuraiSwordSubView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PickSamuraiSwordState extends DailyEventState {
    public static final List<MyColors> SWORD_COLORS =
            List.of(MyColors.BLUE, MyColors.DARK_PURPLE, MyColors.DARK_GREEN, MyColors.DARK_RED, MyColors.DARK_GRAY);

    public PickSamuraiSwordState(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        MyPair<List<SamuraiSword>, List<SamuraiSword>> swordsPair = makeSwords();
        println("Use the arrow keys to select a sword. Use SPACE to go to the next room or quit.");
        PickSamuraiSwordSubView subViewFirst = new PickSamuraiSwordSubView(swordsPair.first);
        PickSamuraiSwordSubView subViewSecond = new PickSamuraiSwordSubView(swordsPair.second);
        PickSamuraiSwordSubView subView = subViewFirst;
        CollapsingTransition.transition(model, subViewFirst);
        do {
            waitUntil(subView, PickSamuraiSwordSubView::timeToTransition);
            if (subView.getSelectedSword() != null) {
                SamuraiSword sword = subView.getSelectedSword();
                if (doesPurchaseSword(model, sword)) {
                    subView.removeSelected();
                    subView.clearSelected();
                }
                subView.clearSelected();
            } else {
                print("Do you want to go to the next room (Y) or quit (N)? ");
                if (!yesNoInput()) {
                    break;
                }
                leaderSay("Can we see the other swords?");
                printQuote("Smith", "Alright, just come with me into the next room.");
                subView.clearSelected();
                model.getLog().waitForAnimationToFinish();
                if (subView == subViewSecond) {
                    subView = subViewFirst;
                } else {
                    subView = subViewSecond;
                }
                CollapsingTransition.transition(model, subView);
                println("Use the arrow keys to select a sword. Use SPACE to go to the next room or quit.");
            }
        } while (true);
    }

    private boolean doesPurchaseSword(Model model, SamuraiSword sword) {
        String inscription = sword.hasInscription() ? " with an inscription" : "";
        if (MyRandom.flipCoin()) {
            printQuote("Smith", "Ah, the " + sword.getColorName() + " " + sword.getTypeName()
                    + inscription + ", it's a nice one.");
        } else {
            printQuote("Smith", "The " + sword.getColorName() + " " + sword.getTypeName()
                    + inscription + ", a fine choice.");
        }
        leaderSay("How much is it?");
        if (MyRandom.flipCoin()) {
            printQuote("Smith", "Let me see. It's " + sword.getCost() + " gold.");
        } else {
            printQuote("Smith", "I require " + sword.getCost() + " gold for it.");
        }
        if (sword.getCost() > model.getParty().getGold()) {
            leaderSay("Hmm... " + iOrWe() + " can't afford that.");
            printQuote("Smith", "Then it stays on the wall for now.");
        } else {
            print("Pay " + sword.getCost() + " gold for the " + sword.getTypeName() + "? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().spendGold(sword.getCost());
                println("You hand " + sword.getCost() + " to the smith.");
                println("The smith brings the sword down from the wall and hands it over to you.");
                printQuote("Smith", "It was crafted with the finest precision. Take good care of it.");
                leaderSay(iOrWe() + " will, thank you.");
                sword.makeItem().addYourself(model.getParty().getInventory());
                return true;
            } else {
                leaderSay("I think " + iOrWe() + "'ll pass for now.");
                printQuote("Smith", "Then it stays on the wall for now.");
            }
        }
        return false;
    }

    private MyPair<List<SamuraiSword>, List<SamuraiSword>> makeSwords() {
        List<SamuraiSword> allSwords = new ArrayList<>();
        for (MyColors color : SWORD_COLORS) {
            for (boolean inscription : List.of(true, false)) {
                for (int size = 0; size < 3; ++size) {
                    SamuraiSword ss = switch (size) {
                        case 0 -> new SmallSamuraiSword(color, inscription);
                        case 1 -> new MediumSamuraiSword(color, inscription);
                        case 2 -> new LargeSamuraiSword(color, inscription);
                        default -> null;
                    };
                    allSwords.add(ss);
                }
            }
        }
        Collections.shuffle(allSwords);
        return new MyPair<>(
                allSwords.subList(0, 15),
                allSwords.subList(15, allSwords.size()));
    }
}
