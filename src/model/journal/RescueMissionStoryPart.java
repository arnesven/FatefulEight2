package model.journal;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.map.CastleLocation;
import model.map.MountainHex;
import model.map.TownLocation;
import model.map.UrbanLocation;
import model.quests.Quest;
import model.quests.RescueMissionQuest;
import model.quests.SpecialDeliveryQuest;
import model.quests.VampiresLairQuest;
import model.races.Race;
import model.states.DailyEventState;
import model.states.dailyaction.TownDailyActionState;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.List;

public class RescueMissionStoryPart extends StoryPart {
    private static final int VISIT_CASTLE_STEP = 0;
    private static final int DO_QUEST_STEP = 1;
    private static final int QUEST_DONE_STEP = 2;
    private static final int COMPLETED = 3;
    private final String libraryTown;
    private final StoryPart witchPart;
    private int internalStep = 0;
    private final String castleName;
    private AdvancedAppearance courtMage = PortraitSubView.makeRandomPortrait(Classes.MAGE, Race.ALL);
    private Point caidQuestPosition = null;

    public RescueMissionStoryPart(StoryPart witchPart, String castleName, String libraryTown) {
        this.witchPart = witchPart;
        this.castleName = castleName;
        this.libraryTown = libraryTown;
    }

    @Override
    public List<JournalEntry> getJournalEntries() {
        return List.of(new GoToCastleTask(castleName));
    }

    @Override
    public void handleTownSetup(TownDailyActionState townDailyActionState) {

    }

    @Override
    public void progress() {
        internalStep++;
    }

    @Override
    public void addQuests(Model model, List<Quest> quests) {
        if (model.getCurrentHex().getLocation() != null && model.getCurrentHex().getLocation() instanceof CastleLocation) {
            if (model.getCurrentHex().getLocation().getName().equals(castleName)) {
                if (internalStep == DO_QUEST_STEP) {
                    CastleLocation castle = model.getWorld().getCastleByName(castleName);
                    quests.add(getQuestAndSetPortrait(RescueMissionQuest.QUEST_NAME, model.getLordPortrait(castle),
                            castle.getLordName()));
                }
            }
        }
        if (giveCaidQuest(model)) {
            quests.add(getQuestAndSetPortrait(VampiresLairQuest.QUEST_NAME, model.getMainStory().getCaidCharacter().getAppearance(),
                    model.getMainStory().getCaidCharacter().getName()));
            this.caidQuestPosition = new Point(model.getParty().getPosition());
        }
    }

    private boolean giveCaidQuest(Model model) {
        return model.getCurrentHex() instanceof MountainHex &&
                internalStep >= COMPLETED &&
                !model.getMainStory().isCaidQuestDone() &&
                (caidQuestPosition == null ||
                        (caidQuestPosition.x == model.getParty().getPosition().x &&
                         caidQuestPosition.y == model.getParty().getPosition().y));
    }

    @Override
    public void drawMapObjects(Model model, int x, int y, int screenX, int screenY) {
        if (caidQuestPosition != null && caidQuestPosition.x == x && caidQuestPosition.y == y &&
                !model.getMainStory().isCaidQuestDone()) {
            model.getScreenHandler().register(MAP_SPRITE.getName(), new Point(screenX, screenY), MAP_SPRITE, 1);
        }
    }

    @Override
    public String getHexInfo(Point position) {
        if (caidQuestPosition != null && caidQuestPosition.x == position.x && caidQuestPosition.y == position.y) {
            return "Quest with Caid Sanchez";
        }
        return super.getHexInfo(position);
    }

    @Override
    protected StoryPart getNextStoryPart(Model model, int track) {
        throw new IllegalStateException("Should not be called.");
    }

    @Override
    public DailyEventState getVisitLordEvent(Model model, UrbanLocation location) {
        if (location instanceof CastleLocation &&
                ((CastleLocation) location).getName().equals(castleName) && internalStep <= COMPLETED) {
            return new RescueMissionLordEvent(model, model.getWorld().getCastleByName(castleName), model.getWorld().getTownByName(libraryTown));
        }
        return super.getVisitLordEvent(model, location);
    }

    @Override
    protected boolean isCompleted() {
        return internalStep >= COMPLETED;
    }

    private class RescueMissionLordEvent extends DailyEventState {
        private final CastleLocation castle;
        private final TownLocation libraryTown;

        public RescueMissionLordEvent(Model model, CastleLocation castle, TownLocation libraryTown) {
            super(model);
            this.castle = castle;
            this.libraryTown = libraryTown;
        }

