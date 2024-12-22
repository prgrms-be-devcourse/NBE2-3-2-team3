package com.example.bestme.dto.community;

import lombok.*;
import org.springframework.core.io.Resource;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFileDTO {
    private Resource resource;
    private String contentType;
}
