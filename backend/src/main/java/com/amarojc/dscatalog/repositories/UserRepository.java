package com.amarojc.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amarojc.dscatalog.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
