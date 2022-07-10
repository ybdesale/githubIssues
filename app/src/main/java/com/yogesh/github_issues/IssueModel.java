package com.yogesh.github_issues;

public class IssueModel {
    int IssueNumber;
    String IssueUrl;
    String CommentsUrl;
    String Title;
    String UpdatedAt;
    String Body;
    String Status;
    UserModel User;

    public int getIssueNumber() {
        return IssueNumber;
    }

    public void setIssueNumber(int issueNumber) {
        IssueNumber = issueNumber;
    }

    public String getIssueUrl() {
        return IssueUrl;
    }

    public void setIssueUrl(String issueUrl) {
        IssueUrl = issueUrl;
    }

    public String getCommentsUrl() {
        return CommentsUrl;
    }

    public void setCommentsUrl(String commentsUrl) {
        CommentsUrl = commentsUrl;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public UserModel getUser() {
        return User;
    }

    public void setUser(UserModel user) {
        User = user;
    }
}
