package com.example.bestme.service;


import com.example.bestme.domain.Guide;

import java.util.List;

public interface GuideService {
    List<Guide> readGuides(Long colorId);
}
