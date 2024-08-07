package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.items.Item;
import model.races.Race;
import model.states.DailyEventState;
import model.states.ShootBallsState;
import model.states.ShopState;
import model.states.dailyaction.BuyHorseState;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyRandom;
import view.subviews.PortraitSubView;
import view.subviews.SubView;

import java.util.ArrayList;
import java.util.List;

public class MarketEvent extends DailyEventState {
    public MarketEvent(Model model) {
        super(model);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Go to market", "It's market day. You'll find both business and pleasure there");
    }

    @Override
    protected void doEvent(Model model) {
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.happyMandolin);
        println("Today is market day. The party casually browses the stands and booths.");
        randomSayIfPersonality(PersonalityTrait.gluttonous, new ArrayList<>(),
                "I wonder if they sell any special kinds of food here?");
        randomSayIfPersonality(PersonalityTrait.playful, new ArrayList<>(),
                "Let's check out if they have any games!");
        if (!chooseAnEvent(model)) {
            println("You leave the market early.");
            return;
        }
        println("There is still time to do do something at the market.");
        chooseAnEvent(model);
    }

    private boolean chooseAnEvent(Model model) {
        println("What do you want to do?");
        List<String> list = List.of("Pushy Merchant", "Food Stands", "Horse Trader", "Shoot Balls", "Retire");
        int chosen = multipleOptionArrowMenu(model, SubView.X_OFFSET+6, SubView.Y_MAX - 20, list);
        if (list.get(chosen).contains("Merchant")) {
            pushyMerchant(model);
            setCurrentTerrainSubview(model);
        } else if (list.get(chosen).contains("Food")) {
            new FoodStandsEvent(model).doEvent(model);
            setCurrentTerrainSubview(model);
        } else if (list.get(chosen).contains("Horse")) {
            horseTrader(model);
        } else if (list.get(chosen).contains("Balls")){
            shootBalls(model);
        } else {
            return false;
        }
        return true;
    }

    private void horseTrader(Model model) {
        println("A breeder is displaying some horses.");
        BuyHorseState buyHorse = new BuyHorseState(model, "Horse Trader");
        buyHorse.setPrice(model.getParty().getHorseHandler().getAvailableHorse(model).getCost() +
                MyRandom.randInt(-15, 5));
        buyHorse.run(model);
        if (model.getParty().getHorseHandler().getAvailableHorse(model) == null) {
            model.getParty().getHorseHandler().newAvailableHorse();
        }
    }

    private void pushyMerchant(Model model) {
        showRandomPortrait(model, Classes.MERCHANT, "Pushy Merchant");
        portraitSay("Hey you! You gotta 'ave a look at my stuff. Premium quality and damn fine prices!");
        model.getParty().randomPartyMemberSay(model, List.of("Doesn't hurt to look.",
                "I'm sure it's the same junk as usual, but let's look.",
                "He looks very honest... why don't we see what he has for sale?"));
        waitForReturn();
        List<Item> items = model.getItemDeck().draw(12);
        int[] prices = new int[12];
        for (int i = 0; i < 4; ++i) {
            prices[i] = items.get(i).getCost() / 2;
            prices[i + 4] = items.get(i + 4).getCost();
            prices[i + 8] = items.get(i + 8).getCost() + 10;
        }
        ShopState shop = new ShopState(model, "Pushy Merchant", items, prices);
        shop.setSellingEnabled(false);
        shop.run(model);
    }


    private void shootBalls(Model model) {
        println("In one booth a young man is calling out.");
        CharacterAppearance youngMan = PortraitSubView.makeRandomPortrait(Classes.None, Race.SOUTHERN_HUMAN, false);
        showExplicitPortrait(model, youngMan, "Young Man");
        portraitSay("Are you fast enough to shoot my balls? Only 2 gold to play!");
        if (model.getParty().getGold() < 2) {
            leaderSay("If only we had the coin...");
            return;
        }
        print("Do you want to play? (Y/N) ");
        if (yesNoInput()) {
            model.getParty().addToGold(-2);
            print("Who will play the ball shooting game?");
            GameCharacter shooter = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
            ShootBallsState ballsState = new ShootBallsState(model, shooter, ShootBallsState.getCharactersBowOrDefault(shooter));
            ballsState.run(model);
            setCurrentTerrainSubview(model);
            showExplicitPortrait(model, youngMan, "Young Man");
            int points = ballsState.getPoints();
            String mrOsMiss = shooter.getGender()?"miss":"mister";
            switch (points) {
                case 0:
                    portraitSay("Rotten luck " + mrOsMiss + "! But you can always try again.");
                    break;
                case 1:
                    portraitSay("Wow! Good shot " + mrOsMiss + "! Here's your prize!");
                    println("The party gains 10 gold.");
                    model.getParty().addToGold(10);
                    break;
                case 2:
                    println("The young man looks a bit surprised.");
                    portraitSay("Oh... eh, good job " + mrOsMiss + ". Here's your prize...");
                    println("The party gains 15 gold.");
                    model.getParty().addToGold(15);
                    break;
                default:
                    println("The young man is obviously annoyed by " + shooter.getFirstName() + "'s skill with the bow.");
                    portraitSay("... seriously? I mean, here you go " + mrOsMiss + ". Now maybe let somebody else have a go?");
                    println("The party gains 30 gold.");
                    model.getParty().addToGold(30);
            }
        } else {
            leaderSay("This distraction isn't worth our time.");
        }
        model.getLog().waitForAnimationToFinish();
        removePortraitSubView(model);
    }
}
