package com.kiesoft.userapi.service.stateless;

import com.kiesoft.userapi.auth.StatelessAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class DefaultStatelessService implements StatelessService {

    @Override
    public void authenticate(Authentication statelessSession) {
        SecurityContextHolder.getContext().setAuthentication(statelessSession);
    }

    @Override
    public StatelessAuthentication getAuthentication() {
        return (StatelessAuthentication)SecurityContextHolder.getContext().getAuthentication();
    }

}
