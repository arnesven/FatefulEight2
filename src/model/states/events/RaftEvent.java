package model.states.events;

import model.Model;
import model.classes.Skill;
import model.map.SeaHex;
import model.map.WorldHex;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.awt.*;

public class RaftEvent extends RiverEvent {

    public RaftEvent(Model model) {
        super(model);
    }

    @Override
    public boolean eventPreventsCrossing(Model model) {
        return true;
    }

    @Override
    protected void doEvent(Model model) {
        InnerRaftEvent innerEvent = new InnerRaftEvent(model);
        innerEvent.doEvent(model);
    }

    private class InnerRaftEvent extends AlternativeTravelEvent {
        private final LoopingSprite sprite;

        public InnerRaftEvent(Model model) {
            super(model);
            this.sprite = new RaftSprite();
        }

        @Override
        protected Sprite getSprite() {
            return sprite;
        }

        @Override
        protected boolean eventIntro(Model model) {
            println("There are enough vines and logs here to make a good raft. " +
                    "Not only could the party use it to cross the river, but travel " +
                    "downstream as well.");
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
                println("You have failed to build the raft, and cannot cross the river today.");
                return false;
            }
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
            return "Please select a hex, adjacent to the river (down stream), to travel to:";
        }

        @Override
        protected boolean isSelectableDestination(Point startPoint, Point cursorPos, int dx, int dy) {
            return startPoint.distance(cursorPos.x+dx, cursorPos.y+dy) < 3;
        }

        @Override
        protected boolean isValidDestination(Model model, Point selectedPos) {
            return model.getWorld().getHex(selectedPos).getRivers() != WorldHex.NONE;
        }
    }

    private class RaftSprite extends LoopingSprite {
        public RaftSprite() {
            super("raft", "enemies.png", 0x60, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.BROWN);
            setColor3(MyColors.DARK_GREEN);
            setColor4(MyColors.LIGHT_BLUE);
            setFrames(2);
        }
    }
}
