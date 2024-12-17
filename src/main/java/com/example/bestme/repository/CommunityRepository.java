package com.example.bestme.repository;

import com.example.bestme.domain.community.CommunityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<CommunityEntity, String> {
}
