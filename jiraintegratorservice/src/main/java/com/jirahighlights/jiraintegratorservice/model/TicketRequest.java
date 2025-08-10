package com.jirahighlights.jiraintegratorservice.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

/**
 * Data class to represent the request body for ticket generation.
 * Note: The assignee field has been removed as it is now randomly selected by the service.
 */
public class TicketRequest {

    @Min(value = 1, message = "Count must be at least 1.")
    private int count;

    @NotEmpty(message = "Project key cannot be empty.")
    private String projectKey;

    private String issueType;

    private String comment;

    // Getters and setters
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
