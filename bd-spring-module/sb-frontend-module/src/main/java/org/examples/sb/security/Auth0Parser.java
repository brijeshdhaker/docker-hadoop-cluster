package org.examples.sb.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class Auth0Parser {

    public Map <String, Claim> getClaims(String accessToken) {

        log.debug("getClaims(): accessToken-> {}",accessToken);

        DecodedJWT decoded = JWT.decode(accessToken);

        Map <String, Claim> claims = decoded.getClaims();

        log.debug("getClaims(): claims-> {}",claims);

        return claims;
    }

}
