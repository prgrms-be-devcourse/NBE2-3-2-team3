package com.example.bestme.dto.community;

import lombok.*;
import org.springframework.hateoas.Link;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseNavigationDTO {

    Long prevBoardId;
    Long nextBoardId;

    String prevBoardSubject;
    String nextBoardSubject;

    Link prevBoardLink;
    Link nextBoardLink;

//    public ResponseNavigationDTO(NavigationInterface navigationInterface) {
//        this.nextBoardSubject = navigationInterface.getNextBoardSubject();
//        this.prevBoardSubject = navigationInterface.getPrevBoardSubject();
//        this.nextBoardId = navigationInterface.getNextBoardId();
//        this.prevBoardId = navigationInterface.getPrevBoardId();
//    }
}
