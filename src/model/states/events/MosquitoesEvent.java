package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.states.DailyEventState;
import view.sprites.MiniPictureSprite;
import view.subviews.MiniPictureSubView;

public class MosquitoesEvent extends DailyEventState {
    private static final MiniPictureSprite SPRITE = new MiniPictureSprite(0x30);

    public MosquitoesEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        model.setSubView(new MiniPictureSubView(model.getSubView(), SPRITE, "Mosquitos"));
        println("As large as a fists, and with an unquenchable thirst for blood. " +
                "Such are the damned insects in this wretched place.");
        leaderSay("How does any traveller deal with this pestilence?");
        boolean result = model.getParty().doSoloSkillCheck(model, this, Skill.Survival, 7);
        if (result) {
            println("Fortunately one experienced party member has just " +
                    "to thing to rub on your skin to repel the locust.");
        } else {
            println("Each party member suffers 1 damage and exhausts 1 SP.");
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                gc.addToSP(-1); // TODO: Can get infected (Poison)
                if (gc.getHP() > 1) {
                    gc.addToHP(-1);
                }
            }
        }
    }
}
