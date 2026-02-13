package view;

import model.Model;
import model.map.InnLocation;
import util.MyStrings;
import view.sprites.Animation;
import view.sprites.AnimationManager;
import view.subviews.ImageSubView;
import view.subviews.SubView;

import java.awt.event.KeyEvent;
import java.security.Key;
import java.sql.Struct;

public class StoryIntroView extends GameView implements Animation {

    private static final int TEXT_WIDTH = 45;
    private static final int TEXT_START_ROW = 10;
    private static SubView arabellaSubView = new ImageSubView("arabella", "xkjals", "asl");
    private static SubView alternativeSubView = new ImageSubView("theinnalt","xxyyzz", "");

    private static final String[] pages = new String[]{
            "Far out in the wilderness, a woman travels alone. " +
            "She has been trekking for weeks, searching for something.\n\n" +
            "The rain comes down in torrents, " +
            "and the muddy terrain is difficult to traverse. Still, the woman trudges on, determined to " +
            "driven by her purpose.\n\n" +
            "As she reaches the top of a hill, she spots a tremendous structure in the distance. " +
            "With a sigh of relief, she makes her way down the slope and continues towards the menacing tower.",

            "As she approaches the structure her mouth curls into a wicked smile.\n\n" +
            "\"At last!\" she whispers.\n\n" +
            "She presses her palms to the massive doors and chants a spell. With an unsettling creak, the doors begin to open. " +
            "The woman steps forward, then hesitates, as if thinking twice about entering.\n\n" +
            "What will she find in this remote, strange place? The secret answer to an ancient riddle? " +
            "A dormant arcane power waiting to be rekindled? A forgotten prophecy lead her here, but now " +
            "that she has arrived, events will be set in motion that will alter the course of history.\n\n" +
            "As the rain continues to pour, the woman steps inside, and the hulking doors close ominously behind her.",

            "Halfway across the continent, the same rain falls on your head, and you are eager to get out of it " +
            "as you push open the door to the inn.\n\n" +
            "As you throw back your hood, your senses are immediately assaulted by the light from the fire place, " +
            "a musky odor and a chorus of loud voices.\n\n" +
            "You haven't been here in years, but the place looks much the same. It's a busy meeting spot for adventurers, " +
            "merchants, travelers, explorers - anyone who needs a warm meal and a cozy bed for the night.\n\n" +
            "The place is almost full but you spot your friend. He's got a little table in the back. You make " +
            "your way across the room and manage to catch the innkeeper's eye, signaling that you want " +
            "food and drink.",

            "You sit down. \"Good to see you.\" you say.\n" +
            "\"Long time no see!\" your friend replies.\n" +
            "\"How's life in the old homestead?\" he chuckles as he sips his ale.\n" +
            "\"Oh, same old, same old...\" you mumble. \"What's city life like?\"\n" +
            "\"Not much different, I'm afraid.\"\n\n" +
            "The innkeeper arrives with your meal.\n\n" +
            "\"Sorry about this loud lot.\" The innkeeper gestures at the large party of adventurers crowded around a " +
            "big round table at the center of the room. \"But they do pay well!\"\n" +
            "\"Must've just come back from a quest or something.\" your friend remarks.\n\n" +
            "The rowdy group bursts into song, and several other patrons join in. When the song ends, " +
            "the leader of the group announces, \"Next round is on us - Bolero's Company!\"\n" +
            "Cheers fill the room, followed by more singing.",

            "\"Oh, to be an adventurer!\" your friend blurts after finishing his third ale. " +
            "\"Exploration, excitement, fighting monsters!\"\n" +
            "\"What do you know about fighting monsters?\" you ask.\n" +
            "\"Well, how hard could it be? Say, we could start our own company!\"\n" +
            "\"That's ridiculous. We don't know the first thing about leading a group like that.\"\n" +
            "\"But we could learn. Honestly friend, do you really want to spend the rest of your life doing what you do now?\"\n\n" +
            "You pause for a moment. You have to admit that the notion has some appeal. But doubts still linger in your mind.\n\n" +
            "\"Where would we find quests? How would we attract partners? Who would teach us to fight?\"\n" +
            "\"Hey, slow down. I'm sure we can find answers to all those questions. After all, we have some " +
            "experts right over there! Now let's go introduce ourselves and offer to buy the next round.\"",

            "What follows is a night full of singing, laughter, talking and drinking. You and your friend " +
            "decide that you will both give up your mundane old lives and take up adventuring full-time. " +
            "The other adventurers at the party are supportive, but offer nothing more than general " +
            "advice and well-wishes.\n\n" +
            "When the night is finally over, you stumble to your room at the inn and " +
            "fall asleep, properly intoxicated on both ale and the prospect of a new adventure.\n\n" +
            "However, when you awake your friend has already left. Perhaps sobering up made him reconsider his change of career.\n" +
            "You, however, are convinced that adventuring is what you were meant for..."};

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

        drawPicture(model);

        for (int i = 0; i < textParts.length; ++i) {
            MyColors textColor;
            if (!fadeOut) {
                textColor = textColors[Math.max(0, Math.min(colorIndex - i, textColors.length - 1))];
            } else {
                textColor = textColors[Math.max(0, textColors.length - colorIndex - 1)];
            }
            int x = 35;
            if (pageIndex == 0) {
                BorderFrame.drawCentered(model.getScreenHandler(), textParts[i], TEXT_START_ROW + i + 5, textColor, MyColors.BLACK);
            } else {
                BorderFrame.drawString(model.getScreenHandler(), textParts[i], x, TEXT_START_ROW + i, textColor, MyColors.BLACK);
            }
        }
        if (fadeOut && colorIndex > textColors.length) {
            fadeOut = false;
            nextPage();
        }

    }

    private void drawPicture(Model model) {
        if (pageIndex == 0) {

        } else if (pageIndex == 1) {
            ((ImageSubView) arabellaSubView).drawArea(model, 2, 5);
        } else if (pageIndex < 4) {
            ((ImageSubView) InnLocation.getSubView()).drawArea(model, 2, 5);
        } else {
            ((ImageSubView) alternativeSubView).drawArea(model, 2, 5);
        }
    }

    @Override
    public GameView getNextView(Model model) {
        return new MainGameView();
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!fadeOut) {
                totalTime = 0;
            }
            if (pageIndex == pages.length-1) {
                exitFade = true;
            } else {
                fadeOut = true;
            }
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
            colorIndex = (int) (totalTime / 150);
            if (oldColorIndex != colorIndex) {
                madeChanges();
            }
        }
    }

    @Override
    public void synch() {

    }
}
