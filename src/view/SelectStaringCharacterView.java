package view;

import model.Model;
import model.characters.GameCharacter;
import model.classes.CharacterClass;
import util.Arithmetics;
import view.party.CharacterCreationView;
import view.party.SelectableListMenu;
import view.sprites.Sprite;

import java.awt.event.KeyEvent;
import java.util.List;

public class SelectStaringCharacterView extends SelectableListMenu {
    private GameCharacter[] currentSet;
    private CharacterClass[] classSet;
    private int selectedIndex = 0;
    private int selectedClass = 0;
    private boolean canceled = false;

    public SelectStaringCharacterView(Model model) {
        super(model.getView(), DrawingArea.WINDOW_COLUMNS-57, DrawingArea.WINDOW_ROWS-8);
        currentSet = model.getAllCharacters().toArray(new GameCharacter[0]);
    }

    private GameCharacter getSelectedCharacter() {
        if (currentSet.length == 0) {
            return null;
        }
        GameCharacter gc = currentSet[selectedIndex];
        classSet = gc.getClasses();
        gc.setClass(classSet[selectedClass]);
        gc.setEquipment(classSet[selectedClass].getStartingEquipment());
        Sprite.resetCallCount();
        return gc;
    }

    @Override
    public void transitionedFrom(Model model) {

    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of(new DrawableObject(xStart+2, yStart+1) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                String title = "- SELECT CHARACTER -";
                BorderFrame.drawCentered(model.getScreenHandler(), title, y++, MyColors.WHITE, MyColors.BLUE);
                GameCharacter gc = getSelectedCharacter();
                if (gc != null) {
                    gc.drawAppearance(model.getScreenHandler(), x + 6, y + 3);
                    BorderFrame.drawCentered(model.getScreenHandler(), gc.getRace().getQualifiedName(),
                            y+11, MyColors.WHITE, MyColors.BLUE);
                    CharacterCreationView.drawClassDetails(model, gc, x+1, y+15);
                }
            }
        });
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        return List.of(
        new CarouselListContent(xStart+3, yStart+3, getSelectedCharacter().getFullName()) {
            @Override
            public void turnLeft(Model model) {
                selectedIndex = Arithmetics.decrementWithWrap(selectedIndex, currentSet.length);
            }

            @Override
            public void turnRight(Model model) {
                selectedIndex = Arithmetics.incrementWithWrap(selectedIndex, currentSet.length);
            }
        },
        new CarouselListContent(xStart+3, yStart+15, getSelectedCharacter().getCharClass().getFullName() +
                " (" + getSelectedCharacter().getCharClass().getShortName() + ")") {
            @Override
            public void turnLeft(Model model) {
                selectedClass = Arithmetics.decrementWithWrap(selectedClass, classSet.length);
            }

            @Override
            public void turnRight(Model model) {
                selectedClass = Arithmetics.incrementWithWrap(selectedClass, classSet.length);
            }
        },
        new SelectableListContent(xStart + 10, yStart+39, "DONE") {
            @Override
            public void performAction(Model model, int x, int y) {
                setTimeToTransition(true);
            }
        },
        new SelectableListContent(xStart + 9, yStart+40, "CANCEL") {
            @Override
            public void performAction(Model model, int x, int y) {
                SelectStaringCharacterView.this.canceled = true;
                setTimeToTransition(true);
            }
        });
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {
        List<ListContent> content = buildContent(model, 0, 0);
        if (content.get(getSelectedRow()) instanceof CarouselListContent) {
            CarouselListContent carousel = (CarouselListContent) content.get(getSelectedRow());
            if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                carousel.turnLeft(model);
                madeChanges();
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                carousel.turnRight(model);
                madeChanges();
            }
        }
    }

    public GameCharacter getFinalCharacter() {
        if (canceled) {
            return null;
        }
        return getSelectedCharacter();
    }
}
