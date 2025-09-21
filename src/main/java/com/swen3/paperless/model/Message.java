package com.swen3.paperless.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.util.UUID;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String text;
    private String author;

    // Standard-Konstruktor (wichtig für JSON → Objekt Mapping)
    public Message() {}

    public Message(String text, String author) {
        this.text = text;
        this.author = author;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
}
