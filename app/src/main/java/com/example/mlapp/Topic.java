package com.example.mlapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Topic {

    private final String id;
    private final String name;
    private final String category;
    private final List<String> keywords;
    private final List<String> synonyms;
    private final String shortAnswer;
    private final String normalAnswer;
    private final String deepConcept;
    private final String howItWorks;
    private final List<String> examples;
    private final List<String> advantages;
    private final List<String> limitations;
    private final List<String> applications;
    private final List<String> relatedTopics;

    public Topic(String id, String name, String category,
                 List<String> keywords, List<String> synonyms,
                 String shortAnswer, String normalAnswer,
                 String deepConcept, String howItWorks,
                 List<String> examples, List<String> advantages,
                 List<String> limitations, List<String> applications,
                 List<String> relatedTopics) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.keywords = keywords != null ? keywords : new ArrayList<>();
        this.synonyms = synonyms != null ? synonyms : new ArrayList<>();
        this.shortAnswer = shortAnswer;
        this.normalAnswer = normalAnswer;
        this.deepConcept = deepConcept;
        this.howItWorks = howItWorks;
        this.examples = examples != null ? examples : new ArrayList<>();
        this.advantages = advantages != null ? advantages : new ArrayList<>();
        this.limitations = limitations != null ? limitations : new ArrayList<>();
        this.applications = applications != null ? applications : new ArrayList<>();
        this.relatedTopics = relatedTopics != null ? relatedTopics : new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public String getShortAnswer() {
        return shortAnswer;
    }

    public String getNormalAnswer() {
        return normalAnswer;
    }

    public String getDeepConcept() {
        return deepConcept;
    }

    public String getHowItWorks() {
        return howItWorks;
    }

    public List<String> getExamples() {
        return examples;
    }

    public List<String> getAdvantages() {
        return advantages;
    }

    public List<String> getLimitations() {
        return limitations;
    }

    public List<String> getApplications() {
        return applications;
    }

    public List<String> getRelatedTopics() {
        return relatedTopics;
    }

    public static class Builder {
        private String id;
        private String name;
        private String category = "General";
        private List<String> keywords = new ArrayList<>();
        private List<String> synonyms = new ArrayList<>();
        private String shortAnswer = "";
        private String normalAnswer = "";
        private String deepConcept = "";
        private String howItWorks = "";
        private List<String> examples = new ArrayList<>();
        private List<String> advantages = new ArrayList<>();
        private List<String> limitations = new ArrayList<>();
        private List<String> applications = new ArrayList<>();
        private List<String> relatedTopics = new ArrayList<>();

        public Builder(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder keywords(String... kw) {
            this.keywords.addAll(Arrays.asList(kw));
            return this;
        }

        public Builder synonyms(String... syn) {
            this.synonyms.addAll(Arrays.asList(syn));
            return this;
        }

        public Builder shortAnswer(String shortAns) {
            this.shortAnswer = shortAns;
            return this;
        }

        public Builder normalAnswer(String normalAns) {
            this.normalAnswer = normalAns;
            return this;
        }

        public Builder deepConcept(String deepConcept) {
            this.deepConcept = deepConcept;
            return this;
        }

        public Builder howItWorks(String howItWorks) {
            this.howItWorks = howItWorks;
            return this;
        }

        public Builder examples(String... ex) {
            this.examples.addAll(Arrays.asList(ex));
            return this;
        }

        public Builder advantages(String... adv) {
            this.advantages.addAll(Arrays.asList(adv));
            return this;
        }

        public Builder limitations(String... lim) {
            this.limitations.addAll(Arrays.asList(lim));
            return this;
        }

        public Builder applications(String... app) {
            this.applications.addAll(Arrays.asList(app));
            return this;
        }

        public Builder relatedTopics(String... rel) {
            this.relatedTopics.addAll(Arrays.asList(rel));
            return this;
        }

        public Topic build() {
            return new Topic(id, name, category, keywords, synonyms,
                    shortAnswer, normalAnswer, deepConcept, howItWorks,
                    examples, advantages, limitations, applications, relatedTopics);
        }
    }
}