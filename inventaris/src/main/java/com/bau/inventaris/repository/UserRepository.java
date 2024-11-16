package com.bau.inventaris.repository;

import com.bau.inventaris.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNim(String nim);
}
