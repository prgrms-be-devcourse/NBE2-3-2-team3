package com.example.bestme.dto.community;

public interface NavigationInterface {

    Long getPrevBoardId();
    Long getNextBoardId();

    String getPrevBoardSubject();
    String getNextBoardSubject();
}
