package model.states.events;

import model.GameStatistics;
import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.FacialExpression;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.enemies.Enemy;
import model.items.Equipment;
import model.items.accessories.Accessory;
import model.items.accessories.ChainGloves;
import model.items.accessories.LargeShield;
import model.items.accessories.LeatherGloves;
import model.items.clothing.Clothing;
import model.items.clothing.LeatherArmor;
import model.items.weapons.*;
import model.map.WorldBuilder;
import model.map.WorldType;
import model.races.Race;
import model.states.DailyEventState;
import model.states.GameState;
import util.MyRandom;
import util.MyStrings;
import view.subviews.PortraitSubView;

import java.util.List;

public class FedUpAdventurerEvent extends GeneralInteractionEvent {

    private static final String GOT_THIS_EVENT = "MetFedUpAdventurer";
    private final CharacterClass cls;
    private final GameCharacter victim;

    public FedUpAdventurerEvent(Model model) {
        super(model, "Talk to", MyRandom.randInt(15, 45), true);
        cls = MyRandom.sample(List.of(Classes.CAP, Classes.MAR, Classes.BBN, Classes.MIN, Classes.PAL));
        CharacterAppearance app = PortraitSubView.makeRandomPortrait(cls);
        Weapon w;
        Accessory a = new LargeShield();
        if (cls.id() == Classes.MAR.id()) {
            w = new HuntersBow();
            a = new ChainGloves();
        } else if (cls.id() == Classes.MIN.id()) {
            w = new BattleAxe();
        } else {
            w = new Broadsword();
        }
        victim = new GameCharacter("Adventurer", "", app.getRace(), cls, app,
                new Equipment(w, new LeatherArmor(), a));
    }

    public static DailyEventState generateEvent(Model model) {
        if (!model.getSettings().getMiscFlags().containsKey(GOT_THIS_EVENT) &&
                WorldBuilder.isInStartingArea(model) && model.getDay() < 10 && MyRandom.rollD10() == 10) {
            return new FedUpAdventurerEvent(model);
        }
        return null;
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        String raceName = victim.getRace().getBasicName();
        println("You encounter " + MyStrings.aOrAn(raceName) + " " + raceName + ". " + heOrSheCap(victim.getGender()) +
                " looks rather haggard, as if " + heOrShe(victim.getGender()) + " been traveling in " +
                "rough conditions or for a long time.");
        showExplicitPortrait(model, victim.getAppearance(), victim.getName());
        getPortraitSubView().setFacialExpression(FacialExpression.sad);
        println("The " + raceName + " looks at the party tiredly.");
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        model.getSettings().getMiscFlags().put(GOT_THIS_EVENT, true);
        leaderSay("Are you alright?");
        portraitSay("No. Not at all... I'm completely exhausted. I think I'd better sit down for a minute.");
        String raceName = victim.getRace().getBasicName();
        println("The " + raceName + " unstraps " + hisOrHer(victim.getGender())+ " pack and sits down on the ground.");
        leaderSay("What happened to you?");
        portraitSay("I joined up with this party, 'Bolero's Company', about a month ago.");
        leaderSay("Bolero? Why does that sound familiar?");
        portraitSay("Jim Bolero is the leader. But we had a real hard time finding good work, and the mood in the team was not great.");
        randomSayIfPersonality(PersonalityTrait.irritable, List.of(), "I know what that's like", FacialExpression.disappointed);
        portraitSay("There was this one Half-Orc, Grok, who kept bothering Jim about taking over as leader of the company, even though he had just joined himself.", FacialExpression.disappointed);
        leaderSay("How rude!");
        randomSayIfPersonality(PersonalityTrait.diplomatic, List.of(), "Not a very diplomatic fellow.", FacialExpression.relief);
        portraitSay("Well we finally got offered this quest. It was a bit of a hike to get there but we figured we could make it, " +
                "despite Grok's whinging. After three days in the wilderness we found the site, an abandoned temple.");
        randomSayIfPersonality(PersonalityTrait.brave, List.of(), "Sounds exciting. What happened next?");
        portraitSay("We were just about to head inside, when Grok picks another fight with Jim. Something about his wages. " +
                "Jim tried to explain that there just wasn't any extra coin to spare, but Grok refused to listen and decided to quit the party, right there on the spot!", FacialExpression.angry);
        randomSayIfPersonality(PersonalityTrait.lawful, List.of(), "That was not very fair of him.");
        randomSayIfPersonality(PersonalityTrait.cold, List.of(), "It was his decision.");
        portraitSay("A couple of the others in the party decided it wasn't worth trying to hunt for treasures in the temple ruins without Grok, so they decided to leave as well. " +
                "Now it was just Jim, Cally and me.");
        leaderSay("What did you do?");
        portraitSay("We decided to spend the night there. So we set up camp, and talked about our options. " +
                "We could head back to town empty-handed, or try our luck in the ruins at half strength.");
        leaderSay("And you pushed your luck. Foolishly.");
        portraitSay("You don't know how desperate we were! We barely had enough coin for rations!");
        leaderSay("Sometimes it's better to starve than to risk your life");
        portraitSay("I'll say. The temple guardians gave us a good thrashing! We were lucky to escape with our skins.", FacialExpression.afraid);
        leaderSay("Adventuring is tough business...");
        portraitSay("No kidding, and guess what? The next night we were ambushed by wolves!", FacialExpression.disappointed);
        leaderSay("Yikes.", FacialExpression.surprised);
        portraitSay("Jim and Cally ran in opposite directions. For a few seconds I actually considered driving " +
                "them away from our tent, but it was hopeless. I grabbed the last of our rations, then I fled too.");
        leaderSay("Smart choice.");
        portraitSay("I've been wandering around, trying to find my way for a few days now. " +
                "Do you know the way to the nearest settlement?");
        leaderSay("Town is over in that direction, not too far.");
        print("Do you offer to let the adventurer join your party? (Y/N) ");
        if (yesNoInput()) {
            leaderSay("What about joining up with my crew?");
        } else {
            leaderSay("What's next then, going to find another group of adventurers?");
        }
        portraitSay("Not a chance! I'm done with adventuring. I should have listened to my mother... In fact, I don't " +
                "even know why I keep lugging this gear around. You look like the adventuring type. Here, you can have it.");
        leaderSay("Are you sure, it'll probably fetch you a few gold coins in town?");
        portraitSay("Honestly, I'm so tired I don't feel like I can carry this stuff another step.");
        Weapon w = victim.getEquipment().getWeapon();
        Clothing c = victim.getEquipment().getClothing();
        Accessory a = victim.getEquipment().getAccessory();
        println("You got " + w.getName() + ", " + c.getName() +
                " and " + a.getName() + " from the adventurer.");
        leaderSay("I'm sure these things will come in handy. So long friend.");
        w.addYourself(model.getParty().getInventory());
        c.addYourself(model.getParty().getInventory());
        a.addYourself(model.getParty().getInventory());
        portraitSay("Goodbye.");
        return false;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm an adventurer. Or, I was. It's time to get off the road and settle down.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        return victim;
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        return List.of();
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_TO_DEATH;
    }
}
