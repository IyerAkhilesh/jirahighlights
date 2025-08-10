package com.jirahighlights.jiraintegratorservice.model;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraTicket {
    private String id;
    private String key;
    private JiraFields fields;
}

@Data
class JiraFields {
    // Basic information
    private String summary;
    private String description;
    private List<String> issueType;
    // private List<String> status;
    // private String priority;

    // People involved
    // private String assignee;
    // private String creator;

    // Dates
    private Date creationDatetime;
    private Date updateDatetime;
    private Date dueDate;

    // Scrum and metadata
    private String sprint;
    private Integer storyPoints;
    private List<String> labels;

    // Other dynamic data
    private String lastComment;
    private List<String> attachmentNames;
}