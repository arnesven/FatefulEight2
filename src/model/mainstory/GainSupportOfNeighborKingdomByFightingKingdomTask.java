package model.mainstory;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.map.*;
import model.map.wars.KingdomWar;
import model.map.wars.PitchedBattleSite;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.battle.*;
import model.states.events.CommandOutpostDailyEventState;
import model.states.events.FightInUnitDuringBattleEvent;
import util.MyLists;
import util.MyRandom;
import view.MyColors;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GainSupportOfNeighborKingdomByFightingKingdomTask extends GainSupportOfNeighborKingdomTask {
    public GainSupportOfNeighborKingdomByFightingKingdomTask(String neighbor, Point position, String kingdom, String castle, Point battlePosition) {
        super(neighbor, position, kingdom, castle, battlePosition);
    }

    @Override
    protected GameState makeBattleEvent(Model model, CastleLocation defender, CastleLocation invader) {
        return new BattleWithKingdomEvent(model, defender, invader);
    }

    @Override
    protected VisitLordEvent innerMakeLordEvent(Model model, UrbanLocation neighbor, CastleLocation castle) {
        return new GetSupportFromLordEvent(model, neighbor, this, castle) {
            @Override
            protected String getShortThreatDescription(CastleLocation kingdom) {
                return kingdom.getLordName() + "'s armed forces";
            }

            @Override
            protected String getLongThreatDescription(CastleLocation kingdom) {
                return "We're currently facing a large invasion force from the " +
                        CastleLocation.placeNameToKingdom(kingdom.getPlaceName());
            }
        };
    }

    private class BattleWithKingdomEvent extends DailyEventState {
        private final CastleLocation defender;
        private final CastleLocation invader;
        private boolean victorious;
        private boolean freeLodging;

        public BattleWithKingdomEvent(Model model, CastleLocation defender, CastleLocation invader) {
            super(model);
            this.defender = defender;
            this.invader = invader;
            this.victorious = false;
            freeLodging = false;
        }

        @Override
        protected void doEvent(Model model) {
            println("The party comes upon an army camp.");
            CharacterAppearance fieldGeneralAppearance = PortraitSubView.makeRandomPortrait(Classes.PAL);
            CommandOutpostDailyEventState.intro(this, fieldGeneralAppearance.getRace());
            showExplicitPortrait(model, fieldGeneralAppearance, "Field General");
            portraitSay("Hello there. Are you the new recruits? Or are you the military tacticians my lord has sent me?");
            leaderSay("Uhm. Depends. What's going on here?");
            portraitSay("Those damnable invaders! Their army " +
                    "is threatening our kingdom. But this can't be news to you. Now please, I'm very busy. What is your business here?");
            KingdomWar war = makeWar(model, defender, invader);
            int choice = multipleOptionArrowMenu(model, 24, 26, List.of("Join a unit", "Direct the battle", "Don't get involved"));
            if (choice == 0) {
                leaderSay("We're the new recruits. Just point us in the direction of this rabble and victory shall soon be ours.");
                portraitSay("I admire your fervor. Just head on down the hill and you'll soon see the ranks. " +
                        "Just find any unit and report to the lieutenant.");
                leaderSay("See you on the other side general.");
                FightInUnitDuringBattleEvent fightEvent = new FightInUnitDuringBattleEvent(model, defender.getPlaceName(),
                        war.getAggressorUnits());
                model.getLog().waitForAnimationToFinish();
                removePortraitSubView(model);
                fightEvent.run(model);
                if (model.getParty().isWipedOut()) {
                    return;
                }
                victorious = fightEvent.isVictorious();
            } else if (choice == 1) {
                leaderSay("We're the tacticians you mentioned. We'll stay with you and help you lead the battle.");
                portraitSay("All right! Let's go give the enemy a taste of our zeal!");
                BattleState battle = new BattleState(model, war, false);
                battle.run(model);
                victorious = battle.wasVictorious();
            } else {
                leaderSay("Actually, we're just passing through.");
                portraitSay("Then what on gods holy earth are you wasting my time for? GET OUT!");
                leaderSay("Yes sir!");
                return;
            }

            setCurrentTerrainSubview(model);
            showExplicitPortrait(model, fieldGeneralAppearance, "Field General");

            if (victorious) {
                portraitSay("What a glorious day, victory is ours!");
                leaderSay("Yes. The troops fought well.");
                portraitSay("Don't discount yourself. Without your effort, defeat would have surely found us.");
                leaderSay("Perhaps. Now it found the enemy instead. What now general?");
                portraitSay("We have driven the invaders off, but we must remain vigilant. " +
                        "This rabble may yet return to terrorize our lands anew.");
                leaderSay("And if they do, we'll be ready.");
                portraitSay("I'm glad we can count on you. Please stay in our camp tonight. We have plenty of spare beds and food for you.");
                leaderSay("Thank you general, and so long.");
                this.freeLodging = true;
                println("Each party member gains 25 XP!");
                MyLists.forEach(model.getParty().getPartyMembers(),
                        (GameCharacter gc) -> model.getParty().giveXP(model, gc, 25));
                print("Would you like to search through the battle field for any salvageable equipment? (Y/N) ");
                if (yesNoInput()) {
                    CommandOutpostDailyEventState.lootBattlefield(model, this);
                }
                GainSupportOfNeighborKingdomByFightingKingdomTask.this.setCompleted(true);
            } else {
                println("In the hectic aftermath of the lost battle, you see the general barking order at " +
                        "the confused units, or what remains of them.");
                portraitSay("Retreat! Retreat I say!");
                leaderSay("General... we did our best but...");
                portraitSay("Fortune was not with us today, needless to say. We must make haste to our " +
                        "rear positions before the enemy overruns us.");
                portraitSay("Now make haste friend or we shall surely meet the same fate as many of our fallen comrades.");
                leaderSay("Yes general.");
                setFledCombat(true);
            }
        }

        @Override
        protected boolean isFreeLodging() {
            return freeLodging;
        }

        private KingdomWar makeWar(Model model, CastleLocation defender, CastleLocation invader) {
            KingdomWar war = new KingdomWar(invader.getPlaceName(), defender.getPlaceName(), invader.getCastleColor(), defender.getCastleColor(),
                    new ArrayList<>(), new PitchedBattleSite(model.getParty().getPosition(), colorForHex(model.getCurrentHex()), "Battle with the " +
                    CastleLocation.placeNameShort(invader.getPlaceName()) + " invaders"),
                    new ArrayList<>());
            return war;
        }

        private MyColors colorForHex(WorldHex currentHex) {
            if (currentHex instanceof TundraHex) {
                return MyColors.WHITE;
            }
            if (currentHex instanceof WastelandHex) {
                return MyColors.TAN;
            }
            if (currentHex instanceof DesertHex) {
                return MyColors.YELLOW;
            }
            return MyColors.GREEN;
        }
    }
}
