package com.mitrais.register;

import com.mitrais.register.model.entity.User;
import com.mitrais.register.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository repo;

    @BeforeEach
    public void setData() {
        User user = new User();
        user.setEmail("existing@email.com");
        user.setFirstName("First");
        user.setLastName("Mock");
        user.setMobileNumber("+62222345");

        repo.save(user);
    }

    @Test
    public void whenUniqueMobileNum_thenMobileNumNotExists() {
        assertThat(repo.existsByMobileNumberEquals("+628123456"))
                .isFalse();
    }

    @Test
    public void whenUniqueEmail_thenEmailNotExists() {
        assertThat(repo.existsByEmailEqualsIgnoreCase("new@email.com"))
                .isFalse();
    }

    @Test
    public void whenExistsMobileNum_thenMobileNumFound() {
        assertThat(repo.existsByMobileNumberEquals("+62222345"))
                .isTrue();
    }

    @Test
    public void whenExistsEmail_thenEmailFound() {
        assertThat(repo.existsByEmailEqualsIgnoreCase("existing@email.com"))
                .isTrue();
    }
}
