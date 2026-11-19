package org.crudfeed.server.controller;

import org.crudfeed.core.dto.*;
import org.crudfeed.core.entity.Post;
import org.crudfeed.core.service.PostService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Transactional
@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/createPost")
    public String createPost(@RequestBody CreatePostRequestDto dto) {
        try {
            postService.createPost(dto.userId(), dto.content());
            return "Post created successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/editPost")
    public String editPost(@RequestBody EditPostRequestDto dto) {
        try {
            postService.editPost(dto.postId(), dto.content(), dto.userId());
            return "Successfully edited!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/deletePost")
    public String deletePost(@RequestBody PostRequestDto dto) {
        try {
            postService.deletePost(dto.postId(), dto.userId());
            return "Successfully deleted!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/viewFeed")
    public List<Post> viewFeed() {
        return postService.viewFeed();
    }

    @PostMapping("/showUserPostsById")
    public List<Post> showUserPostsById(@RequestBody AccountIdRequestDto dto) {
        try {
            return postService.showUserPostsById(dto.userId());
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/viewUserLikes")
    public List<Post> viewUserLikes(@RequestBody AccountIdRequestDto dto) {
        try {
            return postService.showUserLikes(dto.userId());
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/searchPostById")
    public Post searchPostById(@RequestBody PostIdRequestDto dto) {
        try {
            return postService.searchPostById(dto.postId());
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/searchPostByWords")
    public List<Post> searchPostByWords(@RequestBody PostContentRequestDto dto) {
        try {
            return postService.searchPostByWords(dto.content());
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/likePost")
    public String likePost(@RequestBody PostRequestDto dto) {
        try {
            postService.likePost(dto.postId(), dto.userId());
            return "Successfully liked!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/dislikePost")
    public String dislikePost(@RequestBody PostRequestDto dto) {
        try {
            postService.dislikePost(dto.postId(), dto.userId());
            return "Successfully disliked!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}