package view.sprites;

import sprites.CombatSpeechBubble;

public class NonCombatSpeechBubble extends CombatSpeechBubble {
    public NonCombatSpeechBubble(int lengthOfLine) {
        setAnimationDelay(lengthOfLine / 4);
    }
}
