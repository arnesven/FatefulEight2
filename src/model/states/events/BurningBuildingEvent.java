package model.states.events;

import model.Model;
import model.achievements.Achievement;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.map.UrbanLocation;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BurningBuildingEvent extends PersonalityTraitEvent {
    public BurningBuildingEvent(Model model, PersonalityTrait personalityTrait, GameCharacter mainCharacter) {
        super(model, personalityTrait, mainCharacter);
    }

    public static Achievement.Data getAchievementData() {
        return new Achievement.Data(BurningBuildingEvent.class.getCanonicalName(),
                "Burning Building",
                "You found a child trapped in the burning building and rescued it by jumping out of a window.");
    }

    @Override
    public boolean isApplicable(Model model) {
        return model.getCurrentHex().getLocation() instanceof UrbanLocation;
    }

    @Override
    protected void doEvent(Model model) {
        // TODO: mini image subview, otherwise this event activates on top of town subview.
        println("You're strolling down the street, when suddenly you hear loud shouts.");
        printQuote("Man", "Fire! Fire!");
        println("A house stands ablaze on the corner of the street. People are gathering around. " +
                "Some have buckets of water and are trying to extinguish the flames, but it seems to be no use.");
        leaderSay("It's useless. All we can do is try to contain it.");
        printQuote("Man", "Get more water! Do we know if anybody is still in there?");
        println("An paralyzing scream pierces your ears.");
        printQuote("Woman", "My daughter! She is still in the house! Oh no, my poor baby!");
        GameCharacter main = getMainCharacter();
        println("Before anybody else has time to react " + main.getName() + " sprints into the burning building!");
        leaderSay(main.getFirstName() + "! Come back... it's to late!");
        leaderSay("My god...");
        println(main.getName() + " has separated from the party. Press enter to continue.");
        waitForReturn();
        List<GameCharacter> toBench = new ArrayList<>(model.getParty().getPartyMembers());
        toBench.remove(main);
        model.getParty().benchPartyMembers(toBench);

        println("The heat from the fire is maddening and a thick black smoke is covering the ceiling.");
        partyMemberSay(main, "I won't last many minutes in here...");

        while (true) {
            if (loseOneHp(model, main)) {
                return;
            }
            partyMemberSay(main, MyRandom.sample(List.of("Where the heck is that kid?",
                    "Aagh... I have smoke in my eyes.", "The heat... so intense.",
                    "That kid must be around here somewhere.")));
            SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, this, main, Skill.Search,
                    9, 10, main.getRankForSkill(Skill.Perception));
            if (result.isSuccessful()) {
                successful(model, main);
                break;
            } else {
                print("There's nobody in " + main.getFirstName() + "'s current room. Do you continue the search " +
                        "or run out of the building? (Y/N) ");
                if (yesNoInput()) {
                    partyMemberSay(main, "I... I can't stay here any longer...");
                    println(main.getFirstName() + " is trying to find " + hisOrHer(main.getGender()) +
                            " a way out of the burning house.");
                    SkillCheckResult result2 = model.getParty().doSkillCheckWithReRoll(model, this,
                            main, Skill.Search, 6, 10, 0);
                    if (result2.isSuccessful()) {
                        unsuccessful(model, main);
                        break;
                    } else {
                        println(main.getFirstName() + " can't find the way out!");
                    }
                } else {
                    println(main.getFirstName() + " keeps searching the house, despite the raging fire around " +
                            himOrHer(main.getGender()) + ".");
                }
            }
        }
    }

    private void successful(Model model, GameCharacter main) {
        println("Suddenly " + main.getFirstName() + " hears the sound of a crying child." +
                " Upstairs, in a bedroom, a small girl is " +
                "hiding under a table, panicky and crying.");
        partyMemberSay(main, "Come on kid. Let's get out of here!");
        println(main.getFirstName() + " grabs the girl, but at that moment the ceiling above the doorway collapses. " +
                "The only other way out of the room is the window facing the street.");
        partyMemberSay(main, "Get on my back kid, we're going to have to jump.");
        printQuote("Kid", "Jump? Out of the window? Are you nuts?");
        partyMemberSay(main, "It's okay. We'll be alright. Here we go!");
        println(main.getFirstName() + " steps up on the window sill, then leaps into the air!");

        if (main.getHP() > 1) {
            SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model,
                    this, main, Skill.Acrobatics, 7, 10, 0);
            if (!result.isSuccessful()) {
                println(main.getFirstName() + " crashes into the street and is injured.");
                int damage = main.getHP() - 1;
                println(main.getName() + " takes " + damage + " damage.");
                main.addToHP(-damage);
            } else {
                println(main.getFirstName() + " lands gracefully in front of the crowd of townspeople.");
            }
        } else {
            println(main.getFirstName() + " lands gracefully in front of the crowd of townspeople.");
        }
        model.getParty().unbenchAll();
        println("The little girl returns to her mother. " + main.getFirstName() + " is black from soot, sweat " +
                "pouring from " + hisOrHer(main.getGender()) + " brow.");
        leaderSay(main.getFirstName() + "... You're a hero!");
        printQuote("Man", "A hero!");
        printQuote("Woman", "How can we ever repay you!");
        println("The crowd erupts in cheer. People are in such good spirits, all wanting to shake " + main.getFirstName() +
                "'s hand, that they almost seem to have forgotten about the burning building.");
        printQuote("Man", "Okay people, we still have work to do. We can't let this fire spread to the other houses.");
        println("The party spends the rest of the evening trying to help the townspeople contain the fire.");
        partyMemberSay(main, "The building is completely destroyed.");
        leaderSay("Yes, but nobody got hurt. That's what counts. Good job " + main.getFirstName() + ".");
        partyMemberSay(main, "Thanks " + model.getParty().getLeader().getFirstName() + ".");
        leaderSay("Let me just ask you. What were you thinking when you were running into the building?");
        partyMemberSay(main, "Thinking? I didn't think anything... I just did what I had to do.");
        completeAchievement(BurningBuildingEvent.class.getCanonicalName());
    }

    private void unsuccessful(Model model, GameCharacter main) {
        model.getParty().unbenchAll();
        println(main.getFirstName() + " emerges from the building, dirty with soot and sweat.");
        partyMemberSay(main, "I... I couldn't find the baby.");
        println("The mother seems to be in chock. While others sob around her, she falls to her knees.");
        println("Crestfallen, heartbroken and defeated, the party watches the house burn to the ground for hours, " +
                "until nothing but embers remain.");
    }

    private boolean loseOneHp(Model model, GameCharacter main) {
        println(main.getName() + " loses 1 HP.");
        main.addToHP(-1);
        if (main.isDead()) {
            println(main.getName() + " has perished in the burning house.");

            model.getParty().unbenchAll();
            leaderSay(heOrShe(main.getGender()) + " is gone...");
            println("Hours later, when the fire finally subsides you search trough the embers and find the charred remains of " +
                    main.getFirstName() + ".");
            print("You bury " + himOrHer(main.getGender()) + " along with what's left of " + hisOrHer(main.getGender()) +
                    " belongings");
            model.getParty().remove(main, false, false, 0);
            leaderSay(heOrSheCap(main.getGender()) + " was brave, but foolish" +
                    "May " + heOrShe(main.getGender()) + " rest in peace.");
            return true;
        }
        return false;
    }
}
