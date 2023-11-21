package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.classes.Skill;
import model.items.Item;
import model.races.ElvenRace;
import model.races.Race;
import model.states.DailyEventState;
import model.states.EveningState;
import model.states.ShopState;
import util.MyLists;
import util.MyPair;
import util.MyRandom;

import java.util.List;

public class DwarvenCityEvent extends DailyEventState {
    private boolean stayedAtTavern = false;
    private List<Item> shopInventory;
    private int[] prices;
    private boolean parkVisited = false;
    private boolean palaceVisited = false;

    public DwarvenCityEvent(Model model) {
        super(model);
        shopInventory = ShopState.makeGeneralShopInventory(model, 10, 10, 10);
        prices = new int[shopInventory.size()];
        for (int i = 0; i < shopInventory.size(); ++i) {
            int range = 2;
            int cost = shopInventory.get(i).getCost();
            if (cost > 25) {
                range = 10;
            } else if (cost > 10) {
                range = 5;
            }
            prices[i] = cost + MyRandom.randInt(-range, range);
        }
    }

    @Override
    protected void doEvent(Model model) {
        println("At the end of a tunnel, two large stone doors block the way forward. As the party approaches them, " +
                "a little hatch in one of the doors open. You can see the bearded face of a dwarf peering out at you.");
        showRandomPortrait(model, Classes.CONSTABLE, Race.DWARF, "Dwarf");
        if (partyContainsElves(model)) {
            portraitSay("Go away strangers, we don't let pointy-ears into our city!");
            model.getParty().randomPartyMemberSay(model, List.of("What a racist..."));
            GameCharacter dwarf = getDwarfInParty(model);
            if (dwarf != null) {
                model.getParty().partyMemberSay(model, dwarf, List.of("The elf is with me. Come on brother, let us in."));
                admitted(model);
            } else {
                notAdmitted(model);
            }
        } else {
            portraitSay("Who are you folks, who come unannounced to our hidden city?");
            GameCharacter dwarf = getDwarfInParty(model);
            if (dwarf != null) {
                model.getParty().partyMemberSay(model, dwarf, List.of("We are travellers. Don't worry we're good people. Let us in."));
                admitted(model);
            } else {
                leaderSay("We are travellers, seeking shelter. Will you let us in?");
                portraitSay("Strangers must pay an entrance fee of 10 gold.");
                if (model.getParty().getGold() >= 10) {
                    print("Do you pay (Y) or do you try to persuade the dwarf (N)? ");
                    if (yesNoInput()) {
                        println("You hand the dwarf 10 gold through the hatch in the door.");
                        model.getParty().addToGold(-10);
                        admitted(model);
                    } else {
                        attemptPersuade(model);
                    }
                } else {
                    attemptPersuade(model);
                }
            }
        }
    }

