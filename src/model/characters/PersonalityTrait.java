package model.characters;

import model.Model;
import model.states.events.PersonalityTraitEvent;
import model.states.events.TavernBrawlEvent;

public enum PersonalityTrait {
                 // Chars    Usages
    aggressive,  // 4        8
    anxious,     // 3        6
    benevolent,  // 4        10
    calm,        // 5        4
    cold,        // 4        5
    cowardly,    // 4        8
    critical,    // 5        8
    diplomatic,  // 5        4
    encouraging, // 4        5
    forgiving,   // 4        3
    friendly,    // 8        4
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
            default:

        }
        return null;
    }
}
