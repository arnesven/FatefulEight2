package view;

import model.Model;
import model.SettingsManager;
import model.states.GameState;
import model.states.beangame.BeanGameEvent;
import model.states.cardgames.CardGameState;
import model.states.cardgames.knockout.KnockOutCardGame;
import model.states.cardgames.runny.RunnyCardGame;
import model.states.events.*;
import model.states.warehouse.WarehouseEvent;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import view.party.DrawableObject;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SelectMinigameDialog extends SelectableListMenu {
    private GameState selectedEvent;

    public SelectMinigameDialog(GameView view) {
        super(view, 23, 16);
    }

    @Override
    public void transitionedFrom(Model model) {
        model.startGameWithState(selectedEvent);
        model.getParty().add(MyRandom.sample(model.getAllCharacters()));
        model.getParty().addToGold(100);
        model.getParty().addToObols(100);
        model.getSettings().toggleMovementSpeed();
        model.getSettings().toggleLogSpeed();
        SettingsManager.toggleTutorial(model);
    }

    @Override
    public GameView getNextView(Model model) {
        return new MainGameView();
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of(new DrawableObject(xStart+1, yStart+1) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                String title = "- MINI GAMES -";
                BorderFrame.drawCentered(model.getScreenHandler(), title, y, MyColors.WHITE, MyColors.BLUE);
            }
        });
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        AtomicInteger y = new AtomicInteger(yStart + 3);
        int x = xStart + 2;
        List<MyPair<String, EventMaker>> events = new ArrayList<>(List.of(
                new MyPair<>("Bean Game",  m -> new BeanGameEvent(m)),
                new MyPair<>("Market Day", m -> new MarketEvent(m)),
                new MyPair<>("Farmer's Horse Race", m -> new FarmersHorseRaceEvent(m)),
                new MyPair<>("Digging Game", m -> new DiggingGameEvent(m)),
                new MyPair<>("Archery", m -> new ArcheryRangeEvent(m)),
                new MyPair<>("Garden Maze", m -> new GardenMazeEvent(m)),
                new MyPair<>("Runny Card Game", m -> new CardGameState(m, new RunnyCardGame())),
                new MyPair<>("Knockout Card Game", m -> new CardGameState(m, new KnockOutCardGame(1))),
                new MyPair<>("Lotto House", m -> new LottoHouseEvent(m)),
                new MyPair<>("Warehouse", m -> new WarehouseEvent(m))
        ));
        events.sort(Comparator.comparing(p -> p.first));
        return MyLists.transform(events,
                pair -> new MinigameButton(pair.first, x, y.getAndIncrement(), pair.second));
    }

    private void startWithEvent(Model model, GameState state) {
        this.selectedEvent = state;
        setTimeToTransition(true);
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }

    private class MinigameButton extends SelectableListContent {
        private final EventMaker maker;

        public MinigameButton(String minigameName, int x, int y, EventMaker maker) {
            super(x, y, minigameName);
            this.maker = maker;
        }

        @Override
        public void performAction(Model model, int x, int y) {
            startWithEvent(model, maker.makeEvent(model));
        }
    }

    private interface EventMaker {
        GameState makeEvent(Model m);
    }
}
