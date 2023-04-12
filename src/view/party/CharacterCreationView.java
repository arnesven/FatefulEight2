package view.party;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.*;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import model.items.weapons.Weapon;
import model.races.Race;
import util.Arithmetics;
import util.MyPair;
import view.BorderFrame;
import view.DrawingArea;
import view.GameView;
import view.MyColors;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class CharacterCreationView extends SelectableListMenu {

    private static final Integer INPUT_MAX_LENGTH = 13;
    private static final String START_STRING = "þþþþþþþþþþþþ";
    private static final int COLUMN_SKIP = 12;
    private List<MyPair<StringBuffer, Integer>> buffers = new ArrayList<>();
    private boolean gender = true;
    private static final Race[] raceSet = Race.allRaces;
    private static final CharacterEyes[] eyeSet = CharacterEyes.allEyes;
    public static final Integer[] noseSet = new Integer[]{0, 2, 3, 4, 5, 6, 7, 8, 9, 0xA, 0xb, 0xC};
    public static final Integer[] mouthSet = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0xA, 0xB, 0xC};
    private static final Beard[] beardSet = Beard.allBeards;
    private static final MyColors[] hairColorSet = HairStyle.allHairColors;
    private static final HairStyle[] hairStyleSet = HairStyle.allHairStyles;
    private static final CharacterClass classSet[] = Classes.allClasses;
    private int selectedRace = 0;
    private int selectedMouth = 0;
    private int selectedNose = 0;
    private int selectedEyes = 0;
    private int selectedBeard = 0;
    private int selectedHairColor = 0;
    private int selectedHairStyle = 0;
    private int selectedClass = 0;
    private int other1 = 0;
    private int other2 = 0;
    private int other3 = 0;
    private CharacterAppearance lastAppearance;
    private GameCharacter lastCharacter;
    private boolean canceled = false;
    private boolean glasses = false;

    public CharacterCreationView(GameView previous) {
        super(previous, DrawingArea.WINDOW_COLUMNS-34, DrawingArea.WINDOW_ROWS-8);
        for (int i = 0; i < 2; ++i) {
            buffers.add(new MyPair<>(new StringBuffer(START_STRING), 0));
        }
        lastAppearance = makeAppearance();
        lastCharacter = makeCharacter();
    }

    @Override
    public void transitionedFrom(Model model) {  }

    private CharacterAppearance makeAppearance() {
        AdvancedAppearance app = new AdvancedAppearance(raceSet[selectedRace], gender,
                hairColorSet[selectedHairColor], mouthSet[selectedMouth],
                noseSet[selectedNose], eyeSet[selectedEyes], hairStyleSet[selectedHairStyle],
                beardSet[selectedBeard]);
        app.setHasGlasses(glasses);
        if (classSet[selectedClass] == Classes.None) {
            app.reset();
            app.applyFacialHair(raceSet[selectedRace]);
            app.addHairInBack();
        } else {
            app.setClass(classSet[selectedClass]);
        }
        return app;
    }

    private GameCharacter makeCharacter() {
        String firstName = buffers.get(0).first.toString();
        if (firstName.contains("þ")) {
            firstName = firstName.substring(0, firstName.indexOf("þ"));
        }
        String lastName = buffers.get(1).first.toString();
        if (lastName.contains("þ")) {
            lastName = lastName.substring(0, lastName.indexOf("þ"));
        }
        return new GameCharacter(firstName, lastName, raceSet[selectedRace], classSet[selectedClass], makeAppearance(),
                new CharacterClass[]{classSet[other1], classSet[other2], classSet[other3]});
    }


    private void rebuildAppearance() {
        lastAppearance = makeAppearance();
        lastCharacter = makeCharacter();
        Sprite.resetCallCount();
    }

    public GameCharacter getFinishedCharacter() {
        if (canceled) {
            return null;
        }
        rebuildAppearance();
        return lastCharacter;
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of(new DrawableObject(xStart+1, yStart+1) {

            @Override
            public void drawYourself(Model model, int x, int y) {
                String title = "- CUSTOM CHARACTER -";
                BorderFrame.drawCentered(model.getScreenHandler(), title, y++, MyColors.WHITE, MyColors.BLUE);
                lastAppearance.drawYourself(model.getScreenHandler(), x+COLUMN_SKIP+17, y+2);
                model.getScreenHandler().register(lastCharacter.getAvatarSprite().getName(),
                        new Point(x+COLUMN_SKIP+26, y+3),
                        lastCharacter.getAvatarSprite());
                y++;
                String[] labels = new String[]{"First Name", "Last Name", "", "Gender", "Race", "", "Eyes", "Nose",
                        "Mouth", "Beard", "Hair Color", "Hair", "Detail", "", "Class", "Other Class 1",
                        "Other Class 2", "Other Class 3"};
                for (int i = 0; i < labels.length; ++i) {
                    BorderFrame.drawString(model.getScreenHandler(), labels[i], x, y++, MyColors.WHITE, MyColors.BLUE);
                    y++;
                }

                int midX = x + COLUMN_SKIP + 14;
                int row = 17;
                drawCharacterDetails(model, lastCharacter, midX, row);
            }
        });
    }

    public static void drawCharacterDetails(Model model, GameCharacter lastCharacter, int midX, int row) {
        if (lastCharacter.getCharClass().id() != Classes.None.id()) {
            BorderFrame.drawString(model.getScreenHandler(),
                    lastCharacter.getCharClass().getFullName() + " (" + lastCharacter.getCharClass().getShortName() + ")",
                    midX, row++, MyColors.WHITE, MyColors.BLUE);
            row++;
        }
        drawClassDetails(model, lastCharacter, midX, row);
    }

    public static void drawClassDetails(Model model, GameCharacter lastCharacter, int midX, int row) {
        BorderFrame.drawString(model.getScreenHandler(),
                "Health: " + lastCharacter.getMaxHP() + " Speed: " + lastCharacter.getSpeed(),
                midX, row++, MyColors.WHITE, MyColors.BLUE);
        row++;

        for (Skill s : lastCharacter.getSkillSet()) {
            BorderFrame.drawString(model.getScreenHandler(), String.format("%-13s%2d", s.getName(),
                    lastCharacter.getRankForSkill(s)), midX, row++, MyColors.WHITE, MyColors.BLUE);
        }
        Weapon w = lastCharacter.getCharClass().getStartingEquipment().getWeapon();
        if (w != null) {
            BorderFrame.drawString(model.getScreenHandler(),
                    "Starting gear: ",
                    midX, ++row, MyColors.WHITE, MyColors.BLUE);
            BorderFrame.drawString(model.getScreenHandler(),
                    "  " + w.getName(),
                    midX, ++row, MyColors.WHITE, MyColors.BLUE);
            BorderFrame.drawString(model.getScreenHandler(),
                    "  " + lastCharacter.getCharClass().getStartingGold() + " Extra Gold",
                    midX, ++row, MyColors.WHITE, MyColors.BLUE);
            row++;
        }

        if (lastCharacter.getCharClass().id() != Classes.None.id()) {
            BorderFrame.drawString(model.getScreenHandler(),
                    "Armor Class " + (lastCharacter.getCharClass().canUseHeavyArmor() ? "HEAVY" : "LIGHT"),
                    midX, ++row, MyColors.WHITE, MyColors.BLUE);
        }
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        return List.of(new InputFieldContent(xStart + COLUMN_SKIP, yStart + 3, 0),
                new InputFieldContent(xStart + COLUMN_SKIP, yStart + 5, 1),
                new SelectableListContent(xStart + COLUMN_SKIP, yStart + 9, gender ? "Female" : "Male") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        setInnerMenu(new SelectedGenderMenu(CharacterCreationView.this, x, y), model);
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 11, raceSet[selectedRace].getQualifiedName()) {

                    @Override
                    public void turnLeft(Model model) {
                        selectedRace = Arithmetics.decrementWithWrap(selectedRace, raceSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        selectedRace = Arithmetics.incrementWithWrap(selectedRace, raceSet.length);
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 15, "Eyes #" + (selectedEyes + 1)) {
                    @Override
                    public void turnLeft(Model model) {
                        selectedEyes = Arithmetics.decrementWithWrap(selectedEyes, eyeSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        selectedEyes = Arithmetics.incrementWithWrap(selectedEyes, eyeSet.length);
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 17, "Nose #" + (selectedNose + 1)) {
                    @Override
                    public void turnLeft(Model model) {
                        selectedNose = Arithmetics.decrementWithWrap(selectedNose, noseSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        selectedNose = Arithmetics.incrementWithWrap(selectedNose, noseSet.length);
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 19, "Mouth #" + (selectedMouth + 1)) {
                    @Override
                    public void turnLeft(Model model) {
                        selectedMouth = Arithmetics.decrementWithWrap(selectedMouth, mouthSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        selectedMouth = Arithmetics.incrementWithWrap(selectedMouth, mouthSet.length);
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 21, "Beard #" + (selectedBeard + 1)) {
                    @Override
                    public void turnLeft(Model model) {
                        selectedBeard = Arithmetics.decrementWithWrap(selectedBeard, beardSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        selectedBeard = Arithmetics.incrementWithWrap(selectedBeard, beardSet.length);
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 23, hairColorSet[selectedHairColor].toString().replace("_", " ")) {
                    @Override
                    public void turnLeft(Model model) {
                        selectedHairColor = Arithmetics.decrementWithWrap(selectedHairColor, hairColorSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        selectedHairColor = Arithmetics.incrementWithWrap(selectedHairColor, hairColorSet.length);
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 25, "Hair #" + (selectedHairStyle + 1)) {
                    @Override
                    public void turnLeft(Model model) {
                        selectedHairStyle = Arithmetics.decrementWithWrap(selectedHairStyle, hairStyleSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        selectedHairStyle = Arithmetics.incrementWithWrap(selectedHairStyle, hairStyleSet.length);
                    }
                },
                new SelectableListContent(xStart + COLUMN_SKIP, yStart + 27, glasses ? "Glasses" : "None") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        setInnerMenu(new SelectAccessoryMenu(CharacterCreationView.this, x, y), model);
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 31,
                        classSet[selectedClass] == Classes.None ? "Not Set" : classSet[selectedClass].getFullName()) {
                    @Override
                    public void turnLeft(Model model) {
                        selectedClass = Arithmetics.decrementWithWrap(selectedClass, classSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        selectedClass = Arithmetics.incrementWithWrap(selectedClass, classSet.length);
                    }
                },
                new SelectableListContent(xStart + COLUMN_SKIP + 4, yStart + 33, classSet[other1].getShortName()) {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        setInnerMenu(new SelectOtherClassMenu(CharacterCreationView.this, x, y, classSet) {
                            @Override
                            protected void actionPerformedCallback(int index) {
                                setOther1(index);
                            }
                        }, model);
                    }
                },
                new SelectableListContent(xStart + COLUMN_SKIP + 4, yStart + 35, classSet[other2].getShortName()) {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        setInnerMenu(new SelectOtherClassMenu(CharacterCreationView.this, x, y, classSet) {
                            @Override
                            protected void actionPerformedCallback(int index) {
                                setOther2(index);
                            }
                        }, model);
                    }
                },
                new SelectableListContent(xStart + COLUMN_SKIP + 4, yStart + 37, classSet[other3].getShortName()) {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        setInnerMenu(new SelectOtherClassMenu(CharacterCreationView.this, x, y, classSet) {
                            @Override
                            protected void actionPerformedCallback(int index) {
                                setOther3(index);
                            }
                        }, model);
                    }
                },
                new SelectableListContent(xStart + COLUMN_SKIP + 11, yStart + 39, "DONE") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        setTimeToTransition(true);
                    }

                    @Override
                    public boolean isEnabled(Model model) {
                        boolean namesOk = !buffers.get(0).first.toString().equals(START_STRING) &&
                                !buffers.get(1).first.toString().equals(START_STRING);
                        return other1 != 0 && other2 != 0 && other3 != 0 && selectedClass != 0 && namesOk;

                    }
                },
                new SelectableListContent(xStart + COLUMN_SKIP + 10, yStart + 40, "CANCEL") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        CharacterCreationView.this.canceled = true;
                        setTimeToTransition(true);
                    }
                }
        );
    }

    private void setOther1(int index) {
        this.other1 = index;
    }

    public void setOther2(int index) {
        this.other2 = index;
    }

    public void setOther3(int index) {
        this.other3 = index;
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {
        if (getSelectedRow() < buffers.size()) {
            MyPair<StringBuffer, Integer> input = buffers.get(getSelectedRow());
            if (' ' == keyEvent.getKeyChar() || '-' == keyEvent.getKeyChar() ||
                    ('a' <= keyEvent.getKeyChar() && keyEvent.getKeyChar() <= 'z') ||
                    ('A' <= keyEvent.getKeyChar() && keyEvent.getKeyChar() <= 'Z')) {
                if (input.second < INPUT_MAX_LENGTH) {
                    input.first.setCharAt(input.second++, keyEvent.getKeyChar());
                    madeChanges();
                }
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                input.second = Math.max(0, input.second - 1);
                input.first.setCharAt(input.second, 'þ');
                madeChanges();
            }
        }
        List<ListContent> content = buildContent(model, 0, 0);
        if (content.get(getSelectedRow()) instanceof CarouselListContent) {
            CarouselListContent carousel = (CarouselListContent) content.get(getSelectedRow());
            if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                carousel.turnLeft(model);
                madeChanges();
                rebuildAppearance();
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                carousel.turnRight(model);
                madeChanges();
                rebuildAppearance();
            }
        }
    }

    private class InputFieldContent extends ListContent {
        public InputFieldContent(int x, int y, int index) {
            super(x, y, buffers.get(index).first.toString());
        }
    }

    private class SelectAccessoryMenu extends FixedPositionSelectableListMenu {

        public SelectAccessoryMenu(GameView partyView, int x, int y) {
            super(partyView, 10, 3, x, y);
        }

        @Override
        protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
            return new ArrayList<>();
        }

        @Override
        protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
            List<ListContent> result = new ArrayList<>();
            result.add(new SelectableListContent(xStart + 1, yStart + 1, "None") {
                @Override
                public void performAction(Model model, int x, int y) {
                    glasses = false;
                    setTimeToTransition(true);
                    rebuildAppearance();
                }
            });
            result.add(new SelectableListContent(xStart + 1, yStart + 2, "Glasses") {
                @Override
                public void performAction(Model model, int x, int y) {
                    glasses = true;
                    setTimeToTransition(true);
                    rebuildAppearance();
                }
            });
            return result;
        }

        @Override
        protected void specificHandleEvent(KeyEvent keyEvent, Model model) { }
    }

    private class SelectedGenderMenu extends FixedPositionSelectableListMenu {
        public SelectedGenderMenu(GameView view, int x, int y) {
            super(view, 8, 3, x, y);
        }

        @Override
        protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
            return new ArrayList<>();
        }

        @Override
        protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
            List<ListContent> result = new ArrayList<>();
            result.add(new SelectableListContent(xStart + 1, yStart + 1, "Female") {
                @Override
                public void performAction(Model model, int x, int y) {
                    gender = true;
                    setTimeToTransition(true);
                    rebuildAppearance();
                }
            });
            result.add(new SelectableListContent(xStart + 1, yStart + 2, "Male") {
                @Override
                public void performAction(Model model, int x, int y) {
                    gender = false;
                    setTimeToTransition(true);
                    rebuildAppearance();
                }
            });
            return result;
        }

        @Override
        protected void specificHandleEvent(KeyEvent keyEvent, Model model) { }
    }

    private abstract class SelectOtherClassMenu extends FixedPositionSelectableListMenu {
        private final CharacterClass[] someSet;

        public SelectOtherClassMenu(GameView game, int x, int y, CharacterClass[] someSet) {
            super(game, 5, 7, x, y);
            this.someSet = someSet;
        }

        @Override
        protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
            return new ArrayList<>();
        }

        @Override
        protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
            List<ListContent> result = new ArrayList<>();
            for (int i = 0; i < someSet.length; ++i) {
                int finalI = i;
                result.add(new SelectableListContent(xStart+1, yStart+i+1, someSet[finalI].getShortName()) {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        actionPerformedCallback(finalI);
                        setTimeToTransition(true);
                        rebuildAppearance();
                    }

                    @Override
                    public boolean isEnabled(Model model) {
                        return finalI != selectedClass && finalI != other1 && finalI != other2 && finalI != other3;
                    }
                });
            }
            return result;
        }

        protected abstract void actionPerformedCallback(int index);

        @Override
        protected void specificHandleEvent(KeyEvent keyEvent, Model model) { }
    }
}
