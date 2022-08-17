package com.example.redditapp.model;

import java.time.LocalDate;

public class Post {

    private Long id;
    private String postName;
    private String text;
    private String userName;
    private Long userId;
    private String displayName;
    private String communityName;
    private Integer reactionCount;
    private Integer commentCount;
    private String duration;
    private boolean upVote;
    private boolean downVote;

    public Post() {
    }

    public Post(Long id, String postName, String text, String userName, Long userId, String displayName, String communityName, Integer reactionCount, Integer commentCount, String duration, boolean upVote, boolean downVote) {
        this.id = id;
        this.postName = postName;
        this.text = text;
        this.userName = userName;
        this.userId = userId;
        this.displayName = displayName;
        this.communityName = communityName;
        this.reactionCount = reactionCount;
        this.commentCount = commentCount;
        this.duration = duration;
        this.upVote = upVote;
        this.downVote = downVote;
    }

    public Post(String postName, String text, String communityName) {
        this.postName = postName;
        this.text = text;
        this.communityName = communityName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public Integer getReactionCount() {
        return reactionCount;
    }

    public void setReactionCount(Integer reactionCount) {
        this.reactionCount = reactionCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isUpVote() {
        return upVote;
    }

    public void setUpVote(boolean upVote) {
        this.upVote = upVote;
    }

    public boolean isDownVote() {
        return downVote;
    }

    public void setDownVote(boolean downVote) {
        this.downVote = downVote;
    }
}
