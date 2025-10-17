package com.lumospath.gui.views;

import com.lumospath.gui.controllers.MainController;
import com.lumospath.model.MoodEntry;
import com.lumospath.model.MoodType;
import com.lumospath.service.MoodTrackingService;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;

/**
 * Mood tracking view with interactive mood selectors
 */
public class MoodTrackingView {
    private final MainController controller;
    private final MoodTrackingService moodService;
    
    private MoodType selectedMood = null;
    private int selectedScale = 5;
    private VBox moodGrid;
    
    public MoodTrackingView(MainController controller, MoodTrackingService moodService) {
        this.controller = controller;
        this.moodService = moodService;
    }
    
    public ScrollPane createView() {
        VBox container = new VBox(30);
        container.getStyleClass().add("main-container");
        container.setPadding(new Insets(30));
        
        // Header
        VBox header = createHeader();
        
        // Mood entry form
        VBox moodForm = createMoodForm();
        
        // Recent mood history
        VBox historySection = createHistorySection();
        
        container.getChildren().addAll(header, moodForm, historySection);
        
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        
        return scrollPane;
    }
    
    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        
        Label title = new Label("📊 How Are You Feeling Today?");
        title.getStyleClass().add("header-title");
        
        Label subtitle = new Label("Track your emotional journey and understand your patterns");
        subtitle.getStyleClass().add("header-subtitle");
        
        header.getChildren().addAll(title, subtitle);
        
