package model.states.events;

import model.Model;
import model.characters.EnchantressCharacter;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.Condition;
import model.enemies.BearEnemy;
import model.enemies.EnchantressEnemy;
import model.enemies.Enemy;
import model.enemies.WildBoarEnemy;
import model.items.Item;
import model.items.spells.DispellSpell;
import model.items.spells.MindControlSpell;
import model.items.spells.Spell;
import model.states.*;
import util.MyRandom;
import view.MyColors;
import view.sprites.CharSprite;
import view.sprites.Sprite;
import view.subviews.GrassCombatTheme;
import view.subviews.MansionTheme;

import java.util.ArrayList;
import java.util.List;

public class EnchantressEvent extends DailyEventState {

    private static final int INITIAL_PERSUADE_DIFFICULTY = 13;
    private GameCharacter enchantress = new EnchantressCharacter();
    private boolean enchantmentDetected = false;
    private boolean enchantedPartyMember = false;
    private boolean goodArguments = false;
    private int dispellAttempts = 0;
    private int persuadeDifficulty = INITIAL_PERSUADE_DIFFICULTY;

    public EnchantressEvent(Model model) {
        super(model);
    }

    @Override
    public boolean haveFledCombat() {
        return false;
    }

    @Override
    protected void doEvent(Model model) {
        if (model.getParty().isSpecialCharacterMarked(enchantress) ||
                model.getParty().size() < 3 || model.getCurrentHex().hasRoad()) {
            new NoEventState(model).run(model);
            return;
        }
        model.getParty().markSpecialCharacter(enchantress);
        GameCharacter other = arriveAtVillage(model);
        enchantressEnchants(model, other);
        hamletLoop(model, other);
        if (!isWipedOut(model)) {
            println("You leave the hamlet and continue your journey.");
        }
    }

