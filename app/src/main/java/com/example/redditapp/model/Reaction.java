package com.example.redditapp.model;

public class Reaction {

    private ReactionType reactionType;
    private Long id;

    public Reaction() {
    }

    public ReactionType getReactionType() {
        return reactionType;
    }

    public void setReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
