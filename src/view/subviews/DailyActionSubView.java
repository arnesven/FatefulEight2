package view.subviews;

import model.Model;

import java.awt.*;

public abstract class DailyActionSubView extends AvatarSubView {
    public abstract void animateMovement(Model model, Point point, Point destination);
}
