package view.party;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.*;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import model.items.weapons.Weapon;
import model.races.Race;
import model.races.Shoulders;
import util.Arithmetics;
import util.MyPair;
import util.MyRandom;
import view.BorderFrame;
import view.DrawingArea;
import view.GameView;
import view.MyColors;
import view.help.TutorialClassesDialog;
import view.sprites.Sprite;
import view.sprites.Sprite8x8;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class CharacterCreationView extends SelectableListMenu {

    private static final Integer INPUT_MAX_LENGTH = 12;
    private static final String START_STRING = "þþþþþþþþþþþþ";
    private static final int COLUMN_SKIP = 12;
    public static final Sprite CHECK_SPRITE = new Sprite8x8("check", "charset.png", 0xB6, MyColors.BLACK, MyColors.LIGHT_GREEN, MyColors.BLUE, MyColors.CYAN);
    public static final Sprite NOT_OK_SPRITE = new Sprite8x8("notok", "charset.png", 0xB7, MyColors.BLACK, MyColors.LIGHT_RED, MyColors.BLUE, MyColors.CYAN);
    private List<MyPair<StringBuffer, Integer>> buffers = new ArrayList<>();
    private boolean gender = true;
    private static final Race[] raceSet = Race.allRaces;
    private static final CharacterEyes[] eyeSet = CharacterEyes.allEyes;
    public static final Integer[] noseSet = new Integer[]{0, 2, 3, 4, 5, 6, 7, 8, 9, 0xA, 0xb, 0xC, 0xD, 0xE, 0xF};
    public static final Integer[] mouthSet = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF};
    private static final Beard[] beardSet = Beard.allBeards;
    private static final MyColors[] hairColorSet = HairStyle.allHairColors;
    private static final HairStyle[] hairStyleSet = HairStyle.allHairStyles;
    public static final MyColors[] detailColorSet = new MyColors[]{
            MyColors.GRAY, MyColors.GOLD, MyColors.CYAN, MyColors.WHITE, MyColors.DARK_BLUE,
            MyColors.ORANGE, MyColors.RED, MyColors.PINK, MyColors.LIGHT_GREEN, MyColors.BLACK};
    public static final MyColors[] makeupColorSet = new MyColors[]{MyColors.BLUE,
            MyColors.BLACK, MyColors.DARK_GREEN, MyColors.DARK_BLUE, MyColors.DARK_BROWN,
            MyColors.GREEN, MyColors.BLUE, MyColors.RED,
            MyColors.LIGHT_GREEN, MyColors.LIGHT_BLUE, MyColors.LIGHT_RED,
            MyColors.PURPLE, MyColors.GOLD, MyColors.LIGHT_GRAY, MyColors.BEIGE};
    private static final CharacterClass[] classSet = Classes.allClasses;
    private static final Ears[] earSet = Ears.allEars;
    private static final String[] shoulderSet = ShouldersFactory.shoulderNames;
    private static final String[] neckSet = NeckFactory.neckNames;
    private static final FaceDetail[] accessorySet = FaceDetail.ALL_DETAILS;
    private int selectedRace = 0;
    private int selectedMouth = 0;
    private int selectedNose = 0;
    private int selectedEyes = 0;
    private int selectedBeard = 0;
    private int selectedHairColor = 1;
    private int selectedHairStyle = 0;
    private int selectedClass = 0;
    private int other1 = 0;
    private int other2 = 0;
    private int other3 = 0;
    private int accessory = 0;
    private int selectedDetailColor = 0;
    private int selectedEars = 0;
    private int selectedMascara = 0;
    private int selectedLipColor = 0;
    private int selectedShoulders = 0;
    private int selectedNeck = 0;
    private boolean showSkeleton = false;
    private CharacterAppearance lastAppearance;
    private GameCharacter lastCharacter;
    private boolean canceled = false;

    public CharacterCreationView(GameView previous) {
        super(previous, DrawingArea.WINDOW_COLUMNS-34, DrawingArea.WINDOW_ROWS-6);
        for (int i = 0; i < 2; ++i) {
            buffers.add(new MyPair<>(new StringBuffer(START_STRING), 0));
        }
        lastAppearance = makeAppearance();
        lastCharacter = makeCharacter();
    }

    @Override
    protected boolean escapeDisposesMenu() {
        return false;
    }

    @Override
    public void transitionedFrom(Model model) {  }

    private CharacterAppearance makeAppearance() {
        AdvancedAppearance app = new AdvancedAppearance(raceSet[selectedRace], gender,
                hairColorSet[selectedHairColor], mouthSet[selectedMouth],
                noseSet[selectedNose], eyeSet[selectedEyes], hairStyleSet[selectedHairStyle],
                beardSet[selectedBeard]);
        if (showSkeleton) {
            app = new SkeletonAppearance(app.getShoulders(), app.getGender());
        }
        app.setRaceSpecificEars(selectedEars == 0);
        app.setEars(earSet[selectedEars]);
        app.setFaceDetail(accessorySet[accessory]);
        app.setDetailColor(detailColorSet[selectedDetailColor]);
        if (selectedMascara > 0) {
            app.setMascaraColor(makeupColorSet[selectedMascara]);
        }
        if (selectedLipColor > 0) {
            app.setLipColor(makeupColorSet[selectedLipColor]);
        }
        if (selectedShoulders > 0) {
            app.setShoulders(ShouldersFactory.makeShoulders(shoulderSet[selectedShoulders], gender));
        }
        if (selectedNeck > 0) {
            app.setNeck(NeckFactory.makeNeck(neckSet[selectedNeck]));
        }
        if (classSet[selectedClass] == Classes.None) {
            app.reset();
            app.applyFacialHair(raceSet[selectedRace], false);
            app.applyDetail(raceSet[selectedRace], false);
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
                new CharacterClass[]{classSet[selectedClass], classSet[other1], classSet[other2], classSet[other3]});
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
                String[] labels = new String[]{"First Name", "", "Last Name", "", "", "",
                        "Gender", "", "Race", "", "", "",
                        "Eyes", "Eye Shadow", "Nose",
                        "Mouth", "Lipstick", "Beard", "Ears", "Shoulders", "Neck", "",
                        "Hair", "  Color", "", "Detail", "  Color", "", "", "",
                        "Class", "", "Other Class 1", "",
                        "Other Class 2", "", "Other Class 3"};
                for (int i = 0; i < labels.length; ++i) {
                    BorderFrame.drawString(model.getScreenHandler(), labels[i], x, y++, MyColors.WHITE, MyColors.BLUE);
                }

                int midX = x + COLUMN_SKIP + 14;
                int row = 17;
                drawCharacterDetails(model, lastCharacter, midX, row);
                drawChecksAndNotOk(model, x);

            }
        });
    }

    private void drawChecksAndNotOk(Model model, int x) {
        model.getScreenHandler().put(x+COLUMN_SKIP+12, 6, nameOk(1)?CHECK_SPRITE:NOT_OK_SPRITE);
        model.getScreenHandler().put(x+COLUMN_SKIP+12, 8, nameOk(2)?CHECK_SPRITE:NOT_OK_SPRITE);

        model.getScreenHandler().put(x+COLUMN_SKIP-4, 36, selectedClassOk()?CHECK_SPRITE:NOT_OK_SPRITE);

        model.getScreenHandler().put(x+COLUMN_SKIP+8, 38, other1!=0?CHECK_SPRITE:NOT_OK_SPRITE);
        model.getScreenHandler().put(x+COLUMN_SKIP+8, 40, other2!=0?CHECK_SPRITE:NOT_OK_SPRITE);
        model.getScreenHandler().put(x+COLUMN_SKIP+8, 42, other3!=0?CHECK_SPRITE:NOT_OK_SPRITE);
    }

    private boolean selectedClassOk() {
        return selectedClass!=0 &&
                (selectedClass != other1 && selectedClass != other2 && selectedClass != other3);
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
        BorderFrame.drawString(model.getScreenHandler(),
                "Carrying Cap: " + lastCharacter.getRace().getCarryingCapacity(),
                midX, row++, MyColors.WHITE, MyColors.BLUE);
        row++;

        for (Skill s : lastCharacter.getSkillSet()) {
            BorderFrame.drawString(model.getScreenHandler(), String.format("%-13s%2d", s.getName(),
                    lastCharacter.getRankForSkill(s)), midX, row++, MyColors.WHITE, MyColors.BLUE);
        }
        Weapon w = lastCharacter.getCharClass().getStartingEquipment().getWeapon();
        if (w != null) {
            BorderFrame.drawString(model.getScreenHandler(),
                    "Default gear: ",
                    midX, ++row, MyColors.WHITE, MyColors.BLUE);
            BorderFrame.drawString(model.getScreenHandler(),
                    "  " + w.getName(),
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
                new SelectableListContent(xStart + 3, yStart + 7, (showSkeleton?"Hide":"Show") + " Skeleton") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        showSkeleton = !showSkeleton;
                        rebuildAppearance();
                    }
                },
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
                new SelectableListContent(xStart + 3, yStart + 13, "Random Appearance") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        randomizeAppearance();
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
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 16, selectedMascara==0?"NONE": makeupColorSet[selectedMascara].toString().replace("_", " ")) {
                    @Override
                    public void turnLeft(Model model) {
                        selectedMascara = Arithmetics.decrementWithWrap(selectedMascara, makeupColorSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        selectedMascara = Arithmetics.incrementWithWrap(selectedMascara, makeupColorSet.length);
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
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 18, "Mouth #" + (selectedMouth + 1)) {
                    @Override
                    public void turnLeft(Model model) {
                        selectedMouth = Arithmetics.decrementWithWrap(selectedMouth, mouthSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        selectedMouth = Arithmetics.incrementWithWrap(selectedMouth, mouthSet.length);
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 19, selectedLipColor==0?"NONE": makeupColorSet[selectedLipColor].toString().replace("_", " ")) {
                    @Override
                    public void turnLeft(Model model) {
                        selectedLipColor = Arithmetics.decrementWithWrap(selectedLipColor, makeupColorSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        selectedLipColor = Arithmetics.incrementWithWrap(selectedLipColor, makeupColorSet.length);
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 20, "Beard #" + (selectedBeard + 1)) {
                    @Override
                    public void turnLeft(Model model) {
                        selectedBeard = Arithmetics.decrementWithWrap(selectedBeard, beardSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        selectedBeard = Arithmetics.incrementWithWrap(selectedBeard, beardSet.length);
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 21, earSet[selectedEars].getName()) {
                    @Override
                    public void turnLeft(Model model) {
                        selectedEars = Arithmetics.decrementWithWrap(selectedEars, earSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        selectedEars = Arithmetics.incrementWithWrap(selectedEars, earSet.length);
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 22, shoulderSet[selectedShoulders]) {
                    @Override
                    public void turnLeft(Model model) {
                        selectedShoulders = Arithmetics.decrementWithWrap(selectedShoulders, shoulderSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        selectedShoulders = Arithmetics.incrementWithWrap(selectedShoulders, shoulderSet.length);
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 23, neckSet[selectedNeck]) {
                    @Override
                    public void turnLeft(Model model) {
                        selectedNeck = Arithmetics.decrementWithWrap(selectedNeck, neckSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        selectedNeck = Arithmetics.incrementWithWrap(selectedNeck, neckSet.length);
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
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 26, hairColorSet[selectedHairColor].toString().replace("_", " ")) {
                    @Override
                    public void turnLeft(Model model) {
                        selectedHairColor = Arithmetics.decrementWithWrap(selectedHairColor, hairColorSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        selectedHairColor = Arithmetics.incrementWithWrap(selectedHairColor, hairColorSet.length);
                    }
                },
                new SelectableListContent(xStart + COLUMN_SKIP, yStart + 28, accessorySet[accessory].getName()) {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        setInnerMenu(new SelectAccessoryMenu(CharacterCreationView.this, x, y), model);
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 29, detailColorSet[selectedDetailColor].toString().replace("_", " ")) {
                    @Override
                    public void turnLeft(Model model) {
                        selectedDetailColor = Arithmetics.decrementWithWrap(selectedDetailColor, detailColorSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        selectedDetailColor = Arithmetics.incrementWithWrap(selectedDetailColor, detailColorSet.length);
                    }
                },
                new SelectableListContent(xStart + 3, yStart + 31, "About Classes") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        model.transitionToDialog(new TutorialClassesDialog(model.getView()));
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 33,
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
                new SelectableListContent(xStart + COLUMN_SKIP + 4, yStart + 35, classSet[other1].getShortName()) {
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
                new SelectableListContent(xStart + COLUMN_SKIP + 4, yStart + 37, classSet[other2].getShortName()) {
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
                new SelectableListContent(xStart + COLUMN_SKIP + 4, yStart + 39, classSet[other3].getShortName()) {
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
                new SelectableListContent(xStart + COLUMN_SKIP + 12, yStart + 41, "OK") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        setTimeToTransition(true);
                    }

                    @Override
                    public boolean isEnabled(Model model) {
                        boolean namesOk = nameOk(1) && nameOk(2);
                        boolean classOk = selectedClassOk();
                        return other1 != 0 && other2 != 0 && other3 != 0 && classOk && namesOk;

                    }
                },
                new SelectableListContent(xStart + COLUMN_SKIP + 10, yStart + 42, "CANCEL") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        CharacterCreationView.this.canceled = true;
                        setTimeToTransition(true);
                    }
                }
        );
    }

    private boolean nameOk(int i) {
        return !buffers.get(i-1).first.toString().equals(START_STRING);
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
            super(partyView, 10, accessorySet.length+1, x, y);
        }

        @Override
        protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
            return new ArrayList<>();
        }

        @Override
        protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
            List<ListContent> result = new ArrayList<>();
            for (int accessory = 0; accessory < accessorySet.length; ++accessory) {
                int finalAccessory = accessory;
                result.add(new SelectableListContent(xStart + 1, yStart + 1 + finalAccessory, accessorySet[finalAccessory].getName()) {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        CharacterCreationView.this.accessory = finalAccessory;
                        setTimeToTransition(true);
                        rebuildAppearance();
                    }
                });
            }
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

    private void randomizeAppearance() {
        selectedHairColor = MyRandom.randInt(hairColorSet.length);
        selectedDetailColor = MyRandom.randInt(detailColorSet.length);
        selectedMouth = MyRandom.randInt(mouthSet.length);
        selectedLipColor = MyRandom.randInt(makeupColorSet.length);
        selectedNose = MyRandom.randInt(noseSet.length);
        selectedEyes = MyRandom.randInt(eyeSet.length);
        selectedMascara = MyRandom.randInt(makeupColorSet.length);
        selectedHairStyle = MyRandom.randInt(hairStyleSet.length);
        selectedBeard = MyRandom.randInt(beardSet.length);
        accessory = MyRandom.randInt(accessorySet.length);
        selectedDetailColor = MyRandom.randInt(detailColorSet.length);
        rebuildAppearance();
    }
}