    private GameCharacter arriveAtVillage(Model model) {
        println("The party arrives at a little hamlet hidden away from the main road. " +
                "The people here seem busy at work, but friendly. After some casual chitchat, " +
                "the party realizes that something isn't quite right here.");
        leaderSay("What's the matter with people here? They seem distant and distracted.");
        GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
        model.getParty().partyMemberSay(model, other, "It's almost like they're in a perpetual daydream, a trance...");
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            SkillCheckResult result = gc.testSkill(Skill.SpellCasting, 10);
            if (result.isSuccessful()) {
                println(gc.getName() + " detects a powerful enchantment. (Spellcasting " + result.asString() + ")");
                model.getParty().partyMemberSay(model, gc, "They must be enchanted some how...");
                enchantmentDetected = true;
                break;
            }
        }
        leaderSay("They're all working like there's no tomorrow though. Everybody but... that woman.");
        println("Down by a creek, a woman is sitting by the water, casually watching the villagers.");
        model.getParty().partyMemberSay(model, other, "That woman is... beautiful!");
        println("The woman has noticed the party and approaches.");
        showExplicitPortrait(model, enchantress.getAppearance(), "Beautiful Woman");
        println("The beautiful woman puts a hand on " + other.getFirstName() + "'s shoulder and speaks in a strange, resonant voice.");
        portraitSay("Greetings fair travellers. Won't you rest here for awhile in our peaceful hamlet.");
        return other;
    }


    private void enchantressEnchants(Model model, GameCharacter other) {
        SkillCheckResult result = other.testSkill(Skill.Perception, 10, enchantmentDetected ? 2 : 0);
        if (result.isSuccessful()) {
            println(other.getName() + " has withstood the effects of an enchantment! (Perception " + result.asString() + ")");
            enchantedPartyMember = false;
            model.getParty().partyMemberSay(model, other, "Uh, I feel a bit strange. What did you just say?");
            println("The enchantress is noticeably annoyed.");
            portraitSay("I said, WON'T YOU REST HERE FOR AWHILE?");
            println("The enchantress glows slightly and is obviously attempting to use some form of magic on " + other.getName() + ".");
            model.getParty().partyMemberSay(model, other, "Uh, okay. Thanks for the offer. Do you have anywhere " +
                    "we could spend the night? And some rations would be nice.");
            portraitSay("Get out.");
            model.getParty().partyMemberSay(model, other, "Beg your pardon?");
            portraitSay("Get out of my village. My spell obviously isn't working on you. You're probably too " +
                    "thick-headed for it to work. Just get out. I have a community to run.");
            leaderSay("Is that what you call this? A 'community'? Feels more like mass psychosis to me.");
            model.getParty().partyMemberSay(model, other, "Or an orgy.");
            portraitSay("Whatever. I guess I can't force you to leave, but don't disturb the villagers.");
            println("The enchantress walks away.");
        } else {
            println(other.getName() + " has been enchanted! (Perception " + result.asString() + ")");
            enchantedPartyMember = true;
            other.addCondition(new EnchantedCondition());
            model.getParty().partyMemberSay(model, other, "Yes... yes... of course we should stay here and rest...");
            portraitSay("You could stay here for a long time... with me.");
            model.getParty().partyMemberSay(model, other, "Yes... a long time... a long long time.");
            leaderSay(other.getFirstName() + ", what do you mean a long time? We can't stay here.");
            model.getParty().partyMemberSay(model, other, "But she's so lovely... Mmmmm...");
            leaderSay(other.getFirstName() + ", snap out of it!");
            portraitSay("Come with me now...");
            println(other.getName() + " wanders off with the enchantress.");
            leaderSay("What just happened?");
            model.getParty().benchPartyMembers(List.of(other));
            println(other.getName() + " has temporarily left the party.");
        }
        model.getLog().waitForAnimationToFinish();
        removePortraitSubView(model);
    }

    private void hamletLoop(Model model, GameCharacter other) {
        do {
            println("What would you like to do now?");
            model.getLog().waitForAnimationToFinish();
            List<String> options = new ArrayList<>(List.of("Explore the hamlet", "Visit Enchantress", "Go hunting", "Leave hamlet"));
            if (enchantedPartyMember) {
                options.add("Find party member");
            }
            int result = multipleOptionArrowMenu(model, 28, 15, options);
            switch (result) {
                case 0:
                    boolean over = exploreHamlet(model, other);
                    if (over) {
                        removeEnchantedPartyMember(model, other);
                        return;
                    }
                    break;
                case 1:
                    boolean eventOver = visitEnchantress(model, other);
                    if (eventOver) {
                        removeEnchantedPartyMember(model, other);
                        return;
                    }
                    break;
                case 2:
                    goHunting(model);
                    break;
                case 4:
                    helpPartyMember(model, other);
                    break;
                default :
                    model.getParty().partyMemberSay(model, model.getParty().getLeader(),
                            "We better leave this place before we're enchanted too.");
                    print("Do you want to leave the hamlet");
                    if (enchantedPartyMember) {
                        print(" (" + other.getName() + " will permanently leave the party if you do)");
                    }
                    print(". (Y/N) ");
                    if (yesNoInput()) {
                        removeEnchantedPartyMember(model, other);
                        return;
                    }
            }
            if (isWipedOut(model)) {
                return;
            }
            new EveningState(model, false, false, false).run(model);
            print("You are in the enchanted hamlet. ");
        } while (true);
    }

    private boolean isWipedOut(Model model) {
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (!gc.isDead() && !model.getParty().getBench().contains(gc)) {
                return false;
            }
        }
        return true;
    }

    private void helpPartyMember(Model model, GameCharacter other) {
        print("You find " + other.getName() + " hard at work ");
        println(MyRandom.sample(List.of("cutting wood.", "baking bread.", "painting a fence",
                "feeding some chickens.", "cleaning a stable.", "cutting a hedge.")));
        leaderSay(other.getFirstName() + "... hey...");
        if (MyRandom.randInt(2) == 0) {
            println(other.getName() + " doesn't respond.");
        } else {
            showExplicitPortrait(model, other.getAppearance(), other.getFirstName());
            portraitSay("Oh hello " + model.getParty().getLeader().getFirstName() + ". Nice day isn't it...");
            leaderSay("Come on " + other.getFirstName() + ", you got to get a grip.");
            println(other.getName() + " doesn't respond.");
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
        }
        leaderSay("Somehow we must break this spell.");
        boolean result = attemptToBreakSpell(model, 10 - dispellAttempts - (enchantmentDetected?1:0));
        dispellAttempts++;
        if (result) {
            enchantedPartyMemberReturnsToParty(model, other);
        } else {
            println(other.getName() + " keeps working, unaware of your attempts to break the spell on " + himOrHer(other.getGender()) + ".");
        }
    }

    private boolean attemptToBreakSpell(Model model, int difficulty) {
        model.getSpellHandler().acceptSpell(new DispellSpell().getName());
        boolean result = false;
        try {
            result = model.getParty().doSoloSkillCheck(model, this, Skill.MagicBlue, difficulty);
        } catch (SpellCastException sce) {
            if (sce.getSpell().getName().equals(new DispellSpell().getName())) {
                if (sce.getSpell().castYourself(model, this, sce.getCaster())) {
                    result = true;
                }
            }
        }
        model.getSpellHandler().unacceptSpell(new DispellSpell().getName());
        return result;
    }

    private void enchantedPartyMemberReturnsToParty(Model model, GameCharacter other) {
        showExplicitPortrait(model, other.getAppearance(), other.getFirstName());
        portraitSay(model.getParty().getLeader().getFirstName() + "... where am I?");
        leaderSay("Still in the hamlet. That woman put a spell on you.");
        portraitSay("Feels like I've been asleep for a month.");
        leaderSay("Come on, let's get out of here.");
        removePortraitSubView(model);
        model.getParty().unbenchAll();
        println(other.getName() + " has returned to the party!");
        enchantedPartyMember = false;
    }

    private void removeEnchantedPartyMember(Model model, GameCharacter other) {
        if (enchantedPartyMember) {
            model.getParty().unbenchAll();
            model.getParty().remove(other, false, false, 0);
        }
    }

    private void goHunting(Model model) {
        new HuntingEvent(model).doHunting(model);
        setCurrentTerrainSubview(model);
    }

    private boolean visitEnchantress(Model model, GameCharacter other) {
        println("You find the Enchantress in her hut, and approach her.");
        print("Do you want to attack the Enchantress (Y), or reason with her (N)? ");
        if (yesNoInput()) {
            return attackEnchantress(model, other);
        }
        return reasonWithEnchantress(model, other);
    }

    private boolean reasonWithEnchantress(Model model, GameCharacter other) {
        showExplicitPortrait(model, enchantress.getAppearance(), "Enchantress");
        portraitSay("What do you lot want?");
        leaderSay("You have to release these people.");
        if (persuadeDifficulty == INITIAL_PERSUADE_DIFFICULTY) {
            portraitSay("Uh-huh, and why do I have to do that? They seem to be perfectly happy the way they are.");
            if (goodArguments) {
                leaderSay("You are hurting them. Have you actually taken a look at them in a while? They're dirty, starved " +
                        "and tired. They're in bad shape.");
            } else {
                leaderSay("You just have to!");
            }
        } else {
            portraitSay("Not this again...");
        }
        leaderSay("What you're doing is wrong.");
        boolean success = model.getParty().doSoloSkillCheck(model, this, Skill.Persuade, persuadeDifficulty - (goodArguments ? 1 : 0));
        if (!success) {
            persuadeDifficulty++;
            if (enchantedPartyMember) {
                leaderSay("At least release " + other.getFirstName() + ".");
                portraitSay("I'm sorry. The village needs the extra hand. You should leave your friend here and be on your way.");

            } else {
                leaderSay("Can't we do anything to change your mind?");
                portraitSay("I'm sorry. But I think you should leave now.");
            }
            println("You leave the hut.");
            return false;
        }

        portraitSay("I... I guess you're right. When I realized I could control " +
                "people around me, I just got carried away. I'll lift the spell.");
        leaderSay("Do it.");
        println("The Enchantress closes her eyes and concentrates for a bit. Then she opens them again and smiles.");
        giveAllFiftyExp(model);
        portraitSay("There, now they're their own masters again.");
        leaderSay("You did the right thing...");
        leaderSay("What's that noise?");
        possiblyRecruitEnchantress(model, other);
        return true;
    }

    private void giveAllFiftyExp(Model model) {
        println("The enchantment on the hamlet has been lifted. Each character receives 50 XP!");
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (!model.getParty().getBench().contains(gc)) {
                model.getParty().giveXP(model, gc, 50);
            }
        }
    }

    private void possiblyRecruitEnchantress(Model model, GameCharacter other) {
        showExplicitPortrait(model, enchantress.getAppearance(), "Enchantress");
        if (enchantedPartyMember) {
            println(other.getFirstName() + " suddenly comes rushing inside the hut.");
            enchantedPartyMemberReturnsToParty(model, other);
            model.getParty().partyMemberSay(model, other, "I agree with you, but there's an angry mob outside. " +
                    "I don't think they're too happy about what's been going on here.");
        } else {
            portraitSay("Oh no, it's the villagers. They must be rather angry.");
        }
        leaderSay("Not particularly surprising.");
        portraitSay("Oh dear... this is what I was afraid of. I'm sorry but I'll have to use my spell again.");
        leaderSay("No, there's another solution.");
        new RecruitState(model, List.of(enchantress)).run(model);
        if (model.getParty().getPartyMembers().contains(enchantress)) {
            removePortraitSubView(model);
            leaderSay("Come with us. I don't think the villagers will want to tussle with us.");
            model.getParty().partyMemberSay(model, enchantress, "Thank you for accepting me.");
            println("The villagers seem very angry, but keep their distance when you gesture to your weapons.");
            leaderSay("I don't think we'll be able to come back here anytime soon.");
            model.getParty().partyMemberSay(model, enchantress, "That's alright with me. I was getting rather bored anyway.");
            if (!hasSpell(model, MindControlSpell.class)) {
                Item mindControl = new MindControlSpell();
                println("The Enchantress teaches the party a new spell, " + mindControl.getName() + ".");
            }
        } else {
            leaderSay("Face your peers. You have committed a crime and justice must be found here.");
            portraitSay("But they'll lynch me!");
            leaderSay("Don't be so dramatic...");
            println("The door swings open and several villagers storm in and grab the Enchantress.");
            portraitSay("Help help! They'll kill me!");
            leaderSay("I'm sorry. It's out of my hands.");
            println("The angry mob carries the Enchantress away, screaming all the way.");
            leaderSay("Well folks, I don't think there is anymore to see here. Let's move along.");
        }
    }

    private boolean hasSpell(Model model, Class<MindControlSpell> mindControlSpellClass) {
        for (Spell sp : model.getParty().getInventory().getSpells()) {
            if (sp.getClass().isAssignableFrom(mindControlSpellClass)) {
                return true;
            }
        }
        return false;
    }

    private boolean attackEnchantress(Model model, GameCharacter other) {
        Enemy e = new EnchantressEnemy('A');
        runCombat(List.of(e), new MansionTheme(), true);
        setCurrentTerrainSubview(model);
        if (e.isDead()) {
            println("The Enchantress lay slain.");
            leaderSay("It had to be done I guess.");
            print("You leve the hut. Outside there's a commotion as the effects of the spell is waning, and the villagers " +
                    "are coming back to their senses. ");
            if (enchantedPartyMember) {
                println("Nearby you spot " + other.getName() + ", looking as confused as the rest.");
                enchantedPartyMemberReturnsToParty(model, other);
                return true;
            }
            println("You try to explain to some of them what has happened, but you are met with confused and angry gazes, " +
                    "and quickly abandon the effort.");
            leaderSay("It will take some time for them to recuperate from this ordeal. " +
                    "We should probably leave before they blame this whole thing on us.");
            return true;
        }
        showExplicitPortrait(model, enchantress.getAppearance(), "Enchantress");
        portraitSay("You maniacs! Get out of my house!");
        println("You leave the hut.");
        model.getLog().waitForAnimationToFinish();
        persuadeDifficulty += 8;
        return false;
    }


    private boolean exploreHamlet(Model model, GameCharacter other) {
        println("You take a stroll around the hamlet. You attempt to talk to a few people, but as before, they " +
                "are distant and barely reply.");
        boolean passed = model.getParty().doSoloSkillCheck(model, this, Skill.Perception, 7);
        if (passed && !goodArguments) {
            println("You notice that the villagers have very worn and dirty clothing. Their hair and " +
                    "beards are long and unkempt and some even have blisters on their hands and feet.");
            leaderSay("I wonder how long she's kept them in this state.");
            goodArguments = true;
        }

        int successes = 0;
        int totalAttempts = 0;
        do {
            print("Do you wish to attempt to break the spell on a villager? (Y/N) ");
            if (yesNoInput()) {
                totalAttempts++;
                boolean result = attemptToBreakSpell(model, 10 - (enchantmentDetected?1:0));
                if (result) {
                    successes++;
                    boolean gender = MyRandom.flipCoin();
                    println("The villager stops what " + heOrShe(gender) + " is doing and looks at you with a confused expression. " +
                            "When " + heOrShe(gender) + " realizes what has been going on " + heOrShe(gender) + " runs off and tries " +
                            "to rouse other villagers from their trance like state.");
                } else {
                    println("You can't seem to break the spell on this villager. But perhaps there are others who are easier to help?");
                }
            } else {
                break;
            }
        } while (totalAttempts < 6 && successes < 3);

        if (successes == 3) {
            println("There is now a sizeable group of villagers who have broken free from the spell.");
            giveAllFiftyExp(model);
            println("The villagers are gathering in the " +
                    "middle of the hamlet. They seem agitated and angry. You can't but help overhearing what they are saying.");
            println("Angry Villager 1: \"It's that damn Enchantress. She's had us on our knees for months!\"");
            println("Angry Villager 2: \"We must seek vengeance! Let's go to her hut. If we take her by surprise " +
                    "perhaps she won't be able to put a spell on us again.\"");
            println("Angry Villager 1: \"Yes, let's go. She must be punished severely for this atrocity!\"");

            print("You are close to the Enchantress' hut. Do you go to warn her? (Y/N) ");
            if (yesNoInput()) {
                println("You dash to the Enchantress' hut.");
                leaderSay("Listen you, you may not want to hear this but...");
                possiblyRecruitEnchantress(model, other);
            } else {
                println("The villagers storm into the hut and promptly drag the Enchantress out.");
                leaderSay("This might get ugly...");
                println("It doesn't take long before the angry throng has beaten the Enchantress badly. " +
                        "They put her in the stocks, but nut before properly gagging her.");
                leaderSay("Well, that's one kind of justice.");
                if (enchantedPartyMember) {
                    println(other.getFirstName() + " comes wandering over, looking rather confused.");
                    enchantedPartyMemberReturnsToParty(model, other);
                }
            }
            return true;
        }

        if (totalAttempts == 6) {
            println("There aren't anymore villagers around right now.");
        }
        return false;
    }

    private static final Sprite ENC_SPRITE = CharSprite.make((char)(0xD5), MyColors.DARK_PURPLE, MyColors.GREEN, MyColors.PURPLE);

    private static class EnchantedCondition extends Condition {
        public EnchantedCondition() {
            super("Enchanted", "ENC");
        }

        @Override
        protected boolean noCombatTurn() {
            return false;
        }

        @Override
        public Sprite getSymbol() {
            return ENC_SPRITE;
        }
    }
}
