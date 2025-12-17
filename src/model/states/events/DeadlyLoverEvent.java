package model.states.events;

import model.Model;
import model.characters.FemaleLongHairStyle;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.*;
import model.classes.Classes;
import model.classes.Skill;
import model.combat.loot.CombatLoot;
import model.combat.loot.FormerPartyMemberLoot;
import model.combat.loot.StandardCombatLoot;
import model.enemies.BrotherhoodCronyEnemy;
import model.items.Equipment;
import model.items.Item;
import model.items.accessories.LeatherCap;
import model.items.clothing.FancyJerkin;
import model.items.clothing.StuddedJerkin;
import model.items.weapons.*;
import model.map.UrbanLocation;
import model.races.Race;
import util.MyLists;
import util.MyRandom;
import util.MyStrings;
import view.MyColors;
import view.subviews.CollapsingTransition;
import view.subviews.ImageSubView;
import view.combat.MansionTheme;
import view.subviews.SubView;

import java.util.List;

public class DeadlyLoverEvent extends PersonalityTraitEvent {

    public DeadlyLoverEvent(Model model, PersonalityTrait personalityTrait, GameCharacter mainCharacter) {
        super(model, personalityTrait, mainCharacter);
    }

    @Override
    public boolean isApplicable(Model model) {
        return model.getCurrentHex().getLocation() instanceof UrbanLocation;
    }

