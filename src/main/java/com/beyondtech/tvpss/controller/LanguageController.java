package com.beyondtech.tvpss.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Locale;

@Controller
public class LanguageController {


    private final LocaleResolver localeResolver;

    @Autowired
    public LanguageController(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @PostMapping("/change-language")
    public String changeLanguage(@RequestParam("lang") String lang, HttpServletRequest request, HttpServletResponse response) {
        Locale locale = new Locale(lang);
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if (localeResolver != null) {
            localeResolver.setLocale(request, response, locale); // Set the locale in the cookie
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/"); // Redirect to the previous page
    }

}
