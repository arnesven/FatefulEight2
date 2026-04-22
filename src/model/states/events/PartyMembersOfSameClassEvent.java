package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.FacialExpression;
import model.characters.preset.PresetCharacter;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import model.items.spells.Spell;
import model.items.weapons.BladedWeapon;
import model.items.weapons.BluntWeapon;
import model.items.weapons.PolearmWeapon;
import model.items.weapons.Weapon;
import model.map.locations.*;
import model.states.DailyEventState;
import util.MyLists;
import util.MyRandom;

import java.util.*;

public class PartyMembersOfSameClassEvent extends DailyEventState {

    private interface SubEvent {
        void perform(Model model, List<GameCharacter> talkers);
    }

    private Map<CharacterClass, SubEvent> convoMap = new HashMap<>();

    public PartyMembersOfSameClassEvent(Model model) {
        super(model);
        convoMap.put(Classes.AMZ, this::amazonTalk);
        convoMap.put(Classes.ART, this::artisanTalk);
        convoMap.put(Classes.ASN, this::assassinTalk);
        convoMap.put(Classes.BBN, this::barbarianTalk);
        convoMap.put(Classes.BRD, this::bardTalk);
        convoMap.put(Classes.BKN, this::blackKnightTalk);
        convoMap.put(Classes.CAP, this::captainsTalk);
        convoMap.put(Classes.FOR, this::foresterTalk);
        convoMap.put(Classes.DRU, this::druidTalk);
        convoMap.put(Classes.MAG, this::magicianTalk);
        convoMap.put(Classes.MAR, this::marksmanTalk);
        convoMap.put(Classes.MIN, this::minerTalk);
        convoMap.put(Classes.NOB, this::nobleTalk);
        convoMap.put(Classes.PAL, this::paladinTalk);
        convoMap.put(Classes.PRI, this::priestTalk);
        convoMap.put(Classes.SOR, this::sorcererTalk);
        convoMap.put(Classes.SPY, this::spyTalk);
        convoMap.put(Classes.THF, this::thiefTalk);
        convoMap.put(Classes.WIZ, this::wizardTalk);
        convoMap.put(Classes.WIT, this::witchTalk);
    }

