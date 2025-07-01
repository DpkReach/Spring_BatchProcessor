package com.dpk.spring_batch_demo.repository;

import com.dpk.spring_batch_demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
