package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Skill;
import model.states.DailyEventState;
import util.MyLists;
import view.sprites.MiniPictureSprite;
import view.subviews.MiniPictureSubView;

import java.util.ArrayList;
import java.util.List;

public class MushroomsEvent extends DailyEventState {
    private static final MiniPictureSprite SPRITE = new MiniPictureSprite(0x01);

    public MushroomsEvent(Model model) {
        super(model);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Find mushrooms",
                "I know a spot where there are mushrooms growing all over the ground");
    }

    @Override
    protected void doEvent(Model model) {
        model.setSubView(new MiniPictureSubView(model.getSubView(), SPRITE, "Mushrooms"));
        println("A large patch of mushrooms cover the ground ahead. " +
                "The party is hungry and they do look delicious, but are " +
                "they edible?");
        randomSayIfPersonality(PersonalityTrait.gluttonous, new ArrayList<>(),
                "They look absolutely scrumptious!");
        print("Do you pick the mushrooms? (Y/N) ");
        if (yesNoInput()) {
            boolean result = model.getParty().doSoloSkillCheck(model, this, Skill.Survival, 10);
            if (result) {
                println("The party gains 10 rations!");
                model.getParty().addToFood(10);
                model.getParty().randomPartyMemberSay(model, List.of("What a treat!"));
            } else {
                println("Each party member suffers 2 damage.");
                List<GameCharacter> died = new ArrayList<>();
                for (GameCharacter gc : model.getParty().getPartyMembers()) {
                    gc.addToHP(-2); // TODO: Make poisoned
                    if (gc.isDead()) {
                        died.add(gc);
                    }
                }
                MyLists.forEach(died, (GameCharacter gc) ->
                    RiverEvent.characterDies(model, this, gc,
                            " died from food poisoning.", true));
            }
        } else {
            model.getParty().randomPartyMemberSay(model, List.of("Aww, my stomach is growling..."));
        }
    }
}
