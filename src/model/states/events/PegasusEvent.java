package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PegasusEvent extends AlternativeTravelEvent {
    private Sprite pegasusSprite = new PegasusSprite();

    public PegasusEvent(Model model) {
        super(model, false);
    }

    @Override
    public String getDistantDescription() {
        return "a large creature, it's a pegasus";
    }

    @Override
    protected boolean eventIntro(Model model) {
        showEventCard("Pegasus", "A winged horse lands in front of the party. It seems to be " +
                "willing to give them a ride.");
        randomSayIfPersonality(PersonalityTrait.romantic, new ArrayList<>(), "This is like a dream. It's soooo beautiful!");
        if (model.getParty().size() > 3) {
            model.getParty().randomPartyMemberSay(model, List.of("But there's too many of us to fit on its back."));
            println("The pegasus shakes its head like it can understand you, then it flies away.");
            model.getParty().randomPartyMemberSay(model, List.of("What a shame..."));
            return false;
        }
        model.getParty().randomPartyMemberSay(model,
                List.of("We're gonna fly? Oh what a wonderful sensation to soar among the clouds!"));
        if (model.getParty().hasHorses()) {
            print("Do you ride the pegasus? You will have to abandon your horses. (Y/N) ");
        } else {
            print("Do you ride the pegasus? (Y/N) ");
        }
        if (yesNoInput()) {
            model.getParty().getHorseHandler().abandonHorses(model);
            return true;
        }
        return false;
    }

    @Override
    protected void eventOutro(Model model) {
        println("The pegasus lets you off its back, whinnies and flies away.");
        model.getParty().randomPartyMemberSay(model,
                List.of("Bye now, please come again soon!3",
                        "Thanks for the ride!", "Goodbye!", "That was awesome, thank you!3"));
    }

    @Override
    protected String getTravelPrompt() {
        return "Please select a land hex to fly to: ";
    }

    @Override
    protected boolean isSelectableDestination(Point startPoint, Point cursorPos, int dx, int dy) {
        return startPoint.distance(cursorPos.x+dx, cursorPos.y+dy) < 4;
    }

    @Override
    protected String getTitleText() {
        return "FLY ON PEGASUS";
    }

    @Override
    protected String getUnderText() {
        return "You are flying on a pegasus!";
    }

    @Override
    protected Sprite getSprite() {
        return pegasusSprite;
    }

    private class PegasusSprite extends LoopingSprite {
        public PegasusSprite() {
            super("pegasus", "enemies.png", 0x50, 32);
            setColor1(MyColors.GRAY);
            setColor2(MyColors.WHITE);
            setColor3(MyColors.PINK);
            setFrames(7);
        }
    }
}