        @Override
        protected void doEvent(Model model) {
            setCurrentTerrainSubview(model);
            showLord(model);
            if (internalStep == VISIT_CASTLE_STEP) {
                model.getSettings().getMiscFlags().put("MAIN_STORY_LORD_MET", true);
                portraitSay("Welcome traveller, what can I do for you.");
                TownLocation town = model.getMainStory().getStartingLocation(model);
                leaderSay("We've come from the town of " + town.getName() + ", " +
                        "We helped them with some trouble with their local population of frogmen.");
                portraitSay("Ah yes, that issue...");
                leaderSay("Anyway, it's been taken care of. We were promised a reward, but it seems they were out of coin...");
                portraitSay("Very unfortunate, but I'm afraid we can't...");
                leaderSay("Sorry to interrupt you, but you owe the " + town.getLordTitle() + " of " + town.getTownName() +
                        " some money. Am I right?");
                leaderSay("That's correct.");
                leaderSay("I've been authorized to collect a debt of " + InitialStoryPart.REWARD_GOLD +
                        " gold, as payment for the resolving the frogmen problem.");
                portraitSay("Hmm, that seems plausible. Let me just get my purse.");
                println("The party receives " + InitialStoryPart.REWARD_GOLD + " gold!");
                model.getParty().addToGold(InitialStoryPart.REWARD_GOLD);

                if (witchPartCompleted()) {
                    leaderSay("There's an important issue which " + iOrWe() + " must urgently discuss with you.");
                    portraitSay("I understand, but I'm afraid I'm completely occupied in another matter.");
                    leaderSay("But this is important!");
                    portraitSay("One thing at a time. And perhaps you could help me with my problem. Then we can deal with your important issue.");
                    leaderSay("Fine... what seems to be the problem?");
                } else {
                    leaderSay("Thank you. Now we'll just be on our way...");
                    portraitSay("Just a moment please.");
                    leaderSay("Yes?");
                }

                portraitSay("For a while now I've been looking for a capable team of adventurers like yourselves. " +
                        "I have a task that needs doing.");
                leaderSay("I'm listening.");
                portraitSay("Well. It's my most trusted advisor and top agent, Caid. He's been kidnapped. " +
                        "I need somebody to rescue him.");
                leaderSay("Kidnapped? By whom?");
                portraitSay("I'm not sure. They haven't made themselves known.");
                leaderSay("Then how do you know he's been kidnapped?");
                portraitSay("He hasn't left my side for years, now he's been missing for a month. " +
                        "Last time we spoke he was about to start an investigation into a personal matter of mine.");
                leaderSay("Maybe he's just taken a vacation?");
                if (model.getParty().size() > 1) {
                    GameCharacter gc = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                    partyMemberSay(gc, "Maybe he just got knocked off?");
                }
                portraitSay("Not likely, he's also my master-at-arms and my personal fencing instructor. " +
                        "He can handle himself. Will you look for him? I'll pay you. I'll pay you handsomely.");
                leaderSay("Perhaps we'll look into it. What does he look like?");
                portraitSay("He's tall, pretty good-looking. He always wears a headband, and he carries his sword " +
                        "on his back rather than by his side.");
                leaderSay("Alright. We'll keep our eyes open.");
                increaseStep(model);
            } else if (internalStep == DO_QUEST_STEP) {
                portraitSay("Have you found Caid yet?");
                if (witchPartCompleted()) {
                    leaderSay("Actually, there was another matter which I want to bring to your attention.");
                    portraitSay("I'm sorry, but I just can't focus on anything other than Caid's disappearance " +
                            "right now. Please help me find him first.");
                    leaderSay("Fine...");
                } else {
                    leaderSay("Not yet.");
                }
            } else {
                portraitSay("Welcome back. Have you found Caid?");
                leaderSay("Yes. We have.");
                portraitSay("Was it kidnappers? Or something worse?");
                leaderSay("Rivalling gangs. Caid got caught in the middle, but we got him out.");
                portraitSay("Splendid. Where is he now?");
                leaderSay("Still on the mission you gave him. He wanted you to know that.");
                portraitSay("Thank you so much for your help.");
                if (internalStep < COMPLETED) {
                    increaseStep(model);
                }
                if (witchPartCompleted()) {
                    leaderSay("Now, we really need to discuss an urgent matter.");
                    portraitSay("And that is?");
                    println("You bring out the crimson orb and hand it to the " + castle.getLordTitle() +
                            ". He holds it up and inspects it carefully.");
                    portraitSay("Very beautiful... What is it?");
                    leaderSay("A magical pearl which has the power to dominate a person's mind.");
                    println("The " + castle.getLordTitle() + " quickly hands the pearl back to you.");
                    leaderSay("Don't worry, you have to consume it for it to have any effect.");
                    portraitSay("How do you know?");
                    leaderSay("We found it in the stomach of a frogman, who seemed quite deranged from the effects " +
                            "of it. It seems pearls like these were once used by spell casters to impose their will upon others?");
                    portraitSay("What spell casters are you talking about?");
                    leaderSay("Have you ever heard of 'the Quad?'");
                    showCourtMage(model);
                    portraitSay("I have. Every scholar who's studied our lands history have heard about them.");
                    showLord(model);
                    portraitSay("Ah yes, 'the Quad'. Of course I've heard about them. Uhm, would you refresh my memory?");
                    showCourtMage(model);
                    portraitSay("They were a group of powerful sorcerers who lived hundreds of years ago. " +
                            "It is said they overthrew an evil tyrant, but then became even worse oppressors themselves.");
                    showLord(model);
                    portraitSay("But what does this pearl have to do with the Quad? Do you think this pearl was made by the Quad?");
                    leaderSay("It's possible. Or somebody else made it.");
                    showCourtMage(model);
                    portraitSay("This is the stuff of legends. I have never encountered any modern magic user who's " +
                            "used this kind of magic. Needless to say, in the wrong hands, it could be very dangerous.");
                    leaderSay("The frogmen problem was a direct consequence of this pearl. Other incidents could follow.");
                    showLord(model);
                    portraitSay("Hmm...");
                    println("The " + castle.getLordTitle() + " scratches " + hisOrHer(castle.getLordGender()) + " chin.");
                    if (DailyEventState.getPartyAlignment(model) < -1) {
                        leaderSay("So... any chance you want to buy the pearl? Would make a wonderful conversation piece.");
                    } else {
                        leaderSay("We have to stop whoever is making these pearls!");
                    }
                    portraitSay("What we really need is to gather more information. I mean, we just don't have enough facts.");
                    showCourtMage(model);
                    portraitSay("I agree. What happened to the Quad? Where did they go? If they aren't around anymore, " +
                            "who did they pass on their knowledge to?");

                    if (DailyEventState.getPartyAlignment(model) < -1) {
                        leaderSay("All fascinating questions... do you want to buy the pearl or not?");
                    } else {
                        leaderSay("Perhaps you should take the pearl?");
                    }
                    showLord(model);
                    portraitSay("No, you should keep it. You will need it.");
                    leaderSay("Need it? For what?");
                    portraitSay("I'm sending you on a mission, if you are willing to aid me in this. I need " +
                            "somebody to investigate this matter further. I need someone capable, and who knows the " +
                            "details of the case. And most of all, I need somebody I can trust.");
                    if (!model.getParty().getPartyMembers().contains(model.getMainStory().getCaidCharacter())) {
                        leaderSay("What about Caid?");
                        portraitSay("Yes, that would be a good choice. I would have preferred that he go with you, but he " +
                                "is engaged in another task at the moment.");
                    }
                    leaderSay("If I would accept this mission, what would I need to do?");
                    portraitSay("I'm sending you to " + libraryTown.getName() + ". There's a library there and the " +
                            "finest historian in the realm, Willis Johanssen. There is nobody more likely than Willis to know " +
                            "what there is to know about the Quad and the pearl. Or if she doesn't she'll find out from the books in the library.");
                    progressPartThree(model);
                    increaseStep(model);
                } else {
                    leaderSay("We'll be on our way now.");
                    portraitSay("Fortune be with you in your travels.");
                }
            }
        }

