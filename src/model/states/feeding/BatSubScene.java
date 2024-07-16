package model.states.feeding;

import model.Model;
import model.characters.GameCharacter;
import model.quests.QuestEdge;
import model.states.GameState;
import view.MyColors;

import java.awt.*;
import java.util.List;

class BatSubScene extends FeedingSubScene {
    private final VampireFeedingHouse house;

    public BatSubScene(int col, int row, GameCharacter vampire, VampireFeedingHouse house) {
        super(col, row, vampire);
        this.house = house;
    }

    @Override
    protected MyColors getSuccessEdgeColor() {
        return MyColors.WHITE;
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(VampireFeedingHouse.BAT_SPRITE.getName(), new Point(xPos, yPos), VampireFeedingHouse.BAT_SPRITE, 1);
    }

    @Override
    protected QuestEdge specificDoAction(Model model, GameState state, GameCharacter vampire) {
        model.getParty().partyMemberSay(model, vampire, List.of("Batform!", "Bat!", "Dark Wings!",
                "Abracadabra!", "Shazam!", "Here I go!"));
        house.setBatForm(true);

        state.print(vampire.getFirstName() + " flies into the house through ");
        if (house.getOpenWindow() > 0) {
            state.println("the open window.");
        } else {
            state.println("the chimney.");
        }
        return getSuccessEdge();
    }
}
