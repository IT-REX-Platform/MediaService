package de.uni_stuttgart.it_rex.media.security.oauth2.written;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AlfredAuthorization {

    private static String issuerUri = "http://129.69.217.173:9080/auth/realms/jhipster";

    public static void main(String[] args) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation(issuerUri);
        Jwt token = jwtDecoder.decode("eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJaVWozSjVjWG5DQlJKREkwUkV1WGo3ZHhwZjN2ZFVxSWV0VkRXYkdwZ09NIn0.eyJleHAiOjE2MTQwMjA1ODIsImlhdCI6MTYxNDAxOTY4MiwiYXV0aF90aW1lIjoxNjE0MDE5NjgyLCJqdGkiOiI4YmUyOTU3ZS0yY2M5LTRjZGEtYjhjOS1mM2IzNzFhZDllM2MiLCJpc3MiOiJodHRwOi8vMTI5LjY5LjIxNy4xNzM6OTA4MC9hdXRoL3JlYWxtcy9qaGlwc3RlciIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJkY2YzMDUwYi1jZWY4LTRlMDktYjY0MC0xYjhlODc4NTMxYjYiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ3ZWJfYXBwIiwic2Vzc2lvbl9zdGF0ZSI6IjYyMTAxOTYxLTYwMzUtNGVjMi05NGRiLTFjYjg0ZWMyMTI0NCIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovLzEyNy4wLjAuMTo4NzYxIiwiaHR0cDovL2xvY2FsaG9zdDo4MDgwIiwiaHR0cDovL2xvY2FsaG9zdDo5MDAwIiwiaHR0cDovL2xvY2FsaG9zdDo4MTAwIiwiaHR0cHM6Ly9sb2NhbGhvc3Q6ODA4MCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiSVRSRVhfTEVDVFVSRVIiLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJqaGlwc3RlciBlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJyb2xlcyI6WyJJVFJFWF9MRUNUVVJFUiIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXSwicHJlZmVycmVkX3VzZXJuYW1lIjoibGVjdHVyZXIifQ.YaWUIiiFk3A2C3P4hkVkMg_2VJvSLJddbbnqkRgr9XZpH4atzAfOIXQ-CElhbwZoqMAWzDaUto23kIvSWhJYlQnd_El1D2NoH1PWvUSGtNcgONKI5YCLvuN1eYPH0YKYgs0p3cCbRA_xDz15_vRKh13AFrz5V240XorrFzdTFF50KsBMfW__yTxBWeYFGQsALhfJGTeKDEpv46bKpAMROZSK1Ycf-6Ww93qgXeX5WXFvVFmX_-grqw9t6-riZC3dBctr419mDMZm5A03GvA2a4q10icFSMWbu4NMwJQjPpEt0ju61QhJwxeNlTNhu5B9A5AFnhTa_UIxTmhfqQktpg");

        //JwtAuthenticationToken tok = new JwtAuthenticationToken();

        Map tokens = token.getClaims();
        Map headers = token.getHeaders();

        System.out.println("Claims: " + tokens.toString());
        System.out.println("Headers: " + headers.toString());
    }

public boolean userIsCourseParticipant(Jwt token, UUID courseID) {



    return false;
}

public boolean userIsCourseParticipantOrManagerOrOwner(Jwt token, UUID courseID) {

    return false;
}

public boolean userIsCourseManagerOrOwner(Jwt token, UUID courseID) {

    return false;
}

public boolean userIsCourseOwner(Jwt token, UUID courseID) {

    return false;
}

public List<UUID> getCoursesOfUser(Jwt token) {

    return null;
}
}
