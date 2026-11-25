package org.crudfeed.server.controller;

import org.crudfeed.core.dto.post.*;
import org.crudfeed.core.service.PostService;
import org.crudfeed.server.security.TokenStore;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;
    private final TokenStore tokenStore;

    public PostController(PostService postService, TokenStore tokenStore) {
        this.postService = postService;
        this.tokenStore = tokenStore;
    }

    @PostMapping("/createPost")
    public String createPost(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody CreatePostRequestDto dto) {
        try {
            long userId = tokenStore.requireUserId(authHeader);
            return postService.createPost(userId, dto.content());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/editPost")
    public String editPost(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody EditPostRequestDto dto) {
        try {
            long userId = tokenStore.requireUserId(authHeader);
            return postService.editPost(dto.postId(), dto.content(), userId);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/deletePost")
    public String deletePost(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody PostRequestDto dto) {
        try {
            long userId = tokenStore.requireUserId(authHeader);
            return postService.deletePost(dto.postId(), userId);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/viewFeed")
    public List<ViewPostRequestDto> viewFeed() {
        return postService.viewFeed();
    }

    @PostMapping("/showUserPostsById")
    public List<ViewPostRequestDto> showUserPostsById(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        try {
            long userId = tokenStore.requireUserId(authHeader);
            return postService.showUserPostsById(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/viewUserLikes")
    public List<ViewPostRequestDto> viewUserLikes(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION,  required = false) String authHeader) {
        try {
            long userId = tokenStore.getUserId(authHeader);
            return postService.showUserLikes(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/searchPostById")
    public ViewPostRequestDto searchPostById(@RequestBody PostIdRequestDto dto) {
        try {
            return postService.searchPostById(dto.postId());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/searchPostByWords")
    public List<ViewPostRequestDto> searchPostByWords(@RequestBody PostContentRequestDto dto) {
        try {
            return postService.searchPostByWords(dto.content());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/likePost")
    public String likePost(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody PostRequestDto dto) {
        try {
            long userId = tokenStore.requireUserId(authHeader);
            return postService.likePost(dto.postId(), userId);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/dislikePost")
    public String dislikePost(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody PostRequestDto dto) {
        try {
            long userId = tokenStore.requireUserId(authHeader);
            return postService.dislikePost(dto.postId(), userId);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}