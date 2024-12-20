package com.example.bestme.service.impl;


import com.example.bestme.domain.Color;
import com.example.bestme.repository.ColorRepository;
import com.example.bestme.repository.ResultRepository;
import com.example.bestme.service.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ColorServiceImpl implements ColorService {
    private final ColorRepository colorRepository;
    @Override
    @Transactional(readOnly = true)
    public Color getColor(Long colorId) {
        Color color = colorRepository.findById(colorId).orElseThrow(() -> new IllegalArgumentException("color를 찾을 수 없습니다."));
        return color;
    }
}
