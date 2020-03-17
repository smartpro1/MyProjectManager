package com.promise.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.promise.models.Backlog;

@Repository
public interface BacklogRepository extends JpaRepository<Backlog, Long> {

}