        private void showLord(Model model) {
            showExplicitPortrait(model, model.getLordPortrait(castle), castle.getLordName());
        }

        private void showCourtMage(Model model) {
            showExplicitPortrait(model, courtMage, "Court Mage");
        }
    }

    private void progressPartThree(Model model) {
        for (StoryPart part : model.getMainStory().getStoryParts()) {
            if (part instanceof PartThreeStoryPart) {
                part.progress();
                return;
            }
        }
        throw new IllegalStateException("Could not find part three to progress.");
    }

    private boolean witchPartCompleted() {
        return witchPart.isCompleted();
    }

    public class GoToCastleTask extends MainStoryTask {
        private final String castleName;

        public GoToCastleTask(String castleName) {
            super("Reward at " + castleName + "");
            this.castleName = castleName;
        }

        @Override
        public String getText() {
            if (internalStep == VISIT_CASTLE_STEP) {
                return "Travel to " + castleName + " to claim " + InitialStoryPart.REWARD_GOLD +
                        " gold, your reward for dealing with the frogmen problem. ";
            } else if (internalStep == DO_QUEST_STEP) {
                return "Complete the '" + RescueMissionQuest.QUEST_NAME + "' Quest.";
            } else if (internalStep == QUEST_DONE_STEP) {
                return "Report back to " + castleName + " to explain where Caid is.";
            }
            return "You found Caid.\n\nCompleted.";
        }

        @Override
        public boolean isComplete() {
            return RescueMissionStoryPart.this.isCompleted();
        }

        @Override
        public Point getPosition(Model model) {
            return model.getMainStory().getCastlePosition(model);
        }
    }
}
