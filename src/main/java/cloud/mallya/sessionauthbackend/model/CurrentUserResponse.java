package cloud.mallya.sessionauthbackend.model;

import java.util.List;

public record CurrentUserResponse(String userName, List<String> roles) {}
