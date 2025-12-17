package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Skill;
import model.states.DailyEventState;
import model.states.EveningState;
import view.sprites.MiniPictureSprite;
import view.subviews.MiniPictureSubView;

import java.util.List;

public class ChasmEvent extends DailyEventState {
    private static final MiniPictureSprite SPRITE = new MiniPictureSprite(0x21);

    public ChasmEvent(Model model) {
        super(model);
    }

    @Override
    public String getDistantDescription() {
        return "a steep chasm";
    }

    @Override
    protected void doEvent(Model model) {
        model.setSubView(new MiniPictureSubView(model.getSubView(), SPRITE, "Chasm"));
        showEventCard("Chasm", "A deep chasm lies in front of the party. There is a very " +
                "narrow path and it will be difficult to traverse. Does the " +
                "party dare?");
        boolean didSay = randomSayIfPersonality(PersonalityTrait.cowardly, List.of(model.getParty().getLeader()),
                "Aaah! Did I mention I'm scared of heights?");
        if (!didSay) {
            model.getParty().randomPartyMemberSay(model, List.of("This looks very difficult.",
                    "I think we should turn back.", "Are we really going to do this?",
                    "We've come so far. Going back will take so long..."));
        }
        if (model.getParty().hasHorses()) {
            println("Your horses cannot walk on the narrow path.");
        }
        print("If you double back now, the journey will take you an extra day. Do you try to cross the chasm? (Y/N) ");
        if (yesNoInput()) {
            model.getParty().getHorseHandler().abandonHorses(model);
            List<GameCharacter> failers = model.getParty().doCollectiveSkillCheckWithFailers(model, this, Skill.Acrobatics, 3);
            if (failers.isEmpty()) {
                println("The party manages to cross without incident.");
            } else {
                for (GameCharacter gc : failers) {
                    DailyEventState.characterDies(model, this, gc, "has fallen to " + hisOrHer(gc.getGender()) + " death!", false);
                    model.getLog().waitForAnimationToFinish();
                }
            }
        } else {
            println("You turn away from the chasm, but it is already late in the day.");
            new EveningState(model, false, false, false).run(model);
            setCurrentTerrainSubview(model);
            println("You travel away away from the chasm and finally find a new route to take.");
        }
    }
}
