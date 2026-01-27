package com.gdgguadalajara.security.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.gdgguadalajara.membership.model.MemberRole;

import jakarta.ws.rs.NameBinding;

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface OrgRole {
    MemberRole[] value();
}
