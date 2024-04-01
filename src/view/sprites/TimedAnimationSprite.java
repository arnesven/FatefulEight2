package view.sprites;

import model.Model;
import view.MyColors;

import java.io.IOException;
import java.io.ObjectInputStream;

public class TimedAnimationSprite extends Sprite32x32 implements Animation {

    private final int steps;
    private int count = 0;

    public TimedAnimationSprite(String name, String mapPath, int num, int steps, MyColors color1, MyColors color2, MyColors color3) {
        super(name, mapPath, num, color1, color2, color3);
        this.steps = steps;
        AnimationManager.registerPausable(this);
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        if (this.count < steps) {
            this.count++;
        }
    }

    @Override
    public void synch() {

    }

    public boolean isDone() {
        return count >= steps;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        AnimationManager.register(this);
    }
}