    @Override
    protected void doEvent(Model model) {
        Map<CharacterClass, List<GameCharacter>> freqs = countFreqs(model);
        List<Map.Entry<CharacterClass, List<GameCharacter>>> entries = new ArrayList<>(freqs.entrySet());
        var candidates = MyLists.filter(entries,
                e -> e.getValue().size() > 1 &&
                                                convoMap.containsKey(e.getKey()));
        if (candidates.isEmpty()) {
            new NoEventState(model).run(model);
            return;
        }
        Collections.shuffle(candidates);
        var chosen = candidates.getFirst();
        var talkers = chosen.getValue();
        Collections.shuffle(talkers);
        convoMap.get(chosen.getKey()).perform(model, chosen.getValue());
        boolean doNegative = false;
        if (talkers.size() > 2) {
            GameCharacter extra = talkers.get(1);
            if (model.getParty().size() > talkers.size()) {
                println(extra.getName() + " joins in the conversation and offers " +
                        hisOrHer(extra.getGender()) + " insights, but the rest of the party feels " +
                        "left out and is slightly annoyed.");
                doNegative = true;
            } else { // all are talkers.
                println(extra.getName() + " joins in the conversation and offers " +
                        hisOrHer(extra.getGender()) + " insights.");
            }
        }
        println(MyLists.commaAndJoin(talkers, GameCharacter::getFirstName) + " have bonded.");
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            for (GameCharacter other : model.getParty().getPartyMembers()) {
                if (gc != other) {
                    if (talkers.contains(gc) && talkers.contains(other)) {
                        gc.addToAttitude(other, 10);
                    } else if (talkers.contains(other) && !talkers.contains(gc) && doNegative) {
                        gc.addToAttitude(other, -5);
                    }
                }
            }
        }
    }

    private Map<CharacterClass, List<GameCharacter>> countFreqs(Model model) {
        Map<CharacterClass, List<GameCharacter>> result = new HashMap<>();
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (!result.containsKey(gc.getCharClass())) {
                result.put(gc.getCharClass(), new ArrayList<>());
            }
            result.get(gc.getCharClass()).add(gc);
        }
        return result;
    }

    private void amazonTalk(Model model, List<GameCharacter> talkers) {
        GameCharacter first = talkers.getFirst();
        GameCharacter second = talkers.getLast();
        partyMemberSay(first, "Do you enjoy riding " + second.getFirstName() + "?");
        partyMemberSay(second, "Very much. It's a good way to explore the outdoors. I'm very much a nature person.");
        partyMemberSay(first, "We have that in common then! What about fishing and hunting?");
        partyMemberSay(second, "Both wonderful activities. Did I every tell you about the time I " +
                "tracked an albino Elk through the Bogdown marshlands?", FacialExpression.questioning);
        partyMemberSay(first, "I want to hear all about it!");
        println(first.getName() + " and " + second.getName() + " spend the better part of the afternoon talking about " +
                 "the great outdoors.");
    }

    private void artisanTalk(Model model,  List<GameCharacter> talkers) {
        GameCharacter first = talkers.getFirst();
        GameCharacter second = talkers.getLast();
        partyMemberSay(first, "What do you have there " + second.getFirstName() + "?", FacialExpression.questioning);
        partyMemberSay(second, "Oh, just a little piece of wood I've been whittling. It's nothing special. I was just killing time.", FacialExpression.relief);
        println(second.getFirstName() + " puts the item aside.");
        partyMemberSay(first, "No wait, let me see that. Wow, you are pretty good at this!", FacialExpression.surprised);
        partyMemberSay(second, "Thank you. Do you enjoy crafting?");
        partyMemberSay(first, "Of course! I love working with my hands.", FacialExpression.relief);
        println(first.getName() + " and " + second.getName() + " spend the better part of the afternoon talking about " +
                "their respective creative projects.");
    }

    private void assassinTalk(Model model, List<GameCharacter> talkers) {
        GameCharacter first = talkers.getFirst();
        GameCharacter second = talkers.getLast();
        println(first.getName() + " is brooding. " + second.getName() + " approaches " + hisOrHer(first.getGender()) + ".");
        partyMemberSay(second, "What's the matter " + first.getName() + "?", FacialExpression.questioning);
        partyMemberSay(first, "That last fight...", FacialExpression.disappointed);
        partyMemberSay(second, "What about it? We handled it pretty good. You held your own.");
        partyMemberSay(first, "It's not that. I just think we've could have approached the situation better. " +
                "If we'd had taken a more stealthy approach, we could have taken " +
                "out our enemies without them knowing what hit then.");
        partyMemberSay(second, "I agree.");
        partyMemberSay(first, "You do?", FacialExpression.questioning);
        partyMemberSay(second, "Of course. I was actually thinking the same thing after the fight...");
        println(first.getName() + " and " + second.getName() + " spend the better part of the afternoon talking about " +
                "how to best liquidate enemies from the shadows.");
    }

    private void barbarianTalk(Model model, List<GameCharacter> talkers) {
        GameCharacter first = talkers.getFirst();
        GameCharacter second = talkers.getLast();
        println(first.getName() + " is doing some push-ups.");
        partyMemberSay(second, "What are you doing " + first.getFirstName() + "?", FacialExpression.questioning);
        partyMemberSay(first, "Exercising. I need to keep my body in shape.", FacialExpression.disappointed);
        partyMemberSay(second, "Is our adventuring business not enough of a physical challenge for you?", FacialExpression.questioning);
        partyMemberSay(first, "Not at all, that's why I need to do this. Can you move? You're in my way.");
        if (second.hasPersonality(PersonalityTrait.rude)) {
            partyMemberSay(second, "You move, I have a right to stand here.", FacialExpression.disappointed);
            partyMemberSay(first, "Oh grow up.", FacialExpression.disappointed);
        } else {
            partyMemberSay(second, "Sorry.", FacialExpression.disappointed);
        }
        partyMemberSay(first, "Anyway, what I really need is a sparring partner. Somebody to train my fighting style with.");
        partyMemberSay(second, "I'll do it. I need the training too.");
        println(first.getName() + " and " + second.getName() + " spend the better part of the afternoon training with their weapons. " +
                "Afterwards they have a conversation about how best to set up a training schedule that would benefit the both of them.");
    }

    private void bardTalk(Model model, List<GameCharacter> talkers) {
        GameCharacter first = talkers.getFirst();
        GameCharacter second = talkers.getLast();
        println(first.getName() + " is offering to tell a story for the party.");
        partyMemberSay(first, "There once was a magical princess who was trapped in a tall...");
        partyMemberSay(second, "'Spire'?", FacialExpression.questioning);
        partyMemberSay(first, "Uhm... I was going to say tower.");
        partyMemberSay(second, "I think 'Spire' sounds better.");
        partyMemberSay(first, "Can you let me tell the story?");
        if (second.hasPersonality(PersonalityTrait.rude)) {
            partyMemberSay(second, "Naw. You got to tell it right.", FacialExpression.wicked);
            partyMemberSay(first, "Oh grow up.", FacialExpression.disappointed);
        } else {
            partyMemberSay(second, "Sorry.", FacialExpression.relief);
        }
        partyMemberSay(first, "Anyway... the magical princess had been trapped by an evil sorcerer whose name was...");
        partyMemberSay(second, "'Marduk'?", FacialExpression.questioning);
        partyMemberSay(first, "Actually, his name was Johnathan...");
        partyMemberSay(second, "Johnathan? That doesn't sound very sorcererish.", FacialExpression.disappointed);
        partyMemberSay(first, "'Sorcererish'? Is that even a word?", FacialExpression.disappointed);
        GameCharacter sorc = MyLists.find(model.getParty().getPartyMembers(),
                gc -> !talkers.contains(gc) && gc.getCharClass().id() == Classes.SOR.id());
        if (sorc != null) {
            partyMemberSay(sorc, "It's not.", FacialExpression.disappointed);
        }
        partyMemberSay(second, "Of course it is. Here, let me use it in a sentence...");
        println(first.getName() + " and " + second.getName() +
                " spend the better part of the afternoon debating the finer nuances of the spoken word.");
    }

    private void blackKnightTalk(Model model,  List<GameCharacter> talkers) {
        GameCharacter first = talkers.getFirst();
        GameCharacter second = talkers.getLast();
        println(first.getName() + " is brooding. " + second.getName() + " approaches " + hisOrHer(first.getGender()) + ".");
        partyMemberSay(second, "What's the matter " + first.getName() + "?");
        partyMemberSay(first, "That last fight...");
        partyMemberSay(second, "What about it? We handled it pretty good. You held your own.");
        partyMemberSay(first, "We were pulling our punches. Sometimes I think we should just charge in. " +
                "I prefer a quicker, more direct fight.", FacialExpression.disappointed);
        partyMemberSay(second, "I agree. Next time, let's not hold back.");
        partyMemberSay(first, "I'm relieved somebody else feels the same way. " +
                "Do you mind if I pick your brain about some ideas of mine? I think we can seriously improve our combat tactics.");
        partyMemberSay(second, "Fire away!");
        println(first.getName() + " and " + second.getName() + " spend the better part of the afternoon talking about " +
                "hand-to-hand combat.");
    }

    private void captainsTalk(Model model, List<GameCharacter> talkers) {
        GameCharacter first = talkers.getFirst();
        GameCharacter second = talkers.getLast();
        partyMemberSay(first, "Hey " + second.getFirstName() + ", ever been in a battle?", FacialExpression.questioning);
        partyMemberSay(second, "Yes, once. I was in the battle of Edelstone Hill.");
        partyMemberSay(first, "Edelstone? I've heard of that! That was some particularly clever tactics...", FacialExpression.surprised);
        println(first.getName() + " and " + second.getName() + " spend the better part of the afternoon talking about " +
                "military tactics, army gear and the good old days in the army.");
    }

    private void foresterTalk(Model model, List<GameCharacter> talkers) {
        GameCharacter first = talkers.getFirst();
        GameCharacter second = talkers.getLast();
        partyMemberSay(first, "Hey " + second.getFirstName() + ", have you ever travelled in the woods north of Ackerville?", FacialExpression.questioning);
        String homeTown;
        if (second instanceof PresetCharacter) {
            homeTown = second.getHomeTown(model).getPlaceName();
        } else {
            homeTown = MyRandom.sample(model.getWorld().getLordLocations()).getPlaceName();
        }
        partyMemberSay(second, "Many a time. It's beautiful there. Although it doesn't compare to the forests near " + homeTown + ".");
        partyMemberSay(first, "What makes them so special?");
        println(first.getName() + " and " + second.getName() + " spend the better part of the afternoon talking about " +
                "the flora and fauna of various nature areas.");
    }

    private void druidTalk(Model model, List<GameCharacter> talkers) {
        GameCharacter first = talkers.getFirst();
        GameCharacter second = talkers.getLast();
        println(first.getName() + " is crouching down. " + heOrSheCap(first.getGender()) + " is examining the ground.");
        partyMemberSay(second, "Have you found something " + first.getFirstName() + "?", FacialExpression.questioning);
        partyMemberSay(first, "Paw prints. It could be an ermine, or possibly a large ferret.");
        partyMemberSay(second, "Interesting. Do you want to know an interesting fact about ferrets?", FacialExpression.relief);
        partyMemberSay(first, "Yes, tell me.");
        println(first.getName() + " and " + second.getName() + " spend the better part of the afternoon talking about " +
                " wild animals and their behaviors.");
    }

    private void magicianTalk(Model model, List<GameCharacter> talkers) {
        GameCharacter first = talkers.getFirst();
        GameCharacter second = talkers.getLast();
        println(first.getName() + " is performing a magic trick.");
        model.getLog().waitForAnimationToFinish();
        first.testSkill(model, Skill.MagicAny);
        println(first.getFirstName() + " holds a coin in one hand, " + hisOrHer(first.getGender()) + " other hand is empty.");
        println("First " + heOrShe(first.getGender()) + " lets the coin fall from one hand to the other, catching it in " + hisOrHer(first.getGender()) + " fist.");
        println("Then " + heOrShe(first.getGender()) + " lets " + hisOrHer(first.getGender()) + " fists swap places. But when " + heOrShe(first.getGender()) +
                " opens the upper fist, nothing falls out!");
        if (model.getParty().size() > talkers.size()) {
            println("Gasps are heard among " + first.getFirstName() + "'s fellow party members. 'Where did it go?' somebody whispers.");
        }
        println("Finally, " + first.getFirstName() + " opens up the lower fist, and the coin miraculously falls upward and lands in the palm above.");
        println("The other party members admit they are a little impressed, and try to guess how the trick is done. But " + 
                first.getFirstName() + " will not tell " + hisOrHer(first.getGender()) + " secret.");
        println("Later, " + second.getName() + " approaches " + first.getFirstName() + ".");
        partyMemberSay(second, "That was a nice trick earlier.", FacialExpression.relief);
        partyMemberSay(first, "Thank you. I'm glad you enjoyed it.");
        partyMemberSay(second, "I know how you did it, of course.", FacialExpression.wicked);
        partyMemberSay(first, "Is that so? Well, I would imagine somebody like you know a little of slight of hand.");
        partyMemberSay(second, "Actually, if you don't mind, I have a few suggestions that would make the trick even better.");
        partyMemberSay(first, "Really? Do tell!", FacialExpression.questioning);
        println(first.getName() + " and " + second.getName() + " spend the better part of the afternoon talking about " +
                " parlor tricks and entertainment magic.");
    }

    private void marksmanTalk(Model model, List<GameCharacter> talkers) {
        GameCharacter first = talkers.getFirst();
        GameCharacter second = talkers.getLast();
        println(first.getName() + " is practicing archery at a fallen tree stump.");
        partyMemberSay(second, "Do you mind if I join you?", FacialExpression.questioning);
        partyMemberSay(first, "Not at all.");
        partyMemberSay(second, "Five obols to the one who can hit that black root? I'll let you go first.");
        partyMemberSay(first, "You're on.");
        println(first.getFirstName() + " and " + second.getFirstName() + " have a little archery competition.");
        GameCharacter winner = first.getUnmodifiedRankForSkill(Skill.Bows) > second.getUnmodifiedRankForSkill(Skill.Bows) ?
                first : second;
        println("They are both good shots, but " + winner.getFirstName() + " wins their bet.");
        partyMemberSay(winner, "If you want, I can give you a few tips.");
        GameCharacter loser = first == winner ? second : first;
        partyMemberSay(loser, "Sure.");
        println(first.getName() + " and " + second.getName() + " spend the better part of the afternoon talking about " +
                " bows and archery.");
    }

    private void minerTalk(Model model, List<GameCharacter> talkers) {
        GameCharacter first = talkers.getFirst();
        GameCharacter second = talkers.getLast();
        partyMemberSay(first, "Hey " + second.getFirstName() + ", did you ever mine any gems?");
        partyMemberSay(second, "Would I be doing this if I had?", FacialExpression.questioning);
        partyMemberSay(first, "Maybe if you got some small ones...");
        partyMemberSay(second, "I was close to getting a sapphire once. But the rock shattered. " +
                "I was *this* close to getting stinking rich.", FacialExpression.disappointed);
        partyMemberSay(first, "Really? I had a similar experience!");
        partyMemberSay(second, "Was it a big sapphire?");
        partyMemberSay(first, "It was an emerald, and yeah it was big. But the rock didn't shatter.");
        partyMemberSay(second, "So what happened?", FacialExpression.questioning);
        partyMemberSay(first, "The gem was even a little loose. But then a huge cave spider came crawling out of a nook, " +
                "and I had to get out of there.");
        partyMemberSay(second, "What a shame.");
        partyMemberSay(first, "It really was. That rock would have got me a villa in " + EbonshireTown.NAME +
                ", right on the water.");
        partyMemberSay(second, "That reminds me of another time...");
        println(first.getName() + " and " + second.getName() + " spend the better part of the afternoon talking about " +
                " old mine shafts, mining gear and odd encounters they've had deep down in the earth..");
    }

    private void nobleTalk(Model model, List<GameCharacter> talkers) {
        GameCharacter first = talkers.getFirst();
        GameCharacter second = talkers.getLast();
        partyMemberSay(first, second.getFirstName() + ", where did you get that outfit?", FacialExpression.questioning);
        partyMemberSay(second, "There's a clothier in " + UpperThelnTown.NAME + ". Grobulous Anarxes, a half-orc, he's very skilled.");
        partyMemberSay(first, "I've heard of him!", FacialExpression.questioning);
        println(first.getName() + " and " + second.getName() + " spend the better part of the afternoon talking about " +
                " fashion and various luxury items");
    }

    private void paladinTalk(Model model, List<GameCharacter> talkers) {
        GameCharacter first = talkers.getFirst();
        GameCharacter second = talkers.getLast();
        partyMemberSay(first, "What is your preferred weapon, " + second.getFirstName() + "?");
        Weapon w = second.getEquipment().getWeapon();
        if (w.isOfType(BladedWeapon.class) || w.isOfType(BluntWeapon.class) || w.isOfType(PolearmWeapon.class)) {
            partyMemberSay(second, "I quite like this " + w.getName() + ".");
        } else {
            partyMemberSay(second, "I would prefer a warhammer or a lance.");
        }
        partyMemberSay(second, "What about you?", FacialExpression.questioning);
        println("It depends on the situation. Each combat is unique. I focus less on perfecting my martial skill and more on mindfulness.");
        partyMemberSay(first, "How wise. I also see my self more as a spiritual explorer, than a roaming knight.");
        println(first.getName() + " and " + second.getName() + " spend the better part of the afternoon talking about " +
                " philosophy, faith and justice.");
    }

    private void priestTalk(Model model, List<GameCharacter> talkers) {
        GameCharacter first = talkers.getFirst();
        GameCharacter second = talkers.getLast();
        model.getParty().forceEyesClosed(first, true);
        println(first.getFullName() + " is sitting down in a peaceful way.");
        partyMemberSay(second, "Are you praying " + first.getFirstName() + "?", FacialExpression.questioning);
        partyMemberSay(first, "I'm meditating. And praying a little.");
        partyMemberSay(second, "Do you mind if I join you?");
        partyMemberSay(first, "Not at all. Sit down.");
        model.getLog().waitForAnimationToFinish();
        model.getParty().forceEyesClosed(second, true);
        println(first.getName() + " and " + second.getName() + " sit in silence for a little while, they then " +
                "start talking and spend the better part of the afternoon talking about " +
                " religion and belief.");
        model.getLog().waitForAnimationToFinish();
        model.getParty().forceEyesClosed(first, false);
        model.getParty().forceEyesClosed(second, false);
    }

    private void sorcererTalk(Model model, List<GameCharacter> talkers) {
        GameCharacter first = talkers.getFirst();
        GameCharacter second = talkers.getLast();

        String book = "an old tome";
        if (!model.getParty().getSpells().isEmpty()) {
            Spell sp = MyRandom.sample(model.getParty().getSpells());
            book = "the " + sp.getName() + " spell book";
        }

        println(first.getName() + " is reading " + book + ", and is concentrating very much.");
        partyMemberSay(second, "What are you doing, " + first.getFirstName() + "?", FacialExpression.questioning);
        partyMemberSay(first, "Do not disturb me!", FacialExpression.disappointed);
        partyMemberSay(second, "No need to get upset. It was a simple question.");
        partyMemberSay(first, "I'm sorry. I'm just trying to comprehend this particular passage. It's meaning eludes me.", FacialExpression.sad);
        partyMemberSay(second, "Perhaps I can be of some assistance.", FacialExpression.questioning);
        partyMemberSay(first, "Be my guest.");
        println(first.getFirstName() + " hands the book to " + second.getFirstName() + ".");
        partyMemberSay(second, "Hmmm...");
        partyMemberSay(first, "Not so simple, is it?");
        partyMemberSay(second, "Perhaps if we examine the context again...");
        println(first.getName() + " and " + second.getName() + " spend the better part of the afternoon analyzing the " +
                " scripture in the book.");
    }

    private void spyTalk(Model model, List<GameCharacter> talkers) {
        GameCharacter first = talkers.getFirst();
        GameCharacter second = talkers.getLast();
        partyMemberSay(first, second.getFirstName() + ", what did you do before you joined the party?");
        partyMemberSay(second, "I was spy for regent.");
        partyMemberSay(first, "Really, which one?", FacialExpression.questioning);
        partyMemberSay(second, "It's better for you if you don't know.", FacialExpression.wicked);
        partyMemberSay(first, "I understand. I've been in a similar line of work.", FacialExpression.relief);
        partyMemberSay(second, "It's amazing how much dirty work needs doing for the high and mighty.");
        partyMemberSay(first, "Indeed. Did you ever 'Milk a Cow'?");
        println(second.getFirstName() + " chuckles.");
        partyMemberSay(second, "I haven't heard that expression in a long time. Yes I've done it... " +
                "But let's just say I didn't use a 'pail'. Did you ever do a 'Nightly Noose'?", FacialExpression.relief);
        println(first.getName() + " and " + second.getName() + " spend the better part of the afternoon " +
                "discussing covert dealings in code.");
    }

    private void thiefTalk(Model model, List<GameCharacter> talkers) {
        GameCharacter first = talkers.getFirst();
        GameCharacter second = talkers.getLast();
        partyMemberSay(first, "Hey " + second.getFirstName() + ", what's your biggest score?");
        String town = MyRandom.sample(List.of(BullsVilleTown.NAME, SaintQuellinTown.NAME, LittleErindeTown.NAME));
        partyMemberSay(second, "Funny you should ask. I was just thinking about this one time in " + town + ".");
        println(first.getName() + " and " + second.getName() + " spend the better part of the afternoon " +
                "discussing past heists, burglaries, muggings and marks.");
    }

    private void wizardTalk(Model model, List<GameCharacter> talkers) {
        GameCharacter first = talkers.getFirst();
        GameCharacter second = talkers.getLast();
        partyMemberSay(first, "I was wondering " + second.getFirstName() + ", have you heard of the 'Cosmic Zap' spell?");
        partyMemberSay(second, "I'm not familiar with it...");
        partyMemberSay(first, "It is supposed to be an extremely rare Blue Magic spell. I've been looking for it for years. " +
                "Not many magic users have heard about it.");
        partyMemberSay(second, "Perhaps I know it by another name. What are its effects?");
        partyMemberSay(first, "It is supposed to have a very curious effect. To the uninitiated, it appears a simple " +
                "dazzling spell, which simply confounds the target. But to the target himself, it is supposed to grant " +
                "clairvoyance and sight into the spiritual plane.");
        partyMemberSay(second, "How very fascinating! Is it perhaps related to the 'Astral Walk' spell?");
        partyMemberSay(first, "'Astral Walk', I have not heard of that since my school days.");
        println(first.getName() + " and " + second.getName() + " spend the better part of the afternoon " +
                "discussing rare spells and magical phenomena.");
    }

    private void witchTalk(Model model, List<GameCharacter> talkers) {
        GameCharacter first = talkers.getFirst();
        GameCharacter second = talkers.getLast();
        println(first.getName() + " is examining the party's cooking gear.");
        partyMemberSay(first, "This cauldron is in terrible shape.", FacialExpression.disappointed);
        partyMemberSay(second, "I agree. We should definitely look for a new one while we're in town.");
        partyMemberSay(first, "Did you know that a cauldron can keep itself clean, if keep to a strict alchemical routine?");
        partyMemberSay(second, "I've heard the same thing.");
        println(first.getName() + " and " + second.getName() + " spend the better part of the afternoon " +
                "discussing cooking and potion brewing.");
    }
}
