package com.spring2go.socialgithub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.GitHubRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

@Controller
public class RepositoriesController {
    @Autowired
    private GitHub github;

    @Autowired
    private ConnectionRepository connectionRepository;

    // 访问服务根目录'/'，如果没有GitHub用户信息，会跳转到localhost:8080/connect/github
    // 链接到GitHub的站点后，把用户的仓库都列出来，在repositories.html页面展示
    @GetMapping
    public String repositories(Model model) {
        if (connectionRepository.findPrimaryConnection(GitHub.class) == null) {
            return "redirect:/connect/github";
        }

        String name = github.userOperations().getUserProfile().getName();
        String username = github.userOperations().getUserProfile()
                .getUsername();
        model.addAttribute("name", name);

        String uri = "https://api.github.com/users/{user}/repos";
        GitHubRepo[] repos = github.restOperations().getForObject(uri,
                GitHubRepo[].class, username);
        model.addAttribute("repositories", Arrays.asList(repos));

        return "repositories";
    }
}
