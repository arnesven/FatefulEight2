package model.quests;

import model.Model;
import model.characters.PersonalityTrait;
import model.items.weapons.Pistol;
import model.mainstory.pirates.PotentialMutineer;
import model.races.ElvenRace;
import model.races.HalfOrc;
import model.races.HumanRace;
import model.states.DailyEventState;
import model.states.QuestState;
import util.MyLists;
import util.MyRandom;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class TalkToCrewEvent extends DailyEventState {
    private final QuestState state;
    private final List<PotentialMutineer> crew;
    private final Set<PotentialMutineer> blockTalk;

    public TalkToCrewEvent(Model model, QuestState state, List<PotentialMutineer> potentialMutineers) {
        super(model);
        this.state = state;
        this.crew = potentialMutineers;
        this.blockTalk = new HashSet<>();
        for (PotentialMutineer pot : potentialMutineers) {
            if ((pot.isUnkind() && MyRandom.flipCoin()) ||
                    (pot.isAggressive() && MyRandom.randInt(3) == 0)) {
                blockTalk.add(pot);
            }
        }
    }

    @Override
    protected void doEvent(Model model) {
        do {
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
            println("Who do you want to talk to?");
            List<String> options = MyLists.transform(crew, c -> c.getCharacter().getFirstName());
            options.add("Cancel");
            int choice = multipleOptionArrowMenu(model, 24, 24, options);
            if (choice == options.size() - 1) {
                return;
            }
            talkTo(model, crew.get(choice));
        } while (true);
    }

    private void talkTo(Model model, PotentialMutineer person) {
        if (blockTalk.contains(person)) {
            println(person.getName() + " refuses to talk to you.");
            return;
        }
        println("You approach " + person.getName() + ".");
        showExplicitPortrait(model, person.getAppearance(), person.getName());
        do {
            println("What would you like to ask " + person.getName() + " about?");
            List<String> topics = List.of("Gender", "Race", "Personality", "Mutiny", "Return");
            int choice = multipleOptionArrowMenu(model, 24, 24, topics);
            if (choice == 0) {
                if (talkAboutGender(model, person)) {
                    blockTalk.add(person);
                    return;
                }
            } else if (choice == 1) {
                if (talkAboutRace(model, person)) {
                    blockTalk.add(person);
                    return;
                }
            } else if (choice == 2) {
                if (talkAboutPersonality(model, person)) {
                    blockTalk.add(person);
                    return;
                }
            } else if (choice == 3) {
                talkAboutMutiny(model, person);
                blockTalk.add(person);
                return;
            } else {
                println("You walk away from " + person.getName() + ".");
                return;
            }
        } while (true);
    }

    private boolean talkAboutPersonality(Model model, PotentialMutineer person) {
        List<String> questions = List.of("Do you prefer blades or pistols?",
                "Do you prefer rum or wine?");
        int choice = multipleOptionArrowMenu(model, 24, 24, questions);
        leaderSay(questions.get(choice));
        if (choice == 0) {
            answerWeaponQuestion(model, person);
        } else {
            answerDrinkQuestions(model, person);
        }
        int target = 0;
        if (person.isAggressive()) {
            target = 1;
        } else if (person.isUnkind()) {
            target = 2;
        }
        if (MyRandom.randInt(3) < target) {
            portraitSay(MyRandom.sample(List.of("Why are you asking me all these questions?", "Enough talk!",
                    "Bla bla bla! I have better things to do than standing her talking to you.")));
            println(person.getName() + " walks of in annoyance.");
            return true;
        }
        return false;
    }

    private void answerWeaponQuestion(Model model, PotentialMutineer person) {
        boolean hasAPistol = person.usesPistol();
        if (person.isFlippedWeapon()) {
            String preferred = hasAPistol ? "blades" : "pistols";
            portraitSay("I actually prefer " + preferred + ", but I lost mine in a recent fight, and now I have to use " +
                    "this clumsy " + person.getCharacter().getEquipment().getWeapon().getName().toLowerCase() + " instead.");
        } else if (person.isJovial()) {
            if (hasAPistol) {
                portraitSay("I like to use a pistol, but I'm thinking of upgrading to a cannon. Or maybe... two cannons!");
            } else {
                portraitSay("I like this cutlass, but I'm thinking of getting one more, or maybe even two more...");
                leaderSay("How would you hold onto the third one?");
                portraitSay("With my mouth.");
            }
            if (model.getParty().getLeader().hasPersonality(PersonalityTrait.jovial)) {
                leaderSay("Hehehe, that's funny! I like you.");
            } else if (model.getParty().getLeader().hasPersonality(PersonalityTrait.critical)) {
                leaderSay("You're not as funny as you think.");
            } else {
                leaderSay("Funny.");
            }
        } else {
            String extra = "";
            if (person.isAggressive()) {
                extra = " With the one on my belt, I kill anybody who gets in my way!";
            }
            portraitSay("I prefer " + (hasAPistol ? "pistols" : "blades") + "." + extra);
        }
    }

    private void answerDrinkQuestions(Model model, PotentialMutineer person) {
        if (person.isJovial()) {
            if (person.likesRum()) {
                portraitSay("I like rum, the stronger the better!");
            } else {
                portraitSay("I like wine, in really big barrels!");
            }
        } else if (person.isUnkind()) {
            portraitSay("I like exquisite " + (person.likesRum() ? "rums" : "wines") + ". I could be more specific, " +
                    "but I don't expect somebody like you to recognize what I'm talking about.");
        } else {
            portraitSay("I prefer " + (person.likesRum() ? "rum" : "wine") + ".");
        }
    }

    private boolean talkAboutGender(Model model, PotentialMutineer person) {
        List<String> questions = List.of("Are you a woman?", "Are you a man?");
        int choice = multipleOptionArrowMenu(model, 24, 24, questions);
        leaderSay(questions.get(choice));
        if (person.isTrans()) {
            if (person.getGender()) {
                portraitSay("I identify as a man, but most people seem to think I'm a woman.");
            } else {
                portraitSay("I identify as a woman, but most people seem to think I'm a man.");
            }
            return false;
        }
        boolean affirmative = person.getGender() == (choice == 0);
        if (affirmative) {
            if (person.isUnkind()) {
                portraitSay("Of course I'm a " + (person.getGender() ? "woman" : "man") +
                        "! What kind of silly question is that?");
                println(person.getName() + " is annoyed and walks away.");
                return true;
            }
            if (person.isJovial()) {
                if (person.getGender()) {
                    portraitSay("No, I'm a man. Don't you see how muscular and manly I am?");
                } else {
                    portraitSay("No, I'm a woman. Am I not beautiful and feminine?");
                }
                leaderSay("Seriously?");
                portraitSay("Hehehe");
                println(person.getName() + " just laughs and walks away.");
                return true;
            }
            String extra = "";
            if (person.isAggressive()) {
                extra = " So what?";
            }
            portraitSay("Yes, I'm a " + (person.getGender() ? "woman" : "man") + "." + extra);
        } else {
            if (person.isUnkind() || person.isAggressive()) {
                println(person.getName() + " frowns and recoils!");
                portraitSay("What are you implying? I'm obviously a " + (person.getGender() ? "woman" : "man") + "!");
                println(person.getName() + " is annoyed and walks away.");
                return true;
            }
            if (person.isJovial()) {
                if (person.getGender()) {
                    portraitSay("Yes. I'm so muscular and manly! Now, where are some women for me to objectify!?");
                } else {
                    portraitSay("Yes. I'm so beautiful and feminine... but wait, I'm also independent and brave! Eeek a mouse!");
                }
                leaderSay("Uhm... I'm confused.");
                portraitSay("Hehehe");
                println(person.getName() + " just laughs and walks away.");
                return true;
            }
            portraitSay("No, I'm a " + (person.getGender() ? "woman" : "man") + ".");
        }
        return false;
    }

    private boolean talkAboutRace(Model model, PotentialMutineer person) {
        println("What race to ask " + person.getName() + " if they are?");
        List<String> races = List.of("Human", "Elf", "Dwarf", "Halfling", "Half-Orc");
        int choice = multipleOptionArrowMenu(model, 24, 24, races);
        boolean affirmative;
        String article = " a";
        if (choice == 0) {
            article = "";
            leaderSay("Are you human?");
            affirmative = person.getRace() instanceof HumanRace;
        } else if (choice == 1) {
            article = " an";
            leaderSay("Are you an elf?");
            affirmative = person.getRace() instanceof ElvenRace;
        } else {
            leaderSay("Are you a " + races.get(choice) + "?");
            affirmative = person.getRace().getName().equalsIgnoreCase(races.get(choice));
        }

        if (affirmative) {
            if (person.isUnkind()) {
                portraitSay("Of course I'm" + article + " " + races.get(choice) + "! What kind of silly question is that?");
                println(person.getName() + " is annoyed and walks away.");
                return true;
            }
            if (person.isJovial()) {
                if (person.getRace().isShort()) {
                    portraitSay("No, I'm an elf. But I never ate my greens as a child, and my growth was stunted in puberty.");
                } else {
                    portraitSay("No, I'm an overgrown goblin. I was very hungry as a child.");
                }
                leaderSay("Seriously?");
                portraitSay("Hehehe");
                println(person.getName() + " just laughs and walks away.");
                return true;
            }
            if (person.isAggressive()) {
                portraitSay("Yes, I'm" + article + " " + races.get(choice) + ". So what?");
            } else {
                portraitSay("Yes, I'm" + article + " " + races.get(choice) + ". At your service!");
            }
        } else {
            if (person.isUnkind() || person.isAggressive()) {
                println(person.getName() + " frowns and recoils!");
                portraitSay("What are you insinuating? I'm" + article + " " +
                        person.getRace().getName() + ", and proud of it!");
                println(person.getName() + " is annoyed and walks away.");
                return true;
            }
            if (person.isJovial()) {
                if (person.getRace() instanceof HumanRace && races.get(choice).equalsIgnoreCase("Elf")) {
                    portraitSay("Yes! My ears were clipped by a sadistic prison guard.");
                } else if (person.getRace().isShort()) {
                    portraitSay("Yes! I never ate my greens as a child, and my growth was stunted in puberty.");
                } else if (person.getRace() instanceof HalfOrc) {
                    portraitSay("Yes! I was very hungry as a child, and I ate so many green vegetables that my skin turned green.");
                } else {
                    portraitSay("Yes! Don't I look it?");
                }
                leaderSay("Seriously?");
                portraitSay("Hehehe");
                println(person.getName() + " just laughs and walks away.");
                return true;
            }
            portraitSay("What? Obviously not.");
        }
        return false;
    }

    private void talkAboutMutiny(Model model, PotentialMutineer person) {
        if (MyRandom.flipCoin()) {
            leaderSay("Uhm... what are your general feelings about mutiny?");
            if (person.isJovial()) {
                portraitSay("Oh I love it! It's one of my favorite pastimes on the ship.");
                leaderSay("Seriously?");
                portraitSay("Hehehe");
                println(person.getName() + " just laughs and walks away.");
            } else {
                portraitSay("I... hey, did the Captain put you up to this? I'm out of here.");
                println(person.getName() + " stomps off.");
            }
        } else {
            leaderSay("Say... you wouldn't happen to be the mutineer, would you?");
            if (person.isJovial()) {
                portraitSay("Oh no, you found me out! Darn it, and I was so close to getting away with it too!");
                leaderSay("Getting away with... I'm not sure I understand.");
                portraitSay("Hehehe");
                println(person.getName() + " just laughs and walks away.");
            } else {
                portraitSay("I don't feel comfortable talking about this. We're done.");
                println(person.getName() + " stomps off.");
            }
        }

    }
}
