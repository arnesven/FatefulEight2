package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.WitchKingAppearance;
import model.characters.WitchKingCharacter;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.WitchKingEnemy;
import model.items.spells.DispellSpell;
import model.races.Race;
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

import static model.classes.Classes.None;

public class WitchKingEvent extends DailyEventState {
    private GameCharacter witchKingChar = new WitchKingCharacter();

    public WitchKingEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        if (model.getParty().isSpecialCharacterMarked(witchKingChar.getName())) {
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
            portraitSay(model, "Thanks again for removing that awful spell. You have no idea how long I've been trapped in there.");
            model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Would you mind explaining who you are?");
            portraitSay(model, "I am... you know, I've quite forgotten my name.");
            model.getParty().partyMemberSay(model, model.getParty().getLeader(), "I'm sorry...");
            portraitSay(model, "But you can call me the Witch King. I distinctly remember being both a witch and a king.");
            portraitSay(model, "Or at least, I was a king, before my brother trapped me in that stronghold back there.");
            model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Why did he do that?");
            portraitSay(model, "Oh, you know, the normal rivalry among heirs to the Witch Throne.");
            model.getParty().partyMemberSay(model, model.getParty().getLeader(), "... and what is the Witch Throne?");
            portraitSay(model, "The throne of the Witch realm of course!");
            model.getParty().partyMemberSay(model, model.getParty().getLeader(), "... which is...?");
            portraitSay(model, "My dear chap, all this. All you see around you, is the Witch Realm.");
            model.getParty().partyMemberSay(model, model.getParty().getLeader(), "...");
            portraitSay(model, "Or at least, it was. I gather a few things may change while one is imprisoned for a few thousand years.");
            model.getParty().partyMemberSay(model, model.getParty().getLeader(), "I'm afraid you'll find that nobody around here has every heard about the Witch Realm.");
            portraitSay(model, "It didn't even make it into the history books?");
            GameCharacter gc = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            model.getParty().partyMemberSay(model, gc, "Who has time for books?");
            model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Well, Witch King, you're free now. Free to do whatever you want.");
            portraitSay(model, "Yes, I suppose...");
            portraitSay(model, "What are you fellows up to?");
            model.getParty().partyMemberSay(model, gc, "Adventuring, killing monsters, traveling... anything that we happen to fancy.");
            portraitSay(model, "Would it be to presumptuous of me to ask to join you in your travels?");
            waitForReturn();
            RecruitState recruitState = new RecruitState(model, List.of(witchKingChar));
            recruitState.run(model);
            if (model.getParty().getPartyMembers().contains(witchKingChar)) {
                portraitSay(model, "Another adventure awaits...");
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
            model.getParty().partyMemberSay(model, model.getParty().getLeader(),
                    List.of("Okay people. Get ready for a boss fight."));
            model.getParty().randomPartyMemberSay(model, List.of("Wait a minute, is this guy asleep?"));
            model.getParty().randomPartyMemberSay(model, List.of("Looks like he's in some kind of a trance..."));
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                SkillCheckResult result = gc.testSkill(Skill.SpellCasting, 10);
                if (result.isSuccessful()) {
                    exploreRuinsState.println(gc.getName() + " detects a powerful enchantment. (Spellcasting " + result.asString() + ")");
                    model.getParty().partyMemberSay(model, gc, "He's under the effects of an enchantment. A binding spell of black magic.");
                    model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Can we break the spell?");
                    GameCharacter gc2 = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                    model.getParty().partyMemberSay(model, gc2, "Better not. I'm sure whoever put him under did it for a reason.");
                    exploreRuinsState.println("Witch King: \"Please.... Help.... Me....\"");
                    tryBreakSpell(model, exploreRuinsState);
                    model.getParty().partyMemberSay(model, gc, "He appears to be waking up.");
                }
            }
            exploreRuinsState.println("The Witch King suddenly opens his eyes. In a wild fury he lunges at you!");
            do {
                witchKing.setSleeping(false);
                super.entryTrigger(model, exploreRuinsState);
                addObject(witchKing);
                witchKing.setSleeping(true);
                if (witchKing.isDead()) {
                    model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Well, now he will rest in peace.");
                    break;
                } else {
                    exploreRuinsState.println("Witch King: \"Free me.... from the.... spell....\"");
                    model.getParty().partyMemberSay(model, model.getParty().getLeader(), "He looks like he's trying to hold back. " +
                            "But he's struggling with himself, like an unseen force was controlling his limbs.");
                    GameCharacter gc2 = model.getParty().getRandomPartyMember(model.getParty().getLeader());
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
            GameCharacter gc3 = model.getParty().getRandomPartyMember(pair.second);
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
