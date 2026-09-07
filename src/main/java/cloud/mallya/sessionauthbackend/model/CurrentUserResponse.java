package cloud.mallya.sessionauthbackend.model;

import java.util.List;

public record CurrentUser(String userName, List<String> roles) {}
