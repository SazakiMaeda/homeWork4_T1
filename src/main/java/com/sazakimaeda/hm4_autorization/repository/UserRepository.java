package com.sazakimaeda.hm4_autorization.repository;

import com.sazakimaeda.hm4_autorization.model.User;
import com.sazakimaeda.hm4_autorization.request.RegistrationRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
