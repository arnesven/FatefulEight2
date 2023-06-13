package model.journal;

public interface JournalEntry {
    String getName();
    String getText();
    boolean isComplete();
    boolean isFailed();
    boolean isTask();
}
