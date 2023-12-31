package com.example.jeddit.models.models.communities;

import com.example.jeddit.models.entitys.Community;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommunityInfoResponse {
    private long id;
    private String title;
    private String description;

    public CommunityInfoResponse(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public CommunityInfoResponse(Community community) {
        this.id = community.getId();
        this.title = community.getTitle();
        this.description = community.getDescription();
    }
}
