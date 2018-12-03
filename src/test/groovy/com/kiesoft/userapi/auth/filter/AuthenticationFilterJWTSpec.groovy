package com.kiesoft.userapi.auth.filter

import com.kiesoft.userapi.auth.StatelessAuthentication
import com.kiesoft.userapi.service.jwt.JwtService
import com.kiesoft.userapi.service.stateless.StatelessService
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import spock.lang.Specification
import spock.lang.Unroll

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Unroll
class AuthenticationFilterJWTSpec extends Specification {

    final authenticationManager = Mock(AuthenticationManager)
    final jwtService = Mock(JwtService)
    final statelessService = Mock(StatelessService)
    final authorizationFilterJWT = new AuthorizationFilterJWT(authenticationManager)

    def setup() {
        authorizationFilterJWT.setJwtService(jwtService)
        authorizationFilterJWT.setStatelessService(statelessService)
    }

    final httpServletRequest = Mock(HttpServletRequest)
    final httpServletResponse = Mock(HttpServletResponse)
    final filterChain = Mock(FilterChain)

    def "will authenticate with valid Issuer and JWT '#authorizarionHeader'"() {
        given: "there is a Bearer token in the Authorization header"
        httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION) >> authorizarionHeader

        and: "token contains a Issuer (iss)"
        jwtService.getIssuer(_ as String) >> Optional.of("6c5ed861-e5f0-42de-847b-cd7390651daf")

        and: "Authentication Manager authenticate the user"
        final statelessAuthentication = new StatelessAuthentication(UUID.randomUUID().toString(), "Betis", [], "jwt")
        authenticationManager.authenticate(_ as StatelessAuthentication) >> statelessAuthentication

        when:
        authorizationFilterJWT.doFilterInternal(httpServletRequest, httpServletResponse, filterChain)

        then:
        1 * statelessService.authenticate(_ as Authentication)

        where:
        authorizarionHeader                                                                                                                                                                           || _
        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiIyZmIyODEzMS1lODQxLTRjMmQtOWZlNi1iYzVkZGI3MDg3MDQiLCJleHAiOjE1NDM4Mzk1MjQsImlhdCI6MTU0MzgzNTkyNH0.OgFhLRNqGW47gVxQAAudbAAhtO_7NKs_DxeWoeMpKNw"         || _
        "Bearer      eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiIyZmIyODEzMS1lODQxLTRjMmQtOWZlNi1iYzVkZGI3MDg3MDQiLCJleHAiOjE1NDM4Mzk1MjQsImlhdCI6MTU0MzgzNTkyNH0.OgFhLRNqGW47gVxQAAudbAAhtO_7NKs_DxeWoeMpKNw   " || _
    }

    def "will not authenticate when no JWT provided"() {
        given: "there is not token in the Authorization header"
        httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION) >> null

        when:
        authorizationFilterJWT.doFilterInternal(httpServletRequest, httpServletResponse, filterChain)

        then:
        0 * statelessService.authenticate(_ as Authentication)
    }

    def "will not authenticate when invalid JWT '#authorizarionHeader'"() {
        given: "there is not token in the Authorization header"
        httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION) >> null

        when:
        authorizationFilterJWT.doFilterInternal(httpServletRequest, httpServletResponse, filterChain)

        then:
        0 * statelessService.authenticate(_ as Authentication)

        where:
        authorizarionHeader                                                                                                                                                                          || _
        ""                                                                                                                                                                                           || _
        "not a token"                                                                                                                                                                                || _
        "  Bearer    eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiIyZmIyODEzMS1lODQxLTRjMmQtOWZlNi1iYzVkZGI3MDg3MDQiLCJleHAiOjE1NDM4Mzk1MjQsImlhdCI6MTU0MzgzNTkyNH0.OgFhLRNqGW47gVxQAAudbAAhtO_7NKs_DxeWoeMpKNw  " || _
    }

    def "will not authenticate when invalid Isser"() {
        given: "there is not token in the Authorization header"
        httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION) >> "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiIyZmIyODEzMS1lODQxLTRjMmQtOWZlNi1iYzVkZGI3MDg3MDQiLCJleHAiOjE1NDM4Mzk1MjQsImlhdCI6MTU0MzgzNTkyNH0.OgFhLRNqGW47gVxQAAudbAAhtO_7NKs_DxeWoeMpKNw"

        and: "invalid Issuer"
        jwtService.getIssuer(_ as String) >> Optional.empty()

        when:
        authorizationFilterJWT.doFilterInternal(httpServletRequest, httpServletResponse, filterChain)

        then:
        0 * statelessService.authenticate(_ as Authentication)
    }

    def "will not authenticate when Authentication fails"() {
        given: "there is not token in the Authorization header"
        httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION) >> "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiIyZmIyODEzMS1lODQxLTRjMmQtOWZlNi1iYzVkZGI3MDg3MDQiLCJleHAiOjE1NDM4Mzk1MjQsImlhdCI6MTU0MzgzNTkyNH0.OgFhLRNqGW47gVxQAAudbAAhtO_7NKs_DxeWoeMpKNw"

        and: "token contains a Issuer (iss)"
        jwtService.getIssuer(_ as String) >> Optional.of("6c5ed861-e5f0-42de-847b-cd7390651daf")

        and: "authentication fails"
        authenticationManager.authenticate(_ as StatelessAuthentication) >> null

        when:
        authorizationFilterJWT.doFilterInternal(httpServletRequest, httpServletResponse, filterChain)

        then:
        0 * statelessService.authenticate(_ as Authentication)
    }

}
