package model.characters;

import model.Model;
import model.states.events.BurningBuildingEvent;
import model.states.events.HotSpringEvent;
import model.states.events.PersonalityTraitEvent;
import model.states.events.TavernBrawlEvent;

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
                // return new InfatuationEvent(model, this, mainCharacter);
            case naive:
                // return new ConfidenceWomanEvent(model, this, mainCharacter); // lured into bed... then robbed.
            case diplomatic:
                // return new FeudingFamiliesEvent(model, this, mainCharacter);
            case cold:
                // return new GraveyardEvent(model, this, mainCharacter); // Robbing the dead...
            case calm:
                // return new TidalWaterEvent(model, this, mainCharacter); // Water rising in a cave, keep calm?
            case benevolent:
                // return new RefugeeCampEvent(model, this, mainCharacter);
            case playful:
                // return new BallGameEvent(model, this, mainCharacter);
            case gluttonous:
                // return new OuthouseEvent(model, this, mainCharacter);
            case jovial:
                // return new OffendedWomanEvent(model, this, mainCharacter);
            case anxious:
                // return new BurySomeGoldEvent(model, this, mainCharacter);
            case irritable:
                // return new FanEvent(model, this, mainCharacter);
            case snobby:
                // return new DontWantToGetDirtyEvent(model, this, mainCharacter);
            case rude:
                // return new SlapInTheFaceEvent(model, this, mainCharacter);
            case greedy:
                // return new TreasureTroveEvent(model, this, mainCharacter); // push-your-luck infiltration style
            case encouraging:
                // return new FamousPainterEvent(model, this, mainCharacter); // You were the only one who believed in me
            case intellectual:
                // return new GuildHallEvent(model, this, mainCharacter);
            case cowardly:
                // return new NightmareEvent(model, this, mainCharacter);
            case narcissistic:
                // return new DoppelgangerEvent(model, this, mainCharacter);
            default:

        }
        return null;
    }
}
