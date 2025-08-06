package com.jirahighlights.jiraintegratorservice.model;

import lombok.Data;

@Data
public class JiraTicket {
    private String id;
    private String key;
    private JiraFields fields;
}

@Data
class JiraFields {
    private String summary;
    private String description;
}