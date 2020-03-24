package com.promise.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.promise.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
  User findByUsername(String username);
  User getUserById(Long id);
}
