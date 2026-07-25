package org.example.domain.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.example.domain.user.dto.request.UserAddBalanceRequest;
import org.example.domain.user.dto.request.UserRegisterRequest;
import org.example.domain.user.dto.response.UserBalanceResponse;
import org.example.domain.user.dto.response.UserProfileResponse;
import org.example.domain.user.dto.response.UserRoleChangeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    final private UserService userService;

    // READ METHODS

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserProfileResponse> getUser(@AuthenticationPrincipal(expression = "id") Long userId){

        UserProfileResponse user = userService.getUser(userId);

        return ResponseEntity.ok(user);
    }

    // WRITE METHODS

    @PostMapping("/register")
    public ResponseEntity<UserProfileResponse> registerUser(@Valid @RequestBody UserRegisterRequest request) {

        UserProfileResponse user = userService.registerUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/balance")
    public ResponseEntity<UserBalanceResponse> addBalance(@AuthenticationPrincipal(expression = "id") Long userId,
                                                          @Valid @RequestBody UserAddBalanceRequest request) {

        UserBalanceResponse user = userService.addBalance(request, userId);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    /* ADMIN */

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{userId}")
    public ResponseEntity<UserRoleChangeResponse> makeAdmin(
            @PathVariable @Positive Long userId,
            @RequestParam UserRole role) {

        UserRoleChangeResponse user = userService.changeRole(userId, role);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
