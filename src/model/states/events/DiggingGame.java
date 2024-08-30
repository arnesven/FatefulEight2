package model.states.events;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.states.DailyEventState;
import model.states.digging.DiggingSpace;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import sound.SoundEffects;
import util.MyLists;
import util.MyPair;
import view.subviews.CollapsingTransition;
import view.subviews.DiggingGameSubView;
import view.subviews.PortraitSubView;
import view.subviews.SubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiggingGame extends DailyEventState {
    private static final String[] SIZE_NAMES = new String[]{"Easy (8x8, 10)", "Medium (10x10, 16)",
    "Hard (12x12 24)", "Very hard (14x14, 36)", "Impossible (16x18, 60)"};
    private static final int[] FIELD_SIZES = new int[]{8, 10, 12, 14, 16};
    private static final int[] BOULDER_COUNTS = new int[]{10, 16, 24, 36, 60};
    private static final String COUNTER_KEY = "DIGGING_GAME_COUNTER";
    private int boulders;
    private boolean foundBoulder = false;
    private boolean giveUp = false;
    private int hintsGiven = 0;
    private boolean firstTime = true;

    public DiggingGame(Model model) {
        super(model);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Go to digging game", "There's a farmer around here who has this crazy game " +
                "where you get to dig in a meadow for obols");
    }

    @Override
    protected void doEvent(Model model) {
        if (!doIntro(model)) {
            return;
        }
        int fieldSize;
        if (getWinCount(model) >= BOULDER_COUNTS.length) {
            println("Choose a difficulty for the digging game.");
            int choice = multipleOptionArrowMenu(model, 24, 20, List.of(SIZE_NAMES));
            boulders = BOULDER_COUNTS[choice];
            fieldSize = FIELD_SIZES[choice];
        } else {
            int winCount = Math.min(getWinCount(model), BOULDER_COUNTS.length-1);
            this.boulders = BOULDER_COUNTS[winCount];
            fieldSize = FIELD_SIZES[winCount];
        }
        int rows = fieldSize == 16 ? 18 : fieldSize;
        SteppingMatrix<DiggingSpace> matrix = new SteppingMatrix<>(fieldSize, rows);
        matrix.setSoundEnabled(false);
        fill(matrix);
        DiggingGameSubView subView = new DiggingGameSubView(matrix, boulders);
        BackgroundMusic previous = ClientSoundManager.getCurrentBackgroundMusic();
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.jumpyBlip);
        CollapsingTransition.transition(model, subView);
        while (!gameIsOver(matrix)) {
            waitForReturnSilently();
            if (subView.getTopIndex() != -1) {
                if (subView.getTopIndex() == 1) {
                    print("Are you sure you want to abandon the digging game? (Y/N) ");
                    if (yesNoInput()) {
                        this.giveUp = true;
                        break;
                    }
                } else {
                    getHint(model, matrix, subView);
                }
            } else {
                if (firstTime) {
                    populate(matrix, boulders);
                    firstTime = false;
                }
                digSpace(matrix, subView, matrix.getSelectedElement());
            }
        };

        if (foundBoulder) {
            SoundEffects.playFoundBoulder();
            printQuote("Farmer", "That's a boulder! I'm afraid the game is over friend. Better luck next time!");
            leaderSay("Darn it...");
            printQuote("Farmer", "And now... the agreement was, dig up the rest of the boulders.");
            leaderSay("Fair enough.");
            // TODO: Attack farmer, or just leave -> blacklisted?
            boolean success = model.getParty().doCollectiveSkillCheck(model, this, Skill.Labor, 4);
            revealAllBoulders(matrix);
            println("You spend the rest of the day clearing the meadow from boulders.");
            int staminaLoss;
            if (!success) {
                println("The work is very taxing. Each party member exhausts 2 stamina.");
                staminaLoss = 2;
            } else {
                println("The work is hard, and results in a good physical work out for the party. " +
                        "Each party member exhausts 1 stamina.");
                staminaLoss = 1;
            }
            MyLists.forEach(model.getParty().getPartyMembers(), (GameCharacter gc) -> gc.addToSP(-staminaLoss));
        } else if (giveUp) {
            leaderSay("I don't think I want to continue.");
            printQuote("Farmer", "Chicken?");
            leaderSay("...");
            printQuote("Farmer", "Eh, I'm just kiddin. Just take the obols and go.");
            gainObols(model, matrix);
        } else {
            printQuote("Farmer", "I can't believe it, you actually did it!");
            leaderSay("I think there may be a flaw in your business plan...");
            printQuote("Farmer", "Nah... You were just lucky. Here's your obols and your gold back.");
            gainObols(model, matrix);
            println("You got your 10 gold back.");
            model.getParty().addToGold(10);
            increaseWinCount(model);
        }
        model.getLog().waitForAnimationToFinish();
        ClientSoundManager.playBackgroundMusic(previous);
    }

    private void getHint(Model model, SteppingMatrix<DiggingSpace> matrix, DiggingGameSubView subView) {
        leaderSay("Hmmm... I'm a bit stuck here. Can you give me a hint?");
        if (hintsGiven > 4) {
            printQuote("Farmer", "No more hints now. Don't want to spoil the fun.");
            return;
        }
        if (hintsGiven > 0) {
            int hintCost = hintsGiven * hintsGiven;
            printQuote("Farmer", "Okay. but it'll cost you " + hintCost + " gold.");
            if (model.getParty().getGold() >= hintCost) {
                print("Pay " + hintCost + " gold for a hint? (Y/N) ");
            }
            if (model.getParty().getGold() >= hintCost && yesNoInput()) {
                leaderSay("Fine, here.");
                println("You give the farmer " + hintCost + " gold.");
                model.getParty().addToGold(-hintCost);
            } else {
                leaderSay(hintCost + " gold? Darn, you're really milking me here, aren't you?");
                printQuote("Farmer", "The only ones I'm milking is Old Bessy at dawn. " +
                        "You knew what you were getting yourself into. " +
                        "If you don't want to pay for the hint you're just going to have to figure it out for yourself.");
                return;
            }
        } else {
            printQuote("Farmer", "What? And you seemed so confident!");
            leaderSay("Come on... this was trickier than I thought.");
            printQuote("Farmer", "All right, you can have a hint. " +
                    "But any more and I'll have to start charging you.");
            leaderSay("I understand.");
        }

        printQuote("Farmer", "Now let me see...");
        List<DiggingSpace> incorrectlyMarked = MyLists.filter(matrix.getElementList(), (DiggingSpace space) -> space.isMarked() && !space.hasBoulder());
        if (!incorrectlyMarked.isEmpty()) {
            printQuote("Farmer", "I'm pretty sure you marked a spot which doesn't have a boulder.");
            leaderSay("Which one?");
            printQuote("Farmer", "Here. I'll remove the mark for you.");
            model.getLog().waitForAnimationToFinish();
            subView.markSpace(incorrectlyMarked.get(0));
            hintsGiven++;
            return;
        }

        List<DiggingSpace> candidates = MyLists.filter(matrix.getElementList(), (DiggingSpace space) -> !space.isDug() && space.hasBoulder() && !space.isMarked());
        Collections.shuffle(candidates);
        for (DiggingSpace dsp : candidates) {
            if (MyLists.any(dsp.findAdjacentSpaces(matrix), (DiggingSpace neighbor) -> neighbor.isDug() && neighbor.getNumber() > 0)) {
                printQuote("Farmer", "I'm pretty sure there was a bolder right there.");
                model.getLog().waitForAnimationToFinish();
                subView.markSpace(dsp);
                hintsGiven++;
                return;
            }
        }
        printQuote("Farmer", "Actually... I think you need to work a little more by yourself before I can give you a useful hint.");
    }

    private void revealAllBoulders(SteppingMatrix<DiggingSpace> matrix) {
        MyLists.forEach(matrix.getElementList(), (DiggingSpace space) -> {
            if (space.hasBoulder()) {
                space.setDug(true);
            }
        });
    }

    private boolean doIntro(Model model) {
        println("You pass by a pasture. Curiously a farmer is idly laying in the grass.");
        leaderSay("Taking a day off?");
        showRandomPortrait(model, Classes.FARMER, "Farmer");
        portraitSay("Why work? I've got a good thing going here.");
        println("You look around, all you can see is the empty meadow.");
        leaderSay("What do you mean?");
        portraitSay("I have a whole new business idea. I was about to plow this field, but I realized it was full of boulders. " +
                "So I thought, 'How can I get somebody else to dig'em up for me?' And that's when I got my stroke of genius. " +
                "People come, do all the digging, and even pay me for it!");
        print("Listen to the farmer's idea? (Y/N) ");
        if (!yesNoInput()) {
            leaderSay(iOrWeCap() + " really don't have time for this.");
            portraitSay("It's all the same to me. I'm going back to my nap.");
            return false;
        }
        leaderSay("Uh-huh, and how are you going to make them do that?");
        portraitSay("I've buried obols all around the meadow. In each space I've put a number of obols equal to the number of boulders " +
                "adjacent to that space. Here's the challenge; Pay me ten gold, and you can dig as much as you want. You get to keep all the obols you " +
                "find, but as soon as you hit a boulder, you lose all the obols and you've got to dig up the rest of the boulders for me. " +
                "I'll even sweeten the deal a little. " +
                "If you can find every single obol without hitting a boulder, I'll give you back your ten gold!");
        leaderSay("Sounds innovative.");
        portraitSay("Oh it is. I don't think anybody's thought of this game before.");
        portraitSay("By the way. You can give up at any time. I won't hold it against you.");
        leaderSay("That seems fair.");
        portraitSay("So how about it, do you have ten gold?");
        if (model.getParty().getGold() < 10) {
            leaderSay("Nope. " + iOrWeCap() + " are kind of broke.");
            portraitSay("Don't worry. This thing will catch on and you'll get your chance some other time. See ya!");
            return false;
        } else {
            print("Pay 10 gold to play the digging game? (Y/N) ");
            if (yesNoInput()) {
                leaderSay("I'm in. Here's your gold.");
                model.getParty().addToGold(-10);
                portraitSay("If you want, you can mark the spaces where you think the boulders are. I won't mind.");
                leaderSay("That may be help, thanks for the tip.");
                portraitSay("Good luck friend!");
                return true;
            }
        }
        return false;
    }

    private boolean gameIsOver(SteppingMatrix<DiggingSpace> matrix) {
        return foundBoulder || onlyBouldersLeft(matrix);
    }

    private boolean onlyBouldersLeft(SteppingMatrix<DiggingSpace> matrix) {
        return MyLists.intAccumulate(matrix.getElementList(),
                (DiggingSpace sp) -> sp.isDug() ? 1 : 0) == matrix.getColumns() * matrix.getRows() - boulders;
    }

    private void digSpace(SteppingMatrix<DiggingSpace> matrix, DiggingGameSubView subView, DiggingSpace space) {
        if (!space.isDug()) {
            if (space != matrix.getSelectedElement()) {
                matrix.setSelectedElement(space);
            }
            subView.startDigAnimation(false);
            SoundEffects.digging();
            waitUntil(subView, DiggingGameSubView::isDiggingDone);
            space.setDug(true);
            if (space.hasBoulder()) {
                this.foundBoulder = true;
            } else if (space.getNumber() == 0) {
                digAdjacentSpaces(matrix, space, subView);
                matrix.setSelectedElement(space);
            }
        }
    }

    private void digAdjacentSpaces(SteppingMatrix<DiggingSpace> matrix, DiggingSpace space, DiggingGameSubView subView) {
        List<DiggingSpace> spaces = space.findAdjacentSpaces(matrix);
        List<DiggingSpace> nextSet = new ArrayList<>();
        for (DiggingSpace adjSpace : spaces) {
            if (!adjSpace.isDug()) {
                matrix.setSelectedElement(adjSpace);
                subView.startDigAnimation(true);
                SoundEffects.digging();
                waitUntil(subView, DiggingGameSubView::isDiggingDone);
                adjSpace.setDug(true);
                if (adjSpace.getNumber() == 0) {
                    nextSet.add(adjSpace);
                }
            }
        }

        for (DiggingSpace next : nextSet) {
            digAdjacentSpaces(matrix, next, subView);
        }
    }

    private void populate(SteppingMatrix<DiggingSpace> matrix, int boulders) {
        DiggingSpace startingSpace = matrix.getSelectedElement();
        List<DiggingSpace> spaces = new ArrayList<>(matrix.getElementList());
        Collections.shuffle(spaces);
        for ( ; boulders > 0; boulders--) {
            if (startingSpace != spaces.get(0)) {
                spaces.get(0).setBoulder(true);
            }
            spaces.remove(0);
        }
        for (DiggingSpace space : matrix.getElementList()) {
            space.setNumber(matrix);
        }
        System.out.println("Populated digging matrix:");
        for (int y = 0; y < matrix.getRows(); ++y) {
            for (int x = 0; x < matrix.getColumns(); ++x) {
                matrix.getElementAt(x, y).printYourself();
            }
            System.out.println("");
        }
    }

    private void fill(SteppingMatrix<DiggingSpace> matrix) {
        List<DiggingSpace> stuff = new ArrayList<>();
        for (int y = 0; y < matrix.getRows(); ++y) {
            for (int x = 0; x < matrix.getColumns(); ++x) {
                stuff.add(new DiggingSpace(x, y));
            }
        }
        matrix.addElements(stuff);
        matrix.setSelectedPoint(new Point(matrix.getColumns()/2, matrix.getRows()/2));
    }

    private void increaseWinCount(Model model) {
        int winCount;
        if (!model.getSettings().getMiscCounters().containsKey(COUNTER_KEY)) {
            winCount = 1;
        } else {
            winCount = model.getSettings().getMiscCounters().get(COUNTER_KEY) + 1;
        }
        model.getSettings().getMiscCounters().put(COUNTER_KEY, winCount);
        if (winCount == BOULDER_COUNTS.length) {
            println("Congratulations! You have mastered the farmer's digging game!");
            println("You gain 1 reputation!");
            model.getParty().addToReputation(1);
            println("From now on, when encountering the digging game, " +
                    "you will be allowed to choose the difficulty of the game.");
        }
    }

    private int getWinCount(Model model) {
        if (!model.getSettings().getMiscCounters().containsKey(COUNTER_KEY)) {
            return 0;
        }
        return model.getSettings().getMiscCounters().get(COUNTER_KEY);
    }

    private void gainObols(Model model, SteppingMatrix<DiggingSpace> matrix) {
        int obolsGained = countObols(matrix);
        MyLists.forEach(matrix.getElementList(), DiggingSpace::clearNumber);
        println("You gained " + obolsGained + " obols.");
        model.getParty().addToObols(obolsGained);
    }

    private int countObols(SteppingMatrix<DiggingSpace> matrix) {
        return MyLists.intAccumulate(matrix.getElementList(), (DiggingSpace space) -> space.isDug() ? space.getNumber() : 0);
    }
}
