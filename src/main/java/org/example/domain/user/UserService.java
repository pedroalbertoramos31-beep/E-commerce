package org.example.domain.user;

import org.example.domain.user.dto.response.UserProfileResponse;
import org.example.domain.user.dto.response.UserRoleChangeResponse;
import org.example.infrastructure.exception.error.UserException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.cart.Cart;
import org.example.domain.cart.CartRepository;
import org.example.domain.user.dto.request.UserAddBalanceRequest;
import org.example.domain.user.dto.request.UserRegisterRequest;
import org.example.domain.user.dto.response.UserBalanceResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepo;
    private final CartRepository cartRepo;

    private final UserQuery userQuery;

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // READ METHODS

    @Transactional
    public UserProfileResponse getUser(Long userId){

        User user = userQuery.findById(userId);

        return userMapper.toProfileResponse(user);
    }



    @Transactional
    public UserProfileResponse registerUser(UserRegisterRequest request) {

        userQuery.existsByUsername(request.username());

        String password = passwordEncoder.encode(request.password());

        User user = User.create(
                request.username(),
                password
        );

        userRepo.save(user);

        Cart cart = new Cart(user);

        cartRepo.save(cart);

        return userMapper.toProfileResponse(user);
    }

    @Transactional
    public UserBalanceResponse addBalance(UserAddBalanceRequest request, Long userId){

        User user = userQuery.findById(userId);

        user.addBalance(request.balance());

        return userMapper.toBalanceResponse(user);
    }


    /* ADMIN METHODS */

    @Transactional
    public UserRoleChangeResponse changeRole(Long userId, UserRole role){

        User user = userQuery.findById(userId);

        if (user.getRole() == UserRole.ADMIN){
            throw new UserException.AdminRoleModification();
        }

        user.changeRole(role);

        return userMapper.toUserRoleChangeResponse(user);
    }



}
