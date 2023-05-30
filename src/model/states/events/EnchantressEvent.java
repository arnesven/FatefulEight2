package model.states.events;

import model.Model;
import model.characters.EnchantressCharacter;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.Condition;
import model.enemies.BearEnemy;
import model.enemies.Enemy;
import model.enemies.WildBoarEnemy;
import model.items.spells.DispellSpell;
import model.states.*;
import util.MyRandom;
import view.MyColors;
import view.sprites.CharSprite;
import view.sprites.Sprite;

import java.util.ArrayList;
import java.util.List;

public class EnchantressEvent extends DailyEventState {

    private GameCharacter enchantress = new EnchantressCharacter();
    private boolean enchantmentDetected = false;
    private boolean enchantedPartyMember = false;
    private int goodArguments = 0;
    private int dispellAttempts = 0;

    public EnchantressEvent(Model model) {
        super(model);
    }

    @Override
    public boolean haveFledCombat() {
        return false;
    }

    @Override
    protected void doEvent(Model model) {
        if (model.getParty().isSpecialCharacterMarked(enchantress) || model.getParty().size() < 2) { // TODO: also || model.getCurrentHex().hasRoad()
            new NoEventState(model).run(model);
            return;
        }
        model.getParty().markSpecialCharacter(enchantress);

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
        leaderSay("They're all working like there's no tomorrow though. Everybody but... that girl.");
        println("Down by a creek, a woman is sitting by the water, casually watching the townspeople.");
        model.getParty().partyMemberSay(model, other, "That girl is... beautiful!");
        println("The woman has noticed the party and approaches.");
        showExplicitPortrait(model, enchantress.getAppearance(), "Beautiful Woman");
        println("The beautiful woman puts a hand on " + other.getFirstName() + "'s shoulder and speaks in a strange, resonant voice.");
        portraitSay("Greetings fair travellers. Won't you rest a while here in our peaceful village.");

        enchantressEnchants(model, other);

        do {
            println("What would you like to do now?");
            model.getLog().waitForAnimationToFinish();
            List<String> options = new ArrayList<>(List.of("Explore the hamlet", "Visit beautiful woman", "Go hunting", "Leave hamlet"));
            if (enchantedPartyMember) {
                options.add("Help enchanted party member");
            }
            int result = multipleOptionArrowMenu(model, 24, 15, options);
            switch (result) {
                case 0:
                    exploreHamlet(model);
                    break;
                case 1:
                    boolean eventOver = visitBeautifulWoman(model);
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
            new EveningState(model, false, false).run(model);
            print("You are in the enchanted hamlet. ");
        } while (true);


        //new RecruitState(model, List.of(enchantress)).run(model);
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
            portraitSay("Oh hello " + model.getParty().getLeader().getFirstName() + " nice day isn't it...");
            leaderSay("Come on " + other.getFirstName() + ", you got to get a grip.");
            println(other.getName() + " doesn't respond.");
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
        }
        leaderSay("Somehow we must break this spell.");
        model.getSpellHandler().acceptSpell(new DispellSpell().getName());
        boolean result = false;
        try {
            result = model.getParty().doSoloSkillCheck(model, this, Skill.MagicBlue, 10 - dispellAttempts);
        } catch (SpellCastException sce) {
            if (sce.getSpell().getName().equals(new DispellSpell().getName())) {
                if (sce.getSpell().castYourself(model, this, sce.getCaster())) {
                    result = true;
                }
            }
        }
        model.getSpellHandler().unacceptSpell(new DispellSpell().getName());
        dispellAttempts++;
        if (result) {
            showExplicitPortrait(model, other.getAppearance(), other.getFirstName());
            portraitSay(model.getParty().getLeader().getFirstName() + "... where am I?");
            leaderSay("Still in the hamlet. That woman put a spell on you.");
            portraitSay("Feels like I've been asleep for a month.");
            leaderSay("Come on, let's get out of here.");
            removePortraitSubView(model);
            model.getParty().unbenchAll();
            println(other.getName() + " has returned to the party!");
            enchantedPartyMember = false;
        } else {
            println(other.getName() + " keeps working, unaware of your attempts to break the spell on " + himOrHer(other.getGender()) + ".");
        }
    }

