package ru.netology.repository;

import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
    private final AtomicLong postId;
    private final ConcurrentHashMap<Long, Post> posts;

    public PostRepository() {
        postId = new AtomicLong(0);
        posts = new ConcurrentHashMap<>();
    }

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        long existingPostId = post.getId();
        if (existingPostId > 0 && posts.containsKey(existingPostId)) {
            posts.replace(existingPostId, post);
        } else {
            long newPostId = existingPostId == 0 ? postId.incrementAndGet() : existingPostId;
            post.setId(newPostId);
            posts.put(newPostId, post);
        }
        return post;
    }

    public void removeById(long id) {
        posts.remove(id);
    }
}