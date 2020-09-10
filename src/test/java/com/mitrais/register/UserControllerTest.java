package com.mitrais.register;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitrais.register.controller.UserController;
import com.mitrais.register.model.entity.User;
import com.mitrais.register.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @BeforeEach
    public void setUp() {
        Mockito.when(service.isEmailExists("existing@email.com"))
                .thenReturn(true);
        Mockito.when(service.isEmailExists("unique@email.com"))
                .thenReturn(false);
        Mockito.when(service.isMobileNumberExists("+62111"))
                .thenReturn(true);
        Mockito.when(service.isMobileNumberExists("+62999"))
                .thenReturn(false);
        Mockito.when(service.insert(any(User.class)))
                .then(invocation -> invocation.getArgument(0));
    }

    @Test
    public void whenUniqueMobile_thenOk() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/isValidMobileNumber")
                        .param("mobileNumber", "+62999"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenExistingMobile_thenBadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/isValidMobileNumber")
                        .param("mobileNumber", "+62111"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenUniqueEmail_thenOk() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/isValidEmail")
                        .param("email", "unique@email.com"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenExistingEmail_thenBadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/isValidEmail")
                        .param("email", "existing@email.com"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenRequestValid_thenRegisterOk() throws Exception {
        User user = new User();
        user.setEmail("unique@email.com");
        user.setFirstName("First");
        user.setLastName("Mock");
        user.setMobileNumber("+62999");
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(user);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    public void whenRequestRequiredMissing_thenBadRequest() throws Exception {
        User user = new User();
        user.setEmail("unique@email.com");
        user.setFirstName("First");
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(user);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
