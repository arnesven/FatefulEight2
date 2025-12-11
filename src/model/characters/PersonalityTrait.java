package model.characters;

import model.Model;
import model.items.weapons.NaturalWeapon;
import model.states.GameState;
import model.states.events.*;
import util.MyLists;
import util.MyPair;
import util.MyRandom;

import java.util.*;

public enum PersonalityTrait {
                 // Chars    Usages
    aggressive,  // 4        8
    anxious,     // 3        6
    benevolent,  // 4        10
    brave,       // 3        2
    calm,        // 5        4
    cold,        // 4        5
    cowardly,    // 4        8
    critical,    // 5        8
    diplomatic,  // 5        4
    encouraging, // 4        5
    forgiving,   // 4        3
    friendly,    // 6        4
    gluttonous,  // 4        5
    irritable,   // 5        5
    intellectual,// 6        4
    jovial,      // 4        5
    lawful,      // 3        4
    generous,    // 4        7
    greedy,      // 5        5
    mischievous, // 7        3
    naive,       // 3        4
    narcissistic,// 4        5
    playful,     // 5        7
    prudish,     // 4        3
    romantic,    // 6        6
    rude,        // 4        7
    snobby,      // 4        5
    stingy,      // 4        6
    unkind;      // 4        8

    public PersonalityTraitEvent makeEvent(Model model, GameCharacter mainCharacter) {
        switch (this) {
            case aggressive:
                return new TavernBrawlEvent(model, this, mainCharacter);
            case brave:
                return new BurningBuildingEvent(model, this, mainCharacter);
            case prudish:
                return new HotSpringEvent(model, this, mainCharacter);
            case lawful:
                // return new MarshallEvent(model, this, mainCharacter); // which thief stole the thing?
            case stingy:
                // return new CantAffordThatThingEvent(model, this, mainCharacter);
            case romantic:
                return new BoyfriendGirlfriendEvent(model, mainCharacter);
            case naive:
                return new DeadlyLoverEvent(model, this, mainCharacter);
            case diplomatic:
                // return new FeudingFamiliesEvent(model, this, mainCharacter);
            case cold:
                // return new GraveyardEvent(model, this, mainCharacter); // Robbing the dead...
            case calm:
                // return new TidalWaterEvent(model, this, mainCharacter); // Water rising in a cave, keep calm?
            case benevolent:
                return new SwordInTheStoneEvent(model, this, mainCharacter);
            case playful:
                // return new BallGameEvent(model, this, mainCharacter);
            case gluttonous:
                // return new OuthouseEvent(model, this, mainCharacter);
            case jovial:
                // return new OffendedWomanEvent(model, this, mainCharacter);
            case anxious:
                return new BurySomeGoldEvent(model, this, mainCharacter);
            case irritable:
                return new AuctionEvent(model, this, mainCharacter);
            case snobby:
                // return new DontWantToGetDirtyEvent(model, this, mainCharacter);
            case rude:
                // return new SlapInTheFaceEvent(model, this, mainCharacter);
            case greedy:
                // return new TreasureTroveEvent(model, this, mainCharacter); // push-your-luck infiltration style
            case encouraging:
                return new FamousPainterEvent(model, this, mainCharacter); // You were the only one who believed in me
            case intellectual:
                // return new BookWithMissingPagesEvent(model, this, mainCharacter);
            case cowardly:
                // return new NightmareEvent(model, this, mainCharacter);
            case narcissistic:
                return new DoppelgangerEvent(model, mainCharacter);
            default:

        }
        return null;
    }