    @Override
    protected void doEvent(Model model) {
        SubView alternativeSubView = new ImageSubView("theinnalt","EVENT", "The tavern.");
        CollapsingTransition.transition(model, alternativeSubView);
        GameCharacter main = getMainCharacter();
        CharacterAppearance beauty;
        if (MyRandom.flipCoin()) {
            beauty = makeFemaleBeauty(main.getRace());
        } else {
            beauty = makeMaleBeauty(main.getRace());
        }

        model.getParty().benchPartyMembers(MyLists.filter(model.getParty().getPartyMembers(),
                character -> character != getMainCharacter()));
        showEventCard("While the rest of the party members are busy with chores, " + main.getName() +
                " decides to visit the tavern for a light meal.");
        println("The tavern is mostly empty, only one patron, sitting alone at the bar, his or her face concealed by a hood.");
        println(main.getFirstName() + " sits down at a table and is soon served by the barkeep.");
        String race = main.getRace().getName().toLowerCase();
        String racePlural = race.endsWith("f") ? race.substring(0, race.length()-1).concat("ves") : race.concat("s");
        printQuote("Barkeep", "Oh, I would expect you two would want to sit together, " +
                "seeing as you're both " + racePlural + ".");
        partyMemberSay(main, "Uhm, I'm sorry. What do you mean?");
        println("The barkeep gestures to the person sitting at the bar.");
        printQuote("Barkeep", "Your kins" + (beauty.getGender()?"woman":"man") + ".");
        partyMemberSay(main, "Oh... There are plenty of " + racePlural + " in the world you know.");
        printQuote("Barkeep", "For sure. But even I, who am not of your ilk, can recognize something " +
                "special when I see it. Enjoy your meal.");
        println("Puzzled by the barkeep's remarks, " + main.getFirstName() + " finishes " +
                hisOrHer(main.getGender()) + " meal quickly and then gently meanders over to the bar.");
        partyMemberSay(main, "Uhm, hello there.");
        println("The " + race + " pulls down " + hisOrHer(beauty.getGender()) + " hood, and " + main.getFirstName() +
                " is immediately struck by " + hisOrHer(beauty.getGender()) + " beauty.");

        model.getLog().waitForReturn();
        showExplicitPortrait(model, beauty, MyStrings.capitalize(race));
        portraitSay("Oh, hello.");

        partyMemberSay(main, "The bartender insisted we interact, seeing as we're both " + racePlural + ".");
        portraitSay("How silly. Doesn't " + heOrShe(MyRandom.flipCoin()) +
                " know that there are other " + racePlural + " in the world?");
        partyMemberSay(main, "You know, I said the exact same thing!");
        portraitSay("What a coincidence!");
        partyMemberSay(main, "Look. I'm with a group, but they've seem to be busy elsewhere and I have some time to kill. " +
                "Would it be too forward of me to buy you a drink?");
        portraitSay("Oh, not at all. But let's move to a booth where we can have an " +
                "undisturbed conversation about the rarity of " + racePlural + ".");
        partyMemberSay(main, "Hehe, that sounds lovely.");
        println(main.getName() + " and the attractive patron talk for some time. " +
                "Talking and flirting without restraint as evening approaches and the tavern " +
                "slowly fills up with other guests. They both have several drinks, their faces inching closer " +
                "together as they talk.");
        portraitSay("Hey. I don't usually say this, but I feel a real connection with you.");
        partyMemberSay(main, "What a coincidence, I feel the same way about you.");
        portraitSay("I just love talking with you. You're so funny and smart.");
        partyMemberSay(main, "Thank you. My friends don't usually notice.");
        portraitSay("That's too bad. You seem to have a lot to share, and you have a wonderful personality! " +
                "But it's getting awfully loud in here. I can barely hear what you're saying.");
        partyMemberSay(main, "If only there were somewhere more quite we could go.");
        portraitSay("I actually have a room at this tavern. Why don't we take our drinks, " +
                "and continue the conversation upstairs?");
        print("Does " + main.getFirstName() + " take the " + race + " up on " + hisOrHer(beauty.getGender()) + " offer? (Y/N) ");
        if (!yesNoInput()) {
            partyMemberSay(main, "That would have been great, but uh, I see my friends over there. Got to go now. Bye!");
            println(main.getFirstName() + " leaves the " + race + " at the table. " +
                    heOrSheCap(beauty.getGender()) + " is visibly annoyed.");
            model.getParty().unbenchAll();
            return;
        }
        partyMemberSay(main, "That sounds like a great idea. Lead the way.");
        println("The " + race + " gets up and gently takes " + main.getFirstName() + "'s hand, then leads " +
                himOrHer(main.getGender()) + " up the stairs and into one of the tavern's rooms. The room does not have much, " +
                "just a bed, a chair and a small table. " + main.getFirstName() + " enters the room first and the " +
                race + " closes the door behind them. Before " + main.getFirstName() + " has time to speak the " +
                race + " has taken off " + hisOrHer(beauty.getGender()) + " clothes.");
        model.getLog().waitForAnimationToFinish();
        beauty.setSpecificClothing(new SwimAttire());
        partyMemberSay(main, "Oh... wow... I...");
        print("Does " + main.getFirstName() + " make an excuse and leave? (Y/N) ");
        if (yesNoInput()) {
            partyMemberSay(main, "You know what, I don't really think I'm ready for this. " +
                    "It was nice talking and all, but I think I'm going to go back downstairs.");
            println("The " + race + " comes close and pushes " + himOrHer(beauty.getGender()) + "self against " + main.getFirstName() + ".");
            portraitSay("Are you sure? I think we both want this.");
            print("Does " + main.getFirstName() + " give in? (Y/N) ");
            if (!yesNoInput()) {
                partyMemberSay(main, "Yes, I'm sure. Now excuse me. It was nice meeting you.");
                println(main.getFirstName() + " leaves the persistent " + race + ".");
                model.getParty().unbenchAll();
                return;
            }
            partyMemberSay(main, "You're right. I do want this.");
        }
        main.setSpecificClothing(new SwimAttire());
        println(main.getFirstName() + " takes off " + hisOrHer(main.getGender()) +
                " clothes. Then the two " + racePlural + " waste no more time...");
        print("Press enter to continue.");
        waitForReturn();
        removePortraitSubView(model);
        setCurrentTerrainSubview(model);
        model.getParty().unbenchAll();
        model.getParty().benchPartyMembers(List.of(main));
        println("Meanwhile, the rest of the party has been looking for " + main.getFirstName() + ".");
        leaderSay("Where did " + heOrShe(main.getGender()) + " go? " + heOrSheCap(main.getGender()) +
                " said " + heOrShe(main.getGender()) + " was going to the tavern " +
                "but we've looked there, and everywhere else for " + himOrHer(main.getGender()) + ".");
        while (true) {
            leaderSay("We're just going to have to ask around at the tavern again if " +
                    "anybody's seen " + himOrHer(main.getGender()) + ".");
            boolean success = model.getParty().doCollaborativeSkillCheck(model, this, Skill.SeekInfo, 4);
            if (success) {
                break;
            }
        }
        println("While not learning anything of " + main.getName() + "'s whereabouts. The party " +
                "learns about a local gang who regularly lures adventurers " +
                "with one of their members, who happens to be particularly attractive. The gangsters then " +
                "ambushes the unsuspecting adventurer and robs them blind. The gang apparently bribes the " +
                "barkeep to let them run their scheme in the tavern.");
        leaderSay("That " + main.getFirstName() + ", " + heOrShe(main.getGender()) +
                " is such a romantic. I wouldn't be surprised if " + heOrShe(main.getGender()) +
                " has fallen prey to those low lives...");
        print("Press enter to continue.");
        waitForReturn();

        model.getParty().unbenchAll();
        model.getParty().benchPartyMembers(MyLists.filter(model.getParty().getPartyMembers(),
                character -> character != getMainCharacter()));
        CollapsingTransition.transition(model, alternativeSubView);
        println(main.getFullName() + " is awoken by the sound of footsteps enter the room.");
        printQuote("Gruff Voice", "Okay pal. Time to wake up now.");
        println("There are several cloaked figures standing in the room. The attractive " + race + " is one of them.");
        partyMemberSay(main, "What is the meaning of this?");
        beauty.setClass(Classes.THF);
        showExplicitPortrait(model, beauty, MyStrings.capitalize(race));
        partyMemberSay(main, "You... you... who are all these people?");
        portraitSay("These are my friends!");
        partyMemberSay(main, "Uhm, they look like bandits. You... also kind of look like a bandit... Are you?");
        portraitSay("Bingo. I'm sorry " + main.getFirstName() + ", I think we really did have a connection.");
        partyMemberSay(main, "Hey, where is my gear? Where are my clothes?");
        model.getLog().waitForAnimationToFinish();
        FormerPartyMemberLoot mainsLoot = new FormerPartyMemberLoot(main);
        main.setEquipment(new Equipment());
        printQuote("Gangster", "They belong to us now pal. And now I'm afraid we're going " +
                "to have to kill you. No loose ends, you know.");
        partyMemberSay(main, "This is an outrage. I'm not sure what I feel worse about, the fact that I " +
                "was just fooled and swindled by a pretty face, or the fact that I'm about to be murdered!");
        portraitSay("Well, we'll put you out of your misery then. Good bye!");
        partyMemberSay(main, "Wait! I have powerful friends, they'll avenge me!");
        portraitSay("Oh right, your 'party members'. And where would these powerful fellows happen to be?");
        model.getParty().unbenchAll();
        leaderSay("We're right here!");
        runCombat(List.of(new GangsterEnemy('A',  model), new GangsterEnemy('A',  model),
                new GangsterEnemy('A', beauty.getRace(), mainsLoot),
                new GangsterEnemy('A',  model), new GangsterEnemy('A',  model)),
                new MansionTheme(), true);
        if (model.getParty().isWipedOut()) {
            return;
        }
        setCurrentTerrainSubview(model);
        if (haveFledCombat()) {
            println("The party escapes the tavern and quickly gets out of town.");
        }
        if (model.getParty().getLeader() != main) {
            leaderSay("I hope " + heOrShe(beauty.getGender()) + " was worth it " + main.getFirstName() + ".");
        }
        if (!main.isDead()) {
            partyMemberSay(main, "I guess I'm just a hopeless romantic.");
            main.removeSpecificClothing();
        }
    }

