package view;

import model.Model;
import model.map.InnLocation;
import util.MyStrings;
import view.sprites.Animation;
import view.sprites.AnimationManager;
import view.subviews.ImageSubView;

import java.awt.event.KeyEvent;
import java.security.Key;
import java.sql.Struct;

public class StoryIntroView extends GameView implements Animation {

    private static final int TEXT_WIDTH = 45;
    private static final int TEXT_START_ROW = 10;

    private static final String[] pages = new String[]{
            "You are eager to get out of the rain as you push open the heavy door to the Crossroad's Inn. " +
            "As you throw back your hood your senses are at once assailed by the light from the fire place, a musky odor and loud noises. You haven't been " +
            "here in years but the place looks much the same. It's a busy meeting place for adventurers, " +
            "merchants, travelers, explorers or anybody else who needs a warm meal and a cozy bed for the night.\n\n" +
            "The place is almost full but you spot your friend. He's got a little table in the back. You make " +
            "your way across the room and manage to get eye contact with the inn-keeper, signaling that you want " +
            "drink and food.",

            "You sit down. \"Good to see you.\" You say.\n" +
            "\"Long time no see!\" Your friend replies.\n" +
            "\"How's life in the old homestead?\" He chuckles as he sips his ale.\n" +
            "\"Oh, same old, same old...\" You mumble. \"What's the city life like?\"\n" +
            "\"Not much different, I'm afraid.\"\n\n" +
            "The innkeeper arrives with your meal.\n\n" +
            "\"Sorry about this loud lot\". The innkeeper gestures at the large party of adventurers crowded around a " +
            "big round table at the center of the room. \"But they do pay well!\"\n" +
            "\"Must've just come back from a quest or some such.\" Your friend remarks.\n" +
            "The rowdy group bursts into song, and several other patrons join in. When the song is done, " +
            "the leader of the group announces: \"Next round is on us Bolero's Company!\"\n" +
            "Cheers fill the room, then more singing follows.",

            "\"Oh, to be an adventurer!\" Your friend blurts after finishing his third ale. " +
            "\"Exploration, excitement, fighting monsters!\"\n" +
            "\"What do you know about fighting monsters?\" You ask.\n" +
            "\"Well, how hard could it be? Say, we could start our own company!\"\n" +
            "\"That's ridiculous. We don't know the first thing about leading a group like that.\"\n" +
            "\"But we could learn. Honestly friend, do you really want to spend the rest of your life doing what you do now?\"\n" +
            "You pause for a moment. You must admit that the notion has some appeal. But there are still doubts lingering in your mind.\n" +
            "\"Where would we find quests? How would we attract partners? Who would teach us to fight?\"\n" +
            "\"Hey, slow down. I'm sure we can find answers to all those questions. After all, we have some " +
            "experts right over there! Now let's go introduce ourselves and offer to buy the next round.\"",

            "What follows is a night full of singing, laughter, talking and drinking. You and your friend " +
            "decide that you will both give up your mundane old lives and take up adventuring, full time! " +
            "The other adventuring types at the party are supportive, but offer nothing more than some general " +
            "advice and well-wishes. When the night is finally over you stumble to your room at the inn and " +
            "fall asleep, properly intoxicated on both ale and the prospect of a new adventure.\n\n" +
            "However, when you awake your friend has already left. Perhaps sobering up made him reconsider his change of career.\n" +
            "You however are convinced that adventuring is what you were meant for..."};

    private static final MyColors[] textColors = new MyColors[]{MyColors.BLACK, MyColors.DARK_GRAY, MyColors.DARK_RED,
            MyColors.ORANGE, MyColors.LIGHT_YELLOW, MyColors.WHITE};
    private boolean fadeIn;
    private String[] textParts;
    private int pageIndex;
    private int colorIndex;
    private long totalTime;
    private boolean fadeOut;
    private boolean exitFade;

    public StoryIntroView() {
        super(true);
        pageIndex = -1;
        nextPage();
        fadeIn = true;
        exitFade = false;
    }

    private void nextPage() {
        fadeOut = false;
        colorIndex = 0;
        totalTime = 0;
        pageIndex++;
        textParts = MyStrings.partitionWithLineBreaks(pages[pageIndex], TEXT_WIDTH);
    }

    @Override
    public void transitionedTo(Model model) {
        AnimationManager.register(this);
    }

    @Override
    public void transitionedFrom(Model model) {
        AnimationManager.unregister(this);
    }

    @Override
    protected void internalUpdate(Model model) {
        BorderFrame.drawString(model.getScreenHandler(), "ESC = SKIP", 8, DrawingArea.WINDOW_ROWS-4, MyColors.GRAY, MyColors.BLACK);

        ((ImageSubView)InnLocation.getSubView()).drawArea(model, 2, 5);
        for (int i = 0; i < textParts.length; ++i) {
            MyColors textColor;
            if (!fadeOut) {
                textColor = textColors[Math.max(0, Math.min(colorIndex - i, textColors.length - 1))];
            } else {
                textColor = textColors[Math.max(0, textColors.length - colorIndex - 1)];
            }
            BorderFrame.drawString(model.getScreenHandler(), textParts[i], 35, TEXT_START_ROW + i, textColor, MyColors.BLACK);
        }
        if (fadeOut && colorIndex > textColors.length) {
            fadeOut = false;
            nextPage();
        }

    }

    @Override
    public GameView getNextView(Model model) {
        return new MainGameView();
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            if (pageIndex == pages.length-1) {
                exitFade = true;
            } else {
                fadeOut = true;
            }
            totalTime = 0;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            exitFade = true;
            fadeOut = false;
            fadeIn = false;
            totalTime = 0;
        }
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        totalTime += elapsedTimeMs;
        if (fadeIn) {
            double fadeLevel = 1.0 - (double) totalTime / 2000;
            model.getScreenHandler().setFade(fadeLevel, MyColors.BLACK);
            if (fadeLevel <= 0.0) {
                fadeIn = false;
                totalTime = 0;
            }
            madeChanges();
        } else if (exitFade) {
            double fadeLevel = Math.min(1.0, (double) totalTime / 2000);
            model.getScreenHandler().setFade(fadeLevel, MyColors.BLACK);
            if (fadeLevel >= 1.0) {
                setTimeToTransition(true);
            }
            madeChanges();
        } else {
            int oldColorIndex = colorIndex;
            colorIndex = (int) (totalTime / 100);
            if (oldColorIndex != colorIndex) {
                madeChanges();
            }
        }
    }

    @Override
    public void synch() {

    }
}
