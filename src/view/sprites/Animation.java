package view.sprites;

import model.Model;

public interface Animation {
    void stepAnimation(long elapsedTimeMs, Model model);

    void synch();
}
