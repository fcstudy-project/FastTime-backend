package com.fasttime.domain.post.service;

import com.fasttime.domain.member.entity.Member;
import com.fasttime.domain.member.service.MemberService;
import com.fasttime.domain.post.dto.service.response.PostDetailResponseDto;
import com.fasttime.domain.post.entity.Post;
import com.fasttime.domain.post.exception.NotPostWriterException;
import com.fasttime.domain.post.exception.PostNotFoundException;
import com.fasttime.domain.post.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PostCommandService implements PostCommandUseCase {

    private final MemberService memberService;
    private final PostRepository postRepository;

    public PostCommandService(MemberService memberService, PostRepository postRepository) {
        this.memberService = memberService;
        this.postRepository = postRepository;
    }

    @Override
    public PostDetailResponseDto writePost(PostCreateServiceDto serviceDto) {

        final Member writeMember = memberService.getMember(serviceDto.getMemberId());
        final Post createdPost = Post.createNewPost(writeMember, serviceDto.getTitle(),
            serviceDto.getContent(), serviceDto.isAnonymity());

        return PostDetailResponseDto.entityToDto(postRepository.save(createdPost));
    }

    @Override
    public PostDetailResponseDto updatePost(PostUpdateServiceDto serviceDto) {

        final Member updateRequestMember = memberService.getMember(serviceDto.getMemberId());
        Post post = findPostById(serviceDto.getPostId());

        isWriter(updateRequestMember, post);

        post.update(serviceDto.getTitle(), serviceDto.getContent());

        return PostDetailResponseDto.builder()
            .id(post.getId())
            .title(post.getTitle())
            .content(post.getContent())
            .anonymity(post.isAnonymity())
            .likeCount(post.getLikeCount())
            .hateCount(post.getHateCount())
            .build();
    }

    @Override
    public void deletePost(PostDeleteServiceDto serviceDto) {

        final Member deleteRequestMember = memberService.getMember(serviceDto.getMemberId());
        final Post post = findPostById(serviceDto.getPostId());

        validateAuthority(deleteRequestMember, post);

        post.delete(serviceDto.getDeletedAt());
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException(
                String.format("Post Not Found From Persistence Layer / postId = %d", postId)));
    }

    private void validateAuthority(Member requestUser, Post post) {
        isAdmin(requestUser);
        isWriter(requestUser, post);
    }

    private static void isAdmin(Member targetUser) {
        // TODO Admin 정보를 가져와 Admin 유저인지 확인해야 함.
    }

    private void isWriter(Member requestMember, Post post) {
        if (!requestMember.getId().equals(post.getMember().getId())) {
            throw new NotPostWriterException(String.format(
                "This member has no auth to control this post / targetPostId = %d, requestMemberId = %d",
                post.getId(), requestMember.getId()));
        }
    }

}