        return header;
    }
    
    private VBox createMoodForm() {
        VBox form = new VBox(25);
        form.getStyleClass().add("mood-container");
        form.setAlignment(Pos.CENTER);
        form.setMaxWidth(800);
        
        Label formTitle = new Label("Select Your Current Mood");
        formTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        // Mood selector grid
        moodGrid = createMoodGrid();
        
        // Intensity slider
        VBox intensitySection = createIntensitySection();
        
        // Description text area
        VBox descriptionSection = createDescriptionSection();
        
        // Action buttons
        HBox buttonSection = createButtonSection();
        
        form.getChildren().addAll(formTitle, moodGrid, intensitySection, descriptionSection, buttonSection);
        
        return form;
    }
    
    private VBox createMoodGrid() {
        VBox gridContainer = new VBox(15);
        gridContainer.setAlignment(Pos.CENTER);
        
        Label gridLabel = new Label("Choose the emotion that best describes how you feel:");
        gridLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #495057;");
        
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);
        
        MoodType[] moods = MoodType.values();
        int col = 0, row = 0;
        
        for (MoodType mood : moods) {
            VBox moodSelector = createMoodSelector(mood);
            grid.add(moodSelector, col, row);
            
            col++;
            if (col >= 5) {  // 5 columns
                col = 0;
                row++;
            }
        }
        
        gridContainer.getChildren().addAll(gridLabel, grid);
        
        return gridContainer;
    }
    
    private VBox createMoodSelector(MoodType moodType) {
        VBox selector = new VBox(8);
        selector.getStyleClass().add("mood-selector");
        selector.setAlignment(Pos.CENTER);
        selector.setPrefWidth(120);
        selector.setPrefHeight(100);
        selector.setOnMouseClicked(e -> selectMood(moodType, selector));
        
        Label emoji = new Label(moodType.getEmoji());
        emoji.getStyleClass().add("mood-emoji");
        
        Label label = new Label(moodType.getDisplayName());
        label.getStyleClass().add("mood-label");
        label.setMaxWidth(110);
        
        selector.getChildren().addAll(emoji, label);
        
        return selector;
    }
    
    private void selectMood(MoodType moodType, VBox selector) {
        // Clear previous selection
        if (moodGrid != null) {
            moodGrid.lookupAll(".mood-selector").forEach(node -> {
                node.getStyleClass().remove("selected");
            });
        }
        
        // Set new selection
        selectedMood = moodType;
        selector.getStyleClass().add("selected");
    }
    
    private VBox createIntensitySection() {
        VBox section = new VBox(15);
        section.setAlignment(Pos.CENTER);
        
        Label label = new Label("How intense is this feeling? (1 = very mild, 10 = very strong)");
        label.setStyle("-fx-font-size: 16px; -fx-text-fill: #495057;");
        
        Slider intensitySlider = new Slider(1, 10, 5);
        intensitySlider.setShowTickLabels(true);
        intensitySlider.setShowTickMarks(true);
        intensitySlider.setMajorTickUnit(1);
        intensitySlider.setMinorTickCount(0);
        intensitySlider.setSnapToTicks(true);
        intensitySlider.setPrefWidth(400);
        
        Label valueLabel = new Label("5");
        valueLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #4A90E2;");
        
        intensitySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            selectedScale = newVal.intValue();
            valueLabel.setText(String.valueOf(selectedScale));
        });
        
        HBox sliderContainer = new HBox(20);
        sliderContainer.setAlignment(Pos.CENTER);
        sliderContainer.getChildren().addAll(intensitySlider, valueLabel);
        
        section.getChildren().addAll(label, sliderContainer);
        
        return section;
    }
    
    private VBox createDescriptionSection() {
        VBox section = new VBox(10);
        section.setAlignment(Pos.CENTER);
        
        Label label = new Label("Tell us more about what you're experiencing (optional):");
        label.setStyle("-fx-font-size: 16px; -fx-text-fill: #495057;");
        
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("What triggered this feeling? What's going through your mind? How can we support you?");
        descriptionArea.getStyleClass().add("form-field");
        descriptionArea.setPrefRowCount(4);
        descriptionArea.setMaxWidth(600);
        descriptionArea.setWrapText(true);
        
        section.getChildren().addAll(label, descriptionArea);
        
        return section;
    }
    
    private HBox createButtonSection() {
        HBox buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);
        
        Button saveBtn = new Button("💾 Record Mood");
        saveBtn.getStyleClass().add("primary-button");
        saveBtn.setOnAction(e -> recordMood());
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add("secondary-button");
        cancelBtn.setOnAction(e -> controller.showDashboardView());
        
        buttons.getChildren().addAll(saveBtn, cancelBtn);
        
        return buttons;
    }
    
    private void recordMood() {
        if (selectedMood == null) {
            controller.showWarningAlert("No Mood Selected", "Please select how you're feeling first.");
            return;
        }
        
        // Get description from text area
        String description = "";
        try {
            TextArea descArea = (TextArea) moodGrid.getParent().getParent().lookupAll(".form-field")
                    .stream().filter(node -> node instanceof TextArea).findFirst().orElse(null);
            if (descArea != null) {
                description = descArea.getText().trim();
                if (description.isEmpty()) {
                    description = "No description provided";
                }
            }
        } catch (Exception e) {
            description = "No description provided";
        }
        
        // Create mood entry
        int userId = controller.getCurrentUser() != null ? controller.getCurrentUser().getUserId() : 0;
        MoodEntry entry = new MoodEntry(userId, selectedMood, selectedScale, description);
        
        // Store current mood before resetting form
        MoodType recordedMood = selectedMood;
        int recordedScale = selectedScale;
        
        try {
            // Save mood entry to database
            if (userId > 0) {
                controller.getMoodService().saveMoodEntry(entry);
                controller.showInfoAlert("Mood Recorded", 
                    "Your mood has been recorded and saved!\n\n" +
                    "Mood: " + recordedMood.toString() + "\n" +
                    "Intensity: " + recordedScale + "/10" +
                    (recordedMood.isConcerning() ? "\n\n💙 Remember: You're not alone. Consider reaching out for support if needed." : ""));
            } else {
                controller.showInfoAlert("Mood Recorded", 
                    "Your mood has been recorded for this session!\n\n" +
                    "Mood: " + recordedMood.toString() + "\n" +
                    "Intensity: " + recordedScale + "/10\n\n" +
                    "💡 Sign up to save your mood history permanently!" +
                    (recordedMood.isConcerning() ? "\n\n💙 Remember: You're not alone. Consider reaching out for support if needed." : ""));
            }
        } catch (Exception e) {
            controller.showWarningAlert("Save Error", 
                "Your mood was recorded but couldn't be saved permanently. \n\nError: " + e.getMessage());
        }
        
        // Reset form
        resetForm();
        
        // Provide supportive message for concerning moods
        if (recordedMood.isConcerning() || recordedScale <= 3) {
            boolean showSupport = controller.showConfirmation("Support Available", 
                "We notice you might be going through a difficult time.\n\n" +
                "Would you like to explore some support options?\n" +
                "• Motivational quotes and wisdom\n" +
                "• Chat with LumosBot\n" +
                "• Emergency helplines");
            
            if (showSupport) {
                controller.showHelplineView();
            }
        }
    }
    
    private void resetForm() {
        selectedMood = null;
        selectedScale = 5;
        
        // Clear mood selections
        if (moodGrid != null) {
            moodGrid.lookupAll(".mood-selector").forEach(node -> {
                node.getStyleClass().remove("selected");
            });
        }
        
        // Clear description
        try {
            TextArea descArea = (TextArea) moodGrid.getParent().getParent().lookupAll(".form-field")
                    .stream().filter(node -> node instanceof TextArea).findFirst().orElse(null);
            if (descArea != null) {
                descArea.clear();
            }
        } catch (Exception e) {
            // Ignore
        }
    }
    
    private VBox createHistorySection() {
        VBox section = new VBox(20);
        section.setAlignment(Pos.CENTER);
        section.setMaxWidth(800);
        
        Label title = new Label("📈 Your Recent Mood Journey");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        VBox historyCard = new VBox(15);
        historyCard.getStyleClass().add("mood-container");
        historyCard.setAlignment(Pos.CENTER);
        historyCard.setPadding(new Insets(25));
        
        Label noDataLabel = new Label("Start tracking your moods to see your journey here");
        noDataLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #6c757d; -fx-text-alignment: center;");
        
        Button viewHistoryBtn = new Button("📊 View Full History");
        viewHistoryBtn.getStyleClass().add("secondary-button");
        viewHistoryBtn.setOnAction(e -> controller.showInfoAlert("Coming Soon", "Detailed mood history view is being developed!"));
        
        historyCard.getChildren().addAll(noDataLabel, viewHistoryBtn);
        
        section.getChildren().addAll(title, historyCard);
        
        return section;
    }
}