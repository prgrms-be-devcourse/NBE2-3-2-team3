package com.example.bestme.service;


import com.example.bestme.domain.Guide;
import com.example.bestme.domain.Result;
import com.example.bestme.dto.api.GuideRequest;

import java.util.List;

public interface GuideService {
    //해당 퍼스널컬러에 대한 가이드 전체 조회
    List<Guide> readGuides(Long colorId);

    //가이드 생성
    Guide createGuide(GuideRequest.CreateGuideDTO createGuideDTO);
}
