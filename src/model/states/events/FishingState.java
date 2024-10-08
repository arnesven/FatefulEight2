package model.states.events;

import model.GameStatistics;
import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
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

public class FishingState extends GameState {
    private static final MiniPictureSprite SPRITE = new MiniPictureSprite(0x32);
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
        if (!findFishingPole(model)) {
            println("Unfortunately, you don't have a fishing pole.");
            return model.getCurrentHex().getDailyActionState(model);
        }
        goFishing(model);
        print("If you wish to travel today you must leave now. Do you want to continue fishing? (Y/N) ");
        if (!yesNoInput()) {
            return model.getCurrentHex().getDailyActionState(model);
        }
        goFishing(model);
        goFishing(model);
        return model.getCurrentHex().getEveningState(model, false, false);
    }

    private void goFishing(Model model) {
        Fish fish = generateFish();
        MyPair<Boolean, GameCharacter> pair = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.Survival, fish.getDifficulty());
        if (pair.first) {
            GameStatistics.recordMaximumFish(fish.getWeight());
            partyMemberSay(pair.second, "Oh, it's a " + fish.getName().toLowerCase() + ".");
            if (model.getParty().size() > 1 && fish.getWeight() > 1500) {
                partyMemberSay(model.getParty().getRandomPartyMember(pair.second), "Nice catch!");
            }
            print("Do you want to convert the " + fish.getName().toLowerCase() + " into " +
                    fish.getRations() + " rations? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().addToFood(fish.getRations());
            } else {
                model.getParty().getInventory().add(fish);
            }
        } else {
            partyMemberSay(pair.second, "Not a bite.");
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

    private boolean findFishingPole(Model model) {
        if (MyLists.any(model.getParty().getInventory().getAllItems(), (Item it) ->
                (it instanceof Weapon && ((Weapon) it).isOfType(FishingPole.class)))) {
            return true;
        }
        return MyLists.any(model.getParty().getPartyMembers(), (GameCharacter gc) ->
                gc.getEquipment().getWeapon().isOfType(FishingPole.class));
    }
}
