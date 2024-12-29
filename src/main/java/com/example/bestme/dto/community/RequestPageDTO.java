package com.example.bestme.dto.community;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class RequestPageDTO {

    private final int originPage;                   // 1 기반 인덱스 현재 페이지 번호 ( 사용자 요청 데이터 )
    private final int currentPage;                  // 0 기반 인덱스 현재 페이지 번호 ( 페이징 기능 적용을 위한 변환 )
    private final int numberOfDataPerPage = 10;      // 페이지당 데이터 갯수
    private final int numberOfPagesPerGroup = 10;    // 페이지 그룹당 페이지 갯수
    private int  numberPageGroup;                   // 페이지 그룹 갯수
    private int startPageOfGroup;                   // 현재 페이지 그룹의 첫 페이지 번호
    private int lastPageOfGroup;                    // 현재 페이지 그룹의 마지막 페이지 번호


    public RequestPageDTO(int currentPage) {
        this.originPage = currentPage;          // 1 기반 인덱스 현재 페이지 번호 ( 사용자 요청 데이터 )
        this.currentPage = currentPage - 1;     // 0 기반 인덱스 현재 페이지 번호 ( 페이징 기능 적용을 위한 변환 )
    }

    public void setNumberPageGroup(int totalPages) {
        this.numberPageGroup = ( totalPages + ( numberOfPagesPerGroup - 1 ) ) / numberOfPagesPerGroup;
        this.startPageOfGroup = (originPage - ( originPage - 1 ) % numberOfPagesPerGroup);
        this.lastPageOfGroup = (originPage - ( originPage - 1 ) % numberOfPagesPerGroup + numberOfPagesPerGroup - 1);
    }

}
