package model.states.events;

import model.Model;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.util.List;

public class PegasusEvent extends AlternativeTravelEvent {
    private Sprite pegasusSprite = new PegasusSprite();

    public PegasusEvent(Model model) {
        super(model);
    }

    @Override
    protected boolean eventIntro(Model model) {
        println("A winged horse lands in front of the party. It seems to be " +
                "willing to give them a ride.");
        if (model.getParty().size() > 3) {
            model.getParty().randomPartyMemberSay(model, List.of("But there's too many of us to fit on its back."));
            println("The pegasus shakes its head like it can understand you, then it flies away.");
            model.getParty().randomPartyMemberSay(model, List.of("What a shame..."));
            return false;
        }
        model.getParty().randomPartyMemberSay(model,
                List.of("We're gonna fly? Oh what a wonderful sensation to soar among the clouds!"));
        print("Do you ride the pegasus? (Y/N) ");
        if (yesNoInput()) {
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
