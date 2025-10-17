package com.lumospath.gui.views;

import com.lumospath.gui.controllers.MainController;
import com.lumospath.service.EmergencyHelplineService;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class HelplineView {
    private final MainController controller;
    private final EmergencyHelplineService helplineService;
    
    public HelplineView(MainController controller, EmergencyHelplineService helplineService) {
        this.controller = controller;
        this.helplineService = helplineService;
    }
    
    public ScrollPane createView() {
        VBox container = new VBox(30);
        container.getStyleClass().add("main-container");
        container.setPadding(new Insets(30));
        
        Label title = new Label("📞 Emergency Helplines & Support");
        title.getStyleClass().add("header-title");
        
        VBox warningBox = new VBox(15);
        warningBox.getStyleClass().add("welcome-card");
        warningBox.setStyle("-fx-background-color: #fff3cd; -fx-border-color: #ffeeba; -fx-border-width: 2;");
        warningBox.setAlignment(Pos.CENTER);
        
        Label warningTitle = new Label("🚨 Crisis Support Available 24/7");
        warningTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #856404;");
        
        Label warningText = new Label("If you're having thoughts of self-harm or are in crisis, please reach out immediately:");
        warningText.setStyle("-fx-font-size: 16px; -fx-text-fill: #856404; -fx-text-alignment: center;");
        warningText.setWrapText(true);
        
        VBox helplinesList = new VBox(15);
        helplinesList.setAlignment(Pos.CENTER);
        
        // Key helplines
        VBox vandrevala = createHelplineCard("Vandrevala Foundation", "1860-266-2345", "24x7 Suicide Prevention", true);
        VBox kiran = createHelplineCard("Kiran Mental Health", "1800-599-0019", "Ministry of Social Justice", true);
        VBox sneha = createHelplineCard("Sneha Foundation", "044-24640050", "Crisis Intervention", true);
        
        helplinesList.getChildren().addAll(vandrevala, kiran, sneha);
        
        Button viewAllBtn = new Button("📋 View All Helplines");
        viewAllBtn.getStyleClass().add("secondary-button");
        viewAllBtn.setOnAction(e -> controller.showInfoAlert("All Helplines", 
            "Complete helpline directory:\n\n" +
            "• Vandrevala Foundation: 1860-266-2345 (24x7)\n" +
            "• AASRA: 022-27546669\n" +
            "• Sneha Foundation: 044-24640050 (24x7)\n" +
            "• iCall: 022-25563291\n" +
            "• Kiran: 1800-599-0019 (24x7)\n" +
            "• Mann Talks: 8686-139-139\n\n" +
            "Remember: You are not alone! 💙"));
        
        warningBox.getChildren().addAll(warningTitle, warningText);
        
        container.getChildren().addAll(title, warningBox, helplinesList, viewAllBtn);
        
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }
    
    private VBox createHelplineCard(String name, String phone, String description, boolean available24x7) {
        VBox card = new VBox(8);
        card.getStyleClass().add("helpline-card");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setMaxWidth(600);
        
        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("helpline-name");
        
        Label phoneLabel = new Label("📞 " + phone);
        phoneLabel.getStyleClass().add("helpline-phone");
        
        Label descLabel = new Label(description);
        descLabel.getStyleClass().add("helpline-description");
        
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getChildren().add(nameLabel);
        
        if (available24x7) {
            Label badge = new Label("24x7");
            badge.getStyleClass().add("helpline-badge");
            header.getChildren().add(badge);
        }
        
        card.getChildren().addAll(header, phoneLabel, descLabel);
        
        return card;
    }
}