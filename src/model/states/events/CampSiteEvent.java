package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.Item;
import model.items.Prevalence;
import model.items.special.TentUpgradeItem;
import model.states.DailyEventState;
import util.MyLists;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class CampSiteEvent extends DailyEventState {

    private DailyEventState innerEvent = null;

    public CampSiteEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        print("Up in the distance, the party spots a camp site. ");
        int roll = MyRandom.rollD10();


        if (roll < 6) {
           println("It looks like there's nobody there.");
        } else if (roll < 9) {
            println("It looks like there's a few people at the camp site.");
        } else {
            println("It looks like there's a whole crowd of people at the camp site.");
        }

        print("Do you want to go to the camp site (Y), or do you avoid it (N)? ");
        if (!yesNoInput()) {
            return;
        }

        if (roll < 4) {
            emptySite();
        } else if (roll < 6) {
            abandoned(model);
        } else if (roll < 9) {
            innerEvent = MyRandom.sample(makeSomePeopleEvents(model));
            innerEvent.doTheEvent(model);
        } else {
            innerEvent = new CompanyEvent(model);
            innerEvent.doTheEvent(model);
        }
    }

    @Override
    public boolean haveFledCombat() {
        if (innerEvent != null) {
            return innerEvent.haveFledCombat();
        }
        return super.haveFledCombat();
    }

    private List<DailyEventState> makeSomePeopleEvents(Model model) {
        List<DailyEventState> events = new ArrayList<>();
        events.add(new MerchantEvent(model));
        events.add(new MagicianEvent(model));
        events.add(new ArtisanEvent(model));
        events.add(new PriestEvent(model));
        events.add(new OtherPartyEvent(model));
        events.add(new NoblemanEvent(model));
        events.add(new FriendEvent(model));
        events.add(new MageEvent(model));
        events.add(new PilgrimEvent(model));
        events.add(new BanditEvent(model));
        events.add(new MonkEvent(model));
        events.add(new ElfEvent(model));
        events.add(new DwarfEvent(model));
        events.add(new HalfOrcEvent(model));
        events.add(new HalflingEvent(model));
        return events;
    }

    private void emptySite() {
        println("The camp site is empty, only the ashes of a fire remains from its previous visitors.");
        leaderSay("This will be a good place to make camp tonight.");
    }

    private void abandoned(Model model) {
        println("The camp seems to have been hastily abandoned.");
        TentUpgradeItem tent = new TentUpgradeItem();
        println("The party finds a " + tent.getName() + "!");
        tent.addYourself(model.getParty().getInventory());

        leaderSay("Let's search this area and see if we can find any salvageable supplies");
        List<GameCharacter> failers = model.getParty().doCollectiveSkillCheckWithFailers(model, this, Skill.Search, 7);
        for (GameCharacter gc : MyLists.filter(model.getParty().getPartyMembers(), p -> !failers.contains(p))) {
            int roll2 = MyRandom.rollD6();
            if (roll2 < 3) {
                int rations = MyRandom.randInt(1, 3);
                println(gc.getName() + " finds " + rations + " rations.");
                model.getParty().addToFood(rations);
            } else if (roll2 == 3) {
                int obols = MyRandom.randInt(10, 50);
                println(gc.getName() + " finds " + obols + " obols.");
                model.getParty().addToObols(obols);
            } else if (roll2 == 4) {
                int gold = MyRandom.randInt(1, 5);
                println(gc.getName() + " finds " + gold + " gold.");
                model.getParty().addToGold(gold);
            } else if (roll2 == 5) {
                int ingredients = MyRandom.randInt(2, 12);
                println(gc.getName() + " finds " + ingredients + " ingeredients.");
                model.getParty().getInventory().addToIngredients(ingredients);
            } else { // 6
                Item it = model.getItemDeck().draw(1, Prevalence.common).get(0);
                println(gc.getName() + " finds an item, " + it.getName() + ".");
                it.addYourself(model.getParty().getInventory());
            }
        }
    }
}
