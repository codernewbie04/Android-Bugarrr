package com.soopconcept.bugarrr.models;

import com.google.firebase.Timestamp;

public class TipsModel {
    private String content;
    private Timestamp created_at;
    private String id;
    private String image;
    private String subtitle;
    private String title;

    public TipsModel(){

    }
    public TipsModel(String content, Timestamp created_at, String id, String image, String subtitle, String title)
    {
        this.content = content;
        this.created_at = created_at;
        this.id = id;
        this.image = image;
        this.subtitle = subtitle;
        this.title = title;
    }
    // Getter Methods

    public String getContent() {
        return content;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public String getId() {
        return id;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getTitle() {
        return title;
    }

    // Setter Methods

    public void setContent( String content ) {
        this.content = content;
    }

    public void setCreated_at( Timestamp created_at ) {
        this.created_at = created_at;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setSubtitle(String subtitle ) {
        this.subtitle = subtitle;
    }

    public void setTitle( String title ) {
        this.title = title;
    }
}
