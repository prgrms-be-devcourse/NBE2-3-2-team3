package com.example.bestme.dto.response;

public record LikeResponse(
        int likeCount,
        boolean isLiked
) {

    public static LikeResponse of(int likeCount, boolean isLiked) {
        return new LikeResponse(likeCount, isLiked);
    }
}
