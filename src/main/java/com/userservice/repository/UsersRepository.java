package com.userservice.repository;

import com.userservice.entity.CardInfo;
import com.userservice.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<Users, UUID> {

    Optional<Users> findByEmail(String email);

    Optional<Users> findUsersById(UUID id);

    List<Users> findByIdIn(Collection<UUID> ids);

    @Query("SELECT u FROM Users u WHERE u.email = :email")
    Users findUserByEmail(@Param("email") String email);

}
