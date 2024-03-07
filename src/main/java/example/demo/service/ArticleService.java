package example.demo.service;

import example.demo.domain.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleService {


    void writeArticle(Article article);

    List<Article> getAllArticles();

    Article getArticle(Long id);

    void updateArticle(Article article);

    void deleteArticle(Long id);
}