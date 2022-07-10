package com.yogesh.github_issues;

public class CommentsModel {
    String UpdatedAt;
    String Body;
    UserModel User;

    public String getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        UpdatedAt = updatedAt;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public UserModel getUser() {
        return User;
    }

    public void setUser(UserModel user) {
        User = user;
    }
}