    private AdvancedAppearance makeFemaleBeauty(Race race) {
        MyColors hairColor = MyRandom.sample(List.of(MyColors.DARK_GRAY, MyColors.DARK_RED, MyColors.RED,
                MyColors.ORANGE, MyColors.GOLD, MyColors.LIGHT_YELLOW));
        HairStyle hair = MyRandom.sample(List.of(
                new HairStyle3x2(0x164, true, true, true, true, 0x09, 0x07, 0x33, 0x34, "Combed/Long"),
                new FemaleLongHairStyle(0x9, 0x03, "Long #2"),
                new ShortFemaleHair(0x164, 0x13, "Short #2")
        ));
        AdvancedAppearance app = new AdvancedAppearance(race, true, hairColor,
                1, 0xB, new SmallEyesWithBangs(),
                hair, new Beard(0xB, 0x00, false));
        app.setMascaraColor(MyRandom.sample(List.of(MyColors.BLUE, MyColors.LIGHT_RED, MyColors.DARK_GREEN)));
        app.setLipColor(MyRandom.sample(List.of(MyColors.PURPLE, MyColors.RED, MyColors.DARK_RED, MyColors.LIGHT_RED)));
        app.setEars(new Ears("Hair", 0xA5, 0xB5));
        if (race.id() == Race.NORTHERN_HUMAN.id() || race.id() == Race.SOUTHERN_HUMAN.id()) {
            app.setNeck(new SlenderNeck());
        }
        app.addFaceDetail(new TriangularEarringsDetail());
        app.setDetailColor(MyColors.GOLD);
        app.setClass(Classes.BEAUTY);
        return app;
    }

    private AdvancedAppearance makeMaleBeauty(Race race) {
        MyColors hairColor = MyRandom.sample(List.of(MyColors.BROWN, MyColors.GRAY_RED, MyColors.DARK_GRAY, MyColors.GOLD));
        HairStyle hair = MyRandom.sample(List.of(
            new WavyHairStyle(), new BigHairStyle(), new BoyCutHairStyle()
            ));
        Beard beard = MyRandom.sample(List.of(
                new Beard(0, 0x00, false),
                new Beard(0xD, 0x00, false),
                new Beard(0xE, 0x00, false)
        ));
        AdvancedAppearance app = new AdvancedAppearance(race, false, hairColor,
                0, 0xE, new NormalSmallEyes(),
                hair, beard);
        app.setClass(Classes.BEAUTY);
        return app;
    }

    private static class GangsterEnemy extends BrotherhoodCronyEnemy {
        private final CombatLoot loot;

        public GangsterEnemy(char a, Model model) {
            super(a);
            setName("Gangster");
            this.loot = new GangsterLoot(model);
        }

        public GangsterEnemy(char a, Race race, CombatLoot loot) {
            super(a, race);
            setName("Attractive Gangster");
            this.loot = loot;
        }

        @Override
        public CombatLoot getLoot(Model model) {
            return loot;
        }
    }

    private static class GangsterLoot extends StandardCombatLoot {
        public GangsterLoot(Model model) {
            super(model, 0);
            List<Item> its = getItems();
            its.add(MyRandom.sample(List.of(new Scepter(), new Kukri(), new OrcishKnife(), new Wakizashi(),
                    new Club(), new Hatchet(), new LeatherCap(), new StuddedJerkin(), new FancyJerkin())));
        }
    }
}
