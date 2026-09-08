package cloud.mallya.sessionauthbackend.controller;

import cloud.mallya.sessionauthbackend.model.CsrfResponse;
import cloud.mallya.sessionauthbackend.model.CurrentUserResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AuthController {

    @GetMapping("/me")
    public CurrentUserResponse currentUserResponse(Authentication authentication) {

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return new CurrentUserResponse(authentication.getName(), roles);
    }

    @GetMapping("/csrf")
    public CsrfResponse csrfResponse(CsrfToken csrfToken) {

        return new CsrfResponse(csrfToken.getHeaderName(), csrfToken.getToken());
    }
}
