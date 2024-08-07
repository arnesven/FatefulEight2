package model.states.events;

import model.Model;
import model.SteppingMatrix;
import model.items.GoldDummyItem;
import model.items.Item;
import model.items.potions.HealthPotion;
import model.items.potions.RejuvenationPotion;
import model.items.potions.StaminaPotion;
import model.items.potions.StrengthPotion;
import model.items.weapons.Longsword;
import model.quests.QuestEdge;
import model.quests.QuestNode;
import model.states.DailyEventState;
import model.states.QuestState;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import sound.SoundEffects;
import util.MyRandom;
import util.MyStrings;
import view.subviews.CollapsingTransition;
import view.subviews.LottoHouseSubView;
import view.subviews.SubView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LottoHouseEvent extends DailyEventState {
    public LottoHouseEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("You pass a little hut. In front, there's a sign saying 'Lotto House - Try your luck!'.");
        print("Do you enter? (Y/N) ");
        if (!yesNoInput()) {
            return;
        }
        BackgroundMusic previous = ClientSoundManager.getCurrentBackgroundMusic();
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.jumpyBlip);
        goIntoLottoHouse(model);
        ClientSoundManager.playBackgroundMusic(previous);
    }

    private void goIntoLottoHouse(Model model) {
        List<Item> stuffToGet = new ArrayList<>(model.getItemDeck().draw(3));
        stuffToGet.addAll(model.getItemDeck().draw(2, 0.99));
        stuffToGet.add(new HealthPotion());
        stuffToGet.add(new HealthPotion());
        stuffToGet.add(new StaminaPotion());
        stuffToGet.add(new StaminaPotion());
        stuffToGet.add(new RejuvenationPotion());
        while (stuffToGet.size() < 20) {
            stuffToGet.add(new GoldDummyItem(MyRandom.randInt(2, 5)));
        }
        Collections.shuffle(stuffToGet);
        SteppingMatrix<Item> matrix = new SteppingMatrix<>(5, 4);
        matrix.addElements(stuffToGet);

        SubView oldSubView = model.getSubView();
        LottoHouseSubView newSubView = new LottoHouseSubView(matrix);
        CollapsingTransition.transition(model, newSubView);
        lottoManSay("Welcome to my lotto house. I've hidden some prizes in every chest in this room. " +
                "Some prizes are better than others... " +
                "For 10 gold, I'll let you open one of them. Or if you want, you can open five of them for 30 gold. " +
                "Wadda you say?");
        if (model.getParty().getGold() < 10) {
            leaderSay("Sorry. Can't afford it.");
            lottoManSay("Ok. No worries. Just come back when you have to gold.");
            leaderSay("Sure. See ya.");
            return;
        }
        model.getLog().waitForAnimationToFinish();
        int selected = multipleOptionArrowMenu(model, 24, 32, List.of("Open one", "Open five", "Forget it!"));

        if (selected == 2) {
            leaderSay("Na... better save the money for something real.");
            lottoManSay("Ok. No worries. Just come back if you change your mind.");
            return;
        }
        int picks;
        if (selected == 0 || model.getParty().getGold() < 30) {
            picks = 1;
            println("You paid 10 gold to the lotto man.");
            model.getParty().addToGold(-10);
        } else {
            picks = 5;
            println("You paid 30 gold to the lotto man.");
            model.getParty().addToGold(-30);
        }
        lottoManSay("Thanks. Now just pick a chest!");

        while (true) {
            newSubView.enableCursor();

            waitForReturn();
            Item it = newSubView.goToChestAndOpenIt(model);
            if (it == null) {
                lottoManSay("That chest is already open. Pick another one.");
            } else {
                println("You got " + it.getName() + ".");
                if (it.getCost() < 10) {
                    lottoManSay("Better luck next time!");
                } else if (it.getCost() >= 40) {
                    lottoManSay("Bingo! You got a good one!");

                } else {
                    lottoManSay("Nice. I almost wanted to keep that for myself.");
                }
                it.addYourself(model.getParty().getInventory());
                print("Press enter to continue.");
                waitForReturn();
                picks--;
                if (picks == 0) {
                    break;
                } else {
                    lottoManSay("Okay, you've got " + MyStrings.numberWord(picks) + " more chests to open.");
                }
            }
        }
    }

    private void lottoManSay(String text) {
        printQuote("Lotto Man", text);
    }
}
