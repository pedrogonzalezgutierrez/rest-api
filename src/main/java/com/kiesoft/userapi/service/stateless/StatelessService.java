package com.kiesoft.userapi.service.stateless;

import com.kiesoft.userapi.auth.StatelessAuthentication;
import org.springframework.security.core.Authentication;

public interface StatelessService {

    void authenticate(Authentication statelessSession);

    StatelessAuthentication getAuthentication();

}
