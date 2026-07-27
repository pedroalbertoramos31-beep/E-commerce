package org.example.domain.integration.service;

import org.example.domain.cart.Cart;
import org.example.domain.cart.CartQuery;
import org.example.domain.factory.UserTestData;
import org.example.domain.user.*;
import org.example.domain.user.dto.request.UserAddBalanceRequest;
import org.example.domain.user.dto.request.UserRegisterRequest;
import org.example.domain.user.dto.response.UserBalanceResponse;
import org.example.domain.user.dto.response.UserProfileResponse;
import org.example.domain.user.dto.response.UserRoleChangeResponse;
import org.example.infrastructure.exception.error.UserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "file:.env")
public class UserIntegrationTest {

    @Autowired private UserService userService;

    @Autowired private UserRepository userRepository;

    @Autowired private UserQuery userQuery;
    @Autowired private CartQuery cartQuery;
    @Autowired private PasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("Get User Tests")
    class GetUserTests{

        User user;

        @Test
        @DisplayName("Success; retrieve an existing user")
        public void shouldReturnUserProfile_WhenCreateIdExists() {

            // ARRANGE

            this.user = userRepository.saveAndFlush(UserTestData.simpleUser());

            // ACT

            UserProfileResponse response = userService.getUser(this.user.getId());

            // ASSERT - Response

            assertThat(response.id()).isEqualTo(this.user.getId());

            assertThat(response.username()).isEqualTo(this.user.getUsername());

            assertThat(response.role()).isEqualTo(this.user.getRole());
        }

        @Test
        @DisplayName("Fail; userId does not exists")
        public void shouldThrowException_WhenCreateIdDoesNotExists() {

            // ARRANGE

            Long nonExistingId = -1L;

            // ACT & ASSERT

            assertThatThrownBy(() -> userService.getUser(nonExistingId))
                    .isInstanceOf(UserException.UserNotFound.class);
        }

    }

    @Nested
    @DisplayName("UserRegister Tests")
    class RegisterUserTests {


        User user;
        Cart cart;

        @Test
        @DisplayName("Success; create user & cart")
        void ShouldCreateCreateAndCart_WhenRequestIsValid() {

            // ARRANGE

            UserRegisterRequest request = UserTestData.userRegisterRequest();

            // ACT

            UserProfileResponse response = userService.registerUser(request);

            // ASSERT - Persistence

            this.user = userQuery.findById(response.id());

            this.cart = cartQuery.findById(response.id());

            assertThat(request.username()).isEqualTo(this.user.getUsername());

            assertThat(this.user.getRole()).isEqualTo(response.role());

            assertThat(passwordEncoder.matches(request.password(), this.user.getPassword())).isTrue();

        }

        @Test
        @DisplayName("Failure; username already exists")
        void ShouldThrowException_WhenUsernameAlreadyExists() {

            // ARRANGE

            UserRegisterRequest request = UserTestData.userRegisterRequest();

            // ACT

            userService.registerUser(request);

            // ASSERT

            assertThatThrownBy(() -> userService.registerUser(request))
                    .isInstanceOf(UserException.DuplicateUsername.class);
        }
    }

    @Nested
    @DisplayName("Add Balance To User Tests")
    class AddBalanceToUserTest{

        private User user;

        @BeforeEach
        void setUp(){

            this.user = userRepository.saveAndFlush(UserTestData.simpleUser());

        }

        @Test
        @DisplayName("Success; add balance to user")
        public void shouldAddBalanceToCreate_WhenRequestIsValid(){

            // ARRANGE

            UserAddBalanceRequest request = UserTestData.userAddBalanceRequest();

            BigDecimal initialBalance = this.user.getBalance();

            BigDecimal expectedBalance = initialBalance.add(request.balance());

            // ACT

            UserBalanceResponse response = userService.addBalance(request, this.user.getId());

            // ASSERT - Response

            assertThat(this.user.getId()).isEqualTo(response.user().id());

            assertThat(expectedBalance).isEqualTo(response.balance());

            // ASSERT - Persistence

            this.user = userQuery.findById(this.user.getId());

            assertThat(this.user.getBalance()).isEqualTo(expectedBalance);

        }
    }

    @Nested
    @DisplayName("Make An User Admin")
    class MakeUserAdminTest{



        @Test
        @DisplayName("Success; user role is changed to admin")
        public void success_UserRoleChangedToAdmin(){

            // ARRANGE

            UserRole role = UserRole.ADMIN;

            User user = userRepository.saveAndFlush(UserTestData.simpleUser());

            // ACT

            UserRoleChangeResponse response = userService.changeRole(user.getId(), role);

            // ASSERT

            assertThat(response.id()).isEqualTo(user.getId());

            assertThat(response.role()).isEqualTo(role);

            User updatedUser = userQuery.findById(user.getId());

            assertThat(updatedUser.getRole()).isEqualTo(role);

        }


        @Test
        @DisplayName("Failure; admin role can not be modified")
        public void failure_AdminRoleCanNotBeModified(){

            // ARRANGE

            UserRole role = UserRole.USER;

            User user = userRepository.saveAndFlush(UserTestData.simpleUser(UserRole.ADMIN));

            // ACT & ASSERT

            assertThatThrownBy(() -> userService.changeRole(user.getId(), role))
                    .isInstanceOf(UserException.AdminRoleModification.class);

        }

    }


}
