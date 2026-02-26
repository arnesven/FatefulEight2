package view.party;

import control.FatefulEight;
import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.*;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.special.EnchantressClass;
import model.items.weapons.Weapon;
import model.races.EasternHuman;
import model.races.Race;
import util.Arithmetics;
import util.MyRandom;
import view.BorderFrame;
import view.DrawingArea;
import view.GameView;
import view.MyColors;
import view.help.TutorialClassesDialog;
import view.sprites.Sprite;
import view.sprites.Sprite8x8;
import view.widget.ArmorClassWidget;
import view.widget.InputBufferWidget;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class CharacterCreationView extends SelectableListMenu {

    private static final int COLUMN_SKIP = 12;
    public static final Sprite CHECK_SPRITE = new Sprite8x8("check", "charset.png", 0xB6, MyColors.BLACK, MyColors.LIGHT_GREEN, MyColors.BLUE, MyColors.CYAN);
    public static final Sprite NOT_OK_SPRITE = new Sprite8x8("notok", "charset.png", 0xB7, MyColors.BLACK, MyColors.LIGHT_RED, MyColors.BLUE, MyColors.CYAN);
    private final List<InputBufferWidget> buffers = new ArrayList<>();
    private boolean gender = true;
    private static Race[] raceSet = Race.allRaces;
    private static final CharacterEyes[] eyeSet = makeEyeSet();

    // DO NOT CHANGE THE ORDER OF noseSet or mouthSet, IT WILL AFFECT PRESET CHARACTERS
    public static final Integer[] noseSet = new Integer[]{0, 2, 3, 4, 5, 6, 7, 8, 9, 0xA, 0xb, 0xC, 0xD, 0xE, 0xF, 0x18, 0x4A, 0x4B, 0x1DF, 0x1EF};
    public static final Integer[] mouthSet = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF};
    private static final Beard[] beardSet = Beard.allBeards;
    private static final MyColors[] hairColorSet = HairStyle.allHairColors;
    private static final HairStyle[] hairStyleSet = HairStyle.allHairStyles;
    public static final MyColors[] detailColorSet = MyColors.values();
    private static final MyColors[] makeupColorSet = MyColors.values();
    private static final CharacterClass[] classSet = makeClassSet();
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
    private List<Integer> accessories = new ArrayList<>();
    private int selectedDetailColor = 0;
    private List<Integer> detailColors = new ArrayList<>();
    private int selectedEars = 0;
    private int selectedMascara = 0;
    private int selectedLipColor = 0;
    private int selectedShoulders = 0;
    private int selectedNeck = 0;
    private boolean showSkeleton = false;
    private FacialExpression selectedFacialExpression = FacialExpression.none;
    private CharacterAppearance lastAppearance;
    private GameCharacter lastCharacter;
    private Sprite avatarBack = null;
    private boolean canceled = false;
    private boolean isVampire = false;
    private boolean eyesClosed = false;
    private WeepingAmount currentWeepAmount = WeepingAmount.none;
    private WeepingAnimation weepingAnimation = null;

    public CharacterCreationView(GameView previous) {
        super(previous, DrawingArea.WINDOW_COLUMNS-34, DrawingArea.WINDOW_ROWS-6);
        for (int i = 0; i < 2; ++i) {
            buffers.add(new InputBufferWidget(12));
        }
        lastAppearance = makeAppearance();
        lastCharacter = makeCharacter();
        avatarBack = lastCharacter.getAvatarSprite().getAvatarBack();
        if (FatefulEight.inDebugMode()) {
            raceSet = Race.allRacesIncludingMinor;
        }
    }

    @Override
    protected boolean escapeDisposesMenu() {
        return false;
    }

    @Override
    public void transitionedFrom(Model model) {  }

    private CharacterAppearance makeAppearance() {
        AdvancedAppearance app = raceSet[selectedRace].makeAppearance(raceSet[selectedRace], gender,
                hairColorSet[selectedHairColor], mouthSet[selectedMouth],
                noseSet[selectedNose], eyeSet[selectedEyes], hairStyleSet[selectedHairStyle],
                beardSet[selectedBeard]);
        if (showSkeleton) {
            app = new SkeletonAppearance(app.getShoulders(), app.getGender());
        }
        app.setRaceSpecificEars(selectedEars == 0);
        app.setEars(earSet[selectedEars]);
        for (int i = 0; i < accessories.size(); ++i) {
            app.addFaceDetail(accessorySet[accessories.get(i)]);
            app.setDetailColor(detailColorSet[detailColors.get(i)]);
        }
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
        return new GameCharacter(buffers.get(0).getText(), buffers.get(1).getText(), raceSet[selectedRace], classSet[selectedClass], makeAppearance());
    }


    private void rebuildAppearance() {
        lastAppearance = makeAppearance();
        lastCharacter = makeCharacter();
        avatarBack = lastCharacter.getAvatarSprite().getAvatarBack();
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
                Point appearancePos = new Point(x+COLUMN_SKIP+17, y+2);
                lastAppearance.drawYourself(model.getScreenHandler(), appearancePos.x, appearancePos.y);
                if (selectedFacialExpression != FacialExpression.none) {
                    lastAppearance.drawFacialExpression(model.getScreenHandler(), appearancePos.x+3, appearancePos.y+3,
                            selectedFacialExpression, true, isVampire);
                }
                if (eyesClosed) {
                    lastAppearance.drawBlink(model.getScreenHandler(), appearancePos.x+3, appearancePos.y+3);
                }
                if (weepingAnimation != null) {
                    weepingAnimation.drawYourself(model.getScreenHandler());
                }
                model.getScreenHandler().register(lastCharacter.getAvatarSprite().getName(),
                        new Point(x+COLUMN_SKIP+24, y+3),
                        lastCharacter.getAvatarSprite());
                model.getScreenHandler().register(avatarBack.getName(),
                        new Point(x+COLUMN_SKIP+28, y+3),
                        avatarBack);
                y++;
                String[] labels = new String[]{"First Name", "", "Last Name", "", "", "",
                        "Gender", "", "Race", "", "", "",
                        "Eyes", "Eye Shadow", "Nose",
                        "Mouth", "Lipstick", "Beard", "Ears", "Shoulders", "Neck", "",
                        "Hair", "  Color", "", "Details", "  Color", "", "", "",
                        "Class"};
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
    }

    private boolean selectedClassOk() {
        return selectedClass != 0;
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
            int rank = lastCharacter.getRankForSkill(s);
            if (rank > 0) {
                BorderFrame.drawString(model.getScreenHandler(), String.format("%-14s%2d", s.getName(),
                        lastCharacter.getRankForSkill(s)), midX, row++, MyColors.WHITE, MyColors.BLUE);
            }
        }
        Weapon w = lastCharacter.getCharClass().getDefaultEquipment().getWeapon();
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
            ArmorClassWidget.drawYourself(model.getScreenHandler(), midX, ++row,
                    lastCharacter.getCharClass().canUseHeavyArmor());
        }
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        return List.of(new InputFieldContent(xStart + COLUMN_SKIP, yStart + 3, 0),
                new InputFieldContent(xStart + COLUMN_SKIP, yStart + 5, 1),
                new SelectableListContent(xStart + 3, yStart + 7, (showSkeleton ? "Hide" : "Show") + " Skeleton") {
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
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 16, selectedMascara == 0 ? "NONE" : makeupColorSet[selectedMascara].toString().replace("_", " ")) {
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
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 19, selectedLipColor == 0 ? "NONE" : makeupColorSet[selectedLipColor].toString().replace("_", " ")) {
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
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 25,
                        hairStyleSet[selectedHairStyle].getDescription()) {
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
                new SelectableListContent(xStart + COLUMN_SKIP, yStart + 28, getAccessoryLabel()) {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        setInnerMenu(new SelectAccessoryMenu(CharacterCreationView.this, x, y), model);
                    }

                    @Override
                    public boolean isEnabled(Model model) {
                        return accessories.size() < accessorySet.length;
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 29, getAccessoryColorLabel()) {
                    @Override
                    public void turnLeft(Model model) {
                        if (!detailColors.isEmpty()) {
                            selectedDetailColor = Arithmetics.decrementWithWrap(selectedDetailColor, detailColorSet.length);
                            detailColors.set(detailColors.size() - 1, selectedDetailColor);
                        }
                    }

                    @Override
                    public void turnRight(Model model) {
                        if (!detailColors.isEmpty()) {
                            selectedDetailColor = Arithmetics.incrementWithWrap(selectedDetailColor, detailColorSet.length);
                            detailColors.set(detailColors.size() - 1, selectedDetailColor);
                        }
                    }
                },
                new SelectableListContent(xStart + 3, yStart + 30, "Remove last") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        detailColors.removeLast();
                        int removed = accessories.removeLast();
                        rebuildAppearance();
                    }

                    @Override
                    public boolean isEnabled(Model model) {
                        return !accessories.isEmpty();
                    }
                },
                new SelectableListContent(xStart + 3, yStart + 32, "About Classes") {
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
                new CarouselListContent(xStart + 3, yStart + 35, "Expression: " + FacialExpression.values()[selectedFacialExpression.ordinal()].name()) {
                    @Override
                    public void turnLeft(Model model) {
                        selectedFacialExpression = FacialExpression.values()[Arithmetics.decrementWithWrap(selectedFacialExpression.ordinal(), FacialExpression.values().length)];
                    }

                    @Override
                    public void turnRight(Model model) {
                        selectedFacialExpression = FacialExpression.values()[Arithmetics.incrementWithWrap(selectedFacialExpression.ordinal(), FacialExpression.values().length)];
                    }
                },
                new SelectableListContent(xStart + 3, yStart + 36, "Vampire: " + (isVampire ? "Yes" : "No")) {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        isVampire = !isVampire;
                    }
                },
                new SelectableListContent(xStart + 3, yStart + 37, "Eyes: " + (eyesClosed ? "Closed" : "Open")) {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        eyesClosed = !eyesClosed;
                    }
                },
                new CarouselListContent(xStart + 3, yStart + 38, "Weeping: " + WeepingAmount.values()[currentWeepAmount.ordinal()].name()) {
                    final Point appearancePoint = new Point(47, 7);

                    @Override
                    public void turnLeft(Model model) {
                        currentWeepAmount = WeepingAmount.values()[Arithmetics.decrementWithWrap(currentWeepAmount.ordinal(), WeepingAmount.values().length)];
                        if (currentWeepAmount != WeepingAmount.none) {
                            weepingAnimation = new WeepingAnimation(appearancePoint, currentWeepAmount);
                        } else {
                            weepingAnimation = null;
                        }
                    }

                    @Override
                    public void turnRight(Model model) {
                        currentWeepAmount = WeepingAmount.values()[Arithmetics.incrementWithWrap(currentWeepAmount.ordinal(), WeepingAmount.values().length)];
                        if (currentWeepAmount != WeepingAmount.none) {
                            weepingAnimation = new WeepingAnimation(appearancePoint, currentWeepAmount);
                        } else {
                            weepingAnimation = null;
                        }
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
                        return classOk && namesOk;
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

    private String getAccessoryColorLabel() {
        if (accessories.isEmpty()) {
            return "N/A";
        }
        return detailColorSet[detailColors.getLast()].toString().replace("_", " ");
    }

    private String getAccessoryLabel() {
        if (accessories.isEmpty()) {
            return "None";
        }
        return accessorySet[accessories.getLast()].getName();
    }

    private boolean nameOk(int i) {
        return buffers.get(i-1).hasChanged();
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {
        if (getSelectedRow() < buffers.size()) {
            InputBufferWidget input = buffers.get(getSelectedRow());
            if (input.enterKeyStroke(keyEvent)) {
                madeChanges();
            }
        }
        if (super.handleCarousels(keyEvent, model)) {
            rebuildAppearance();
        }
    }

    private class InputFieldContent extends ListContent {
        public InputFieldContent(int x, int y, int index) {
            super(x, y, buffers.get(index).getRawText());
        }
    }

    private class SelectAccessoryMenu extends FixedPositionSelectableListMenu {
        public SelectAccessoryMenu(GameView partyView, int x, int y) {
            super(partyView, 11, accessorySet.length+2, x, y);
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
                        CharacterCreationView.this.accessories.add(finalAccessory);
                        CharacterCreationView.this.detailColors.add(selectedDetailColor);
                        setTimeToTransition(true);
                        rebuildAppearance();
                    }

                    @Override
                    public boolean isEnabled(Model model) {
                        return !accessories.contains(finalAccessory);
                    }
                });
            }
            result.add(new SelectableListContent(xStart + 1, yStart + 1 + accessorySet.length, "CANCEL") {
                @Override
                public void performAction(Model model, int x, int y) {
                    setTimeToTransition(true);
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
        if (MyRandom.rollD6() > 3) {
            accessories.clear();
            int randomAccessory = MyRandom.randInt(accessorySet.length);
            accessories.add(randomAccessory);
            detailColors.clear();
            detailColors.add(MyRandom.randInt(detailColorSet.length));
        }
        rebuildAppearance();
    }


    private static CharacterEyes[] makeEyeSet() {
        CharacterEyes[] result = new CharacterEyes[CharacterEyes.allEyes.length +
                EasternHuman.EYES.toArray().length];
        int index = 0;
        for (CharacterEyes eyes : CharacterEyes.allEyes) {
            result[index++] = eyes;
        }
        for (CharacterEyes eyes : EasternHuman.EYES) {
            result[index++] = eyes;
        }
        return result;
    }

    private static CharacterClass[] makeClassSet() {
        CharacterClass[] arr = Classes.allClasses;
        if (!FatefulEight.inDebugMode()) {
            return arr;
        }
        CharacterClass[] arr2 = new CharacterClass[arr.length+3];
        System.arraycopy(arr, 0, arr2, 0, arr.length);
        arr2[arr.length] = Classes.ENCHANTRESS;
        arr2[arr.length+1] = Classes.BRIGAND;
        arr2[arr.length+2] = Classes.SWORD_MASTER;
        return arr2;
    }
}
