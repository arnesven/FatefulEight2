package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.WitchKingCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.WitchKingEnemy;
import model.items.spells.DispellSpell;
import model.ruins.DungeonMonster;
import model.ruins.DungeonRoom;
import model.ruins.FinalDungeonLevel;
import model.ruins.RuinsDungeon;
import model.states.DailyEventState;
import model.states.ExploreRuinsState;
import model.states.RecruitState;
import model.states.SpellCastException;
import util.MyPair;

import java.awt.*;
import java.util.List;

public class WitchKingEvent extends DailyEventState {
    private GameCharacter witchKingChar = new WitchKingCharacter();

    public WitchKingEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        if (model.getParty().isSpecialCharacterMarked(witchKingChar)) {
            new NoEventState(model).doEvent(model);
            return;
        }
        model.getParty().randomPartyMemberSay(model, List.of("Looks like a stronghold up ahead... Looks pretty intimidating"));
        print("Do you enter the stronghold? (Y/N) ");
        if (!yesNoInput()) {
            return;
        }
        RuinsDungeon dungeon =  new RuinsDungeon(30, 3, 4);
        WitchKingChambersRoom witchKingRoom = new WitchKingChambersRoom();
        FinalDungeonLevel finalLevel = (FinalDungeonLevel) dungeon.getLevel(dungeon.getNumberOfLevels()-1);
        finalLevel.setFinalRoom(witchKingRoom);

