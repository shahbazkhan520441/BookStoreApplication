package com.book.store.application.repository;

import com.book.store.application.entity.User;
import com.book.store.application.jwt.RefreshToken;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByRefreshToken(String rt);

    List<RefreshToken> findByUserAndIsBlockedAndRefreshTokenNot(User user, boolean b, String refreshToken);

    List<RefreshToken> findByUserAndIsBlocked(User user, boolean b);

    List<RefreshToken> findByExpirationBefore(LocalDateTime now);
}