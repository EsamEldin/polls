package com.example.polls.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author <egadEldin.ext@orange.com> Essam Eldin
 * 
 * Spring security provides an annotation called @AuthenticationPrincipal 
 * to access the currently authenticated user in the controllers.
 *
 */
import org.springframework.security.core.annotation.AuthenticationPrincipal;


/*
 * 
 * Spring security provides an annotation called @AuthenticationPrincipal 
 * to access the currently authenticated user in the controllers.
 * 
 * 
 *We’ve created a meta-annotation so that we don’t get too much tied up of with 
 *Spring Security related annotations everywhere in our project. 
 *This reduces the dependency on Spring Security. 
 *So if we decide to remove Spring Security from our project, 
 *we can easily do it by simply changing the CurrentUser annotation-
 */



@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {

}
