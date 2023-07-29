package view.sprites;

import model.Model;
import view.subviews.AvatarSubView;

import java.util.HashSet;
import java.util.Set;

public class AnimationManager {
    private static Set<Animation> animatons = new HashSet<>();
    private static Set<Animation> pausableAnimations = new HashSet<>();
    private static long totalTime = 0;
    private static long totalTimePausable = 0;

    public static synchronized void register(Animation as) {
        animatons.add(as);
    }
    public static synchronized void registerPausable(Animation as) {pausableAnimations.add(as); }

    public static synchronized void stepAnimations(long elapsedTimeMs, Model model) {
        totalTime += elapsedTimeMs;
        if (totalTime > 10000) {
            totalTime = 0;
            System.out.println("Number of animations: " + animatons.size());
//            for (Animation a : animatons) {
//                System.out.println(" " + a);
//            }
        }
        animatons.forEach((animation -> animation.stepAnimation(elapsedTimeMs, model)));
    }

    public static synchronized void stepPausableAnimations(long timeSinceLast, Model model) {
        totalTimePausable += timeSinceLast;
        if (totalTimePausable > 10000) {
            totalTimePausable = 0;
            System.out.println("Number of pausable animations: " + animatons.size());
        }
        pausableAnimations.forEach((animation) -> animation.stepAnimation(timeSinceLast, model));
    }

    public static void synchAnimations() {
        animatons.forEach((animation -> animation.synch()));
    }

    public static void unregister(Animation ani) {
        animatons.remove(ani);
    }
}
