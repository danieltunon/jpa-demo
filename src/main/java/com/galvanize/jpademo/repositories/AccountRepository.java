package com.galvanize.jpademo.repositories;

import com.galvanize.jpademo.entities.Account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    
}