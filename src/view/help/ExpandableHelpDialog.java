package view.help;

import view.GameView;

import java.util.List;

public abstract class ExpandableHelpDialog extends HelpDialog {
    private final boolean midLevel;
    private boolean expanded = false;
    private final List<HelpDialog> subsections;

    public ExpandableHelpDialog(GameView view, String title, String text, boolean midLevel) {
        super(view, title, text);
        subsections = makeSubSections(view);
        this.midLevel = midLevel;
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

    @Override
    public String getTitle() {
        if (midLevel) {
            return ((char)0x7D) + super.getTitle();
        }
        return super.getTitle();
    }
}
