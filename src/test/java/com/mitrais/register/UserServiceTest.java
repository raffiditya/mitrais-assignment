package com.mitrais.register;

import com.mitrais.register.model.entity.User;
import com.mitrais.register.repo.UserRepository;
import com.mitrais.register.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private UserService service;

    @Mock
    private UserRepository repository;

    @BeforeEach
    public void setUp() {
        Mockito.lenient()
                .when(repository.existsByEmailEqualsIgnoreCase("existing@email.com"))
                .thenReturn(true);
        Mockito.lenient()
                .when(repository.existsByEmailEqualsIgnoreCase("unique@email.com"))
                .thenReturn(false);
        Mockito.lenient()
                .when(repository.existsByMobileNumberEquals("+62111"))
                .thenReturn(true);
        Mockito.lenient()
                .when(repository.existsByMobileNumberEquals("+62999"))
                .thenReturn(false);
        Mockito.lenient()
                .when(repository.save(any(User.class)))
                .then((Answer<User>) invocation -> invocation.getArgument(0));

        service = new UserService(repository);
    }

    @Test
    public void whenRegisterWithUniqueMobileAndEmail_thenReturnEntity() {
        User user = new User();
        user.setEmail("unique@email.com");
        user.setFirstName("First");
        user.setLastName("Mock");
        user.setMobileNumber("+62999");

        assertThat(service.insert(user))
                .isEqualTo(user);
    }

    @Test
    public void whenRegisterWithExistingMobileNum_thenThrow() {
        User user = new User();
        user.setEmail("unique@email.com");
        user.setFirstName("First");
        user.setLastName("Mock");
        user.setMobileNumber("+62111");

        assertThatThrownBy(() -> service.insert(user))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    public void whenRegisterWithExistingEmail_thenThrow() {
        User user = new User();
        user.setEmail("existing@email.com");
        user.setFirstName("First");
        user.setLastName("Mock");
        user.setMobileNumber("+62999");

        assertThatThrownBy(() -> service.insert(user))
                .isInstanceOf(ResponseStatusException.class);
    }
}
