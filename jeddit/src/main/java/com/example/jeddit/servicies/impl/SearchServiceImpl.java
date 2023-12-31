package com.example.jeddit.servicies.impl;

import com.example.jeddit.models.entitys.Community;
import com.example.jeddit.models.entitys.Post;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.SearchRequest;
import com.example.jeddit.repositories.CommunitiesRepository;
import com.example.jeddit.repositories.PostRepository;
import com.example.jeddit.repositories.UserRepository;
import com.example.jeddit.servicies.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private CommunitiesRepository communitiesRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> searchUser(SearchRequest request, int from, int to) {
        List<User> users = new ArrayList<>();

        if (request.getText().split(" ").length == 1 && to > from) {
            users = userRepository.searchAllByLogin(request.getText(), from, to - from);
        }

        return users;
    }

    @Override
    public List<Community> searchCommunity(SearchRequest request, int from, int to) {
        List<Community> communities = new ArrayList<>();

        if (request.getText().split(" ").length == 1 && to > from) {
            communities = communitiesRepository.searchAllByTitle(request.getText(), from, to - from);
        }
        if (communities.size() == 0 && to > from) {
            communities = communitiesRepository.searchAllByDescription(request.getText(), from, to - from);
        }

        return communities;
    }

    @Override
    public List<Post> searchPost(SearchRequest request, int from, int to) {
        List<Post> posts = new ArrayList<>();

        if (to > from) {
            posts = postRepository.searchAllByTitle(request.getText(), from, to - from);
        }
        if (posts.size() == 0 && to > from) {
            posts = postRepository.searchAllByText(request.getText(), from, to - from);
        }

        return posts;
    }
}