    private void attemptPersuade(Model model) {
        MyPair<Boolean, GameCharacter> result = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.Persuade, 10);
        if (result.first) {
            model.getParty().partyMemberSay(model, result.second,
                    "But we've come to admire this wondrous city of yours, and to trade. " +
                            "Please admit us and we all shall profit from the meeting.");
            admitted(model);
        } else {
            notAdmitted(model);
        }
    }

    private void notAdmitted(Model model) {
        println("The face disappears and the little hatch slams shut.");
        if (model.getParty().size() > 1) {
            model.getParty().randomPartyMemberSay(model, List.of("I don't think they're going to let us in."));
        }
        println("You go back the way you came, annoyed by the stubbornness of the dwarves. ");
    }

    private GameCharacter getDwarfInParty(Model model) {
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc.getRace().id() == Race.DWARF.id()) {
                return gc;
            }
        }
        return null;
    }

    private boolean partyContainsElves(Model model) {
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc.getRace() instanceof ElvenRace) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean isFreeLodging() {
        return stayedAtTavern;
    }

    private void admitted(Model model) {
        portraitSay("Alright then, but we're keeping an eye on you lot. Don't try any funny business!");
        println("The stone doors swing open and reveal a large cavern inside. The dwarf was not lying. It is indeed a city within, " +
                "carved entirely out of stone. Dwarf men and women give you a surprised look as they hustle by, going about their " +
                "daily business. You can see shops, a market, dwellings, even a park illuminated by a beam of " +
                "daylight coming in through a shaft to the surface.");
        model.getParty().randomPartyMemberSay(model, List.of("This is spectacular!", "Breathtaking", "I never knew...",
                "It's a whole community.", "Typical Dwarves...", "Well, I'm impressed."));
        portraitSay("Well, where do you want to go?");
        do {
            setCurrentTerrainSubview(model);
            int result = multipleOptionArrowMenu(model, 32, 15,
                    List.of("Tavern", "Shop", "Market", "Park", "Palace", "Make Camp", "Leave"));
            switch (result) {
                case 0:
                    goToTavern(model);
                    break;
                case 1:
                    println("The dwarven shop has all kinds of items. You browse the wares while the proprietor eyes you with suspicion.");
                    ShopState shop = new ShopState(model, "Dwarven Shop", shopInventory, prices);
                    shop.run(model);
                    break;
                case 2:
                    println("The market has surprisingly fresh produce. You're guessing the dwarves have a secret passageway to " +
                            "the surface nearby.");
                    EveningState.buyRations(model, this);
                    break;
                case 3:
                    goToPark(model);
                    break;
                case 4:
                    if (!goToPalace(model)) {
                        return;
                    }
                    break;
                case 5:
                    parkVisited = false;
                    new EveningState(model, false, false, false).run(model);
                    setCurrentTerrainSubview(model);
                    break;
                default :
                    model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Okay gang, it's time to leave.");
                    println("You leave the dwarven city.");
                    return;
            }
            println("You are in the dwarven city. Where do you want to go?");
        } while (true);
    }

    private boolean goToPalace(Model model) {
        if (!palaceVisited) {
            println("You are admitted to the palace. After a little while you gain an audience with the king.");
            int d10Roll = MyRandom.rollD10();
            if (d10Roll < 3) {
                println("The king is angered by your presence and orders his guards to expel you from the city.");
                return false;
            } else if (d10Roll < 5) {
                println("The king is pleased to meet you and gives you a small stipend.");
                println("The party gains 20 gold.");
                model.getParty().addToGold(20);
            } else if (d10Roll < 7) {
                println("The king gladly takes you on a tour of the city and you learn much about its culture and history.");
                println("Each party member gains 20 experience.");
                MyLists.forEach(model.getParty().getPartyMembers(),
                        (GameCharacter gc) -> model.getParty().giveXP(model, gc, 20));
            } else if (d10Roll < 9) {
                println("The king is happy to receive the party and offers a gift of some rare crafting materials.");
                println("The party gains 15 materials.");
                model.getParty().getInventory().addToMaterials(15);
            } else {
                println("The king is impressed by the tales of your exploits and promises to use his influence to " +
                        "open doors for you in the future.");
                println("The party gains 1 reputation!");
                model.getParty().addToReputation(1);
            }
            model.getParty().randomPartyMemberSay(model, List.of("I like him."));
            this.palaceVisited = true;
            return true;
        }
        println("The palace is closed now.");
        return true;
    }

    private void goToPark(Model model) {
        if (!parkVisited) {
            println("You visit the park. There are some dwarves here.");
            new DwarfEvent(model).doEvent(model);
            parkVisited = true;
        } else {
            println("You visit the park. But nobody is around now.");
        }
    }

    private void goToTavern(Model model) {
        println("The dwarven tavern is much like any other you've seen, and so are the prices.");
        int cost = model.getParty().size()*2;
        if (model.getParty().getGold() >= cost) {
            print("Pay " + cost + " to stay at the tavern tonight? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().addToGold(-cost);
                EveningState eveningState = new EveningState(model, false, true, false);
                eveningState.run(model);
                setCurrentTerrainSubview(model);
                parkVisited = false;
            }
        } else {
            println("Unfortunately, you cannot afford to stay at the tavern.");
        }
    }
}
