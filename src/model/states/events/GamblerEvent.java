package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.classes.Skill;
import model.combat.loot.CombatLoot;
import model.combat.loot.ObolsLoot;
import model.enemies.BanditEnemy;
import model.enemies.BrotherhoodCronyEnemy;
import model.enemies.Enemy;
import model.enemies.MuggerEnemy;
import model.items.spells.Spell;
import model.items.spells.TelekinesisSpell;
import model.races.Race;
import model.states.DailyEventState;
import util.MyPair;
import util.MyRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GamblerEvent extends DailyEventState {

    private static final int COST_TO_PLAY = 5;
    private int potSize;
    private ConstableEvent innerEvent;

    public GamblerEvent(Model model) {
        super(model);
        potSize = MyRandom.randInt(54)*2 + 10;
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Seek out gamblers",
                "There are always some gamblers about, if you're into that kind of stuff");
    }

    @Override
    protected void doEvent(Model model) {
        showEventCard("Gambler", "You pass by an alley and see a small crowd gathered there. A few of " +
                "them are squatting, some of them shouting and laughing. You casually " +
                "meander over and look what the commotion is about.");
        model.getParty().randomPartyMemberSay(model, List.of("Dice... I should've guessed"));
        showRandomPortrait(model, Classes.THF, Race.ALL, "Gambler");
        portraitSay("Hey newcomer, it's five obols to roll the dice. Want in?");
        if (model.getParty().getObols() < COST_TO_PLAY) {
            if (model.getParty().getGold() < 1) {
                leaderSay("We don't have the coin, or the time for this.");
                return;
            } else {
                leaderSay("Don't have any obols, but I have gold.");
                portraitSay("That's fine, I can make change.");
            }
        }

        print("Do you want to play dice? (Y/N) ");
        acceptTelekinesis(model);
        if (yesNoInput()) {
            payGambler(model);
            portraitSay("It's simple. You roll two dice and add them together. On eight or more, you get your money back. " +
                    "Otherwise your money goes into the pot. On double sixes, you win the pot.");
            leaderSay("How big is the pot?");
            portraitSay("Looks like we're up to " + potSize + " obols now. Who knows, maybe you'll get lucky?");
            while (true) {
                println("The gambler hands you the dice.");
                int die1 = MyRandom.randInt(1, 6);
                int die2 = MyRandom.randInt(1, 6);
                int troubleChance = 2;
                if (telekinesisCast(model)) {
                    die1 = 6;
                    die2 = 6;
                    troubleChance = 5;
                }
                println("You roll a " + die1 + " and a " + die2 + ", for a total of " + (die1 + die2) + ".");
                if (die1 == 6 && die2 == 6) {
                    leaderSay("Double sixes!");
                    if (MyRandom.rollD6() < troubleChance) {
                        telekinesisWin(model);
                    } else {
                        normalWin(model);
                    }
                    return;
                } else if (die1 + die2 < 8) {
                    potSize += 2;
                    portraitSay("Bad luck newcomer. But hey, now the pot is even bigger. " + potSize + " obols!");
                } else {
                    portraitSay("Good roll! Here's your money back newcomer.");
                    println("The party receives " + COST_TO_PLAY + " obols.");
                    model.getParty().addToObols(COST_TO_PLAY);
                }
                portraitSay("Wanna go again?");
                randomSayIfPersonality(PersonalityTrait.greedy, List.of(model.getParty().getLeader()),
                        "Just think, we could win those obols!");
                if (model.getParty().getGold() < 1) {
                    println("Unfortunately you cannot afford to continue the game. So you excuse yourself.");
                    break;
                }
                print("Do you? (Y/N) ");
                acceptTelekinesis(model);
                if (!yesNoInput()) {
                    break;
                } else {
                    payGambler(model);
                    if (MyRandom.rollD10() == 1) {
                        println("The gambler is about to hand you the dice when somebody shouts 'constable!'. The gambler " +
                                "quickly scoops up the pot and bounds away down the street and the crowd disperses.");
                        innerEvent = new ConstableEvent(model);
                        innerEvent.doEvent(model);
                        unacceptTelekinises(model);
                        return;
                    }
                }
            }

        }

        println("You leave the gamblers to their game.");
        unacceptTelekinises(model);
    }

    private void payGambler(Model model) {
        if (model.getParty().getObols() < COST_TO_PLAY) {
            println("You hand the gambler 1 gold and the gambler hands you " + (10 - COST_TO_PLAY) + " obols back.");
            model.getParty().goldTransaction(-1);
            model.getParty().addToObols(10 - COST_TO_PLAY);
        } else {
            println("You hand the gambler " + COST_TO_PLAY + " obols.");
            model.getParty().addToObols(-COST_TO_PLAY);
        }
    }

    private void telekinesisWin(Model model) {
        portraitSay("Hey... there was something weird going on with that roll!");
        leaderSay("Whatever do you mean?");
        portraitSay("The dice where jumping all funny. Like something was messing with them.");
        MyPair<Boolean, GameCharacter> result =
                model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.Persuade, 10);
        if (result.first) {
            partyMemberSay(result.second, "Relax, it was just the wind. The afternoon breeze is picking up.");
            portraitSay("Hmm... possibly. But it sure looked strange... Ah, maybe it's my imagination. Here's your winnings.");
            model.getParty().addToObols(potSize);
            println("The party gains " + potSize + " obols.");
            println("The crowd then disperses and the gambler stomps off.");
        } else {
            leaderSay("Are you calling me a cheater?");
            portraitSay("As a matter of fact I am! Come on gang, we'll show them what we do with cheaters.");
            println("The gamblers rush you and a fight breaks out!");
            List<Enemy> enemies = new ArrayList<>(List.of(
                    new MuggerEnemy('A'), new MuggerEnemy('A'),
                    new MuggerEnemy('A'), new MuggerEnemy('A'),
                    new BanditEnemy('B'), new BanditEnemy('B'),
                    new BanditEnemy('B'), new BanditEnemy('B'),
                    new BrotherhoodCronyEnemy('C'), new BrotherhoodCronyEnemy('C'),
                    new BrotherhoodCronyEnemy('C'), new BrotherhoodCronyEnemy('C')
            ));
            Collections.shuffle(enemies);
            for (int toRemove = MyRandom.randInt(3, 6); toRemove > 0; --toRemove) {
                enemies.remove(0);
            }
            runCombat(enemies);
        }
    }

    @Override
    protected List<CombatLoot> getExtraCombatLoot(Model model) {
        return new ArrayList<>(List.of(new ObolsLoot(potSize)));
    }

    private void normalWin(Model model) {
        portraitSay("You lucky bastard...#");
        leaderSay("Pay up man, I won that pot fair and square.");
        println("Looks around nervously as to assess his options.");
        leaderSay("Fine! Take it... I was getting bored of this game anyway.");
        model.getParty().addToObols(potSize);
        println("The party gains " + potSize + " obols.");
        println("The crowd then disperses and the gambler stomps off.");
    }

    private boolean telekinesisCast(Model model) {
        if (model.getSpellHandler().spellReady()) {
            MyPair<Spell, GameCharacter> sp = model.getSpellHandler().getCastSpell();
            if (sp.first instanceof TelekinesisSpell &&
                    sp.first.castYourself(model, this, sp.second)) {
                println("As the dice roll, " + sp.second.getName() + " influence them with an unseen force.");
                model.getSpellHandler().unacceptSpell(new TelekinesisSpell().getName());
                return true;
            }
        }
        return false;
    }

    private void unacceptTelekinises(Model model) {
        model.getSpellHandler().unacceptSpell(new TelekinesisSpell().getName());
    }

    private void acceptTelekinesis(Model model) {
        model.getSpellHandler().acceptSpell(new TelekinesisSpell().getName());
    }

    @Override
    public boolean haveFledCombat() {
        if (innerEvent != null) {
            return innerEvent.haveFledCombat();
        }
        return super.haveFledCombat();
    }
}
