package model.states.dailyaction.tavern;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.states.GameState;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyRandom;

import java.util.List;

public class MusicalPerformanceInnWork extends InnWorkAction {
    public MusicalPerformanceInnWork() {
        super("Musical performance", "This place has been a little slow lately. " +
                "We need something to lively this place up a bit. Do you think you could muster a " +
                "musical performance for us?");
    }

    @Override
    public void doWork(Model model, TalkToBartenderState state) {
        GameCharacter gc = model.getParty().getPartyMember(0);
        if (model.getParty().size() > 1) {
            state.print("Which party member should do the musical performance? ");
            gc = model.getParty().partyMemberInput(model, state, gc);
        }
        state.println(gc.getName() + " steps up on a little make shift stage... ");
        SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, state, gc, Skill.Entertain, 7, 20, 0);
        if (result.isSuccessful()) {
            ClientSoundManager.playBackgroundMusic(BackgroundMusic.festiveSong);
        }
        int tipsGold = 0;
        int tipsObols = 0;
        String adjective = "good";
        if (result.getModifiedRoll() > 12) {
            tipsGold = MyRandom.randInt(3, 10);
            tipsObols = MyRandom.randInt(5, 50);
            adjective = "fantastic";
            state.println("And plays an incredible masterpiece of a song! While " + GameState.heOrShe(gc.getGender()) + " is playing, " +
                    " lots of more people come into the tavern to listen, dance and sing along to the wonderful music. When it's over, the patrons are all " +
                    "standing up, cheering and handing you tips, begging for an encore.");
        } else if (result.getModifiedRoll() > 11) {
            tipsGold = MyRandom.randInt(0, 5);
            tipsObols = MyRandom.randInt(0, 50);
            adjective = "great";
            state.println("And plays some great music! Some guests get up to dance. The mood in the tavern is much more cheerful " +
                    "when " + gc.getFirstName() + " finally steps off the stage. The tavern is more crowded now.");
        } else if (result.getModifiedRoll() > 9) {
            tipsObols = MyRandom.randInt(0, 30);
            state.println("And plays a lovely tune. Some patrons sing along with the song. When it's over there " +
                    "are definitely more people in the tavern than earlier.");
        } else if (result.isSuccessful()) {
            state.println("And plays a nice little melody on " + GameState.hisOrHer(gc.getGender())+ " instrument." +
                    "The patrons seem pleased and when it's over and there " +
                    "are more of them in the tavern than earlier.");
        }

        if (result.isFailure()) {
            state.println("But the sounds coming from " + GameState.hisOrHer(gc.getGender())+ " instrument are so " +
                    "terrible that some patrons start leaving the tavern. The ones that remain start booing " +
                    gc.getFirstName() + " off the stage." );
            state.partyMemberSay(gc, MyRandom.sample(List.of("Hey, I'm trying my best!",
                    "Give me a break.", "I'm just nervous!")));
            state.bartenderSay(model, "What was that? I thought you could lift our spirits a little, " +
                    "but you just made it worse! I refuse to pay to you.");
            state.leaderSay("Aw man...");
        } else { // isSuccessful
            state.bartenderSay(model, "That a " + adjective + " performance. Here's your pay.");
            state.println("You receive 6 gold from the bartender.");
            model.getParty().addToGold(6);
            model.getParty().addToGold(tipsGold);
            model.getParty().addToObols(tipsObols);
            if (tipsGold > 0 && tipsObols > 0) {
                state.println("You collected " + tipsGold + " gold and " + tipsObols + " obols in tips!");
            } else if (tipsObols > 0) {
                state.println("You collected " + tipsObols + " obols in tips!");
            } else if (tipsGold > 0) {
                state.println("You collected " + tipsObols + " gold in tips!");
            }
        }
    }
}
