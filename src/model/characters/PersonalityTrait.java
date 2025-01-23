package model.characters;

import model.Model;
import model.states.events.*;
import util.MyRandom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static Map<PersonalityTrait, List<String>> makeConversations(GameCharacter gc) {
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
                List.of("I'm not sure we're spending out gold responsibly.",
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
                                "a egg for three obols these days.",
                        "Half an egg used to be equivalent to an obol, every kid knew that.",
                        "Those were the days."),
                PersonalityTrait.unkind,
                List.of("I think your outfit is in rather poor fashion.",
                        "Hmmph! Maybe you should keep such things to yourself.#",
                        "You were the one who asked me what was on my mind.")));
        return answers;
    }

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
                return new DeadlyLoverEvent(model, this, mainCharacter);
            case naive:
                // return ?
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
                // return new GuildHallEvent(model, this, mainCharacter);
            case cowardly:
                // return new NightmareEvent(model, this, mainCharacter);
            case narcissistic:
                return new DoppelgangerEvent(model, mainCharacter);
            default:

        }
        return null;
    }
}
