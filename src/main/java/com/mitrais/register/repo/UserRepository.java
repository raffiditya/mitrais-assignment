package com.mitrais.register.repo;

import com.mitrais.register.model.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsByEmailEqualsIgnoreCase(String email);

    boolean existsByMobileNumberEquals(String mobileNumber);
}
