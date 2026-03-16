package view;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.characters.preset.PresetCharacter;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.races.Race;
import util.Arithmetics;
import util.MyRandom;
import util.MyStrings;
import view.party.CharacterCreationView;
import view.party.DrawableObject;
import view.party.SelectableListMenu;
import view.sprites.Sprite;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class StartingCharacterView extends SelectableListMenu {
    private static final int BASE_WIDTH = DrawingArea.WINDOW_COLUMNS - 57;
    private static final int EXTRA_WIDTH = 17;
    private final Race[] raceSet;
    private GameCharacter[] currentSet;
    private CharacterClass[] classSet;
    private int selectedIndex = 0;
    private int selectedClass = 0;
    private int selectedRace = 0;
    private boolean canceled = false;
    private static final CharacterClass[] allClassesWithoutNone = makeAllClasses();

    public StartingCharacterView(Model model, GameCharacter[] charSet, boolean wide) {
        super(model.getView(), BASE_WIDTH + (wide ? EXTRA_WIDTH : 0), DrawingArea.WINDOW_ROWS-8);
        this.currentSet = charSet;
        selectedIndex = MyRandom.randInt(currentSet.length);
        if (currentSet[selectedIndex] instanceof PresetCharacter) {
            selectedClass = MyRandom.randInt(((PresetCharacter) currentSet[selectedIndex]).getClasses().length);
            raceSet = new Race[]{currentSet[selectedIndex].getRace()};
        } else {
            selectedClass = MyRandom.randInt(allClassesWithoutNone.length);
            raceSet = Race.allRaces;
            for (int i = 0; i < raceSet.length; ++i) {
                Race r = raceSet[i];
                if (r.id() == currentSet[selectedIndex].getRace().id()) {
                    selectedRace = i;
                    break;
                }
            }
        }
    }

    @Override
    protected boolean escapeDisposesMenu() {
        return false;
    }

    private GameCharacter getSelectedCharacter() {
        if (currentSet.length == 0) {
            return null;
        }
        GameCharacter gc = currentSet[selectedIndex];
        if (gc instanceof PresetCharacter) {
            classSet = ((PresetCharacter)gc).getClasses();
        } else {
            classSet = allClassesWithoutNone;
        }
        gc.setClass(classSet[selectedClass]);
        gc.addToHP(1000); // Start with max hp for class.
        gc.setEquipment(classSet[selectedClass].getDefaultEquipment());
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
                String title = getViewTitle();
                BorderFrame.drawCentered(model.getScreenHandler(), title, y++, MyColors.WHITE, MyColors.BLUE);
                GameCharacter gc = getSelectedCharacter();
                if (gc != null) {
                    gc.drawAppearance(model.getScreenHandler(), x + 6, y + 3);
                    if (raceSet.length == 1) {
                        String raceName = gc.getRace().getQualifiedName();
                        BorderFrame.drawString(model.getScreenHandler(), raceName,
                                x + (BASE_WIDTH - (raceName.length() + 1)) / 2 - 1, y + 10, MyColors.WHITE, MyColors.BLUE);
                    }
                    if (classSet.length == 1) {
                        BorderFrame.drawString(model.getScreenHandler(), gc.getCharClass().getFullName() + " (" + gc.getCharClass().getShortName() + ")",
                                x + 1, y+12, MyColors.WHITE, MyColors.BLUE);
                    }
                    CharacterCreationView.drawClassDetails(model, gc, x+1, y+14);

                    if (gc instanceof PresetCharacter) {
                        String description = ((PresetCharacter)gc).getDescription();
                        String[] parts = MyStrings.partition(description, EXTRA_WIDTH+1);
                        for (int i = 0; i < parts.length; ++i) {
                            BorderFrame.drawString(model.getScreenHandler(), parts[i],
                                    x + EXTRA_WIDTH + 2, y + 3 + i, MyColors.WHITE, MyColors.BLUE);
                        }
                    }
                }
            }
        });
    }

    protected abstract String getViewTitle();

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> content = new ArrayList<>();
        if (currentSet.length > 1) {
            String name = getSelectedCharacter().getFullName();
            content.add(new CarouselListContent(xStart+(getWidth()-name.length()+1)/2, yStart+3, name) {
                @Override
                public void turnLeft(Model model) {
                    selectedIndex = Arithmetics.decrementWithWrap(selectedIndex, currentSet.length);
                }

                @Override
                public void turnRight(Model model) {
                    selectedIndex = Arithmetics.incrementWithWrap(selectedIndex, currentSet.length);
                }
            });
        } else {
            content.add(new ListContent(xStart+3, yStart+3, getSelectedCharacter().getFullName()));
        }

        if (raceSet.length > 1) {
            content.add(new CarouselListContent(xStart + 3, yStart + 12, raceSet[selectedRace].getQualifiedName()) {
                @Override
                public void turnLeft(Model model) {
                    selectedRace = Arithmetics.decrementWithWrap(selectedRace, raceSet.length);
                    currentSet[selectedIndex] = replaceRace(currentSet[selectedIndex], raceSet[selectedRace]);
                }

                @Override
                public void turnRight(Model model) {
                    selectedRace = Arithmetics.incrementWithWrap(selectedRace, raceSet.length);
                    currentSet[selectedIndex] = replaceRace(currentSet[selectedIndex], raceSet[selectedRace]);
                }
            });
        }

        if (classSet.length > 1) {
            content.add(new CarouselListContent(xStart + 3, yStart + 14, getSelectedCharacter().getCharClass().getFullName() +
                    " (" + getSelectedCharacter().getCharClass().getShortName() + ")") {
                @Override
                public void turnLeft(Model model) {
                    selectedClass = Arithmetics.decrementWithWrap(selectedClass, classSet.length);
                }

                @Override
                public void turnRight(Model model) {
                    selectedClass = Arithmetics.incrementWithWrap(selectedClass, classSet.length);
                }
            });
        }

        content.add(new SelectableListContent(xStart + getWidth()/2, yStart+39, "OK") {
            @Override
            public void performAction(Model model, int x, int y) {
                setTimeToTransition(true);
            }
        });
        content.add(new SelectableListContent(xStart + getWidth()/2 - 2, yStart+40, "CANCEL") {
            @Override
            public void performAction(Model model, int x, int y) {
                StartingCharacterView.this.canceled = true;
                setTimeToTransition(true);
            }
        });
        return content;
    }

    private GameCharacter replaceRace(GameCharacter gc, Race race) {
        CharacterAppearance app = gc.getAppearance().copy();
        app.setRace(race);
        app.setShoulders(race.makeShoulders(app.getGender()));
        app.setNeck(race.makeNeck(app.getGender()));
        if (gc.getAppearance().getMascaraColor() == gc.getRace().getColor()) {
            app.setMascaraColor(race.getColor());
        }
        app.reset();
        return new GameCharacter(gc.getFirstName(), gc.getFullName().replace(gc.getFirstName() + " ", ""),
                race, gc.getCharClass(), app);
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {
        super.handleCarousels(keyEvent, model);
    }

    public GameCharacter getFinalCharacter() {
        if (canceled) {
            return null;
        }
        return getSelectedCharacter();
    }

    private static CharacterClass[] makeAllClasses() {
        List<CharacterClass> result = new ArrayList<>();
        for (int i = 0; i < Classes.allClasses.length; ++i) {
            if (Classes.allClasses[i].id() != Classes.None.id()) {
                result.add(Classes.allClasses[i]);
            }
        }
        return result.toArray(new CharacterClass[0]);
    }
}
