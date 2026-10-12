package com.example.mlapp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class QueryAnalyzer {

    private static final Set<String> ML_DOMAIN_ANCHORS = new HashSet<>(Arrays.asList(
            "machine learning", "ml", "ai", "artificial intelligence", "deep learning", "dl",
            "model", "algorithm", "dataset", "data", "training", "testing", "validation",
            "supervised", "unsupervised", "reinforcement", "classification", "regression",
            "feature", "label", "target", "prediction", "accuracy", "precision", "recall",
            "f1", "overfitting", "underfitting", "bias", "variance", "loss", "gradient",
            "tree", "forest", "svm", "knn", "cluster", "clustering", "neural", "network",
            "cricket", "sports", "healthcare", "finance"
    ));

    private static final List<String> AMBIGUOUS_WORDS = Arrays.asList(
            "explain model", "what is model", "model kya hai", "model",
            "explain data", "what is data"
    );

    public static boolean isMLRelated(String rawQuery) {
        if (rawQuery == null || rawQuery.trim().isEmpty()) return false;
        String lower = rawQuery.toLowerCase(Locale.ROOT);

        for (String anchor : ML_DOMAIN_ANCHORS) {
            if (lower.contains(anchor)) {
                return true;
            }
        }

        List<String> keywords = NLPProcessor.extractKeywords(rawQuery);
        for (Topic topic : KnowledgeBase.getInstance().getAllTopics()) {
            double sim = NLPProcessor.jaccardSimilarity(keywords, topic.getKeywords());
            if (sim > 0.2) return true;
        }

        return false;
    }

    public static String detectComparisonPairKey(String rawQuery) {
        String lower = rawQuery.toLowerCase(Locale.ROOT);

        if ((lower.contains("supervised") && lower.contains("unsupervised")) ||
                (lower.contains("supervised vs") || lower.contains("unsupervised vs"))) {
            return "supervised_vs_unsupervised";
        }
        if (lower.contains("classification") && lower.contains("regression")) {
            return "classification_vs_regression";
        }
        if ((lower.contains("machine learning") && lower.contains("deep learning")) ||
                (lower.contains("ml vs dl") || lower.contains("ml versus dl"))) {
            return "ml_vs_dl";
        }
        if (lower.contains("decision tree") && lower.contains("random forest")) {
            return "decision_tree_vs_random_forest";
        }
        if ((lower.contains("svm") && lower.contains("knn")) ||
                (lower.contains("svm vs knn") || lower.contains("knn vs svm"))) {
            return "svm_vs_knn";
        }
        if (lower.contains("precision") && lower.contains("recall")) {
            return "precision_vs_recall";
        }
        if (lower.contains("overfitting") && lower.contains("underfitting")) {
            return "overfitting_vs_underfitting";
        }
        if (lower.contains("normalization") && lower.contains("standardization")) {
            return "normalization_vs_standardization";
        }
        if (lower.contains("bagging") && lower.contains("boosting")) {
            return "bagging_vs_boosting";
        }

        return null;
    }

    public static boolean isAmbiguousQuery(String rawQuery) {
        String normalized = NLPProcessor.normalizeText(rawQuery);
        for (String amb : AMBIGUOUS_WORDS) {
            if (normalized.equalsIgnoreCase(amb)) {
                return true;
            }
        }
        return false;
    }

    public static Topic findBestMatchingTopic(String rawQuery) {
        if (rawQuery == null || rawQuery.trim().isEmpty()) return null;
        String lower = rawQuery.toLowerCase(Locale.ROOT);
        KnowledgeBase kb = KnowledgeBase.getInstance();

        // 1. Priority: Specific Multi-word Phrase & Exact Intent Mapping
        if (lower.contains("types of machine learning") || lower.contains("types of ml") ||
                lower.contains("categories of machine learning") || lower.contains("different types of ml") ||
                lower.contains("how many types of machine learning")) {
            return kb.getTopicById("ml_types");
        }
        if (lower.contains("what is machine learning") || lower.contains("machine learning definition") ||
                lower.contains("what is ml") || lower.contains("define machine learning")) {
            return kb.getTopicById("ml_definition");
        }
        if (lower.contains("support vector machine") || lower.contains("svm")) {
            return kb.getTopicById("svm");
        }
        if (lower.contains("k-nearest neighbors") || lower.contains("nearest neighbors") || lower.contains("knn")) {
            return kb.getTopicById("knn");
        }
        if (lower.contains("random forest")) {
            return kb.getTopicById("random_forest");
        }
        if (lower.contains("decision tree")) {
            return kb.getTopicById("decision_tree");
        }
        if (lower.contains("linear regression")) {
            return kb.getTopicById("linear_regression");
        }
        if (lower.contains("logistic regression")) {
            return kb.getTopicById("logistic_regression");
        }
        if (lower.contains("naive bayes")) {
            return kb.getTopicById("naive_bayes");
        }
        if (lower.contains("gradient boosting") || lower.contains("xgboost")) {
            return kb.getTopicById("gradient_boosting");
        }
        if (lower.contains("k-means") || lower.contains("kmeans")) {
            return kb.getTopicById("k_means");
        }
        if (lower.contains("principal component analysis") || lower.contains("pca")) {
            return kb.getTopicById("pca");
        }
        if (lower.contains("neural network")) {
            return kb.getTopicById("neural_network");
        }
        if (lower.contains("deep learning")) {
            return kb.getTopicById("deep_learning");
        }
        if (lower.contains("overfitting")) {
            return kb.getTopicById("overfitting");
        }
        if (lower.contains("underfitting")) {
            return kb.getTopicById("underfitting");
        }
        if (lower.contains("bias") && lower.contains("variance")) {
            return kb.getTopicById("bias");
        }
        if (lower.contains("cross validation") || lower.contains("k-fold")) {
            return kb.getTopicById("cross_validation");
        }
        if (lower.contains("feature engineering") || lower.contains("feature scaling")) {
            return kb.getTopicById("feature_engineering");
        }
        if (lower.contains("normalization") && lower.contains("standardization")) {
            return kb.getTopicById("normalization");
        }
        if (lower.contains("normalization")) {
            return kb.getTopicById("normalization");
        }
        if (lower.contains("standardization")) {
            return kb.getTopicById("normalization");
        }
        if (lower.contains("accuracy") || lower.contains("precision") || lower.contains("recall") ||
                lower.contains("f1 score") || lower.contains("confusion matrix")) {
            return kb.getTopicById("evaluation_metrics");
        }
        if (lower.contains("hyperparameter tuning") || lower.contains("grid search")) {
            return kb.getTopicById("hyperparameter_tuning");
        }
        if (lower.contains("regularization") || lower.contains("lasso") || lower.contains("ridge")) {
            return kb.getTopicById("regularization");
        }
        if (lower.contains("gradient descent")) {
            return kb.getTopicById("gradient_descent");
        }
        if (lower.contains("cricket") && (lower.contains("ml") || lower.contains("machine learning") || lower.contains("used"))) {
            return kb.getTopicById("cricket_applications");
        }
        if (lower.contains("supervised learning")) {
            return kb.getTopicById("supervised_learning");
        }
        if (lower.contains("unsupervised learning")) {
            return kb.getTopicById("unsupervised_learning");
        }
        if (lower.contains("reinforcement learning")) {
            return kb.getTopicById("reinforcement_learning");
        }
        if (lower.contains("classification")) {
            return kb.getTopicById("classification");
        }
        if (lower.contains("regression")) {
            return kb.getTopicById("regression");
        }

        // 2. Fallback to scoring all topics
        List<String> queryTokens = NLPProcessor.tokenize(rawQuery);
        List<String> queryKeywords = NLPProcessor.removeStopWords(queryTokens);
        String normalizedQuery = NLPProcessor.normalizeText(rawQuery);

        Topic bestTopic = null;
        double highestScore = 0.0;

        for (Topic topic : kb.getAllTopics()) {
            double score = 0.0;
            String normName = NLPProcessor.normalizeText(topic.getName());

            if (normalizedQuery.equalsIgnoreCase(normName)) {
                score += 15.0;
            } else if (normalizedQuery.contains(normName) || normName.contains(normalizedQuery)) {
                score += 8.0;
            }

            for (String kw : topic.getKeywords()) {
                String normKw = NLPProcessor.normalizeText(kw);
                if (normalizedQuery.contains(normKw)) {
                    score += 5.0;
                }
            }

            double jaccard = NLPProcessor.jaccardSimilarity(queryKeywords, topic.getKeywords());
            score += jaccard * 4.0;

            if (score > highestScore) {
                highestScore = score;
                bestTopic = topic;
            }
        }

        return (highestScore >= 2.0) ? bestTopic : null;
    }
}