    public static Map<PersonalityTrait, List<String>> tavernConversation(GameCharacter gc) {
        Map<PersonalityTrait, List<String>> answers = new HashMap<>(Map.of(
                PersonalityTrait.anxious,
                List.of("Feeling a bit nervous about what's ahead of us.",
                        "Don't worry. We can handle ourselves."),
                PersonalityTrait.aggressive,
                List.of("My fingers are itching. I'm ready to crack some skulls!",
                        "Whoa, settle down now. Let's not get into some random fight."),
                PersonalityTrait.rude,
                List.of("Why don't you mind your own business?",
                        "Calm down. It was just a question."),
                PersonalityTrait.benevolent,
                List.of("Just breathing in the wonderful atmosphere here. It just feels great.",
                        "Yeah... I guess it isn't too bad."),
                PersonalityTrait.brave,
                List.of("I'm longing for a real adventure. Like seeking a lost treasure, discovering the " +
                                "secrets of an ancient empire or facing a dragon, and coming out on top!",
                        "Those all seem good. I love your enthusiasm."),
                PersonalityTrait.calm,
                List.of("Not much. Just enjoying my drink here. Wondering where tomorrow will take me.",
                        "We'll find out together."),
                PersonalityTrait.cold,
                List.of("Thinking about the last fight I was in. My opponents dead body... I should have looted it more thoroughly",
                        "Damn, that's cold."),
                PersonalityTrait.cowardly,
                List.of("I thinking about this bully I met once. He was a real meanie, " +
                                "gave my buddy a real pounding.",
                        "Did you stand up to him?",
                        "Not really. I just sort of ran away."),
                PersonalityTrait.critical,
                List.of("I'm not sure we're spending our gold responsibly.",
                        "Do you want to be the leader of this party?",
                        "No no... It was just a casual remark."),
                PersonalityTrait.diplomatic,
                List.of("We should be working more with the authorities here. I'm sure our services are needed.",
                        "That's not a bad suggestion. I'll let you do the talking.",
                        "Fine with me.")));

        answers.putAll(Map.of(
                PersonalityTrait.encouraging,
                List.of("I see some good potential in the members of our party.",
                        "That's good to hear."),
                PersonalityTrait.forgiving,
                List.of("Don't worry so much about what happened earlier.",
                        "I'm not sure I know what you are referring to",
                        "Good. Let's not dwell on the past."),
                PersonalityTrait.friendly,
                List.of("I'm thinking about the good times we've had so far. I'm thankful for being in this party.",
                        "That's a relief. It's good to have you on the team."),
                PersonalityTrait.generous,
                List.of("If I had some extra gold or equipment, I'd donate it to the party." +
                                "That's mighty generous of you.",
                        "I know it's for a good cause. When we all chip in, we all benefit."),
                PersonalityTrait.gluttonous,
                List.of("I'm starving! Can we get some grub already?"),
                PersonalityTrait.greedy,
                List.of("I really think you ought to be paying us a little bit more.",
                        "I think you know how our money situation is right now.",
                        "Still, I hope you'll remember this conversation when things turn around."),
                PersonalityTrait.intellectual,
                List.of("I've been studying some of the decorations on the wall here.",
                        "Oh. Found anything interesting?",
                        "There are trophies, plaques, antlers and candle holders, but no paintings. Isn't that odd?",
                        "Huh, you're right. Why do you think that is?",
                        "I haven't the foggiest."),
                PersonalityTrait.irritable,
                List.of("Good gravy, the smell in here! And the noise!",
                        "It's a tavern, what do you expect?"),
                PersonalityTrait.jovial,
                List.of("A blind man walks into a bar, and a table, and a chair.",
                        "Hehe, that's a good one."),
                PersonalityTrait.lawful,
                List.of("We should be more careful about what jobs we accept. Don't want to get into trouble with the law.",
                        "We'll avoid them if we can, but everybody needs to eat.")));

        answers.putAll(Map.of(PersonalityTrait.mischievous,
                List.of("Who's up for a practical joke? I'll tell the tavern wench there's hair in my soup, " +
                                "and when she looks down into my bowl, I'll fling it up in her face!",
                        "That's not as funny as you think.",
                        "Aaah, come on you sourpuss, I was joking."),
                PersonalityTrait.naive,
                List.of("There's a guy over there, he told me he was a wizard. He asked me for a coin, and when I said " +
                                "I didn't have any he pulled one out from behind my ear and told me I was richer than I thought.",
                        "Yeah, a real wizard. Did you get to keep the coin?",
                        "Uhm, no. He said he would keep it as donation.",
                        "What a cheap trick."),
                PersonalityTrait.narcissistic,
                List.of("I think this new garb makes me look rather stylish",
                        "Yes... very."),
                PersonalityTrait.playful,
                List.of("I think I saw some people play cards here. We should join in!",
                        "They're gamblers, but maybe we'll try a hand."),
                PersonalityTrait.prudish,
                List.of("Did you see the restrooms here? It's just a row of holes! No walls!",
                        "You're free to go in the bushes if you like.",
                        "What? What if somebody saw me? No way."),
                PersonalityTrait.romantic,
                List.of("You see that " + (MyRandom.flipCoin() ? "boy":"girl") + " over there? Absolutely adorable. " +
                                "Do you think I should go over and introduce myself?",
                        "Why not? You've definitely have a shot.",
                        "Hmm... I think I'll just have another drink first. You know, to work up my courage.",
                        "Never pass up a good thing " + gc.getFirstName() + "."),
                PersonalityTrait.snobby,
                List.of("I would like to see a menu please.",
                        "Uhm, I don't think they have menus here. You'll just have to eat what's served.",
                        "And what's that?",
                        "Looks like... A yes, it's hedgehog goulash.",
                        "Hard pass.",
                        "Suit yourself."),
                PersonalityTrait.stingy,
                List.of("One gold for food, and ONE gold for a room? That's criminal.",
                        "I think it's pretty standard pricing actually.",
                        "I say, when I was a kid, it was six obols for food and three for a room.",
                        "That was a long time ago. Prices have gone up. You can scarcely get " +
                                "an egg for three obols these days.",
                        "Half an egg used to be equivalent to an obol, every kid knew that.",
                        "Those were the days."),
                PersonalityTrait.unkind,
                List.of("I think your outfit is in rather poor fashion.",
                        "Hmmph! Maybe you should keep such things to yourself.#",
                        "You were the one who asked me what was on my mind.")));
        return answers;
    }

