package com.example.springboottest.article;

import com.example.springboottest.user.SiteUser;
import com.example.springboottest.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RequestMapping("/article")
@RequiredArgsConstructor
@Controller
public class ArticleController {
    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping("/list")
    public String articleList(Model model, @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        List<Article> articleList = this.articleService.getList(keyword);
        model.addAttribute("articleList", articleList);
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
    public String articlecreate(ArticleForm articleForm) {
        return "article_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String aritcleCreate(@Valid ArticleForm articleForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "article_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.articleService.create(articleForm.getTitle(), articleForm.getContent(), siteUser);
        return "redirect:/article/list";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String articlemodify(ArticleForm articleForm, @PathVariable("id") Integer id, Principal principal) {
        Article article = this.articleService.getArticle(id);
        if (!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        article.setTitle(article.getTitle());
        article.setContent(article.getContent());
        return "article_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@ModelAttribute("articleForm") @Valid ArticleForm articleForm, BindingResult bindingResult, Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "article_form";
        }
        Article article = this.articleService.getArticle(id);
        if (!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.articleService.modify(article, articleForm.getTitle(), articleForm.getContent());
        return String.format("redirect:/article/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String articledelete(Principal principal, @PathVariable("id") Integer id) {
        Article article = this.articleService.getArticle(id);
        if (!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.articleService.delete(article);
        return "redirect:/";
    }
}
