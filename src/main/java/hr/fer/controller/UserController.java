package hr.fer.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import hr.fer.common.ApiPaths;
import hr.fer.dto.UserDto;
import hr.fer.security.UserPrincipal;

@CrossOrigin
@RestController
public class UserController {

    public UserController() {

    }

    @GetMapping(ApiPaths.GET_CURRENT_USER)
    public UserDto getCurrentUser() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return new UserDto(userPrincipal.getId(), userPrincipal.getName(), userPrincipal.getUsername(),
                userPrincipal.getEmail());
    }

}
