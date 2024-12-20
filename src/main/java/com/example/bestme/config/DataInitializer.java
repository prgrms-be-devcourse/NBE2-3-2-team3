package com.example.bestme.config;

import com.example.bestme.domain.Color;
import com.example.bestme.repository.ColorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ColorRepository colorRepository;

    public DataInitializer(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (colorRepository.count() == 0) {
            colorRepository.save(new Color(1L, "봄 웜톤"));
            colorRepository.save(new Color(2L, "여름 쿨톤"));
            colorRepository.save(new Color(3L, "가을 웜톤"));
            colorRepository.save(new Color(4L, "겨울 쿨톤"));
        }
    }
}
