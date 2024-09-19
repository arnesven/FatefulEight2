package model.states.dailyaction;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.states.cardgames.CardGameState;
import model.states.GameState;
import util.MyRandom;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.PortraitSubView;
import view.subviews.TavernSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardGameNode extends DailyActionNode {
    public static final Sprite TABLE = new Sprite32x32("cardstable", "world_foreground.png", 0x88,
            MyColors.BLACK, MyColors.TAN, MyColors.BROWN, MyColors.WHITE);
    private final ArrayList<Point> offsets;
    private CardGameState cardGameState = null;
    private final ArrayList<GameCharacter> defaultGuys = new ArrayList<>();

    public CardGameNode() {
        super("Play Cards");
        this.offsets = new ArrayList<>(List.of(new Point(-4, 0),
                new Point(-4, 4), new Point(0, 4)));
        for (int i = MyRandom.randInt(1, 3); i >= 0; --i) {
            CharacterAppearance randApp = PortraitSubView.makeRandomPortrait(Classes.None);
            GameCharacter gc = new GameCharacter("", "", randApp.getRace(),
                    Classes.None, randApp, Classes.NO_OTHER_CLASSES);
            defaultGuys.add(gc);
        }
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        if (cardGameState == null) {
            cardGameState = new CardGameState(model);
        }
        return cardGameState;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        if (cardGameState == null) {
            return true;
        }
        if (cardGameState.getNumberOfPlayers() < 2) {
            state.println("It seems like nobody is interested in playing any more.");
            return false;
        }
        return true;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        super.drawYourself(model, p);
        for (int i = 0; i < Math.min(3, defaultGuys.size()); ++i) {
            AvatarSprite avatarSprite = defaultGuys.get(i).getAvatarSprite();
            avatarSprite.synch();
            Point pos = new Point(p.x + offsets.get(i).x, p.y + offsets.get(i).y);
            model.getScreenHandler().register(avatarSprite.getName(), pos, avatarSprite);
        }
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }

    @Override
    public Sprite getBackgroundSprite() {
        return TavernSubView.FLOOR;
    }

    @Override
    public Sprite getForegroundSprite() {
        return TABLE;
    }
}
