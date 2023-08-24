package com.fasttime.domain.member.service;

import com.fasttime.domain.post.entity.Post;
import com.fasttime.domain.post.entity.ReportStatus;
import com.fasttime.domain.post.repository.PostRepository;
import java.rmi.AccessException;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final PostRepository postRepository;

    public List<Post> FindReportedPost(){
        return postRepository.findAllByReportStatus(ReportStatus.REPORTED);
    }

    public Post FindOneReportedPost(Long id) throws AccessException {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));
        System.out.println(post.getReportStatus());
        if (!post.getReportStatus().equals(ReportStatus.REPORTED)){
            throw new AccessException("잘못된 접근입니다.");
        }
        return post;
    }
    public void DeletePost(Long id) throws AccessException {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));
        if (!post.getReportStatus().equals(ReportStatus.REPORTED)){
            throw new AccessException("잘못된 접근입니다.");
        }
        postRepository.delete(post);
    }
    public void PassPost(Long id) throws AccessException {
        Post post = postRepository.findById(id).
            orElseThrow(()-> new IllegalArgumentException("게시글이 없습니다."));
        if (!post.getReportStatus().equals(ReportStatus.REPORTED)){
            throw new AccessException("잘못된 접근입니다.");
        }
        post.rejectReport();
    }

}