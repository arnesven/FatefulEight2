package model.states.events;

import model.Model;
import model.classes.Skill;
import model.map.Direction;
import view.sprites.LoopingSprite;
import view.sprites.RaftSprite;
import view.sprites.Sprite;

import java.awt.*;

class RaftEvent extends AlternativeTravelEvent {
    private final LoopingSprite sprite;
    private final String description;

    public RaftEvent(Model model, String description) {
        super(model, true);
        this.sprite = new RaftSprite();
        this.description = description;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    protected boolean eventIntro(Model model) {
        println("There are enough vines and logs here to make a good raft. " +
                "Not only could the party use it to " + description + ", but travel " +
                "downstream as well.");
        if (model.getParty().hasHorses()) {
            println("Your horses cannot accompany you on a raft.");
        }
        print("Do you wish to attempt to build a raft? (Y/N) ");
        if (!yesNoInput()) {
            return false;
        }
        print("Would you like to use Survival instead of Labor for the upcoming collaborative skill check? (Y/N) ");
        boolean success;
        if (yesNoInput()) {
            success = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Survival, 7);
        } else {
            success = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Labor, 7);
        }
        if (!success) {
            println("You have failed to build the raft, and cannot " + description + ".");
            return false;
        }
        model.getParty().getHorseHandler().abandonHorses(model);
        return true;
    }

    @Override
    protected void eventOutro(Model model) {
    }

    @Override
    protected String getTitleText() {
        return "TRAVEL ON RAFT";
    }

    @Override
    protected String getUnderText() {
        return "You're floating on the river on a raft.";
    }

    @Override
    protected String getTravelPrompt() {
        return "Please select a hex, adjacent to the river, to travel to:";
    }

    @Override
    protected boolean isSelectableDestination(Point startPoint, Point cursorPos, int dx, int dy) {
        return startPoint.distance(cursorPos.x + dx, cursorPos.y + dy) < 3;
    }

    @Override
    protected boolean isValidDestination(Model model, Point selectedPos) {
        return model.getWorld().getHex(selectedPos).getRivers() != Direction.NONE;
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Find materials for raft",
                "I saw some materials which would be great for building a rafts a little while ago");
    }
}
