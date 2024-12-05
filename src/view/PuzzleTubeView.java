package view;

import control.FatefulEight;
import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.puzzletube.DwarvenPuzzleTube;
import model.items.puzzletube.WordPuzzle;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import sound.SoundEffects;
import util.Arithmetics;
import util.MyStrings;
import view.sprites.*;
import view.widget.TopText;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class PuzzleTubeView extends GameView {
    private static final int Y_START = 18;
    private static final int PARCHMENT_WIDTH = 4;
    private static final int PARCHMENT_HEIGHT = 5;
    private static final int PARCHMENT_START_Y = 4;
    private static final int PARCHMENT_START_X = 0;
    private final GameView previous;
    private final TopText topText;
    private final WordPuzzle puzzle;

    private static final Sprite[][][] RING_GROUPS = {
            // Unmarked group
            {makeBigLetters(MyColors.LIGHT_GRAY),
             makeLittleLetters(MyColors.GRAY),
             makeTinyLetters(MyColors.DARK_GRAY)},

            // Yellow group
            {makeBigLetters(MyColors.YELLOW),
             makeLittleLetters(MyColors.GOLD),
             makeTinyLetters(MyColors.BROWN)},

            // blue group
            {makeBigLetters(MyColors.CYAN),
             makeLittleLetters(MyColors.LIGHT_BLUE),
             makeTinyLetters(MyColors.BLUE)},

            // green group
            {makeBigLetters(MyColors.ORC_GREEN),
             makeLittleLetters(MyColors.GREEN),
             makeTinyLetters(MyColors.DARK_GREEN)},

            {makeBigLetters(MyColors.PINK),
             makeLittleLetters(MyColors.LIGHT_RED),
             makeTinyLetters(MyColors.DARK_RED)},

    };

    private static final Sprite[][] MAP_SPRITES = TreasureMapView.makeMapSprites();

    private static final Sprite LEFT_SIDE = makeSideSprite(0);
    private static final Sprite RIGHT_SIDE = makeSideSprite(180);
    private static final Sprite EJECTED_SIDE = makeCompartmentSprite(MyColors.BLACK, MyColors.BEIGE);
    private static final Sprite EJECTED_EMPTY = makeCompartmentSprite(MyColors.BROWN, MyColors.BROWN);

    private final int wordLength;
    private final GameCharacter user;
    private final DwarvenPuzzleTube tube;
    private int selectedIndex = 0;
    private final Map<Integer, Integer> ringGroups = new HashMap<>();
    private BackgroundMusic bgMusic;
    private long startTime;

    public PuzzleTubeView(GameView inventoryView, WordPuzzle puzzle, GameCharacter user, DwarvenPuzzleTube tube) {
        super(true);
        this.previous = inventoryView;
        this.topText = new PuzzleTubeTopText();
        this.puzzle = puzzle;
        this.user = user;
        this.wordLength = this.puzzle.getWord(0).length();
        this.tube = tube;
        resetGroups();
    }

    private void resetGroups() {
        for (int i = 0; i < wordLength; ++i) {
            ringGroups.put(i, 0);
        }
    }

    @Override
    public void transitionedTo(Model model) {
        bgMusic = ClientSoundManager.getCurrentBackgroundMusic();
        ClientSoundManager.stopPlayingBackgroundSound();

        model.getTutorial().puzzleTubes(model);

        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void transitionedFrom(Model model) {
        ClientSoundManager.playBackgroundMusic(bgMusic);
    }

    @Override
    protected void internalUpdate(Model model) {
        model.getScreenHandler().clearAll();
        topText.drawYourself(model);
        BorderFrame.drawFrameTop(model.getScreenHandler());
        int xOffset = (DrawingArea.WINDOW_COLUMNS - wordLength*4) / 2;
        if (puzzle.isParchmentRemoved()) {
            xOffset += 14;
        }
        drawLetters(model, xOffset);
        drawSides(model, xOffset);
        if (puzzle.isParchmentRemoved()) {
            drawParchment(model);
            drawParchmentText(model);
        } else {
            if (!puzzle.isSolved()) {
                drawCursor(model, xOffset);
            }

            // Debug:
            if (FatefulEight.inDebugMode()) {
                BorderFrame.drawString(model.getScreenHandler(), "Active Locks: " + puzzle.getNumberOfActiveLocks() +
                                " (" + puzzle.getActiveLocksSnapshot() + ")", xOffset, 31,
                        MyColors.GRAY, MyColors.BLACK);
            }
        }
        int seconds = (int)(puzzle.getTimeSpentMillis() / 1000);
        int minutes = seconds / 60;
        BorderFrame.drawString(model.getScreenHandler(), "Word Length: " + wordLength + ", Time: " +
                        String.format("%02d:%02d", minutes, seconds % 60), xOffset, 30,
        MyColors.WHITE, MyColors.BLACK);
        if (!puzzle.isSolved()) {
            long timeDiff = System.currentTimeMillis() - startTime;
            startTime = System.currentTimeMillis();
            puzzle.addToTimeSpent(timeDiff);
        }
    }

    private void drawParchmentText(Model model) {
        int row = PARCHMENT_START_Y + 7;
        BorderFrame.drawStringInForeground(model.getScreenHandler(), "You solved the puzzle,",
                PARCHMENT_START_X + 3, row++, MyColors.BLACK, MyColors.BEIGE);
        BorderFrame.drawStringInForeground(model.getScreenHandler(), "congratulations!",
                PARCHMENT_START_X + 3, row++, MyColors.BLACK, MyColors.BEIGE);
        row++;
        BorderFrame.drawStringInForeground(model.getScreenHandler(), "The word was indeed",
                PARCHMENT_START_X + 3, row++, MyColors.BLACK, MyColors.BEIGE);
        BorderFrame.drawStringInForeground(model.getScreenHandler(), puzzle.getSolutionWord(),
                PARCHMENT_START_X + 6, row++, MyColors.BLACK, MyColors.BEIGE);
        row++;

        String rest = "Can you solve all my puzzles? I have hidden another one " +
                tube.getNextDescription() + ".";
        String[] parts = MyStrings.partition(rest, 26);
        for (String s : parts) {
            BorderFrame.drawStringInForeground(model.getScreenHandler(), s,
                    PARCHMENT_START_X + 3, row++, MyColors.BLACK, MyColors.BEIGE);
        }

    }

    private void drawParchment(Model model) {
        int y = PARCHMENT_START_Y;
        drawParchmentRow(model, 0, y);
        for (int row = 1; row < PARCHMENT_HEIGHT - 1; ++row) {
            drawParchmentRow(model, 1, y + row*8);
        }
        drawParchmentRow(model, 2, y + (PARCHMENT_HEIGHT-1)*8);

    }

    private void drawParchmentRow(Model model, int row, int y) {
        int x = PARCHMENT_START_X;
        model.getScreenHandler().put(x, y, MAP_SPRITES[0][row]);
        for (int i = 0; i < PARCHMENT_WIDTH - 2; ++i) {
            x += 8;
            model.getScreenHandler().put(x, y, MAP_SPRITES[1][row]);
        }
        x += 8;
        model.getScreenHandler().put(x, y, MAP_SPRITES[2][row]);
    }

    public void drawLetters(Model model, int xOffset) {
        String word = puzzle.getWord(0);
        String wordUp = puzzle.getWord(9);
        String wordUpUp = puzzle.getWord(8);
        String wordDown = puzzle.getWord(1);
        String wordDownDown = puzzle.getWord(2);

        for (int i = 0; i < word.length(); ++i) {
            int xPos = xOffset + i * 4;
            int yPos = Y_START;
            Sprite[][] spriteSet = RING_GROUPS[ringGroups.get(i)];
            model.getScreenHandler().put(xPos, yPos, spriteSet[2][wordUpUp.charAt(i) - 'A']);
            yPos += 1;
            model.getScreenHandler().put(xPos, yPos, spriteSet[1][wordUp.charAt(i) - 'A']);
            yPos += 2;
            model.getScreenHandler().put(xPos, yPos, spriteSet[0][word.charAt(i) - 'A']);
            yPos += 4;
            model.getScreenHandler().put(xPos, yPos, spriteSet[1][wordDown.charAt(i) - 'A']);
            yPos += 2;
            model.getScreenHandler().put(xPos, yPos, spriteSet[2][wordDownDown.charAt(i) - 'A']);
        }
    }


    private void drawSides(Model model, int xOffset) {
        if (puzzle.isSolved()) {
            model.getScreenHandler().put(xOffset - 4, Y_START + 1, LEFT_SIDE);
            if (puzzle.isParchmentRemoved()) {
                model.getScreenHandler().put(xOffset - 2, Y_START + 1, EJECTED_EMPTY);
            } else {
                model.getScreenHandler().put(xOffset - 2, Y_START + 1, EJECTED_SIDE);
            }
        } else {
            model.getScreenHandler().put(xOffset - 2, Y_START + 1, LEFT_SIDE);
        }
        model.getScreenHandler().put(xOffset + wordLength*4, Y_START+1, RIGHT_SIDE);
    }

    protected void drawCursor(Model model, int xOffset) {
        Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
        int x = xOffset + selectedIndex * 4;
        int y = 12;
        model.getScreenHandler().put(x, y, cursor);
    }

    @Override
    public GameView getNextView(Model model) {
        return previous;
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            setTimeToTransition(true);
        }

        if (puzzle.isSolved()) {
            return;
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            int incr = Arithmetics.incrementWithWrap(ringGroups.get(selectedIndex), RING_GROUPS.length);
            ringGroups.put(selectedIndex, incr);
            madeChanges();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            puzzle.startLockCheck();
            rotateUp();
            playSound(puzzle.didLocksActivate());
            checkForEject(model);
            madeChanges();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            int before = puzzle.getNumberOfActiveLocks();
            rotateDown();
            playSound(before < puzzle.getNumberOfActiveLocks());
            checkForEject(model);
            madeChanges();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            selectedIndex = Arithmetics.decrementWithWrap(selectedIndex, wordLength);
            madeChanges();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            selectedIndex = Arithmetics.incrementWithWrap(selectedIndex, wordLength);
            madeChanges();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_F3) {
            useLockPick(model);
            madeChanges();
        }
    }

    private void useLockPick(Model model) {
        if (model.getParty().getInventory().getLockpicks() == 0) {
            model.transitionToDialog(new PuzzleMessage(this, "You don't have any lockpicks."));
            return;
        }
        model.getParty().getInventory().addToLockpicks(-1);
        SkillCheckResult result = user.testSkillHidden(Skill.Security, WordPuzzle.LOCKPICK_DIFFICULTY, 0);
        if (result.isSuccessful()) {
            if (puzzle.addNewLock()) {
                SoundEffects.playUnlock();
                model.transitionToDialog(new PuzzleMessage(this, user.getName() +
                        " used a lockpick on the puzzle tube. The internal mechanism has been altered. The lockpick broke."));
            } else {
                model.transitionToDialog(new PuzzleMessage(this, user.getName() +
                        " used a lockpick on the puzzle tube, but failed to accomplish anything. "));
                model.getParty().getInventory().addToLockpicks(1);
            }
        } else {
            model.transitionToDialog(new PuzzleMessage(this, user.getName() +
                    " used a lockpick on the puzzle tube, but failed to accomplish anything. The lockpick broke."));
        }
    }

    private void checkForEject(Model model) {
        if (puzzle.isSolved()) {
            resetGroups();
            SoundEffects.playSound("tube_eject");
            model.transitionToDialog(new PuzzleCompletedMessage(this));
        }
    }

    private void rotateUp() {
        puzzle.rotateUp(selectedIndex);
        for (int i = 0; i < wordLength; ++i) {
            if (i != selectedIndex) {
                int currentRingGroup = ringGroups.get(selectedIndex);
                if (currentRingGroup != 0 && ringGroups.get(i).equals(currentRingGroup)) {
                    puzzle.rotateUp(i);
                }
            }
        }
    }

    private void rotateDown() {
        puzzle.rotateDown(selectedIndex);
        for (int i = 0; i < wordLength; ++i) {
            if (i != selectedIndex) {
                int currentRingGroup = ringGroups.get(selectedIndex);
                if (currentRingGroup != 0 && ringGroups.get(i).equals(currentRingGroup)) {
                    puzzle.rotateDown(i);
                }
            }
        }
    }

    private void playSound(boolean newLock) {
        if (newLock) {
            SoundEffects.playSound("tube_lock");
        } else {
            SoundEffects.playClickSound();
        }
    }

    private static Sprite[] makeBigLetters(MyColors color2) {
        Sprite[] result = new Sprite[26];
        for (int i = 0; i < result.length; ++i) {
            result[i] = new Sprite("puzzelletterbig" + i, "puzzletube.png", i, 0, 32, 32);
            result[i].setColor1(MyColors.GOLD);
            result[i].setColor2(color2);
            result[i].setColor3(MyColors.BLACK);
            result[i].setColor4(color2);
        }
        return result;
    }

    private static Sprite[] makeLittleLetters(MyColors color2) {
        Sprite[] result = new Sprite[26];
        for (int i = 0; i < result.length; ++i) {
            result[i] = new Sprite("puzzellettersmall" + i, "puzzletube.png", i, 2, 32, 16);
            result[i].setColor1(MyColors.BROWN);
            result[i].setColor2(color2);
            result[i].setColor3(MyColors.BLACK);
            result[i].setColor4(color2);
        }
        return result;
    }

    private static Sprite[] makeTinyLetters(MyColors color2) {
        Sprite[] result = new Sprite[26];
        for (int i = 0; i < result.length; ++i) {
            result[i] = new Sprite("puzzleborder"+i, "puzzletube.png", i, 6, 32, 8);
            result[i].setColor1(MyColors.DARK_BROWN);
            result[i].setColor2(color2);
            result[i].setColor3(MyColors.BLACK);
            result[i].setColor4(color2);
        }
        return result;
    }

    private static Sprite makeSideSprite(int rotation) {
        Sprite sprite = new Sprite("puzzletubeside"+rotation, "puzzletube.png", 52, 0,
                16, 64);
        sprite.setColor1(MyColors.BROWN);
        sprite.setColor2(MyColors.GOLD);
        sprite.setColor3(MyColors.GRAY_RED);
        sprite.setRotation(rotation);
        return sprite;
    }

    private static Sprite makeCompartmentSprite(MyColors color3, MyColors color4) {
        Sprite sprite = new Sprite("puzzlecompart", "puzzletube.png", 53, 0,
                16, 64);
        sprite.setColor1(MyColors.BROWN);
        sprite.setColor2(MyColors.GOLD);
        sprite.setColor3(color3);
        sprite.setColor4(color4);
        return sprite;
    }

    public void setMadeChanges() {
        madeChanges();
    }

    private static class PuzzleTubeTopText extends TopText {
        private static final String ARROW_STRING = (char)(0xB1) + "" + (char)(0xB5) + "" +
                (char)(0xB2) + "" + (char)(0xB0) + "";

        @Override
        protected void drawKeyTexts(Model model) {
            BorderFrame.drawString(model.getScreenHandler(),
                    "SPACE=mark F3=pick " +
                            ARROW_STRING +
                            "=turn ESC=exit", 43, 0, MyColors.WHITE);
        }
    }

    private class PuzzleMessage extends SimpleMessageView {
        public PuzzleMessage(GameView previous, String message) {
            super(previous, message);
        }

        @Override
        public void transitionedTo(Model model) {
            setTimeToTransition(false);
        }

        @Override
        protected int getYStart() {
            return super.getYStart() + 8;
        }

        @Override
        public void transitionedFrom(Model model) {
            super.transitionedFrom(model);
            PuzzleTubeView.this.madeChanges();
        }
    }

    private class PuzzleCompletedMessage extends PuzzleMessage {
        public PuzzleCompletedMessage(GameView previous) {
            super(previous, "A little compartment on the side of the puzzle tube has opened. " +
                    "Inside is a piece of parchment.");
        }

        @Override
        protected void specificHandleEvent(KeyEvent keyEvent, Model model) {
            super.specificHandleEvent(keyEvent, model);
            puzzle.removeParchment(model);
            tube.addDestinationTask(model);
        }
    }
}
