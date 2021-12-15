package com.spring.baseproject.utils.auth;

import com.spring.baseproject.modules.auth.models.dtos.AuthorizedUser;
import com.spring.baseproject.modules.auth.models.dtos.CustomUserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {
    public static AuthorizedUser getAuthorizedUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
            return new AuthorizedUser(userDetail.getUserID(), userDetail.getUsername());
        } catch (Exception e) {
            return new AuthorizedUser();
        }
    }
}