    public static Map<PersonalityTrait, List<MyPair<GameCharacter, String>>> makeEveningConversation(Model model, GameCharacter main, GameCharacter other) {
        Map<PersonalityTrait, List<MyPair<GameCharacter, String>>> convos = new HashMap<>();

        if (MyRandom.flipCoin() &&
                !main.getEquipment().getWeapon().isRangedAttack() &&
                !main.getEquipment().getWeapon().isOfType(NaturalWeapon.class)) {
            return makeCleanWeaponConvos(main, other);
        }

        return makeRationsConvos(model, main, other);
    }

    private static Map<PersonalityTrait, List<MyPair<GameCharacter, String>>> makeRationsConvos(Model model, GameCharacter main, GameCharacter other) {
        Map<PersonalityTrait, List<MyPair<GameCharacter, String>>> convos = new HashMap<>();
        List<MyPair<GameCharacter, String>> primer = new ArrayList<>(List.of(
                new MyPair<>(null, main.getFirstName() + " is counting the rations.")));

        if (model.getParty().getFood() < model.getParty().size() * 3) { // low rations
            convos.put(PersonalityTrait.stingy,
                    MyLists.merge(primer, List.of(
                            new MyPair<>(other, "How are we on rations " + main.getFirstName() + "?"),
                            new MyPair<>(main, "We're running low. Perhaps we should go down to half rations."),
                            new MyPair<>(other, "What? You can't be serious?"),
                            new MyPair<>(main, "Well... Nobody eats more than their share!"),
                            new MyPair<>(other, "Relax! Jeez..."))));

            convos.put(PersonalityTrait.aggressive,
                    MyLists.merge(primer, List.of(
                            new MyPair<>(other, "How are we on rations " + main.getFirstName() + "?"),
                            new MyPair<>(main, "I think somebody's been having late night snacks..."),
                            new MyPair<>(other, "You think somebody is stealing food?"),
                            new MyPair<>(main, "Arrrg... when I get my hands on them!"),
                            new MyPair<>(other, "You probably just counted wrong."),
                            new MyPair<>(null, main.getFirstName() + " spends the rest of the evening interrogating the other party members about the alleged theft."),
                            new MyPair<>(main, "I'm so pissed off!"))));

            convos.put(PersonalityTrait.diplomatic,
                    MyLists.merge(primer, List.of(
                            new MyPair<>(other, "How are we on rations " + main.getFirstName() + "?"),
                            new MyPair<>(main, "We're low. If things get bad, we need to figure out who gets to eat."),
                            new MyPair<>(other, "Uh-huh, and I suppose you are a priority?"),
                            new MyPair<>(main, "Actually yes. If I'm fed I can more easily secure food for the rest of the party."),
                            new MyPair<>(other, "Let's make sure it doesn't come to that."))));

            convos.put(PersonalityTrait.cold,
                    MyLists.merge(primer, List.of(
                            new MyPair<>(other, "How are we on rations " + main.getFirstName() + "?"),
                            new MyPair<>(main, "We're low. Perhaps we will starve soon."),
                            new MyPair<>(other, "You really think so?"),
                            new MyPair<>(main, "Of course. Plenty of people starve every day. It's bound to happen to us sooner or later."),
                            new MyPair<>(other,"I'm trying not to think about it."))));

            convos.put(PersonalityTrait.calm,
                    MyLists.merge(primer, List.of(
                            new MyPair<>(other, "How are we on rations " + main.getFirstName() + "?"),
                            new MyPair<>(main, "We're low."),
                            new MyPair<>(other, "Are we going to run out?"),
                            new MyPair<>(main, "We'll be fine."))));

            convos.put(PersonalityTrait.benevolent,
                    MyLists.merge(primer, List.of(
                            new MyPair<>(other, "How are we on rations " + main.getFirstName() + "?"),
                            new MyPair<>(main, "We're low. But I don't have to eat so much. Somebody else probably needs it more."),
                            new MyPair<>(other, "That's generous of you. But we all need to keep up our strength."))));

            convos.put(PersonalityTrait.anxious,
                    MyLists.merge(primer, List.of(
                            new MyPair<>(other, "How are we on rations " + main.getFirstName() + "?"),
                            new MyPair<>(main, "We're low. What if we run out?"),
                            new MyPair<>(other, "I'm sure we'll be able to get some more soon."),
                            new MyPair<>(main, "I hope so. I don't want to starve."))));
        }

        if (model.getParty().getFood() > model.getParty().size() * 7) { // high rations
            convos.put(PersonalityTrait.naive,
                    MyLists.merge(primer, List.of(
                            new MyPair<>(other, "Checking on the rations?"),
                            new MyPair<>(main, "Yeah, I was thinking, since we have so much. Maybe we should eat more."),
                            new MyPair<>(other, "We'll just get sick if we eat too much."),
                            new MyPair<>(main, "Maybe."))));

            convos.put(PersonalityTrait.gluttonous,
                    MyLists.merge(primer, List.of(
                            new MyPair<>(other, "Checking on the rations?"),
                            new MyPair<>(main, "Yes... we have a lot. And I'm hungry..."),
                            new MyPair<>(other, "Hey! Didn't we just eat?"),
                            new MyPair<>(main, "Yummy yummy...."),
                            new MyPair<>(other, "I'm keeping my eye on you!"))));

            convos.put(PersonalityTrait.encouraging,
                    MyLists.merge(primer, List.of(
                            new MyPair<>(other, "Checking on the rations?"),
                            new MyPair<>(main, "Yes... we have plenty. Enough for everybody"),
                            new MyPair<>(other, "That's good to hear."))));
        }

        convos.put(PersonalityTrait.prudish,
                MyLists.merge(primer, List.of(
                        new MyPair<>(other, "Checking on the rations?"),
                        new MyPair<>(main, "I'm sick of this stuff. I want a real meal."),
                        new MyPair<>(other, "We'll hit a tavern sooner or later."),
                        new MyPair<>(null, main.getFirstName() + " picks up some dry bread."),
                        new MyPair<>(main, "Yuck..."))));

        convos.put(PersonalityTrait.romantic,
                MyLists.merge(primer, List.of(
                        new MyPair<>(other, "It would be better with some real food, yeah?"),
                        new MyPair<>(main, "I don't mind actually. I imagine it's the most scrumptious entry at the finest diner, and it tastes a lot better."),
                        new MyPair<>(other, "I wish I had your imagination."))));

        convos.put(PersonalityTrait.snobby,
                MyLists.merge(primer, List.of(
                        new MyPair<>(other, "It would be better with some real food, yeah?"),
                        new MyPair<>(main, "Indeed. This stuff is basically swill."),
                        new MyPair<>(other, "I wouldn't go that far."))));

        convos.put(PersonalityTrait.rude,
                MyLists.merge(primer, List.of(
                        new MyPair<>(other, "Checking on the rations?"),
                        new MyPair<>(main, "Yeah. Have you been stealing?."),
                        new MyPair<>(other, "What!? Of course not."),
                        new MyPair<>(main, "Just don't get any ideas."),
                        new MyPair<>(other, "I don't like your tone " + main.getFirstName() + "."),
                        new MyPair<>(main, "That sounds like a you-problem."),
                        new MyPair<>(null, other.getFirstName() + " is offended and walks off."))));

        convos.put(PersonalityTrait.greedy,
                MyLists.merge(primer, List.of(
                        new MyPair<>(other, "Checking on the rations?"),
                        new MyPair<>(main, "Yeah. I think I deserve double rations."),
                        new MyPair<>(other, "Really? Why."),
                        new MyPair<>(main, "I just do."))));
        return convos;
    }

