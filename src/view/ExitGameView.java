package view;

import model.Model;

import java.util.List;

public class ExitGameView extends SimpleMessageView {
    public ExitGameView(GameView menuView) {
        super(menuView,
                "Are you sure you want to quit the game? Any unsaved progress will be lost.");
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> content = super.buildContent(model, xStart, yStart);
        content.remove(content.size()-1);
        content.add(new SelectableListContent(xStart+getWidth()/2-6, yStart+getHeight()-3, "QUIT TO TITLE") {
            @Override
            public void performAction(Model model, int x, int y) {
                setTimeToTransition(true);
                model.setGameAbandoned(true);
                model.setExitGame(true);
            }

            @Override
            public boolean isEnabled(Model model) {
                return true;
            }
        });
        content.add(new SelectableListContent(xStart+getWidth()/2-4, yStart+getHeight()-2, "QUIT TO OS") {
            @Override
            public void performAction(Model model, int x, int y) {
                setTimeToTransition(true);
                model.setExitGame(true);
            }

            @Override
            public boolean isEnabled(Model model) {
                return true;
            }
        });
        content.add(makeCancelButton(model, xStart+getWidth()/2-2, yStart+getHeight()-1, this));
        return content;
    }
}
