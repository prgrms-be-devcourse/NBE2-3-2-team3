package com.example.bestme.dto.community;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommunityTO {

    private Long board_id;
    private Long user_id;
    private String subject;
    private String imagename;
    private String content;
    private Long view;
    private CategoryCode category;
}
