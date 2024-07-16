package model.states.feeding;

import model.Model;
import model.characters.GameCharacter;
import model.quests.QuestEdge;
import model.states.GameState;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.List;

class BatSubScene extends FeedingSubScene {
    public static final Sprite32x32 BAT_SPRITE = new Sprite32x32("batsubscene", "quest.png", 0x92,
            MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BLACK);
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
        model.getScreenHandler().register(BAT_SPRITE.getName(), new Point(xPos, yPos), BAT_SPRITE, 1);
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
