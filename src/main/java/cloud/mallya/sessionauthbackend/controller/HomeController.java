package cloud.mallya.sessionauthbackend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class HomeController {

    @GetMapping("/home")
    public Map<String, String> home(Authentication authentication) {
        return Map.of("message", "Hello " + authentication.getName() + " , welcome to the home page");
    }
}
