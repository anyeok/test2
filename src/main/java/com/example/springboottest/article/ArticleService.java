package com.example.springboottest.article;

import com.example.springboottest.DataNotFoundException;
import com.example.springboottest.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<Article> getList(String keyword) {
        return articleRepository.findAllByKeyword(keyword);
    }

    public Article getArticle(Integer id) {
        Optional<Article> optionalArticle = this.articleRepository.findById(id);
        if (optionalArticle.isPresent()) {
            return optionalArticle.get();
        } else {
            throw new DataNotFoundException("article not found");
        }
    }

    public void create(String title, String content, SiteUser siteUser) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setCreateDate(LocalDateTime.now());
        article.setAuthor(siteUser);
        this.articleRepository.save(article);
    }

    public void modify(Article article, String title, String content) {
        article.setTitle(title);
        article.setContent(content);
        article.setModifyDate(LocalDateTime.now());
        this.articleRepository.save(article);
    }

    public void delete(Article article) {
        this.articleRepository.delete(article);
    }
}
