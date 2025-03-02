package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.*;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.classes.SkillChecks;
import model.enemies.Enemy;
import model.items.Item;
import model.items.spells.HarmonizeSpell;
import model.items.spells.Spell;
import model.races.Race;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.SpellCastException;
import model.states.fishing.Fish;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import view.subviews.PortraitSubView;
import view.subviews.SubView;

import java.util.ArrayList;
import java.util.List;

public class CatInATreeEvent extends GeneralInteractionEvent {
    private CharacterAppearance portrait;
    private boolean hasTriedTalking = false;

    public CatInATreeEvent(Model model) {
        super(model, "Talk to", MyRandom.randInt(2, 10));
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        this.portrait = PortraitSubView.makeOldPortrait(Classes.None, Race.randomRace(), true);
        showExplicitPortrait(model, portrait, "Old Woman");
        println("As you cut through a small park you spot an old woman standing under a tree.");
        return true;
    }

    private List<String> makeOptions(Model model) {
        List<String> result = new ArrayList<>(List.of("Climb the tree", "Walk away"));
        if (!hasTriedTalking) {
            result.add(0, "Call for the cat");
        }
        if (MyLists.any(model.getParty().getInventory().getAllItems(), it -> it instanceof Fish)) {
            result.add(0, "Offer fish");
        }
        return result;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        println("");
        portraitSay("Come down Quincy. Come down... Oh, excuse me sir. Would you mind helping me?");
        leaderSay("What's the problem then?");
        portraitSay("My cat, Quincy. He won't come down. I think he chased a squirrel up there, " +
                "and now he can't get down.");
        model.getParty().randomPartyMemberSay(model, List.of("Oh bother...", "You got to be kitten me!",
                "What a CATastrophe!"));
        randomSayIfPersonality(PersonalityTrait.benevolent, List.of(model.getParty().getLeader()),
                "We simply must help...");
        acceptHarmonize(model);

        do {
            List<String> options = makeOptions(model);
            SubView previousSubView = model.getSubView();
            int selectedChoice = 0;
            try {
                selectedChoice = multipleOptionArrowMenu(model, 24, 24, options);
                if (options.get(selectedChoice).contains("Call")) {
                    if (talkToCat(model)) {
                        oldWomanReward(model);
                        break;
                    }
                } else if (options.get(selectedChoice).contains("Climb")) {
                    if (climbTheTree(model)) {
                        oldWomanReward(model);
                        break;
                    }
                } else if (options.get(selectedChoice).contains("Offer")) {
                    offerFish(model);
                    oldWomanReward(model);
                    break;
                } else {
                    println("You excuse yourself and carry on with your day.");
                    unacceptHarmonize(model);
                    return false;
                }
            } catch (SpellCastException sce) {
                model.setSubView(previousSubView);
                if (sce.getSpell().castYourself(model, this, sce.getCaster())) {
                    println("The cat seems pacified. It slowly get to its feet and nimbly " +
                            "jumps down from the tree to land in front of the old woman.");
                    unacceptHarmonize(model);
                    oldWomanReward(model);
                    break;
                }
            }
        } while (true);
        unacceptHarmonize(model);
        return true;
    }

    private void unacceptHarmonize(Model model) {
        model.getSpellHandler().unacceptSpell(new HarmonizeSpell().getName());
    }

    private void acceptHarmonize(Model model) {
        if (MyLists.any(model.getParty().getSpells(), sp -> sp instanceof HarmonizeSpell)) {
            model.getSpellHandler().acceptSpell(new HarmonizeSpell().getName());
            Spell.giveCueMessage(model);
        }
    }

    private void offerFish(Model model) {
        Fish f = (Fish)MyLists.find(model.getParty().getInventory().getAllItems(), it -> it instanceof Fish);
        model.getParty().getInventory().remove(f);
        println("You pull out a " + f.getName() + " from your inventory and lay it on the ground.");
        printQuote("Cat", "Meooow!");
        println("The cat quickly leaps down from the tree and starts to eat the " + f.getName());
        leaderSay("That was easy.");
    }

    private boolean talkToCat(Model model) {
        println("You approach the cat in the tree. It hisses loudly.");
        hasTriedTalking = true;
        boolean success = false;
        MyPair<Boolean, GameCharacter> result =
                model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.Survival, 8);
        if (result.first) {
            partyMemberSay(result.second, "Here kitty kitty, come here...");
            println(result.second.getFirstName() + " gently reaches toward the cat. The cat seems to shy away at first, " +
                    "but then nimbly leaps down on " + result.second.getFirstName() + "s arm.");
            partyMemberSay(result.second, "I think she was just a little scared.");
            success = true;
        } else {
            partyMemberSay(result.second, "Meow, meeooow, meeeeoooow?");
            println("The cat sits very still in the tree, looking at you curiously.");
        }
        return success;
    }

    private boolean climbTheTree(Model model) {
        MyPair<Boolean, GameCharacter> result =
                model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.Acrobatics, 7);
        if (result.first) {
            println(result.second.getName() + " manages to climb the tree and retrieve Quincy the cat.");
            SkillCheckResult spotting = result.second.testSkillHidden(Skill.Perception,
                    SkillChecks.adjustDifficulty(model, 6), 0);
            if (spotting.isSuccessful()) {
                println(result.second.getName() + " also spots a shiny trinket (Perception " + spotting.asString() + "). How did that get there?");
                Item it = model.getItemDeck().getRandomJewelry();
                println("The party gained " + it.getName() + ".");
                it.addYourself(model.getParty().getInventory());
            }
            return true;
        }
        println(result.second.getName() + " falls out of the tree nad takes 1 damage.");
        result.second.addToHP(-1);
        if (result.second.isDead()) {
            DailyEventState.characterDies(model, this, result.second,
                    " has fallen to " + GameState.hisOrHer(result.second.getGender()) + " death!",
                    true);
        } else {
            model.getParty().partyMemberSay(model, result.second, "Ouch!#");
        }
        return false;
    }

    private void oldWomanReward(Model model) {
        portraitSay("Oh thank you so much for getting my beloved Quincy for me! Here, " +
                "please take this, It's all I have on me.");
        int gold = 10;
        model.getParty().addToGold(gold);
        println("The party gains " + 10 + " gold.");
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm just an old woman. Won't you help me out?";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        return new GameCharacter("Old Woman", "", portrait.getRace(), Classes.None, portrait,
                Classes.NO_OTHER_CLASSES);
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        return new ArrayList<>();
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.ALWAYS_ESCAPE;
    }

}
