package com.book.store.application.repository;

import com.book.store.application.entity.User;
import com.book.store.application.jwt.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    Optional<AccessToken> findByAccessToken(String at);

    List<AccessToken> findByUserAndIsBlockedAndAccessTokenNot(User user, boolean b, String accessToken);

    List<AccessToken> findByUserAndIsBlocked(User user, boolean b);

    List<AccessToken> findByExpirationBefore(LocalDateTime now);
}