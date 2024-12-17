package com.example.bestme.service.impl;

import com.example.bestme.domain.Result;
import com.example.bestme.dto.ResultRequest;
import com.example.bestme.repository.ResultRepository;
import com.example.bestme.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.config.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;
    private final ModelMapper modelMapper = new ModelMapper();


    @Override
    public Result createResult(Long userId, Long colorId, ResultRequest.CreateResultDTO createResultDTO) {
        //null 예외 처리
        if (createResultDTO.getLightestSkinColor() == null || createResultDTO.getLightestSkinColor().isBlank()) {
            throw new IllegalArgumentException("lightestSkinColor는 필수 값입니다.");
        }
        if (createResultDTO.getDarkestSkinColor() == null || createResultDTO.getDarkestSkinColor().isBlank()) {
            throw new IllegalArgumentException("darkestSkinColor는 필수 값입니다.");
        }
        if (createResultDTO.getLipColor() == null || createResultDTO.getLipColor().isBlank()) {
            throw new IllegalArgumentException("lipColor는 필수 값입니다.");
        }
        if (createResultDTO.getPupilColor() == null || createResultDTO.getPupilColor().isBlank()) {
            throw new IllegalArgumentException("pupilColor는 필수 값입니다.");
        }
        if (createResultDTO.getIrisColor() == null || createResultDTO.getIrisColor().isBlank()) {
            throw new IllegalArgumentException("irisColor는 필수 값입니다.");
        }
        if (createResultDTO.getHairColor() == null || createResultDTO.getHairColor().isBlank()) {
            throw new IllegalArgumentException("hairColor는 필수 값입니다.");
        }

        Result result = modelMapper.map(createResultDTO, Result.class);
        result.setUserId(userId);
        result.setColorId(colorId);
        resultRepository.save(result);

        return result;
    }

    @Override
    public List<Result> readResults() {
        return List.of();
    }
}
