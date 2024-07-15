package view.subviews;

import model.Model;
import model.characters.GameCharacter;
import model.quests.*;
import model.states.feeding.VampireFeedingHouse;
import model.states.feeding.VampireFeedingState;

import java.awt.*;

public class VampireFeedingSubView extends AvatarSubView {
    private final GameCharacter character;
    private final VampireFeedingHouse house;
    private final VampireFeedingState state;
    private boolean avatarEnabled = true;

    public VampireFeedingSubView(VampireFeedingState state, GameCharacter vampire, VampireFeedingHouse house) {
        this.state = state;
        this.character = vampire;
        this.house = house;
    }

    @Override
    protected void specificDrawArea(Model model) {
        model.getScreenHandler().clearSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX);
        drawEdges(model);
        drawSubScenes(model);
    }

    private void drawSubScenes(Model model) {
        for (QuestNode node : house.getNodes()) {
            Point conv = convertToScreen(new Point(node.getColumn(), node.getRow()));
            int xPos = conv.x;
            int yPos = conv.y;
            node.drawYourself(model, xPos, yPos);

            if (state.getCurrentPosition() == node && avatarEnabled) {
                model.getScreenHandler().register("feedingAvatar", new Point(xPos, yPos),
                        character.getAvatarSprite(), 2);
            }
        }
    }

    public static Point convertToScreen(Point p) {
        return new Point(X_OFFSET + p.x*4, Y_OFFSET + p.y*4 + 2);
    }

    private void drawEdges(Model model) {
        for (QuestJunction j : house.getJunctions()) {
            for (QuestEdge edge : j.getConnections()) {
                edge.drawYourself(model.getScreenHandler(), j, X_OFFSET, Y_OFFSET);
            }
        }
        for (QuestSubScene qss : house.getSubScenes()) {
            if (qss.getSuccessEdge() != null) {
                qss.getSuccessEdge().drawYourself(model.getScreenHandler(), qss, X_OFFSET, Y_OFFSET);
            }
            if (qss.getFailEdge() != null) {
                qss.getFailEdge().drawYourself(model.getScreenHandler(), qss, X_OFFSET, Y_OFFSET);
            }
        }
    }

    @Override
    protected String getUnderText(Model model) {
        return "Feeding";
    }

    @Override
    protected String getTitleText(Model model) {
        return "VAMPIRE - FEEDING";
    }

    public void moveAlongEdge(Point from, QuestEdge edge) {
        avatarEnabled = false;

        QuestSubView.animateAvatarAlongEdge(this, from,
                edge, character.getAvatarSprite());

        avatarEnabled = true;
    }
}
