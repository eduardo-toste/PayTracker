package com.eduardo.paytracker.repository;

import com.eduardo.paytracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
