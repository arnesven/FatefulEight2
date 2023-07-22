package view.help;

import view.GameView;

import java.util.List;

public abstract class ExpandableHelpDialog extends HelpDialog {
    private boolean expanded = false;
    private final List<HelpDialog> subsections;

    public ExpandableHelpDialog(GameView view, String title, String text) {
        super(view, title, text);
        subsections = makeSubSections(view);
    }

    protected abstract List<HelpDialog> makeSubSections(GameView view);


    @Override
    public boolean isExpandable() {
        return true;
    }

    @Override
    public boolean isExpanded() {
        return expanded;
    }

    @Override
    public void setExpanded(boolean expand) {
        expanded = expand;
    }

    @Override
    public List<HelpDialog> getSubSections() {
        return subsections;
    }
}
