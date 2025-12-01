package model.states.events;

import model.GameStatistics;
import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.Item;
import model.items.weapons.FishingPole;
import model.items.weapons.Weapon;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.fishing.*;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import view.sprites.MiniPictureSprite;
import view.subviews.MiniPictureSubView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FishingState extends GameState {
    private static final MiniPictureSprite SPRITE = new MiniPictureSprite(0x32);
    private static final int ATTEMPTS_PER_PERSON_LONG = 2;
    private MiniPictureSubView miniSubView;

    public FishingState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        println("The waters nearby may be suitable for fishing.");
        this.miniSubView = new MiniPictureSubView(model.getSubView(), SPRITE, "Fishing");
        model.setSubView(miniSubView);
        model.getTutorial().fishing(model);
        if (countFishingPoles(model) == 0) {
            println("Unfortunately, you don't have a fishing pole.");
            return model.getCurrentHex().getDailyActionState(model);
        }
        multipleFishingAttempts(model, 1);
        print("If you wish to travel today you must leave now. Do you want to continue fishing? (Y/N) ");
        if (!yesNoInput()) {
            return model.getCurrentHex().getDailyActionState(model);
        }
        multipleFishingAttempts(model, ATTEMPTS_PER_PERSON_LONG);
        return model.getCurrentHex().getEveningState(model, false, false);
    }

    private void multipleFishingAttempts(Model model, int attemptsPerPerson) {
        int fishingAttempts = Math.min(model.getParty().size(), countFishingPoles(model)) * attemptsPerPerson;
        println("You have " + fishingAttempts + " fishing attempts.");
        Map<GameCharacter, Integer> fishingCounts = new HashMap<>();
        GameCharacter lastFisher = model.getParty().getPartyMember(0);
        for (int i = 0; i < fishingAttempts; ++i) {
            lastFisher = goFishing(model, attemptsPerPerson, fishingCounts, lastFisher);
        }
    }

    private GameCharacter goFishing(Model model, int maxAttempts, Map<GameCharacter, Integer> counts, GameCharacter fisher) {
        fisher = selectFisher(model, maxAttempts, counts, fisher);
        tryToCatchFish(model, fisher);
        return fisher;
    }

    private GameCharacter selectFisher(Model model, int maxAttempts, Map<GameCharacter, Integer> counts, GameCharacter fisher) {
        while (true) {
            print("Which character do you want to fish with next? ");
            fisher = model.getParty().partyMemberInput(model, this, fisher);
            if (!counts.containsKey(fisher)) {
                counts.put(fisher, 1);
                break;
            }
            if (counts.get(fisher) == maxAttempts) {
                println(fisher.getName() + " cannot fish more right now.");
            } else {
                counts.put(fisher, counts.get(fisher) + 1);
                break;
            }
        }
        return fisher;
    }

    private void tryToCatchFish(Model model, GameCharacter fisher) {
        Fish fish = generateFish();
        SkillCheckResult checkResult = model.getParty().doSkillCheckWithReRoll(model, this, fisher, Skill.Survival, fish.getDifficulty(), 10, 0);
        if (checkResult.isSuccessful()) {
            GameStatistics.incrementFishCaught();
            GameStatistics.recordMaximumFish(fish.getWeight());
            partyMemberSay(fisher, "Oh, it's a " + fish.getName().toLowerCase() + ".");
            if (model.getParty().size() > 1 && fish.getWeight() > 1500) {
                partyMemberSay(model.getParty().getRandomPartyMember(fisher),
                        MyRandom.sample(List.of("Nice catch!", "That's a big one.", "Wow! Big!")));
            }
            print("Do you want to convert the " + fish.getName().toLowerCase() + " into " +
                    fish.getRations() + " rations? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().addToFood(fish.getRations());
            } else {
                model.getParty().getInventory().add(fish);
            }
        } else {
            partyMemberSay(fisher, "Not a bite.");
        }
    }

    private Fish generateFish() {
        int dieRoll = MyRandom.rollD10();
        if (dieRoll < 5) {
            return new BreamFish();
        }
        if (dieRoll < 6) {
            return new PikeFish();
        }
        if (dieRoll < 7) {
            return new CarpFish();
        }
        if (dieRoll < 8) {
            return new BassFish();
        }
        if (dieRoll < 9) {
            return new SalmonFish();
        }
        return new TunaFish();
    }

    public static int countFishingPoles(Model model) {
        int polesInInventory = MyLists.intAccumulate(model.getParty().getInventory().getAllItems(), (Item it) ->
                (it instanceof Weapon && ((Weapon) it).isOfType(FishingPole.class)) ? 1 : 0);
        int polesEquipped = MyLists.intAccumulate(model.getParty().getPartyMembers(), (GameCharacter gc) ->
                gc.getEquipment().getWeapon().isOfType(FishingPole.class) ? 1 : 0);
        return polesInInventory + polesEquipped;
    }
}