        ExploreRuinsState explore = new ExploreRuinsState(model, dungeon, "Stronghold");
        explore.run(model);
        setCurrentTerrainSubview(model);
        if (witchKingRoom.spellBroken) {
            showExplicitPortrait(model, witchKingChar.getAppearance(), "Witch King");
            portraitSay("Thanks again for removing that awful spell. You have no idea how long I've been trapped in there.");
            leaderSay("Would you mind explaining who you are?");
            portraitSay("I am... you know, I've quite forgotten my name.");
            leaderSay("I'm sorry...");
            portraitSay("But you can call me the Witch King. I distinctly remember being both a witch and a king.");
            portraitSay("Or at least, I was a king, before my brother trapped me in that stronghold back there.");
            leaderSay("Why did he do that?");
            portraitSay("Oh, you know, the normal rivalry among heirs to the Witch Throne.");
            leaderSay("... and what is the Witch Throne?");
            portraitSay("The throne of the Witch realm of course!");
            leaderSay("... which is...?");
            portraitSay("My dear chap, all this. All you see around you, is the Witch Realm.");
            leaderSay("...");
            portraitSay("Or at least, it was. I gather a few things may change while one is imprisoned for a few thousand years.");
            leaderSay("I'm afraid you'll find that nobody around here has every heard about the Witch Realm.");
            portraitSay("It didn't even make it into the history books?");
            GameCharacter gc = model.getParty().getRandomPartyMember();
            model.getParty().partyMemberSay(model, gc, "Who has time for books?");
            leaderSay("Well, Witch King, you're free now. Free to do whatever you want.");
            portraitSay("Yes, I suppose...");
            portraitSay("What are you fellows up to?");
            model.getParty().partyMemberSay(model, gc, "Adventuring, killing monsters, traveling... anything that we happen to fancy.");
            portraitSay("Would it be to presumptuous of me to ask to join you in your travels?");
            waitForReturn();
            witchKingChar.setLevel((int)Math.max(1, Math.floor(RecruitState.calculateAverageLevel(model))));
            RecruitState recruitState = new RecruitState(model, List.of(witchKingChar));
            recruitState.run(model);
            if (model.getParty().getPartyMembers().contains(witchKingChar)) {
                portraitSay("Another adventure awaits...");
            }
        }
    }

    private static class WitchKingObject extends DungeonMonster {
        private WitchKingEnemy witchKingEnemy;

        public WitchKingObject() {
            super(List.of(new WitchKingEnemy('A')));
            setSleeping(true);
            witchKingEnemy = (WitchKingEnemy) getEnemies().get(0);
            setInternalPosition(new Point(3, 5));
        }

        @Override
        protected int getTimeLimit() {
            return 3;
        }

        public boolean isDead() {
            return witchKingEnemy.isDead();
        }
    }

    private class WitchKingChambersRoom extends DungeonRoom {
        private final WitchKingObject witchKing;
        private Point relPos;
        private boolean spellBroken = false;

        public WitchKingChambersRoom() {
            super(5, 5);
            this.witchKing = new WitchKingObject();
            addObject(witchKing);
            this.relPos = new Point(0, 0);

        }

        @Override
        public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
            model.getParty().markSpecialCharacter(WitchKingEvent.this.witchKingChar);
            this.relPos = new Point(2, 1);
            exploreRuinsState.moveCharacterToCenterAnimation(model, relPos);
            exploreRuinsState.leaderSay("Okay people. Get ready for a boss fight.");
            model.getParty().randomPartyMemberSay(model, List.of("Wait a minute, is this guy asleep?"));
            model.getParty().randomPartyMemberSay(model, List.of("Looks like he's in some kind of a trance..."));
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                SkillCheckResult result = gc.testSkill(Skill.SpellCasting, 10);
                if (result.isSuccessful()) {
                    exploreRuinsState.println(gc.getName() + " detects a powerful enchantment. (Spellcasting " + result.asString() + ")");
                    model.getParty().partyMemberSay(model, gc, "He's under the effects of an enchantment. A binding spell of black magic.");
                    leaderSay("Can we break the spell?");
                    GameCharacter gc2 = model.getParty().getRandomPartyMember();
                    model.getParty().partyMemberSay(model, gc2, "Better not. I'm sure whoever put him under did it for a reason.");
                    exploreRuinsState.println("Witch King: \"Please.... Help.... Me....\"");
                    tryBreakSpell(model, exploreRuinsState);
                    model.getParty().partyMemberSay(model, gc, "He appears to be waking up.");
                    break;
                }
            }
            exploreRuinsState.println("The Witch King suddenly opens his eyes. In a wild fury he lunges at you!");
            do {
                witchKing.setSleeping(false);
                super.entryTrigger(model, exploreRuinsState);
                addObject(witchKing);
                witchKing.setSleeping(true);
                if (witchKing.isDead()) {
                    leaderSay("Well, now he will rest in peace.");
                    break;
                } else {
                    exploreRuinsState.println("Witch King: \"Free me.... from the.... spell....\"");
                    leaderSay("He looks like he's trying to hold back. " +
                            "But he's struggling with himself, like an unseen force was controlling his limbs.");
                    GameCharacter gc2 = model.getParty().getRandomPartyMember();
                    model.getParty().partyMemberSay(model, gc2, "Maybe we can nullify the spell somehow?");
                    model.getSpellHandler().acceptSpell(new DispellSpell().getName());
                    try {
                        boolean result = tryBreakSpell(model, exploreRuinsState);
                        if (result) {
                            return;
                        }
                    } catch (SpellCastException sce) {
                        if (sce.getSpell().getName().equals(new DispellSpell().getName())) {
                            if (sce.getSpell().castYourself(model, exploreRuinsState, sce.getCaster())) {
                                breakSpell(exploreRuinsState);
                                break;
                            }
                        }
                    }
                }
            } while (true);
            model.getSpellHandler().unacceptSpell(new DispellSpell().getName());
            exploreRuinsState.setDungeonExited(true);
        }

        private boolean tryBreakSpell(Model model, ExploreRuinsState exploreRuinsState) {
            MyPair<Boolean, GameCharacter> pair = model.getParty().doSoloSkillCheckWithPerformer(model, exploreRuinsState, Skill.MagicBlue, 10);
            if (pair.first) {
                breakSpell(exploreRuinsState);
                return true;
            }
            model.getParty().partyMemberSay(model, pair.second, "That's one tough spell to break. " +
                    "But if you keep him calm, maybe I can try again.");
            GameCharacter gc3 = model.getParty().getRandomPartyMember();
            model.getParty().partyMemberSay(model, gc3, "Too late, here he comes again!");
            return false;
        }

        public void breakSpell(ExploreRuinsState state) {
            spellBroken = true;
            state.println("The witch king calms down and shudders with relief.");
            state.println("Witch King: \"Thank you. Come, I'll tell you more outside. Let's get out of this wretched place.\"");
        }

        @Override
        public Point getRelativeAvatarPosition() {
            return relPos;
        }

    }

}
