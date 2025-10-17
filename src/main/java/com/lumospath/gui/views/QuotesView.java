package com.lumospath.gui.views;

import com.lumospath.gui.controllers.MainController;
import com.lumospath.service.MotivationalQuoteService;
import com.lumospath.model.MotivationalQuote;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class QuotesView {
    private final MainController controller;
    private final MotivationalQuoteService quoteService;
    
    public QuotesView(MainController controller, MotivationalQuoteService quoteService) {
        this.controller = controller;
        this.quoteService = quoteService;
    }
    
    public ScrollPane createView() {
        VBox container = new VBox(30);
        container.getStyleClass().add("main-container");
        container.setPadding(new Insets(30));
        
        Label title = new Label("💫 Motivational Quotes & Wisdom");
        title.getStyleClass().add("header-title");
        
        VBox quoteContainer = new VBox(20);
        quoteContainer.getStyleClass().add("quote-container");
        quoteContainer.setAlignment(Pos.CENTER);
        
        MotivationalQuote quote = quoteService.getRandomQuote();
        
        Label quoteText = new Label("\"" + quote.getQuote() + "\"");
        quoteText.getStyleClass().add("quote-text");
        
        Label quoteAuthor = new Label("- " + quote.getAuthor());
        quoteAuthor.getStyleClass().add("quote-author");
        
        Button newQuoteBtn = new Button("✨ New Quote");
        newQuoteBtn.getStyleClass().add("primary-button");
        newQuoteBtn.setOnAction(e -> controller.showQuotesView()); // Refresh
        
        quoteContainer.getChildren().addAll(quoteText, quoteAuthor, newQuoteBtn);
        
        container.getChildren().addAll(title, quoteContainer);
        
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }
}