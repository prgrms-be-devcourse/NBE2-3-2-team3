package com.example.bestme.service.impl;


import com.example.bestme.domain.Category;
import com.example.bestme.domain.Color;
import com.example.bestme.domain.Guide;
import com.example.bestme.dto.api.GuideRequest;
import com.example.bestme.repository.CategoryRepository;
import com.example.bestme.repository.ColorRepository;
import com.example.bestme.repository.GuideRepository;
import com.example.bestme.service.CategoryService;
import com.example.bestme.service.ColorService;
import com.example.bestme.service.GuideService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GuideServiceImpl implements GuideService {
    private final GuideRepository guideRepository;
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;


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

    @Override
    public Guide createGuide(GuideRequest.CreateGuideDTO createGuideDTO) {
        //null 예외 처리
        if (createGuideDTO.getColor() == null) {
            throw new IllegalArgumentException("퍼스널 컬러 Id는 필수 입력 값입니다.");
        }
        if (createGuideDTO.getCategory() == null) {
            throw new IllegalArgumentException("카테고리 Id는 필수 입력 값입니다.");
        }
        if (createGuideDTO.getDescription() == null || createGuideDTO.getDescription().isBlank()) {
            throw new IllegalArgumentException("설명은 필수 입력 값입니다.");
        }

        // Category와 Color 엔티티 조회
        Category category = categoryRepository.findById(createGuideDTO.getCategory())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 ID입니다."));
        Color color = colorRepository.findById(createGuideDTO.getColor())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 컬러 ID입니다."));

        // Guide 생성 및 설정
        Guide guide = new Guide();
        guide.setCategory(category);
        guide.setColor(color);
        guide.setDescription(createGuideDTO.getDescription());

        return guideRepository.save(guide);
    }
}