    private void removeEnchantedPartyMember(Model model, GameCharacter other) {
        if (enchantedPartyMember) {
            model.getParty().unbenchAll();
            model.getParty().remove(other, false, false, 0);
        }
    }

    private void goHunting(Model model) {
        println("You travel into a nearby copse of trees. Pretty soon you find a pig's trail and follow it.");
        List<Enemy> enemies = new ArrayList<>();
        if (MyRandom.randInt(4) == 0) {
            leaderSay("That's not a boar... that's a... BEAR!");
            enemies.add(new BearEnemy('A'));
        } else {
            leaderSay("Shhh... there... wild boar!");
            int noOfBoar = MyRandom.randInt(1, 3);
            for (int i = 0; i < noOfBoar; ++i) {
                enemies.add(new WildBoarEnemy('A'));
            }
        }
        runCombat(enemies);
    }

    private boolean visitBeautifulWoman(Model model) {
        showExplicitPortrait(model, enchantress.getAppearance(), "Beautiful Woman");
        portraitSay("I'm not in the mood to talk to you. Goodbye.");
        return false;
    }

    private void enchantressEnchants(Model model, GameCharacter other) {
        SkillCheckResult result = other.testSkill(Skill.Perception, 10, enchantmentDetected ? 2 : 0);
        if (result.isSuccessful()) {
            println(other.getName() + " has withstood the effects of an enchantment!");
            enchantedPartyMember = false;
            model.getParty().partyMemberSay(model, other, "Uh, I feel a bit strange. What did you just say?");
            println("The beautiful woman is noticeably annoyed.");
            portraitSay("I said, WON'T YOU REST A WHILE HERE?");
            println("The beautiful woman glows slightly and is obviously attempting to use some form of magic on " + other.getName() + ".");
            model.getParty().partyMemberSay(model, other, "Uh, okay. Thanks for the offer. Do you have anywhere " +
                    "we could spend the night? And some rations would be nice.");
            portraitSay("Get out.");
            model.getParty().partyMemberSay(model, other, "Beg your pardon?");
            portraitSay("Get out of my village. My spell obviously isn't working on you. You're probably too " +
                    "thick-headed for it to work. Just get out. I have a community to run.");
            leaderSay("Is that what you call this? A 'community'? Feels more like mass psychosis to me.");
            model.getParty().partyMemberSay(model, other, "Or an orgy.");
            portraitSay("Whatever. I guess I can't force you to leave, but don't disturb the townspeople.");
            println("The beautiful woman walks away.");
        } else {
            println(other.getName() + " has been enchanted!");
            enchantedPartyMember = true;
            other.addCondition(new EnchantedCondition());
            model.getParty().partyMemberSay(model, other, "Yes... yes... of course we should stay here and rest...");
            portraitSay("You could stay here for a long time... with me.");
            model.getParty().partyMemberSay(model, other, "Yes... a long time... a long long time.");
            leaderSay(other.getFirstName() + ", what do you mean a long time? We can't stay here.");
            model.getParty().partyMemberSay(model, other, "But she's so lovely... Mmmmm...");
            leaderSay(other.getFirstName() + ", snap out of it!");
            portraitSay("Come with me now...");
            println(other.getName() + " wanders off with the beautiful woman.");
            leaderSay("What just happened?");
            model.getParty().benchPartyMembers(List.of(other));
            println(other.getName() + " has temporarily left the party.");
        }
        removePortraitSubView(model);
    }


    private void exploreHamlet(Model model) {
        println("You take a stroll around the hamlet. You attempt to talk to a few people, but as before, they " +
                "are distant and barely reply.");
        boolean passed = model.getParty().doSoloSkillCheck(model, this, Skill.Perception, 7);
        if (passed && goodArguments == 0) {
            println("You notice that the townspeople have very worn and dirty clothing. Their hair and " +
                    "beards are long and unkempt and some even have blisters on their hands and feet.");
            leaderSay("I wonder how long she's kept them in this state.");
            goodArguments++;
        }
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
