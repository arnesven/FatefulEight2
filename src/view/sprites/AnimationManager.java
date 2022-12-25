package view.sprites;

import model.Model;

import java.util.HashSet;
import java.util.Set;

public class AnimationManager {
    private static Set<Animation> animatons = new HashSet<>();
    private static Set<Animation> pausableAnimations = new HashSet<>();

    public static synchronized void register(Animation as) {
        animatons.add(as);
    }
    public static synchronized void registerPausable(Animation as) {pausableAnimations.add(as); }

    public static synchronized void stepAnimations(long elapsedTimeMs, Model model) {
        animatons.forEach((animation -> animation.stepAnimation(elapsedTimeMs, model)));
    }

    public static synchronized void stepPausableAnimations(long timeSinceLast, Model model) {
        pausableAnimations.forEach((animation) -> animation.stepAnimation(timeSinceLast, model));
    }

    public static void synchAnimations() {
        animatons.forEach((animation -> animation.synch()));
    }
}
