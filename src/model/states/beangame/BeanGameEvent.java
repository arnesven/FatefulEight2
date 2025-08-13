package model.states.beangame;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.classes.SkillChecks;
import model.items.spells.TelekinesisSpell;
import model.races.Race;
import model.states.DailyEventState;
import model.states.SpellCastException;
import model.states.events.GuideData;
import sound.*;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import util.MyStrings;
import view.GameView;
import view.SimpleMessageView;
import view.subviews.BeanGameSubView;
import view.subviews.ChooseBeanGameSubView;
import view.subviews.CollapsingTransition;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BeanGameEvent extends DailyEventState {

    private static final String BEAN_GAME_FIRST_TIME = "BEAN_GAME_FIRST_TIME";
    private final Race gamblerRace;
    private AdvancedAppearance gamblerAppearance;

    public BeanGameEvent(Model model, Race gamblerRace) {
        super(model);
        this.gamblerRace = gamblerRace;
    }

    public BeanGameEvent(Model model) {
        this(model, Race.randomRace());
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Seek out bean game",
                "Some times you can play a bean game on a street.");
    }

    @Override
    protected void doEvent(Model model) {
        this.gamblerAppearance = PortraitSubView.makeRandomPortrait(Classes.THF, gamblerRace);
        println("As you walk down a street, a " + manOrWoman(gamblerAppearance.getGender()) + " calls out to you.");
        model.getLog().waitForAnimationToFinish();
        showExplicitPortrait(model, gamblerAppearance, "Gambler");
        portraitSay("Hey, look over here. You wanna?");
        if (isFirstTime(model)) {
            println("The " + manOrWoman(gamblerAppearance.getGender()) + " is standing next to some wooden boards " +
                    "propped up against a wall. Each board is adorned with colorful nails in entrancing patterns.");
            leaderSay("What's this, street-art?");
            portraitSay("Hehe, no no. This is the Bean Game. Never heard of it?");
            leaderSay("No... What's it about?");
            portraitSay("Oh, it's a lot of fun friend. Pay me five obols and I'll give you a little bean. You drop " +
                    "it at the top of one of my boards and it'll bounce down to the bottom. " +
                    "If it lands in a pocket with a number - you win! The payout is five times the number, in obols.");

            leaderSay("Sounds interesting...");
            portraitSay("And I haven't even told you about the Jackpot!");
            leaderSay("The Jackpot?");
            portraitSay("That's the big leagues friend. Pay me one gold and pick a board. I'll give you three beans. " +
                    "If you manage to hit a winning pocket all three times, all multiply those numbers and pay you - in gold!");
            randomSayIfPersonality(PersonalityTrait.greedy, List.of(model.getParty().getLeader()),
                    "Did " + heOrShe(gamblerAppearance.getGender()) + " say the words 'multiply' and 'gold'?");
            leaderSay("What if I don't hit a winning pocket?");
            portraitSay("Then the game is over. But you look like a lucky individual.");
            leaderSay("I believe I am... Let me have a look at the boards.");
            setFirstTimeDone(model);
        } else {
            println("You recognize the set-up for the Bean Game. " +
                    "You step over to look at the boards lined up against the wall.");
        }
        model.getLog().waitForAnimationToFinish();

        List<BeanGameBoard> boards = makeBoards();
        ClientSoundManager.stopPlayingBackgroundSound();
        printQuote("Gambler", "Do ya just want to play Singles, " +
                "or do you want to try your luck on the Jackpot?");
        int constableTime = MyRandom.randInt(3, 24);

        for (int timesPlayed = 0; ; timesPlayed++) {
            ChooseBeanGameSubView chooseView = new ChooseBeanGameSubView(boards);
            CollapsingTransition.transition(model, chooseView);

            int choice = multipleOptionArrowMenu(model, 24, 32, List.of("Singles", "Jackpot", "Walk away"));
            if (choice == 2) {
                break;
            }
            if (timesPlayed > constableTime) {
                constableCome(model);
                break;
            }
            if (choice == 0) {
                if (!playSingles(model, chooseView)) {
                    break;
                }
            } else {
                if (!playJackpot(model, chooseView)) {
                    break;
                }
            }
            print("Press enter to continue.");
            waitForReturn();
            gamblerSay("Up for another game?");
        }
        println("You walk away from the bean games.");
    }

    private void constableCome(Model model) {
        setCurrentTerrainSubview(model);
        println("You're just about to talk to the gambler when you see a constable coming " +
                "around the corner down the street. You discretely nod your head at him.");
        showExplicitPortrait(model, gamblerAppearance, "Gambler");
        portraitSay("Fiddlesticks! Well friend, it's been fun. But I'd better skedaddle.");
        leaderSay("Yeah, I had an inkling this wasn't exactly legal.");
        println("The gambler begins to hurriedly collect " + hisOrHer(gamblerAppearance.getGender()) +
                " bean game board.");
    }

    private boolean isFirstTime(Model model) {
        return !model.getSettings().getMiscFlags().containsKey(BEAN_GAME_FIRST_TIME);
    }

    private void setFirstTimeDone(Model model) {
        model.getSettings().getMiscFlags().put(BEAN_GAME_FIRST_TIME, true);
    }

    private boolean playSingles(Model model, ChooseBeanGameSubView chooseView) {
        if (model.getParty().getObols() < 5) {
            if (model.getParty().getGold() < 1) {
                leaderSay("I'd love to play, but...");
                gamblerSay("No cash? Come back when you got coin friend.");
                return false;
            } else {
                leaderSay("I want to play Singles, but I don't have any obols. I have gold though.");
                gamblerSay("Don't worry. I've got change.");
                println("You hand the gambler 1 gold and get 5 obols back.");
                model.getParty().goldTransaction(-1);
                model.getParty().addToObols(5);
            }
        } else {
            leaderSay(MyRandom.sample(List.of("Singles.", "I'l play some singles.",
                    "Just singles for now.", "I only have time for a game or two.")));
            gamblerSay("Okay. That will be 5 obols.");
            println("You hand the gambler 5 obols.");
            model.getParty().addToObols(-5);
        }
        gamblerSay("Okay. Go ahead and pick a board.");
        model.getLog().waitForAnimationToFinish();
        chooseView.enableSelect();
        waitForReturnSilently();
        BeanGameBoard beanMatrix = chooseView.getSelectedBoard();
        if (MyRandom.flipCoin()) {
            gamblerSay(MyRandom.sample(List.of(
                    "You wanna play the ",
                    "The ")) + beanMatrix.getName() +
                    "? Okay then, good luck!");
        } else {
            leaderSay("I want to try the " + beanMatrix.getName() + ".");
            gamblerSay("Here's your bean. Go ahead.");
        }
        int prize = runBeanGame(model, beanMatrix);
        if (model.getParty().isWipedOut()) {
            return false;
        }
        if (prize == 0) {
            if (MyRandom.flipCoin()) {
                gamblerSay(MyRandom.sample(List.of("Bad luck friend.", "Trickier than it looks, right?",
                        "Sorry friend.", "Close one!")));
            } else {
                leaderCommentOnLoss();
            }
        } else {
            leaderSay(MyRandom.sample(List.of("Yes!", "I won!", "Yay!", "Superb.", "That's the way!",
                    "Lucky!", "In the pocket!", "Great. Pay up!", "I knew it!")));
            gamblerSay("Okay, here's your money.");
            int money = prize * 5;
            println("The gambler hands you " + money + " obols.");
            model.getParty().addToObols(money);
        }
        return true;
    }

    private boolean playJackpot(Model model, ChooseBeanGameSubView chooseView) {
        if (model.getParty().getGold() < 1) {
            leaderSay("I'd love to play, but...");
            gamblerSay("No cash? Come back when you got coin friend.");
            return false;
        } else {
            leaderSay("Let me try for the Jackpot.");
            gamblerSay("That's the spirit. Show me a golden coin and pick a board.");
            println("You hand the gambler 1 gold.");
            model.getParty().goldTransaction(-1);
        }

        model.getLog().waitForAnimationToFinish();
        chooseView.enableSelect();
        waitForReturnSilently();
        BeanGameBoard beanMatrix = chooseView.getSelectedBoard();
        leaderSay("That one. The " + beanMatrix.getName() + ".");
        gamblerSay("Jackpot on the " + beanMatrix.getName() + ", how exciting! Here are your three beans.");

        List<Integer> prizes = new ArrayList<>();
        for (int round = 1; round <= 3; round++) {
            gamblerSay(MyStrings.capitalize(MyStrings.nthWord(round)) + " bean. Good luck!");
            int prize = runBeanGame(model, beanMatrix);
            if (model.getParty().isWipedOut()) {
                return false;
            }
            prizes.add(prize);
            if (prize == 0) {
                leaderCommentOnLoss();
                gamblerSay("Sorry friend. I was of rooting for you, honestly. " +
                        "But you can always try again.");
                return true;
            } else if (round < 3) {
                String prizeWord = MyStrings.numberWord(prize);
                leaderSay(MyRandom.sample(List.of("A " + prizeWord + ", good.", "Nice, a " + prizeWord + ".",
                        "Great, a " + prizeWord + ".", "Phew, a " + prizeWord + ".")));
                if (round == 1) {
                    gamblerSay("So far so good. But you still got two beans to go.");
                } else { // round == 2
                    gamblerSay("Wow... Now it all hangs on the last bean!");
                }
            }
        }
        jackpotWin(model, prizes);
        portraitSay("Now, I'm afraid I have to pack it in for today. Good bye!");
        leaderSay("He he. I think we broke the house.");
        return false;
    }

    private void jackpotWin(Model model, List<Integer> prizes) {
        leaderSay("JACKPOT!!!");
        model.getLog().waitForAnimationToFinish();
        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, gamblerAppearance, "Gambler");
        println("The gambler's enthusiasm is quickly replaced by obvious disappointment.");
        portraitSay("Jackpot indeed.");
        leaderSay("I guess this is my lucky day.");
        int result = prizes.get(0) * prizes.get(1) * prizes.get(2);
        SkillCheckResult skillResult = model.getParty().getLeader().testSkillHidden(Skill.Logic,
                SkillChecks.adjustDifficulty(model, 8), 0);
        if (result <= 10 || skillResult.isSuccessful()) {
            leaderSay("Now pay me my " + result + " gold please.");
            println("(" + skillResult.asString() + ".)");
            portraitSay("Hmm... yes, congratulations...");
            println("The gambler brings out a bag of money and counts up your prize. You get " + result + " gold.");
            model.getParty().earnGold(result);
        } else {
            portraitSay("Let me just calculate your reward. " + MyStrings.numberWord(prizes.get(0)) +
                    " times " + MyStrings.numberWord(prizes.get(1)) + " times " +
                    MyStrings.numberWord(prizes.get(2)) + " is ...");
            int fakeResult = (result * 2) / 3;
            println("The gambler brings out a bag of money and counts up coins.");
            portraitSay(" uhm, it's " + fakeResult + " gold. Here you go.");

            MyPair<SkillCheckResult, GameCharacter> passiveResult = doPassiveSkillCheck(Skill.Logic, 8);

            if (passiveResult.first.isSuccessful()) {
                GameCharacter gc = passiveResult.second;
                println(gc.getName() + " does some quick counting on " + hisOrHer(gc.getGender()) +
                        " fingers (" + skillResult.asString() + ") ");
                partyMemberSay(gc, "Hey, that doesn't seem right! I get it to " + result + " gold.");
                portraitSay("Yes, of course, how silly of me. An honest mistake, here you go.");
                println("The gambler nervously pulls out some more money. You get " + result + " gold.");
                model.getParty().earnGold(result);
                return;
            }

            leaderSay("Thank you!");
            println("You gladly accept the " + fakeResult + " gold.");
            model.getParty().earnGold(fakeResult);
        }
    }

    private int runBeanGame(Model model, BeanGameBoard beanMatrix) {
        BeanGameSubView beanSubView = new BeanGameSubView(beanMatrix, model.getParty().getLeader());
        CollapsingTransition.transition(model, beanSubView);
        if (MyRandom.flipCoin()) {
            leaderSay(MyRandom.sample(List.of("Let's see...", "Where to drop it?", "Hmm...", "I drop it up here?")));
        }
        model.getSpellHandler().acceptSpell(new TelekinesisSpell().getName());
        waitForReturnSilently();
        if (MyRandom.flipCoin()) {
            leaderSay(MyRandom.sample(List.of("Drop!", "Go!", "Bye little bean.", "Fingers crossed!")));
        }
        beanSubView.start();
        try {
            waitUntilOrSpell(beanSubView, BeanGameSubView::gameIsOver);
        } catch (SpellCastException sce) {
            beanSubView.setPause(true);
            if (sce.getSpell() instanceof TelekinesisSpell &&
                    sce.getSpell().castYourself(model, this, sce.getCaster())) {
                beanSubView.enableTelekinesis();
                model.getLog().waitForAnimationToFinish();
                model.transitionToDialog(new TelekinesisActivatedDialog(model, sce.getCaster()));
            }
            beanSubView.setPause(false);
            waitUntil(beanSubView, BeanGameSubView::gameIsOver);
        }
        model.getSpellHandler().unacceptSpell(new TelekinesisSpell().getName());
        beanSubView.stop();
        return beanMatrix.getPrize(beanSubView.getWinPocket()-1);
    }

    private static List<BeanGameBoard> makeBoards() {
        List<BeanGameBoard> boards = new ArrayList<>(List.of(
                new LadyBeanBoard(), new SwordBeanBoard(),
                new DragonBeanBoard(), new LightHouseBeanBoard(),
                new UnicornBeanBoard(), new WaterfallBeanBoard()));
        Collections.shuffle(boards);
        for (int i = MyRandom.randInt(1, 2); boards.size() > i; ) {
            boards.remove(0);
        }
        boards.add(BeanGameBoardMaker.generateBeanMatrix());
        Collections.shuffle(boards);
        return boards;
    }

    private void gamblerSay(String line) {
        printQuote("Gambler", line);
    }

    private void leaderCommentOnLoss() {
        leaderSay(MyRandom.sample(List.of("Drat!", "It was so close!", "Rotten luck.",
                "It's over?", "Nothing.", "Meh.", "Dung beetles!", "That was unfortunate.")));
    }

    private static class TelekinesisActivatedDialog extends SimpleMessageView {
        public TelekinesisActivatedDialog(Model model, GameCharacter caster) {
            super(model.getView(), caster.getName() + " is now affecting the bean with Telekinesis! " +
                    "You can nudge the ball by using the arrow keys. Attention: excessive use may " +
                    "cause suspicion!");
        }
    }
}
