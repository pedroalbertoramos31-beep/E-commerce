package org.example.domain.user;


import lombok.RequiredArgsConstructor;
import org.example.infrastructure.exception.error.UserException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQuery {

    private final UserRepository userRepository;


    public User findById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException.UserNotFound(userId));
    }

    public void existsByUsername(String username){
        if (userRepository.existsByUsername(username)) {
            throw new UserException.DuplicateUsername(username);
        }
    }



}
