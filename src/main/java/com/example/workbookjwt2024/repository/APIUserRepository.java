package com.example.workbookjwt2024.repository;

import com.example.workbookjwt2024.domain.APIUser;
import org.springframework.data.jpa.repository.JpaRepository;


public interface APIUserRepository  extends JpaRepository<APIUser, String> {
}
