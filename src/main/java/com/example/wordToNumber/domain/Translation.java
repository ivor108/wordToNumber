package com.example.wordToNumber.domain;

import javax.persistence.*;

@Entity
public class Translation {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String textFrom;
    private String textTo;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    public Translation() {
    }

    public Translation(String textFrom, String textTo, User user) {
        this.author = user;
        this.textFrom = textFrom;
        this.textTo = textTo;
    }

    public String getAuthorName() {
        return author != null ? author.getUsername(): "none";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTextFrom() {
        return textFrom;
    }

    public void setTextFrom(String textFrom) {
        this.textFrom = textFrom;
    }

    public String getTextTo() {
        return textTo;
    }

    public void setTextTo(String textTo) {
        this.textTo = textTo;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
