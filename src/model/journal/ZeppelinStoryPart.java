package model.journal;

import model.Model;
import model.actions.DailyAction;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.items.Item;
import model.items.PotionRecipe;
import model.items.potions.Potion;
import model.items.potions.ZeppelinFuel;
import model.map.WorldHex;
import model.quests.Quest;
import model.races.Race;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.ShopState;
import model.states.dailyaction.TownDailyActionState;
import model.states.events.AlternativeTravelEvent;
import util.MyRandom;
import view.sprites.Sprite;
import view.sprites.ZeppelinSprite;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ZeppelinStoryPart extends StoryPart {
    private static final int INITIAL_STEP = 0;
    private static final int NOT_BOUGHT_YET = 1;
    private static final int BOUGHT = 2;
    private static final Sprite ZEPPELIN = new ZeppelinSprite();

    private final String lordTitle;
    private int step = INITIAL_STEP;
    private Point workshopPos;
    private Point zeppelinPos;
    private CharacterAppearance xelbiAppearance = PortraitSubView.makeRandomPortrait(Classes.ART, Race.DWARF);
    private int stockDay;
    private boolean zeppelinFueled = true;
    private boolean inAnimation = false;

    public ZeppelinStoryPart(Point xelbiPosition, String lordTitle) {
        this.workshopPos = xelbiPosition;
        this.lordTitle = lordTitle;
        zeppelinPos = new Point(workshopPos.x, workshopPos.y);
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        return List.of(new ZeppelinJournalEntry());
    }

    @Override
    public void handleTownSetup(TownDailyActionState townDailyActionState) { }

    @Override
    public void progress() {
        step++;
    }

    @Override
    public List<DailyAction> getDailyActions(Model model, WorldHex worldHex) {
        Point hexPoint = model.getWorld().getPositionForHex(worldHex);
        List<DailyAction> actions = new ArrayList<>();
        if (workshopPos.x == hexPoint.x && workshopPos.y == hexPoint.y) {
            actions.add(new DailyAction("Visit Workshop", new VisitWorkshopEvent(model)));
        }
        if (zeppelinPos.x == hexPoint.x && zeppelinPos.y == hexPoint.y && step == BOUGHT) {
            if (zeppelinFueled) {
                actions.add(new DailyAction("Use Zeppelin", new FlyWithZeppelinEvent(model)));
            } else {
                actions.add(new DailyAction("Refuel Zeppelin", new RefuelZeppelinState(model)));
            }
        }
        if (!actions.isEmpty()) {
            return actions;
        }
        return super.getDailyActions(model, worldHex);
    }

    @Override
    public void drawMapObjects(Model model, int x, int y, int screenX, int screenY) {
        if (workshopPos.x == x && workshopPos.y == y) {
            model.getScreenHandler().register(MAP_SPRITE.getName(), new Point(screenX, screenY), MAP_SPRITE, 1);
        }
        if (step == BOUGHT && zeppelinPos.x == x && zeppelinPos.y == y && !inAnimation) {
            model.getScreenHandler().register(ZEPPELIN.getName(), new Point(screenX, screenY), ZEPPELIN, 2);
        }
    }

    @Override
    public String getHexInfo(Point position) {
        String extra = null;
        if (workshopPos.x == position.x && workshopPos.y == position.y) {
            extra = "Xelbi's Workshop";
        }
        if (zeppelinPos.x == position.x && zeppelinPos.y == position.y) {
            if (extra != null) {
                extra += ", ";
            } else {
                extra = "";
            }
            extra += "the zeppelin";
        }
        return extra;
    }

    @Override
    protected StoryPart getNextStoryPart(Model model, int track) {
        return null;
    }

    @Override
    public void addQuests(Model model, List<Quest> quests) { }

    @Override
    protected boolean isCompleted() {
        return step == BOUGHT;
    }

    private class ZeppelinJournalEntry extends MainStoryTask {
        public ZeppelinJournalEntry() {
            super("Xelbi's Project");
        }

        @Override
        public String getText() {
            if (step == INITIAL_STEP) {
                return "Meet Xelbi, the dwarven tinkerer at the secret workshop.";
            } else if (step == NOT_BOUGHT_YET) {
                return "Xelbi has offered to sell you his zeppelin for 250 gold.";
            }
            return "You purchased Xelbi's zeppelin. You may buy more fuel from him at his workshop.\n\nCompleted.";
        }

        @Override
        public boolean isComplete() {
            return ZeppelinStoryPart.this.isCompleted();
        }
    }

    private class VisitWorkshopEvent extends DailyEventState {
        public VisitWorkshopEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            showExplicitPortrait(model, xelbiAppearance, "Xelbi");
            if (step == 0) {
                println("You finally find the workshop, which is indeed well hidden. Inside are gears, cogs, " +
                        "metal slats, pistons, levers and all kinds of tools scattered about. A dwarf pops out from behind " +
                        "one of the junk piles. He seems upset that you've barged in unannounced.");
                portraitSay("Excuse me, who are you? What are you doing in my workshop? " +
                        "How did you even find this place? It should be a well guarded secret.");
                leaderSay("I'm sure it is. But we are here on order of the " + lordTitle + ".");
                portraitSay("Oh, now I remember. You are the group of adventurer's mentioned in the letter.");
                leaderSay("Letter?");
                portraitSay("The " + lordTitle + " sent me a letter recently, stating that I should expect " +
                        "a group of adventurers on a special mission in the service of the realm. ");
                leaderSay("I guess that's us.");
                portraitSay("Sorry I was rude earlier, I was just expecting somebody more...");
                leaderSay("Somebody more what?");
                portraitSay("Well, official looking I guess. Anyway, I have the letter here.");
                println("Xelbi brings out a crumpled piece of parchment.");
                portraitSay("I am to divulge the nature of and details about my latest project " +
                        "and offer my services to assist you in anyway I can.");
                leaderSay("That sounds wonderful. What's this project of yours? This looks interesting, is that it?");
                println("On the wall hangs what looks like a large handheld cannon.");
                portraitSay("Sorry no, that's an experimental projectile weapon, but it's not functioning. " +
                        "I could never get it to work properly.");
                leaderSay("What a shame. What about this then?");
                println("In one of the junk piles lay a shield held by a mechanical arm.");
                portraitSay("Oh yes, my extra-shield-arm project, it is quite interesting, but it's not working either.");
                leaderSay("Can you just tell us what it is then?");
                portraitSay("Of course I have it right here on the table.");
                println("On the workbench in front of you lay two small circular devices made from brass, " +
                        "each one not larger than a fist. Xelbi picks one up and demonstrates how the device can be opened.");
                leaderSay("Very pretty. What does it do?");
                portraitSay("It's a communications device. With this you can communicate over long distances.");
                leaderSay("Uh-huh...");
                portraitSay("I'll show you. You take this...");
                println("He hands you one of the two devices.");
                portraitSay("And I'll go outside....");
                println("Xelbi leaves the workshop. After a while you can hear his voice from the device in your hand. " +
                        "He asks if you can hear him, and you reply. Then he comes back inside, obviously immensely proud.");
                leaderSay("Fascinating. But I'm not sure how we could use this in our mission.");
                portraitSay("But it's a technological masterpiece. A wonder of our time.");
                println("Xelbi is visibly crestfallen.");
                leaderSay("I'm sure it is, but I'm afraid we've just wasted our time by coming here, and yours.");
                portraitSay("But, but...");
                leaderSay("Perhaps you could sell it to some scholars at a university or something. Now, if you'll excuse us, " +
                        "we have a dark a sinister plot to take over the realm to thwart.");
                println("You leave Xelbi in an apathetic state and make your way outside. ");
                println("When you come outside you notice something by the side of the workshop. It's a basket as large as a wagon. " +
                        "It has a machine with a propeller mounted on it, and has " +
                        "ropes fastened to it and to what looks like a huge sail. The whole thing looks curious. As you stand there, " +
                        "puzzling over its purpose, Xelbi approaches you again.");
                portraitSay("Ah yes, my zeppelin. Marvelous thing, it's motorized you know. I flew and visited the " + lordTitle + " once.");
                leaderSay("It's a flying machine? This thing actually flew and could take you places?");
                portraitSay("Yes of course.");
                leaderSay("Xelbi, are you sure the " + lordTitle + " didn't mean for you to show us this, and not those silly talky thingies?");
                portraitSay("My projects are not silly!");
                leaderSay("Well, this right here may actually be of some use to us. What do we need to make it fly?");
                portraitSay("The balloon must be inflated with a particular gas. It's not a rare gas, but it takes a whole day for it to fill up.");
                leaderSay("That's fine, we can wait.");
                portraitSay("The motor on the other hand, needs a special fuel to run. I have a few containers of it, but...");
                leaderSay("Yes? What's the problem.");
                portraitSay("My zeppelin... It took a great deal of time to make. And it was costly too... " +
                        "Parting with it without some kind of compensation just doesn't feel right.");
                leaderSay("What do you consider to be a fair price for it?");
                portraitSay("Well, the materials alone, including the engine, was more than 300 gold. " +
                        "But I will consider letting it go for 250. Although I'll want more if you want to by extra fuel.");

                if (model.getParty().getGold() < 250) {
                    rejectOffer();
                } else {
                    possiblyBuyZepplin(model);
                }
                increaseStep(model);
            } else if (step == NOT_BOUGHT_YET) {
                println("As you approach the workshop you can see that the zepplin has been inflated and it does indeed look " +
                        "marvellous. Xelbi steps out of the workshop and greets you.");
                portraitSay("You're back. Still interested in buying my zeppelin?");
                if (model.getParty().getGold() < 250) {
                    rejectOffer();
                } else {
                    possiblyBuyZepplin(model);
                }
            } else { // BOUGHT
                println("You visit Xelbi in his workshop. You can see he has been working on producing the zeppelin fuel, among his other projects.");
                portraitSay("Oh hello. Are you enjoying my zeppelin. Quite the feeling isn't it, soaring with the birds?");
                leaderSay(MyRandom.sample(List.of("It's otherwordly.", "It's like nothing I've ever experienced before.",
                        "It's magical.", "It's wonderful.")));
                String word = "a little";
                int amount = 1;
                if (model.getDay() - stockDay > 4) {
                    word = "lots";
                    amount = MyRandom.randInt(5, 10);
                }
                stockDay = model.getDay();
                portraitSay("Need more fuel? I've made " + word + " since you were here last.");
                List<Item> potions = new ArrayList<>();
                int[] prices = new int[amount + 1];
                for (int i = 0; i < amount; ++i) {
                    potions.add(new ZeppelinFuel());
                    prices[i] = potions.get(i).getCost();
                }
                potions.add(new PotionRecipe(new ZeppelinFuel()));
                prices[prices.length-1] = potions.get(potions.size()-1).getCost();
                ShopState shopState = new ShopState(model, "Xelbi", potions, prices);
                shopState.setSellingEnabled(false);
                ShopState.pressToEnterShop(this);
                shopState.run(model);
            }
        }

        private void possiblyBuyZepplin(Model model) {
            print("Do you buy the zeppelin from Xelbi for 250 gold? (Y/N) ");
            if (yesNoInput()) {
                buyZeppelin(model);
            } else {
                rejectOffer();
            }
        }

        private void rejectOffer() {
            leaderSay("We can't afford that Xelbi...");
            portraitSay("I understand. Could you raise the money later?");
            leaderSay("Possibly...");
            if (step == 0) {
                portraitSay("I tell you what. I'll inflate the balloon anyway and keep it ready for you if you " +
                        "should want to return later to purchase it from me. If not, well, talking about it has made me " +
                        "want to take a trip in it again. But don't worry, if you need it I'll sell it to you for sure.");
            } else {
                portraitSay("I'll keep it inflated for you, if you change your mind.");
            }
            leaderSay("Thank you. We may come back for it.");
            println("Wondering what it would be like to fly among the clouds, You leave Xelbi's workshop for now.");
        }

        private void buyZeppelin(Model model) {
            leaderSay("Okay Xelbi, you've got yourself a deal.");
            model.getParty().addToGold(-250);
            if (step == 0) {
                portraitSay("Okay then. I'll inflate the balloon.");
                println("Xelbi gets to work connecting a large hose to the balloon. Slowly it inflates while you " +
                        "daydream about what it'll be like to fly among the clouds. It's late in the day before the whole thing is ready. " +
                        "Now with the balloon inflated, it looks spectacular.");
            }
            portraitSay("There's enough fuel in the engine for one trip, and there's two spare canisters in the basket. " +
                    "In total you have enough for three trips.");
            println("You received 2 " + (new ZeppelinFuel()).getName() + ".");
            model.getParty().getInventory().add(new ZeppelinFuel());
            model.getParty().getInventory().add(new ZeppelinFuel());
            portraitSay("You can come back and see me any time if you want to buy more. It takes a little time to make " +
                    "and it's a volatile substance so don't expect me to have a huge stock of it on hand.");
            if (model.getParty().hasHorses()) {
                portraitSay("Oh, and your horses won't fit in the zeppelin, so if you want to fly, " +
                        "you will have to leave them behind.");
            }
            stockDay = model.getDay();
            leaderSay("Thanks Xelbi, that's really helpful.");
            if (step == INITIAL_STEP) {
                step++;
            } else {
                increaseStep(model);
            }
        }
    }

    private class FlyWithZeppelinEvent extends AlternativeTravelEvent {
        public FlyWithZeppelinEvent(Model model) {
            super(model);
        }

        @Override
        protected Sprite getSprite() {
            return ZEPPELIN;
        }

        @Override
        protected boolean isValidDestination(Model model, Point selectedPos) {
            WorldHex hex = model.getWorld().getHex(selectedPos);
            if (hex.getLocation() != null && !hex.getLocation().isDecoration()) {
                return false;
            }
            return super.isValidDestination(model, selectedPos);
        }

        @Override
        protected boolean eventIntro(Model model) {
            inAnimation = true;
            model.getParty().getHorseHandler().abandonHorses(model);
            return true;
        }

        @Override
        protected void eventOutro(Model model) {
            zeppelinPos = new Point(model.getParty().getPosition());
            inAnimation = false;
            println("You land the zeppelin and step out of the basket.");
            GameCharacter chara = model.getParty().getRandomPartyMember();
            partyMemberSay(chara, MyRandom.sample(List.of(
                    "What a feeling.", "Flying is my favorite way to travel." ,
                    "Let's go again soon.", "What a nice ride.", "That was lovely.",
                    "A bit windy, but the feeling is great.")));
            zeppelinFueled = false;
        }

        @Override
        protected String getTitleText() {
            return "FLY WITH ZEPPELIN";
        }

        @Override
        protected String getUnderText() {
            return "You are flying with the zeppelin!";
        }

        @Override
        protected String getTravelPrompt() {
            return "Please select a land hex to fly to. You cannot land in towns, castles, temples, ruins or at inns.";
        }

        @Override
        protected boolean isSelectableDestination(Point startPoint, Point cursorPos, int dx, int dy) {
            return startPoint.distance(cursorPos.x+dx, cursorPos.y+dy) < 5;
        }
    }

    private class RefuelZeppelinState extends GameState {
        public RefuelZeppelinState(Model model) {
            super(model);
        }

        private Potion findFuelPotion(List<Potion> potions) {
            for (Potion p : potions) {
                if (p instanceof ZeppelinFuel) {
                    return p;
                }
            }
            return null;
        }

        @Override
        public GameState run(Model model) {
            println("The engine of the zeppelin needs to be refueled in order to run.");
            Potion p = findFuelPotion(model.getParty().getInventory().getPotions());
            if (p == null) {
                println("Unfortunately you do not have any zeppelin fuel.");

            } else {
                print("Do you want to refuel the zeppelin with fuel? (Y/N) ");
                if (yesNoInput()) {
                    model.getParty().getInventory().remove(p);
                    zeppelinFueled = true;
                    println("You fill the tank up with fuel.");
                } else {
                    println("You step away from the zeppelin.");
                }
            }
            return model.getCurrentHex().getDailyActionState(model);
        }
    }
}
