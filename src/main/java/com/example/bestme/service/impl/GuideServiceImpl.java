package com.example.bestme.service.impl;


import com.example.bestme.domain.Guide;
import com.example.bestme.repository.GuideRepository;
import com.example.bestme.service.GuideService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GuideServiceImpl implements GuideService {
    private final GuideRepository guideRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Guide> readGuides(Long colorId) {
        List<Guide> results = guideRepository.findAllByColorId(colorId);
        //결과가 없는 경우
        if (results.isEmpty()) {
            throw new EntityNotFoundException("해당 퍼스널 컬러에 대한 가이드가 존재하지 않습니다. (colorId: " + colorId + ")");
        }
        return results;
    }
}
