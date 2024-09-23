package com.example.springboottest.article;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/article")
@RequiredArgsConstructor
@Controller
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/list")
    public String articlelist(Model model, @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        List<Article> articleList = this.articleService.getList(keyword);
        model.addAttribute("articleList", articleList);
        model.addAttribute("keyword", keyword);
        return "article_list";
    }

    @GetMapping("/detail/{id}")
    public String articledetail(Model model, @PathVariable("id") Integer id) {
        Article article = articleService.getArticle(id);
        model.addAttribute("article", article);
        return "article_form";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String articlecreate() {
        return "article_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@RequestParam(value = "title") String title, @RequestParam(value = "content") String content) {
        this.articleService.create(title, content);
        return "article_list";
    }

}
