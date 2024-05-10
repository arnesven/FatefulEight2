package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.special.RedKnightCharacter;
import model.enemies.RedKnightEnemy;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.RecruitState;

import java.util.List;

public class RedKnightEvent extends DailyEventState {
    private GameCharacter redKnightChar = new RedKnightCharacter();

    public RedKnightEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        if (model.getParty().isSpecialCharacterMarked(redKnightChar)) {
            new NoEventState(model).doEvent(model);
            return;
        }
        model.getParty().markSpecialCharacter(redKnightChar);
        println("You spot two figures on the ground up ahead. One is lying face down, in a pool of dark liquid. The other " +
                "is a knight, clad in full plate armor, his head obscured by a full helm.");
        leaderSay("Let's approach carefully here and find out what happened.");
        leaderSay("Hello there. Are you in need of aid?");
        showExplicitPortrait(model, redKnightChar.getAppearance(), "Red Knight");
        portraitSay("Hello there travellers. No, I'm sorry to say that my poor master " +
                "is not in need of aid, for he has passed into the beyond. ");
        leaderSay("What happened?");
        portraitSay("We were ambushed by bandits. We fought well, but there were many of them. " +
                "I managed to route them, but my master here had taken a mortal wound.");
        portraitSay("I was sworn to protect him, but I failed him.");
        leaderSay("Our condolences friend.");
        portraitSay("I appreciate that. Will you help me bury him?");
        print("Help the Red Knight bury his fallen master? (Y/N) ");
        if (!yesNoInput()) {
            leaderSay("I'm sorry, but we must keep moving. Safe travels.");
            portraitSay("Good bye travellers.");
            return;
        }

        println("You help the Red Knight dig a grave and you lower the body down into it. All of you " +
                "get very warm. You are amazed the Red Knight can do the work in his armor.");
        leaderSay("Aren't you warm in that armor?");
        portraitSay("I am indeed.");
        leaderSay("Why don't you take it off?");
        portraitSay("I can't.");
        leaderSay("Why not?");
        portraitSay("I've taken a solemn vow never to remove my armor.");
        otherPersonSay("That's the most stupid thing I ever heard!");
        leaderSay("Come on. You're sweating buckets under there. Take it off, who's going to know? We won't tell.");
        portraitSay("I'm sorry. I am a man of principle.");
        println("You finish burying the Red Knight's master and you all sit down for a bit of a breather. " +
                "After a while, you get up to continue the journey.");
        leaderSay("Well, we're heading in that direction. Will you come along?");
        portraitSay("I'm afraid not.");
        println("You look around, but you can't see any settlements nearby, nor any kind of shelter.");
        leaderSay("Are you sure? Where else would you go?");
        portraitSay("I'm staying here.");
        leaderSay("Here... but we're in the middle of nowhere!");
        portraitSay("So it would seem.");
        leaderSay("But why!?");
        portraitSay("I'm forbidden to leave my master's side.");
        leaderSay("But he's dead... surely he can't need your protection now?");
        portraitSay("Unfortunately I've taken a solemn vow which prevents me from leaving his side. " +
                "The vow did not address whether he be alive or not.");
        otherPersonSay("Now THAT is the most stupid thing I ever heard!");
        leaderSay("Come on friend, you can't be serious. You'll starve to death out here.");
        portraitSay("I am serious, and you're likely right.");
        leaderSay("But there must be some way for you to break free from your servitude.");
        portraitSay("Only if I am disgraced by defeat in combat am I released from my servitude. " +
                "But since I have not been, I shall remain in my master's service.");
        leaderSay("Hang on... so if we defeat you in combat, you can come with us?");
        portraitSay("Hmm... yes, I assume so. But I must warn you, I am a fierce fighter, defeating me would not be easy.");
        leaderSay("Couldn't you just, you know... pull your punches a bit?");
        portraitSay("No, I am afraid not. I may never fight one whit below my abilities because...");
        otherPersonSay("Let me guess, you've taken a solemn vow?");
        leaderSay("Alright, I get it. Hmm, now let me think.");
        print("Do you wish to fight the Red Knight? (Y/N) ");
        if (!yesNoInput()) {
            leaderSay("Honestly, I don't feel comfortable doing this. The choice is yours knight, " +
                    "If you wish to stay here and starve so be it. But at least we gave you a chance.");
            portraitSay("I shall remain here.");
            leaderSay("Now really...");
            println("You leave the Red Knight and continue your journey.");
            return;
        }

        leaderSay("Alright Red Knight, we shall fight, but I'm telling you now, " +
                "go easy on us, and we'll go easy on you.");
        runCombat(List.of(new RedKnightEnemy('A')));
        if (haveFledCombat() || model.getParty().isWipedOut()) {
            return;
        }
        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, redKnightChar.getAppearance(), "Red Knight");
        println("The Red Knight lay on the ground, severely beaten by the fight.");
        portraitSay("I have been defeated in combat... I am disgraced...");
        leaderSay("Congratulations you are no longer bound by your moronic vow.");
        portraitSay("I... I think I'm dying...");
        print("Do you let the Red Knight die? (Y/N) ");
        if (yesNoInput()) {
            portraitSay("Aaargh...");
            otherPersonSay("What an idiot...");
            return;
        }
        println("You do your best to dress the knights wounds, even though he protests wildly when you try to remove his armor.");
        redKnightChar.addToHP(-redKnightChar.getMaxHP()+3);
        redKnightChar.setLevel((int)Math.max(1, Math.floor(GameState.calculateAverageLevel(model))));
        portraitSay("Thank you for helping me. I owe you an immense debt...");
        leaderSay("Oh no, don't you say it...");
        portraitSay("How can I ever repay you?");
        leaderSay("Don't even think it!");
        portraitSay("I know what I must do. I shall protect you.");
        leaderSay("No no no no no!");
        portraitSay("A swear a solemn vow never to leave your side! I shall be with you through wind and rain. " +
                "Through mishaps and hardships. Only by the disgrace of combat shall I be banished from your service!");
        leaderSay("I can't believe this guy...");
        new RecruitState(model, List.of(redKnightChar)).run(model);

    }

    private void otherPersonSay(String line) {
        Model model = getModel();
        if (model.getParty().size() > 1) {
            GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            model.getParty().partyMemberSay(model, other, line);
        }
    }
}
