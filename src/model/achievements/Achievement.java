package model.achievements;

import model.Model;

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
    private final String type;

    public Achievement(Achievement.Data data) {
        this.data = data;
        this.type = "General";
        completed = false;
    }

    public String getName() {
        return data.name;
    }

    public String getDescription() {
        return data.description;
    }

    public String getKey() {
        return data.key;
    }

    public String getType() {
        return type;
    }

    public boolean isCompleted(Model model) {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
