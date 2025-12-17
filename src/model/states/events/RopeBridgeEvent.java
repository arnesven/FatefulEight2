package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.states.GameOverState;
import view.sprites.MiniPictureSprite;
import view.subviews.MiniPictureSubView;

import java.util.List;
import java.util.Locale;

public class RopeBridgeEvent extends RiverEvent {
    private static final MiniPictureSprite SPRITE = new MiniPictureSprite(0x52);
    private boolean walkAway = false;

    public RopeBridgeEvent(Model model) {
        super(model, true);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Go to rope bridge",
                "There's a rope bridge across the river not too far from here");
    }

    @Override
    public String getDistantDescription() {
        return "a river with a rope bridge across it";
    }

    @Override
    public boolean eventPreventsCrossing(Model model) {
        return walkAway;
    }

    @Override
    protected void doRiverEvent(Model model) {
        model.setSubView(new MiniPictureSubView(model.getSubView(), SPRITE, "Rope Bridge"));
        showEventCard("Rope Bridge", "A bridge of rope and planks is hoisted over the river, " +
                "it looks very old and worn. Crossing will obviously be " +
                "perilous.");
        if (model.getParty().hasHorses()) {
            println("Your horses cannot cross the rope bridge.");
        }
        randomSayIfPersonality(PersonalityTrait.anxious, List.of(model.getParty().getLeader()),
                "I've got a bad feeling about this.");
        print(" Do you try to cross? (Y/N) ");
        if (yesNoInput()) {
            model.getParty().getHorseHandler().abandonHorses(model);
            List<GameCharacter> failers = model.getParty().doCollectiveSkillCheckWithFailers(model, this, Skill.Acrobatics, 4);
            if (failers.isEmpty()) {
                println("The party manages to cross without incident.");
            } else {
                for (GameCharacter gc : failers) {
                    fallIntoRiver(model, gc, "has fallen into the river and tries to swim across!");
                }
            }
        } else {
            walkAway = true;
        }
    }
}
