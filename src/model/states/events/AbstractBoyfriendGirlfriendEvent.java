package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.map.UrbanLocation;
import util.MyRandom;

import java.util.List;

public abstract class AbstractBoyfriendGirlfriendEvent extends PersonalityTraitEvent {
    private static final PersonalityTrait PERSONALITY_TRAIT = PersonalityTrait.naive;
    private static final int TALK_ABOUT_ADVENTURING = 0;
    private static final int WANTS_TO_VISIT_CAFE = 1;
    private static final int LIKES_LONG_WALKS = 2;

    public AbstractBoyfriendGirlfriendEvent(Model model, GameCharacter mainCharacter) {
        super(model, PERSONALITY_TRAIT, mainCharacter);
    }

    @Override
    public boolean isApplicable(Model model) {
        return model.getCurrentHex().getLocation() instanceof UrbanLocation &&
                getMainCharacter().hasHomeTown();
    }

    protected int topicChoice(Model model, GameCharacter main, GameCharacter friend) {
        String friendName = friend.getFirstName();
        println("What topic do you want " + main.getFirstName() + " to steer the conversation toward?");
        int choice = multipleOptionArrowMenu(model, 24, 24, List.of(
                "Life as an adventurer",
                friendName + "'s hopes and dreams"));
        if (choice == TALK_ABOUT_ADVENTURING) {
            println(main.getFirstName() + " tells the grand tale about " + hisOrHer(main.getGender()) +
                    " time in the party. At times " + friendName + " seems impressed, and at times " +
                    heOrShe(friend.getGender()) + " seems dismayed. ");
        } else {
            partyMemberSay(main, "But please, tell me more about your life.");
            println(friendName + " speaks about the odd jobs " + heOrShe(friend.getGender()) + " has done " +
                    "in her life. " + heOrSheCap(friend.getGender()) + " then goes on to speak of " +
                    hisOrHer(friend.getGender()) + " hopes, dreams and wishes.");
            print("In particular " + friendName + " expresses ");
            int wish = MyRandom.randInt(1, 3);
            if (wish == WANTS_TO_VISIT_CAFE) {
                println("a love for visiting cafes and restaurants.");
            } else if (wish == LIKES_LONG_WALKS) {
                println("an interest in long walks in nature.");
            } else {
                println("a wish to be out on the water in the moonlight.");
            }
            choice = wish;
        }
        return choice;
    }
}
