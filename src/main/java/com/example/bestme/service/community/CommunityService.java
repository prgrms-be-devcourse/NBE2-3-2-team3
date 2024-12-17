package com.example.bestme.service.community;

import com.example.bestme.domain.community.CommunityEntity;
import com.example.bestme.dto.community.CommunityTO;
import com.example.bestme.repository.CommunityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommunityService {

    private final CommunityRepository communityRepositorySpy;

    public CommunityService(CommunityRepository communityRepositorySpy) {
        this.communityRepositorySpy = communityRepositorySpy;
    }

    // 테스트용 데이터 생성
    public int insert(CommunityTO to) {

        int flag = 0;

        ModelMapper modelMapper = new ModelMapper();
        CommunityEntity ce = modelMapper.map(to, CommunityEntity.class);

        communityRepositorySpy.save(ce);

        return flag;
    }

    public List<CommunityTO> selectAll() {

        List<CommunityEntity> boards = communityRepositorySpy.findAll();

        ModelMapper modelMapper = new ModelMapper();
        List<CommunityTO> lists = boards.stream()
                .map(board -> modelMapper.map(board, CommunityTO.class))
                .collect(Collectors.toList());

        return lists;
    }
}
