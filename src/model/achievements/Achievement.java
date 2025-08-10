package model.achievements;

import java.io.Serializable;

public class Achievement implements Serializable {

    public static class Data implements Serializable {
        private String key;
        private String name;
        private String description;

        public Data(String k, String n, String d) {
            key = k;
            name = n;
            description = d;
        }

        public String getKey() {
            return key;
        }
    }

    private final Data data;
    private boolean completed;

    public Achievement(Achievement.Data data) {
        this.data = data;
        completed = true;
    }

    public String getName() {
        return data.name;
    }

    public String getDescription() {
        return data.description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
