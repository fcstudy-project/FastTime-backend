package com.fasttime.domain.post.dto.controller.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostUpdateRequestDto {

    @NotNull
    private final Long postId;

    @NotNull
    private final Long memberId;

    @NotBlank
    private final String title;

    @NotBlank
    private final String content;

    @JsonCreator
    public PostUpdateRequestDto(
        @JsonProperty("postId") Long postId,
        @JsonProperty("memberId") Long memberId,
        @JsonProperty("title") String title,
        @JsonProperty("content") String content) {
        this.postId = postId;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
    }
}
