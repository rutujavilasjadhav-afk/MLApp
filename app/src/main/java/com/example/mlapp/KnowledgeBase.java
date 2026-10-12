package com.example.mlapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KnowledgeBase {

    private static KnowledgeBase instance;
    private final Map<String, Topic> topicsById;
    private final Map<String, String> comparisonMap;

    private KnowledgeBase() {
        topicsById = new HashMap<>();
        comparisonMap = new HashMap<>();
        populateTopics();
        populateComparisons();
    }

    public static synchronized KnowledgeBase getInstance() {
        if (instance == null) {
            instance = new KnowledgeBase();
        }
        return instance;
    }

    public List<Topic> getAllTopics() {
        return new ArrayList<>(topicsById.values());
    }

    public Topic getTopicById(String id) {
        return topicsById.get(id);
    }

    public String getComparison(String pairKey) {
        return comparisonMap.get(pairKey.toLowerCase());
    }

    private void addTopic(Topic topic) {
        topicsById.put(topic.getId().toLowerCase(), topic);
    }

    private void populateTopics() {
        // 1. ML Definition
        addTopic(new Topic.Builder("ml_definition", "Machine Learning Definition")
                .category("Fundamentals")
                .keywords("machine learning", "what is machine learning", "ml definition", "ml meaning", "what is ml", "define machine learning")
                .synonyms("machine learning", "ml", "ml kya hai")
                .shortAnswer("Machine Learning (ML) is a branch of Artificial Intelligence that allows computers to learn patterns from data and make predictions without being explicitly programmed.")
                .normalAnswer("Machine Learning is a subset of Artificial Intelligence where algorithms process datasets, discover underlying statistical patterns, and build mathematical models to make decisions and predictions on new, unseen data.\n\n" +
                        "Key Aspects:\n" +
                        "• Data-driven decision making rather than hardcoded rules\n" +
                        "• Improves accuracy automatically through experience\n" +
                        "• Example: An email spam filter automatically learning to identify spam based on historical word patterns.")
                .deepConcept("Machine Learning represents a paradigm shift from traditional rule-based programming. In classical programming, humans write code (rules) and input data to produce output. In Machine Learning, data and historical answers are fed into algorithms to compute rules/models that generalize to novel scenarios.\n\n" +
                        "The ML Workflow:\n" +
                        "1. Problem Definition\n" +
                        "2. Data Collection & Preprocessing\n" +
                        "3. Feature Engineering\n" +
                        "4. Model Selection & Training\n" +
                        "5. Evaluation & Hyperparameter Tuning\n" +
                        "6. Deployment & Monitoring")
                .howItWorks("ML models analyze input vectors (features) through loss functions and optimization algorithms (like Gradient Descent) to iteratively adjust parameters (weights and biases) until predictions match targets with high accuracy.")
                .examples("• Email Spam Detection (Spam vs Inbox)\n• Medical Diagnosis (Detecting tumors from X-rays)\n• Recommendation Systems (Netflix, YouTube)")
                .advantages("• Handles complex, high-dimensional datasets\n• Continuous improvement over time with new data\n• Automates routine and intricate decision tasks")
                .limitations("• Requires large amounts of clean data\n• Susceptible to algorithmic bias and data noise\n• Black-box models lack interpretability")
                .applications("• Healthcare, Fraud Detection, Autonomous Vehicles, Natural Language Processing")
                .relatedTopics("ml_types", "supervised_learning", "deep_learning")
                .build());

        // 2. ML Types
        addTopic(new Topic.Builder("ml_types", "Types of Machine Learning")
                .category("Types of ML")
                .keywords("types of machine learning", "types of ml", "categories of machine learning", "different types of ml", "how many types of machine learning", "types")
                .synonyms("ml types", "categories of ml")
                .shortAnswer("Machine Learning is primarily categorized into four main types: Supervised Learning, Unsupervised Learning, Semi-Supervised Learning, and Reinforcement Learning.")
                .normalAnswer("Machine Learning is divided into four primary paradigms based on the nature of data and feedback available:\n\n" +
                        "1. **Supervised Learning**: Trained on labeled data (inputs + correct output labels). Example: Spam classification.\n" +
                        "2. **Unsupervised Learning**: Finds hidden structures in unlabeled data. Example: Customer clustering (K-Means).\n" +
                        "3. **Semi-Supervised Learning**: Uses a small amount of labeled data combined with a large amount of unlabeled data.\n" +
                        "4. **Reinforcement Learning**: Trains an agent to make decisions by receiving rewards and penalties in an environment. Example: Game AI / Autonomous driving.")
                .deepConcept("These paradigms cover the spectrum of learning scenarios. Supervised learning requires human annotation; unsupervised learning discovers intrinsic data geometry; semi-supervised bridges annotation scarcity; reinforcement learning solves sequential decision processes via trial and error.")
                .howItWorks("Each paradigm uses distinct optimization objectives: Supervised minimizes prediction error against labels; unsupervised maximizes data density/clustering compactness; reinforcement maximizes cumulative reward.")
                .examples("• Supervised: House price prediction\n• Unsupervised: Customer segmentation\n• Semi-Supervised: Web page classification with few labels\n• Reinforcement: Autonomous robot navigation")
                .advantages("• Covers all possible problem formulations across industry and research")
                .limitations("• Requires choosing the correct paradigm for the specific data structure")
                .applications("• Industry-wide AI applications")
                .relatedTopics("supervised_learning", "unsupervised_learning", "reinforcement_learning")
                .build());

        // 3. Supervised Learning
        addTopic(new Topic.Builder("supervised_learning", "Supervised Learning")
                .category("Types of ML")
                .keywords("supervised learning", "supervised", "supervised machine learning")
                .synonyms("supervised learning", "labeled data learning")
                .shortAnswer("Supervised Learning is a type of machine learning where the model is trained on labeled data containing both input features and correct output labels.")
                .normalAnswer("In Supervised Learning, algorithms learn from input-output pairs. The goal is to learn a mapping function f(X) = y so that when given new, unseen input data X, the model accurately predicts output y.\n\n" +
                        "Main Subtypes:\n" +
                        "1. Classification (predicting discrete categories e.g., Spam / Not Spam)\n" +
                        "2. Regression (predicting continuous numerical values e.g., House Price)")
                .deepConcept("Supervised learning mathematically minimizes a loss function L(y, f(X)) over a labeled dataset D = {(x1, y1), ..., (xn, yn)}.")
                .howItWorks("The model generates predictions, calculates error against target labels, adjusts weights using optimization algorithms, and repeats until convergence.")
                .examples("• House Price Prediction (Regression)\n• Disease Detection (Classification)")
                .advantages("• High accuracy when labeled data is abundant\n• Clear metrics for evaluation")
                .limitations("• Labeling data manually is expensive and time-consuming")
                .applications("• Medical Imaging, Finance Fraud Detection, Weather Forecasting")
                .relatedTopics("classification", "regression", "svm", "knn")
                .build());

        // 4. Unsupervised Learning
        addTopic(new Topic.Builder("unsupervised_learning", "Unsupervised Learning")
                .category("Types of ML")
                .keywords("unsupervised learning", "unsupervised", "clustering", "dimensionality reduction")
                .synonyms("unsupervised learning", "unlabeled data learning")
                .shortAnswer("Unsupervised Learning is a machine learning paradigm that finds hidden patterns, structures, or groupings in unlabeled input data.")
                .normalAnswer("Unlike Supervised Learning, Unsupervised Learning algorithms work with data that has no target labels. The model analyzes relationships between inputs to discover inherent structure (Clustering, Dimensionality Reduction).")
                .deepConcept("Unsupervised algorithms estimate density distributions or geometric structures in high-dimensional feature space without feedback signals.")
                .howItWorks("Algorithms compute distance metrics or variance matrices between feature vectors to cluster data points.")
                .examples("• Customer Segmentation for marketing campaigns\n• Anomaly detection in credit card transactions")
                .advantages("• Does not require costly labeled datasets")
                .limitations("• Harder to evaluate quality because there are no true target labels")
                .applications("• Market Research, Gene Expression Analysis, Image Compression")
                .relatedTopics("k_means", "pca")
                .build());

        // 5. Reinforcement Learning
        addTopic(new Topic.Builder("reinforcement_learning", "Reinforcement Learning")
                .category("Types of ML")
                .keywords("reinforcement learning", "semi supervised learning", "rl", "agent reward state")
                .synonyms("reinforcement learning", "rl", "semi-supervised")
                .shortAnswer("Reinforcement Learning trains an agent to make decisions in an environment by receiving rewards for good actions and penalties for bad ones.")
                .normalAnswer("Reinforcement Learning (RL) involves an Agent interacting with an Environment. The agent takes actions, receives state transitions and scalar rewards/penalties, learning an optimal Policy to maximize cumulative rewards over time.")
                .deepConcept("RL is modeled as a Markov Decision Process (MDP) defined by states, actions, transition probabilities, and reward functions.")
                .howItWorks("The agent explores actions (trial and error), learns an action-value function Q(s,a), and exploits high-reward paths.")
                .examples("• Autonomous Driving (keeping lanes, avoiding collisions)\n• Game Playing AI (AlphaGo, Chess)")
                .advantages("• Solves sequential decision-making problems where optimal steps are unknown")
                .limitations("• Requires millions of simulation iterations")
                .applications("• Game AI, Robotics, Financial Trading Strategy")
                .relatedTopics("supervised_learning", "unsupervised_learning")
                .build());

        // 6. Classification
        addTopic(new Topic.Builder("classification", "Classification")
                .category("Supervised Learning")
                .keywords("classification", "classifier", "binary classification", "multiclass classification")
                .synonyms("classification", "categorical prediction")
                .shortAnswer("Classification is a supervised learning task where the target output is a categorical or discrete class label.")
                .normalAnswer("Classification assigns input data into discrete categories or classes (Binary: Spam/Not Spam; Multiclass: Digit recognition 0-9).")
                .deepConcept("Classification algorithms model boundary decision surfaces separating classes in feature space using probability outputs.")
                .howItWorks("Input vector X -> Model computes probability P(y=c|X) -> Decision Threshold -> Predicted Class C.")
                .examples("• Spam Detection\n• Handwriting Digit Recognition")
                .advantages("• Directly applicable to discrete decision-making problems")
                .limitations("• Sensitive to class imbalance")
                .applications("• Medical Screening, Fraud Detection")
                .relatedTopics("logistic_regression", "svm", "knn", "decision_tree")
                .build());

        // 7. Regression
        addTopic(new Topic.Builder("regression", "Regression")
                .category("Supervised Learning")
                .keywords("regression", "continuous prediction", "linear regression model")
                .synonyms("regression", "numerical prediction")
                .shortAnswer("Regression is a supervised learning task that predicts continuous numerical values (e.g., price, temperature, score).")
                .normalAnswer("In Regression, the algorithm learns a mapping function to output continuous numerical quantities rather than discrete categories.")
                .deepConcept("Regression computes continuous outputs y = f(X) + epsilon, minimized via MSE or MAE loss functions.")
                .howItWorks("Minimizes error distance between predicted floating-point value and actual continuous value across samples.")
                .examples("• House Price Prediction\n• Salary estimation")
                .advantages("• Provides precise quantitative estimates")
                .limitations("• Sensitive to numerical outliers")
                .applications("• Financial Forecasting, Sales Prediction")
                .relatedTopics("linear_regression", "mse", "mae")
                .build());

        // 8. SVM (Support Vector Machine)
        addTopic(new Topic.Builder("svm", "Support Vector Machine (SVM)")
                .category("Algorithms")
                .keywords("svm", "support vector machine", "hyperplane", "kernel trick", "support vectors", "margin")
                .synonyms("svm", "support vector machines", "support vector classifier")
                .shortAnswer("Support Vector Machine (SVM) is a powerful supervised algorithm that finds an optimal hyperplane with maximum margin separating different classes.")
                .normalAnswer("SVM operates by finding the decision boundary (hyperplane) in n-dimensional space that maximizes the margin (distance) between data points of different classes.\n\n" +
                        "Key Concepts:\n" +
                        "• Hyperplane: The decision boundary separating classes.\n" +
                        "• Support Vectors: Data points closest to the hyperplane that influence its position.\n" +
                        "• Margin: The distance between the hyperplane and closest support vectors.\n" +
                        "• Kernel Trick: Transforms non-linear data into higher dimensions where a linear hyperplane can separate them (e.g., RBF kernel).")
                .deepConcept("SVM minimizes structural risk by solving a constrained quadratic optimization problem: min 0.5||w||^2 + C * sum(xi), subject to class separation constraints. Kernels like Linear, Polynomial, and RBF allow SVM to handle complex non-linear boundaries efficiently.")
                .howItWorks("1. Identifies features and classes.\n2. Computes the optimal separating hyperplane maximizing the margin.\n3. Uses support vector points to anchor the decision boundary.\n4. Applies kernel functions if data is non-linearly separable.")
                .examples("• Text and document classification\n• Handwritten digit recognition\n• Protein structure classification in bioinformatics")
                .advantages("• Highly effective in high-dimensional spaces\n• Memory efficient because it uses a subset of training points (support vectors)\n• Versatile through customizable kernel functions")
                .limitations("• Computationally intensive on very large datasets\n• Sensitive to noise and overlapping class outliers\n• Harder to interpret than decision trees")
                .applications("• Text categorization, Image classification, Bioinformatics")
                .relatedTopics("knn", "logistic_regression", "classification")
                .build());

        // 9. KNN (K-Nearest Neighbors)
        addTopic(new Topic.Builder("knn", "K-Nearest Neighbors (KNN)")
                .category("Algorithms")
                .keywords("knn", "k nearest neighbors", "nearest neighbor", "euclidean distance", "k value")
                .synonyms("knn", "k-nearest neighbors", "nearest neighbors algorithm")
                .shortAnswer("K-Nearest Neighbors (KNN) is a non-parametric, lazy learning algorithm that classifies a data point based on the majority vote of its K nearest neighbors.")
                .normalAnswer("KNN is a simple, intuitive algorithm where classification or regression is determined by the closest data points in feature space.\n\n" +
                        "How it determines proximity:\n" +
                        "• Calculates distance (e.g., Euclidean distance) between the query point and all training points.\n" +
                        "• Selects the top K nearest points.\n" +
                        "• Assigns the most common class among those K neighbors.")
                .deepConcept("KNN is a 'lazy learner' (instance-based learning), meaning it builds no explicit parametric model during training; instead, all computation is deferred to the query/prediction phase.")
                .howItWorks("1. Choose K (number of neighbors).\n2. Calculate distance metrics (Euclidean, Manhattan) to all training samples.\n3. Sort distances and pick top K.\n4. Perform majority vote (classification) or average (regression).")
                .examples("• Recommending products or movies based on user similarity\n• Simple handwritten digit recognition")
                .advantages("• Simple to understand and implement with zero training phase")
                .limitations("• Very slow prediction time for large datasets\n• Highly sensitive to irrelevant features and unscaled data")
                .applications("• Recommendation engines, Pattern recognition, Anomaly detection")
                .relatedTopics("svm", "classification", "feature_scaling")
                .build());

        // 10. Decision Tree
        addTopic(new Topic.Builder("decision_tree", "Decision Tree")
                .category("Algorithms")
                .keywords("decision tree", "entropy", "gini impurity", "information gain", "tree split", "root node")
                .synonyms("decision tree", "dt", "cart algorithm")
                .shortAnswer("A Decision Tree is a non-parametric model that splits data into branch decisions based on feature rules to make predictions.")
                .normalAnswer("Decision Trees structure decisions like an upside-down tree:\n" +
                        "• Root Node: Top starting feature split\n" +
                        "• Internal Nodes: Decision splits on features\n" +
                        "• Leaf Nodes: Final predicted outcome.\n\n" +
                        "Splits are chosen using metrics like Gini Impurity or Information Gain (Entropy).")
                .deepConcept("Decision Trees partition feature space into axis-aligned rectangular regions. Splitting is optimized to maximize Information Gain at each node.")
                .howItWorks("Evaluates all features and split thresholds, chooses the split maximizing Information Gain, and recursively splits until stopping criteria are met.")
                .examples("• Loan Approval Decision Tree\n• Weather decision tree for outdoor activities")
                .advantages("• Easy to visualize and explain\n• Handles numerical and categorical data without scaling")
                .limitations("• Highly prone to overfitting if not pruned")
                .applications("• Medical diagnosis flowcharts, Credit Risk Analysis")
                .relatedTopics("random_forest", "overfitting", "gradient_boosting")
                .build());

        // 11. Random Forest
        addTopic(new Topic.Builder("random_forest", "Random Forest")
                .category("Algorithms")
                .keywords("random forest", "rf", "bagging trees", "ensemble trees", "forest classifier")
                .synonyms("random forest", "ensemble decision trees")
                .shortAnswer("Random Forest is an ensemble algorithm that builds multiple individual decision trees using random data subsets and merges their predictions for higher accuracy.")
                .normalAnswer("Random Forest combines many Decision Trees using Bagging (Bootstrap Aggregating) and feature randomness to prevent overfitting and boost predictive performance.")
                .deepConcept("Introduces two levels of randomness: Bootstrap sampling of training data and random feature selection at each tree split.")
                .howItWorks("1. Generate N bootstrap datasets.\n2. Train N decision trees using random feature subsets.\n3. Aggregate predictions via majority vote or averaging.")
                .examples("• Predicting customer churn\n• Credit scoring and fraud detection")
                .advantages("• Highly accurate, robust against overfitting, provides feature importance")
                .limitations("• Slower prediction time than single trees")
                .applications("• Finance, Healthcare, E-Commerce")
                .relatedTopics("decision_tree", "ensemble_learning", "bagging")
                .build());

        // 12. Linear Regression
        addTopic(new Topic.Builder("linear_regression", "Linear Regression")
                .category("Algorithms")
                .keywords("linear regression", "linear model", "least squares", "slope and intercept", "ols")
                .synonyms("linear regression", "ols", "ordinary least squares")
                .shortAnswer("Linear Regression models the linear relationship between independent features (X) and a continuous target variable (y) using a straight line equation y = mx + c.")
                .normalAnswer("Linear Regression fits a straight line through data points to minimize prediction errors using the equation y = w0 + w1*x1 + ... + wn*xn.")
                .deepConcept("Optimized via Ordinary Least Squares (OLS) or Gradient Descent to minimize Mean Squared Error.")
                .howItWorks("Calculates predicted line, measures error using MSE, updates weights iteratively until residual sum of squares is minimized.")
                .examples("• Predicting house price from square footage")
                .advantages("• Simple, fast, highly interpretable")
                .limitations("• Cannot capture complex non-linear patterns without feature transformation")
                .applications("• Sales Forecasting, Economic Trend Modeling")
                .relatedTopics("regression", "logistic_regression", "gradient_descent")
                .build());

        // 13. Logistic Regression
        addTopic(new Topic.Builder("logistic_regression", "Logistic Regression")
                .category("Algorithms")
                .keywords("logistic regression", "logit", "sigmoid function", "binary classification algorithm")
                .synonyms("logistic regression", "sigmoid classifier")
                .shortAnswer("Logistic Regression is a classification algorithm that uses the Sigmoid function to map linear outputs into probabilities between 0 and 1.")
                .normalAnswer("Despite its name, Logistic Regression is used for Classification. It takes linear combination of features and passes it through the Sigmoid activation function.")
                .deepConcept("Models log-odds: ln(p / (1-p)) = w^T X, optimized via Maximum Likelihood Estimation (MLE).")
                .howItWorks("1. Calculates z = w^T X + b\n2. Applies Sigmoid function p = 1 / (1 + e^-z)\n3. Compares against threshold 0.5.")
                .examples("• Email Spam Classification (Spam/Not Spam)")
                .advantages("• Outputs well-calibrated class probabilities")
                .limitations("• Assumes linear decision boundary")
                .applications("• Medical Risk Assessment, Credit Scoring")
                .relatedTopics("classification", "linear_regression", "sigmoid")
                .build());

        // 14. Naive Bayes
        addTopic(new Topic.Builder("naive_bayes", "Naive Bayes")
                .category("Algorithms")
                .keywords("naive bayes", "bayes theorem", "conditional probability", "text classification")
                .synonyms("naive bayes classifier")
                .shortAnswer("Naive Bayes is a fast probabilistic classifier based on Bayes' Theorem, assuming conditional independence between features.")
                .normalAnswer("Naive Bayes calculates the probability of each class given input features using Bayes' rule P(y|X) = P(X|y)*P(y) / P(X), assuming features are independent.")
                .deepConcept("Highly efficient for high-dimensional text datasets even when independence assumption is violated.")
                .howItWorks("Computes class prior probabilities and conditional feature probabilities from training data to classify new samples.")
                .examples("• Spam filtering, Sentiment analysis")
                .advantages("• Extremely fast, works well with small training data and high dimensions")
                .limitations("• Zero-frequency problem for unseen feature values")
                .applications("• Text Categorization, Spam Detection")
                .relatedTopics("classification", "logistic_regression")
                .build());

        // 15. Gradient Boosting
        addTopic(new Topic.Builder("gradient_boosting", "Gradient Boosting")
                .category("Algorithms")
                .keywords("gradient boosting", "xgboost", "boosting", "gbm", "gradient boosted trees")
                .synonyms("gradient boosting", "xgboost")
                .shortAnswer("Gradient Boosting is an ensemble technique that builds trees sequentially, where each new tree specifically trains to correct errors (residuals) made by previous trees.")
                .normalAnswer("Gradient Boosting (including XGBoost, LightGBM) combines weak decision trees sequentially. Each successive tree fits the gradient of the loss function.")
                .deepConcept("Minimizes loss by adding weak learners using gradient descent in function space.")
                .howItWorks("1. Train base model.\n2. Compute pseudo-residuals (errors).\n3. Train next tree on residuals.\n4. Add to ensemble with learning rate shrinkage.")
                .examples("• Winning Kaggle machine learning competitions")
                .advantages("• State-of-the-art accuracy on tabular data")
                .limitations("• Prone to overfitting if hyperparameters are untuned; slower training")
                .applications("• Search ranking, Fraud detection, Competitions")
                .relatedTopics("random_forest", "bagging", "ensemble_learning")
                .build());

        // 16. K-Means
        addTopic(new Topic.Builder("k_means", "K-Means Clustering")
                .category("Algorithms")
                .keywords("k means", "kmeans", "clustering", "centroid", "unsupervised clustering")
                .synonyms("k-means", "kmeans clustering")
                .shortAnswer("K-Means is an unsupervised clustering algorithm that partitions a dataset into K distinct clusters by iteratively assigning points to the nearest cluster centroid.")
                .normalAnswer("K-Means groups unlabelled data into K clusters. The algorithm alternates between assigning points to centroids and recalculating centroid positions.")
                .deepConcept("Minimizes within-cluster variance (inertia): sum of squared distances from points to assigned cluster centroids.")
                .howItWorks("1. Initialize K random centroids.\n2. Assign each data point to nearest centroid.\n3. Recalculate centroids as mean of assigned points.\n4. Repeat until convergence.")
                .examples("• Customer segmentation by purchasing behavior")
                .advantages("• Simple, fast, and scales well to large datasets")
                .limitations("• Must pre-specify K; sensitive to initial centroid placement and outliers")
                .applications("• Market Segmentation, Image Compression")
                .relatedTopics("unsupervised_learning", "pca")
                .build());

        // 17. PCA
        addTopic(new Topic.Builder("pca", "Principal Component Analysis (PCA)")
                .category("Algorithms")
                .keywords("pca", "principal component analysis", "dimensionality reduction", "eigenvectors", "variance")
                .synonyms("pca", "dimensionality reduction")
                .shortAnswer("PCA is an unsupervised dimensionality reduction technique that transforms high-dimensional data into orthogonal principal components while retaining maximum variance.")
                .normalAnswer("PCA projects high-dimensional data onto lower-dimensional principal axes (eigenvectors of the covariance matrix) that maximize data variance.")
                .deepConcept("Performs linear transformation preserving maximum spread, reducing feature space complexity.")
                .howItWorks("1. Standardize data.\n2. Compute covariance matrix.\n3. Compute eigenvectors and eigenvalues.\n4. Project data onto top principal components.")
                .examples("• Visualizing 100-dimensional dataset in 2D scatter plots")
                .advantages("• Removes multicollinearity, reduces computation time")
                .limitations("• Assumes linear relationships; principal components lack direct physical interpretation")
                .applications("• Data Visualization, Noise Filtering, Image Processing")
                .relatedTopics("unsupervised_learning", "feature_scaling")
                .build());

        // 18. Neural Networks
        addTopic(new Topic.Builder("neural_network", "Neural Networks")
                .category("Algorithms")
                .keywords("neural networks", "artificial neural network", "ann", "neurons", "layers", "backpropagation")
                .synonyms("neural network", "ann", "multilayer perceptron")
                .shortAnswer("Neural Networks are computing systems inspired by biological brains, composed of interconnected layers of artificial neurons that learn complex non-linear patterns.")
                .normalAnswer("An Artificial Neural Network consists of an input layer, one or more hidden layers, and an output layer. Neurons apply weights, biases, and activation functions.")
                .deepConcept("Trained via Forward Propagation and Backpropagation using Gradient Descent and chain rule differentiation.")
                .howItWorks("Inputs -> Weighted sum + bias -> Activation function -> Hidden layers -> Output prediction -> Backpropagation weight update.")
                .examples("• Image recognition, Speech synthesis")
                .advantages("• Can model highly complex non-linear functions")
                .limitations("• Requires large data and compute; black-box interpretability")
                .applications("• Computer Vision, NLP, Speech Recognition")
                .relatedTopics("deep_learning", "gradient_descent")
                .build());

        // 19. Deep Learning
        addTopic(new Topic.Builder("deep_learning", "Deep Learning")
                .category("Types of ML")
                .keywords("deep learning", "dl", "deep neural network", "representation learning")
                .synonyms("deep learning", "dl")
                .shortAnswer("Deep Learning is a specialized branch of Machine Learning using deep multi-layered neural networks to automatically extract hierarchical features from raw data.")
                .normalAnswer("Deep Learning eliminates manual feature engineering by using neural networks with many hidden layers to learn complex representations directly from raw images, audio, and text.")
                .deepConcept("Scales performance massively with big data and GPU acceleration.")
                .howItWorks("Propagates raw data through deep hierarchical layers, automatically extracting edge, shape, and semantic representations.")
                .examples("• ChatGPT, Facial Recognition, Autonomous driving vision")
                .advantages("• Unmatched accuracy on unstructured data (images, text, audio)")
                .limitations("• Requires massive labeled datasets and expensive hardware (GPUs)")
                .applications("• Computer Vision, LLMs, Speech Recognition")
                .relatedTopics("neural_network", "ml_vs_dl")
                .build());

        // 20. Overfitting
        addTopic(new Topic.Builder("overfitting", "Overfitting")
                .category("Model Problems")
                .keywords("overfitting", "overfit", "high variance", "memorizing noise")
                .synonyms("overfitting", "model overfit")
                .shortAnswer("Overfitting occurs when a model learns training data noise and specific details too well, resulting in poor generalization on new test data.")
                .normalAnswer("An overfitted model has high training accuracy but low test accuracy because it memorized the training set rather than learning general patterns.")
                .deepConcept("Caused by excessive model complexity relative to training data size (High Variance).")
                .howItWorks("Model fits training data points almost perfectly with complex wiggly boundaries, failing on unseen samples.")
                .examples("• A student who memorizes exact textbook practice questions but fails exam questions with changed wording.")
                .advantages("• None (undesirable model state)")
                .limitations("• Fails in production on real-world unseen data")
                .applications("• Diagnosed via train-test performance divergence")
                .relatedTopics("underfitting", "regularization", "cross_validation")
                .build());

        // 21. Underfitting
        addTopic(new Topic.Builder("underfitting", "Underfitting")
                .category("Model Problems")
                .keywords("underfitting", "underfit", "high bias", "oversimplification")
                .synonyms("underfitting", "model underfit")
                .shortAnswer("Underfitting occurs when a model is too simple to capture the underlying patterns in the data, resulting in poor performance on both training and test sets.")
                .normalAnswer("An underfitted model has high bias because its assumptions are too restrictive (e.g. trying to fit curved data with a straight line).")
                .deepConcept("Caused by insufficient model complexity or inadequate features.")
                .howItWorks("Model fails to capture data trends, leaving high residual errors.")
                .examples("• Trying to predict housing prices using only the number of doors.")
                .advantages("• None (undesirable model state)")
                .limitations("• Inability to solve the problem")
                .applications("• Diagnosed by low training and validation accuracy")
                .relatedTopics("overfitting", "bias", "variance")
                .build());

        // 22. Bias & Variance
        addTopic(new Topic.Builder("bias", "Bias and Variance")
                .category("Model Problems")
                .keywords("bias", "variance", "bias variance tradeoff", "model error")
                .synonyms("bias and variance", "variance tradeoff")
                .shortAnswer("Bias is the error from erroneous assumptions in the model (leading to underfitting), while Variance is the error from sensitivity to fluctuations in training data (leading to overfitting).")
                .normalAnswer("• Bias: Error from oversimplifying assumptions.\n• Variance: Error from sensitivity to small changes in training data.\n\n" +
                        "Bias-Variance Tradeoff: Balancing model complexity to minimize total error.")
                .deepConcept("Total Error = Bias^2 + Variance + Irreducible Error.")
                .howItWorks("Complex models have low bias and high variance; simple models have high bias and low variance.")
                .examples("• Linear line on quadratic data = High bias; 100-degree polynomial = High variance.")
                .advantages("• Guides model selection and regularization tuning")
                .limitations("• Tradeoff cannot be completely eliminated")
                .applications("• Model evaluation and tuning")
                .relatedTopics("overfitting", "underfitting", "hyperparameter_tuning")
                .build());

        // 23. Cross Validation
        addTopic(new Topic.Builder("cross_validation", "Cross Validation")
                .category("Advanced")
                .keywords("cross validation", "k fold", "k-fold cross validation", "validation fold")
                .synonyms("cross validation", "k-fold")
                .shortAnswer("Cross Validation is a robust evaluation technique where the dataset is split into K folds, training on K-1 folds and validating on the remaining fold repeatedly.")
                .normalAnswer("K-Fold Cross Validation ensures every data point is used for both training and validation, providing an unbiased performance estimate.")
                .deepConcept("Reduces variance in evaluation scores compared to a single train-test split.")
                .howItWorks("1. Split data into K parts.\n2. Train on K-1 parts, test on 1 part.\n3. Repeat K times.\n4. Average accuracy across folds.")
                .examples("• Evaluating model stability across 5 folds of data")
                .advantages("• Prevents lucky or unlucky test train splits")
                .limitations("• Increases training time K-fold")
                .applications("• Model validation and hyperparameter tuning")
                .relatedTopics("hyperparameter_tuning", "dataset_split")
                .build());

        // 24. Feature Engineering & Scaling
        addTopic(new Topic.Builder("feature_engineering", "Feature Engineering & Scaling")
                .category("Preprocessing")
                .keywords("feature engineering", "feature scaling", "normalization", "standardization", "data preprocessing")
                .synonyms("feature engineering", "scaling features")
                .shortAnswer("Feature Engineering creates new informative variables from raw data, while Feature Scaling normalizes numerical ranges so algorithms learn effectively.")
                .normalAnswer("Preprocessing includes feature creation, handling missing values, encoding categorical data, and scaling features via Normalization ([0,1]) or Standardization (Zero mean, unit variance).")
                .deepConcept("Ensures distance-based algorithms (KNN, SVM, Gradient Descent) are not biased by features with larger numerical magnitudes.")
                .howItWorks("Transforming raw columns into standardized numerical vectors suitable for model ingestion.")
                .examples("• Combining height and weight into BMI feature")
                .advantages("• Dramatically improves model convergence speed and accuracy")
                .limitations("• Risk of data leakage if scaled before train-test split")
                .applications("• Universal ML preprocessing step")
                .relatedTopics("normalization", "standardization", "data_preprocessing")
                .build());

        // 25. Normalization vs Standardization
        addTopic(new Topic.Builder("normalization", "Normalization and Standardization")
                .category("Preprocessing")
                .keywords("normalization", "standardization", "min max scaling", "z score scaling")
                .synonyms("normalization vs standardization")
                .shortAnswer("Normalization scales feature values strictly to a [0, 1] range (Min-Max), while Standardization centers features to zero mean and unit variance (Z-score).")
                .normalAnswer("• Normalization: (x - min) / (max - min)\n• Standardization: (x - mean) / std_dev\n\nNormalization is sensitive to outliers; standardization handles outliers better.")
                .deepConcept("Rescales feature distributions for distance-based and gradient-based models.")
                .howItWorks("Applies mathematical scaling formulas column-wise across dataset.")
                .examples("• Scaling test scores [0, 100] to [0, 1] via normalization")
                .advantages("• Essential for KNN, SVM, and Neural Networks")
                .limitations("• Tree-based models do not require scaling")
                .applications("• Preprocessing pipelines")
                .relatedTopics("feature_scaling", "data_preprocessing")
                .build());

        // 26. Evaluation Metrics (Accuracy, Precision, Recall, F1, Confusion Matrix)
        addTopic(new Topic.Builder("evaluation_metrics", "Model Evaluation Metrics (Accuracy, Precision, Recall, F1)")
                .category("Evaluation")
                .keywords("accuracy", "precision", "recall", "f1 score", "confusion matrix", "roc auc")
                .synonyms("evaluation metrics", "classification metrics")
                .shortAnswer("Evaluation metrics quantify classification performance: Accuracy (overall correct), Precision (true positives / predicted positives), Recall (true positives / actual positives), and F1 Score (harmonic mean).")
                .normalAnswer("• Accuracy: (TP+TN)/(TP+TN+FP+FN)\n• Precision: TP/(TP+FP) [Minimizes false alarms]\n• Recall: TP/(TP+FN) [Minimizes missed cases]\n• F1 Score: 2 * (Precision * Recall) / (Precision + Recall)\n• Confusion Matrix: 2x2 table of TP, FP, TN, FN.")
                .deepConcept("Confusion matrix breaks down exact prediction outcomes to evaluate classification error distribution.")
                .howItWorks("Compares model class predictions against true dataset labels.")
                .examples("• Medical diagnosis prioritizing high Recall to catch all diseases.")
                .advantages("• Provides nuanced insight beyond simple accuracy on imbalanced data")
                .limitations("• Must choose correct metric based on business cost")
                .applications("• Model validation")
                .relatedTopics("classification", "confusion_matrix")
                .build());

        // 27. Hyperparameter Tuning
        addTopic(new Topic.Builder("hyperparameter_tuning", "Hyperparameter Tuning")
                .category("Advanced")
                .keywords("hyperparameter tuning", "grid search", "random search", "model tuning")
                .synonyms("hyperparameter optimization")
                .shortAnswer("Hyperparameter Tuning is the process of finding the optimal configuration of model parameters (like tree depth or learning rate) using Grid Search or Random Search.")
                .normalAnswer("Unlike internal weights learned during training, hyperparameters (e.g., K in KNN, C in SVM, learning rate) must be set before training. Grid Search tests all combinations; Random Search samples randomly.")
                .deepConcept("Optimizes validation performance over a search space.")
                .howItWorks("Iteratively trains models with different hyperparameter combinations and evaluates validation scores.")
                .examples("• Finding optimal max_depth for Random Forest")
                .advantages("• Maximizes model predictive power")
                .limitations("• Computationally expensive")
                .applications("• ML model optimization")
                .relatedTopics("cross_validation", "overfitting")
                .build());

        // 28. Regularization
        addTopic(new Topic.Builder("regularization", "Regularization (L1 & L2)")
                .category("Advanced")
                .keywords("regularization", "l1 regularization", "l2 regularization", "lasso", "ridge")
                .synonyms("regularization l1 l2", "lasso ridge")
                .shortAnswer("Regularization adds a penalty term to the loss function to prevent model overfitting by discouraging overly complex weights (L1 Lasso shrinks weights to zero; L2 Ridge shrinks weights close to zero).")
                .normalAnswer("• L1 Regularization (Lasso): Adds sum of absolute weights (|w|). Can drive weights to 0 (useful for feature selection).\n• L2 Regularization (Ridge): Adds sum of squared weights (w^2). Keeps weights small and distributed.")
                .deepConcept("Controls model capacity: Loss = OriginalLoss + lambda * Penalty.")
                .howItWorks("Penalizes large weights during gradient optimization.")
                .examples("• Preventing logistic regression coefficients from blowing up on noisy data")
                .advantages("• Prevents overfitting and improves generalization")
                .limitations("• Requires tuning regularization strength parameter (lambda)")
                .applications("• Regression and Neural Network training")
                .relatedTopics("overfitting", "linear_regression")
                .build());

        // 29. Gradient Descent
        addTopic(new Topic.Builder("gradient_descent", "Gradient Descent")
                .category("Advanced")
                .keywords("gradient descent", "optimization", "learning rate", "loss function optimization")
                .synonyms("gradient descent algorithm")
                .shortAnswer("Gradient Descent is an iterative optimization algorithm used to find the minimum of a loss function by taking steps proportional to the negative gradient.")
                .normalAnswer("Gradient Descent updates model weights in the direction opposite to the gradient of the loss function. The step size is controlled by the Learning Rate.")
                .deepConcept("Core optimization engine behind linear models, logistic regression, and neural networks.")
                .howItWorks("1. Initialize weights randomly.\n2. Compute loss gradient.\n3. Update weights: w = w - learning_rate * gradient.\n4. Repeat until convergence.")
                .examples("• Optimizing weights in linear regression")
                .advantages("• Universal optimization method for differentiable models")
                .limitations("• Can get stuck in local minima or saddle points if learning rate is wrong")
                .applications("• Training neural networks and regression models")
                .relatedTopics("linear_regression", "neural_network", "loss_function")
                .build());

        // 30. Machine Learning in Cricket
        addTopic(new Topic.Builder("cricket_applications", "Machine Learning in Cricket")
                .category("Applications")
                .keywords("cricket", "ml in cricket", "machine learning in cricket", "sports analytics", "win probability")
                .synonyms("cricket ml", "machine learning cricket")
                .shortAnswer("Machine Learning in cricket predicts match win probabilities, player performance, auction valuation, and tracks ball trajectory using computer vision.")
                .normalAnswer("In cricket analytics, ML models process ball-by-ball data (run rate, wickets, pitch conditions) to predict live win probabilities, forecast player scores, and optimize team selection.")
                .deepConcept("Combines historical match databases with real-time ensemble models (XGBoost/Random Forest) to output dynamic win percentages.")
                .howItWorks("Feeds match state vectors into trained classification models to output real-time win probabilities.")
                .examples("• ESPNcricinfo win predictor, IPL player auction analytics")
                .advantages("• Provides data-driven insights for fans and teams")
                .limitations("• Unpredictable match dynamics (weather, sudden collapses) add uncertainty")
                .applications("• Sports broadcasting, franchise auction strategy")
                .relatedTopics("supervised_learning", "classification", "random_forest")
                .build());
    }

    private void populateComparisons() {
        comparisonMap.put("supervised_vs_unsupervised",
                "### 📊 Supervised Learning vs Unsupervised Learning\n\n" +
                        "| Feature | Supervised Learning | Unsupervised Learning |\n" +
                        "| :--- | :--- | :--- |\n" +
                        "| **Data Requirement** | Labeled Data (Inputs X + Target Labels y) | Unlabeled Data (Inputs X only) |\n" +
                        "| **Primary Goal** | Predict outcomes for new unseen inputs | Discover hidden patterns or clusters |\n" +
                        "| **Common Subtypes** | Classification & Regression | Clustering & Dimensionality Reduction |\n" +
                        "| **Algorithms** | Linear/Logistic Regression, Decision Trees, SVM | K-Means, PCA, Hierarchical Clustering |\n" +
                        "| **Feedback Signal** | Direct feedback (compares prediction vs true label) | No direct output feedback |\n" +
                        "| **Example Use Case** | Spam detection, House price estimation | Customer segmentation, Anomaly detection |");

        comparisonMap.put("classification_vs_regression",
                "### 📊 Classification vs Regression\n\n" +
                        "| Feature | Classification | Regression |\n" +
                        "| :--- | :--- | :--- |\n" +
                        "| **Target Variable** | Discrete / Categorical labels (e.g., Spam/Not Spam) | Continuous numerical values (e.g., $250,000) |\n" +
                        "| **Output Type** | Class category or class probability | Floating-point real number |\n" +
                        "| **Evaluation Metrics** | Accuracy, Precision, Recall, F1-Score, ROC-AUC | Mean Squared Error (MSE), MAE, R-Squared |\n" +
                        "| **Algorithms** | Logistic Regression, Decision Tree, SVM, KNN | Linear Regression, Ridge/Lasso, Decision Tree Regressor |\n" +
                        "| **Example** | Predicting whether a patient has diabetes | Predicting a patient's blood pressure level |");

        comparisonMap.put("ml_vs_dl",
                "### 📊 Machine Learning vs Deep Learning\n\n" +
                        "| Feature | Traditional Machine Learning | Deep Learning |\n" +
                        "| :--- | :--- | :--- |\n" +
                        "| **Feature Engineering** | Manual feature extraction by domain experts | Automatic feature learning via deep layers |\n" +
                        "| **Data Requirement** | Works well on small to medium structured data | Requires massive unstructured datasets |\n" +
                        "| **Hardware Requirement** | Runs efficiently on standard CPUs | Requires GPUs / TPUs for high parallel processing |\n" +
                        "| **Algorithms** | Linear Regression, Random Forest, SVM | Artificial/Convolutional/Recurrent Neural Networks |\n" +
                        "| **Interpretability** | Higher transparency (e.g. Decision Trees) | Black box (hard to interpret internal activations) |");

        comparisonMap.put("decision_tree_vs_random_forest",
                "### 📊 Decision Tree vs Random Forest\n\n" +
                        "| Feature | Decision Tree | Random Forest |\n" +
                        "| :--- | :--- | :--- |\n" +
                        "| **Structure** | Single individual decision tree | Ensemble of hundreds of decision trees |\n" +
                        "| **Overfitting Risk** | High (tends to memorize training noise) | Low (averaging cancels out individual tree noise) |\n" +
                        "| **Accuracy** | Moderate | Higher and more stable |\n" +
                        "| **Speed** | Fast training and fast predictions | Slower training and prediction time |\n" +
                        "| **Interpretability** | Easy to view and trace decision paths | Harder to visualize entire forest |");

        comparisonMap.put("svm_vs_knn",
                "### 📊 SVM vs KNN\n\n" +
                        "| Feature | Support Vector Machine (SVM) | K-Nearest Neighbors (KNN) |\n" +
                        "| :--- | :--- | :--- |\n" +
                        "| **Learning Type** | Parametric / Optimized hyperplane | Non-parametric / Lazy learner (instance-based) |\n" +
                        "| **Training Speed** | Slower (requires solving quadratic optimization) | Zero training time (stores all data) |\n" +
                        "| **Prediction Speed** | Very fast (uses support vectors) | Slow for large datasets (calculates distances) |\n" +
                        "| **Memory Usage** | Low (only stores support vectors) | High (stores entire training set) |");

        comparisonMap.put("precision_vs_recall",
                "### 📊 Precision vs Recall\n\n" +
                        "| Feature | Precision | Recall (Sensitivity) |\n" +
                        "| :--- | :--- | :--- |\n" +
                        "| **Formula** | TP / (TP + FP) | TP / (TP + FN) |\n" +
                        "| **Core Focus** | Out of all predicted positives, how many are correct? | Out of all actual positives, how many did we catch? |\n" +
                        "| **Penalty Concern** | Penalizes False Positives (False Alarms) | Penalizes False Negatives (Missed Cases) |\n" +
                        "| **Best Example Use** | Email Spam Filter (avoiding sending good emails to spam) | Cancer Diagnosis (avoiding missing sick patients) |");

        comparisonMap.put("overfitting_vs_underfitting",
                "### 📊 Overfitting vs Underfitting\n\n" +
                        "| Feature | Overfitting | Underfitting |\n" +
                        "| :--- | :--- | :--- |\n" +
                        "| **Train Accuracy** | Very High | Low |\n" +
                        "| **Test Accuracy** | Low | Low |\n" +
                        "| **Cause** | High Variance (model is overly complex) | High Bias (model is overly simplistic) |\n" +
                        "| **Solutions** | Regularization, pruning, more data, dropout | Add features, increase model complexity, reduce penalty |");

        comparisonMap.put("normalization_vs_standardization",
                "### 📊 Normalization vs Standardization\n\n" +
                        "| Feature | Normalization (Min-Max) | Standardization (Z-Score) |\n" +
                        "| :--- | :--- | :--- |\n" +
                        "| **Formula** | (x - min) / (max - min) | (x - mean) / std_dev |\n" +
                        "| **Output Range** | Rescales feature values strictly to [0, 1] | Centers mean at 0 with unit variance (no fixed range) |\n" +
                        "| **Sensitivity to Outliers** | Highly sensitive (outliers squash normal values) | Robust to extreme outliers |\n" +
                        "| **Best Used When** | Distribution is unknown or non-Gaussian | Data follows a Gaussian / Normal distribution |");

        comparisonMap.put("bagging_vs_boosting",
                "### 📊 Bagging vs Boosting\n\n" +
                        "| Feature | Bagging (Bootstrap Aggregating) | Boosting |\n" +
                        "| :--- | :--- | :--- |\n" +
                        "| **Tree Construction** | Trees built independently in parallel | Trees built sequentially in series |\n" +
                        "| **Core Objective** | Reduces Variance (prevents overfitting) | Reduces Bias (turns weak learners into strong ones) |\n" +
                        "| **Weighting** | Equal weight given to all trees | Higher weight given to trees that fix previous errors |\n" +
                        "| **Example Algorithms** | Random Forest | XGBoost, Gradient Boosting, LightGBM, AdaBoost |");
    }
}