package com.ezfarm.ezfarmback.common;

import com.ezfarm.ezfarmback.user.domain.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    long id() default 1L;

    String name() default "user";

    String email() default "test@gmail.com";

    String password() default "password";

    Role roles() default Role.ROLE_USER;

}