    private static Map<PersonalityTrait, List<MyPair<GameCharacter, String>>> makeCleanWeaponConvos(GameCharacter main, GameCharacter other) {
        Map<PersonalityTrait, List<MyPair<GameCharacter, String>>> convos = new HashMap<>();

        List<MyPair<GameCharacter, String>> primer = new ArrayList<>(List.of(
                new MyPair<>(null, main.getFirstName() + " is cleaning " + GameState.hisOrHer(main.getGender()) + " " + main.getEquipment().getWeapon().getName().toLowerCase() + ".")));

        convos.put(PersonalityTrait.aggressive,
                MyLists.merge(primer, List.of(
                        new MyPair<>(other, "Keeping it sharp " + main.getFirstName() + "?"),
                        new MyPair<>(main, "Yes. I can't wait to get back into the fight!"))));

        convos.put(PersonalityTrait.brave,
                MyLists.merge(primer, List.of(
                        new MyPair<>(other, "Keeping it clean " + main.getFirstName() + "?"),
                        new MyPair<>(main, "I have to. It won't do for it to be rusty when we need to defend ourselves."))));

        convos.put(PersonalityTrait.lawful,
                MyLists.merge(primer, List.of(
                        new MyPair<>(other, "Are you expecting to use that soon?"),
                        new MyPair<>(main, "I've heard there are bandits around here. We should bring them to justice."))));

        convos.put(PersonalityTrait.stingy,
                MyLists.merge(primer, List.of(
                        new MyPair<>(other, "Keeping it clean " + main.getFirstName() + "?"),
                        new MyPair<>(main, "This equipment is expensive, we should take good care of it."))));

        convos.put(PersonalityTrait.romantic,
                MyLists.merge(primer, List.of(
                        new MyPair<>(other, "What's the point of cleaning that, " + main.getFirstName() + "?"),
                        new MyPair<>(main, "I have to. What if I meet a cute " + GameState.boyOrGirl(MyRandom.flipCoin()) + "? " +
                                "They'll run the other way if my gear looks shabby."),
                        new MyPair<>(other, "That's your reason for doing that? Incredible."))));

        convos.put(PersonalityTrait.cold,
                MyLists.merge(primer, List.of(
                        new MyPair<>(other, "Are those blood stains?"),
                        new MyPair<>(main, "They are. Been doing a lot of killing."),
                        new MyPair<>(other, "..."))));

        convos.put(PersonalityTrait.calm,
                MyLists.merge(primer, List.of(
                        new MyPair<>(other, "Keeping it sharp " + main.getFirstName() + "?"),
                        new MyPair<>(main, "Yes. This is almost like meditation for me."))));

        convos.put(PersonalityTrait.benevolent,
                MyLists.merge(primer, List.of(
                        new MyPair<>(other, "Are you expecting to use that soon?"),
                        new MyPair<>(main, "I hope not. I actually abhor violence."))));

        convos.put(PersonalityTrait.jovial,
                MyLists.merge(primer, List.of(
                        new MyPair<>(other, "Are those blood stains?"),
                        new MyPair<>(main, "No no, this is jam. I was preparing a very large sandwich earlier."),
                        new MyPair<>(other, "Uhm... I don't think I"),
                        new MyPair<>(main, "I'm just yanking your chain mate."),
                        new MyPair<>(other, "Oh. Ha-ha."))));

        convos.put(PersonalityTrait.irritable,
                MyLists.merge(primer, List.of(
                        new MyPair<>(other, "Keeping it clean " + main.getFirstName() + "?"),
                        new MyPair<>(main, "Mind your own business " + other.getName() + "."),
                        new MyPair<>(other, "Hmph!"))));

        convos.put(PersonalityTrait.prudish,
                MyLists.merge(primer, List.of(
                        new MyPair<>(other, "Keeping it sharp " + main.getFirstName() + "?"),
                        new MyPair<>(main, "Yes... but can you do this for me?"),
                        new MyPair<>(other, "What? Why should I?"),
                        new MyPair<>(main, "Well... I don't want to get dirty."),
                        new MyPair<>(null, other.getFirstName() + " turns away in disgust."))));

        convos.put(PersonalityTrait.snobby,
                MyLists.merge(primer, List.of(
                        new MyPair<>(other, "Keeping it sharp " + main.getFirstName() + "?"),
                        new MyPair<>(main, "Yes... but can you do this for me?"),
                        new MyPair<>(other, "What? Why should I?"),
                        new MyPair<>(main, "This type of chore is beneath me. It's servant stuff."),
                        new MyPair<>(other, "Are you saying I'm your servant?"),
                        new MyPair<>(main, "No but..."),
                        new MyPair<>(null, other.getFirstName() + " turns away in disgust."))));

        convos.put(PersonalityTrait.rude,
                MyLists.merge(primer, List.of(
                        new MyPair<>(other, "Keeping it clean " + main.getFirstName() + "?"),
                        new MyPair<>(main, "Not all of us want to have dirty gear " + other.getName() + "."),
                        new MyPair<>(other, "Hey!"))));

        convos.put(PersonalityTrait.encouraging,
                MyLists.merge(primer, List.of(
                        new MyPair<>(other, "Keeping it clean " + main.getFirstName() + "?"),
                        new MyPair<>(main, "Yes. The way I see it, why not look good while we're fighting?"),
                        new MyPair<>(other, "Can't argue with that."))));

        convos.put(PersonalityTrait.intellectual,
                MyLists.merge(primer, List.of(
                        new MyPair<>(other, "Keeping it sharp " + main.getFirstName() + "?"),
                        new MyPair<>(main, "Dirt can actually degrade your weapon faster than using it in battle."),
                        new MyPair<>(other, "I don't know if I believe that."),
                        new MyPair<>(main, "Well, it's true. I once attended a lecture about it..."),
                        new MyPair<>(null, main.getName() + " starts reminiscing about " + GameState.hisOrHer(main.getGender()) + " time at college."),
                        new MyPair<>(other, "Fine, I believe you. Just stop talking please..."))));

        convos.put(PersonalityTrait.cowardly,
                MyLists.merge(primer, List.of(
                        new MyPair<>(other, "Are you expecting to use that soon?"),
                        new MyPair<>(main, "Actually, I don't know why I lug this thing around. " +
                                "Am I expected to use it in combat?"),
                        new MyPair<>(other, "Uhm, yeah."),
                        new MyPair<>(main, "Sounds risky."),
                        new MyPair<>(other, "Adventuring is risky business " + main.getFirstName() +
                                ". You'd better get used to it, or find another occupation."),
                        new MyPair<>(main, "I'll be fine. Let's just keep the fighting to a minimum."))));

        convos.put(PersonalityTrait.narcissistic,
                MyLists.merge(primer, List.of(
                        new MyPair<>(other, "What's the point of cleaning that, " + main.getFirstName() + "?"),
                        new MyPair<>(main, "I want it to look extra shiny. This weapon is part of my image."))));
        return convos;
    }
}
