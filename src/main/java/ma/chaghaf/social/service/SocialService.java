package ma.chaghaf.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.chaghaf.social.dto.SocialDtos.*;
import ma.chaghaf.social.entity.Message;
import ma.chaghaf.social.entity.Post;
import ma.chaghaf.social.entity.PostLike;
import ma.chaghaf.social.repository.MessageRepository;
import ma.chaghaf.social.repository.PostLikeRepository;
import ma.chaghaf.social.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialService {

    private final PostRepository postRepo;
    private final PostLikeRepository likeRepo;
    private final MessageRepository messageRepo;

    public Page<PostResponse> getPosts(Long userId, int page, int size) {
        return postRepo.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size))
            .map(p -> toPostResponse(p, userId));
    }

    @Transactional
    public PostResponse createPost(Long userId, String name, String avatar, String role, CreatePostRequest req) {
        Post post = postRepo.save(Post.builder()
            .userId(userId)
            .userName(name)
            .userAvatar(avatar)
            .userRole(role)
            .content(req.getContent())
            .likesCount(0)
            .build());
        log.info("Post {} created by user {}", post.getId(), userId);
        return toPostResponse(post, userId);
    }

    @Transactional
    public PostResponse toggleLike(Long userId, Long postId) {
        Post post = postRepo.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        likeRepo.findByPostIdAndUserId(postId, userId).ifPresentOrElse(
            like -> {
                likeRepo.delete(like);
                post.setLikesCount(Math.max(0, post.getLikesCount() - 1));
            },
            () -> {
                likeRepo.save(PostLike.builder().post(post).userId(userId).build());
                post.setLikesCount(post.getLikesCount() + 1);
            }
        );

        return toPostResponse(postRepo.save(post), userId);
    }

    @Transactional
    public MessageResponse sendMessage(Long senderId, SendMessageRequest req) {
        Message msg = messageRepo.save(Message.builder()
            .senderId(senderId)
            .receiverId(req.getReceiverId())
            .content(req.getContent())
            .read(false)
            .build());
        return toMessageResponse(msg);
    }

    public List<MessageResponse> getConversation(Long userId, Long otherId) {
        return messageRepo.findConversation(userId, otherId)
            .stream().map(this::toMessageResponse).toList();
    }

    private PostResponse toPostResponse(Post p, Long currentUserId) {
        boolean likedByMe = currentUserId != null && likeRepo.existsByPostIdAndUserId(p.getId(), currentUserId);
        return new PostResponse(p.getId(), p.getUserId(), p.getUserName(),
            p.getUserAvatar(), p.getUserRole(), p.getContent(),
            p.getLikesCount(), likedByMe, p.getCreatedAt());
    }

    private MessageResponse toMessageResponse(Message m) {
        return new MessageResponse(m.getId(), m.getSenderId(), m.getReceiverId(),
            m.getContent(), m.getRead(), m.getSentAt());
    }
}
