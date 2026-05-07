package com.dapp.docuchain.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 👤 USER PROFILE IMAGES
        registry.addResourceHandler("/user_profile_pictures/**")
                .addResourceLocations("file:C:/Project/docuchain/docuchain-api/user_profile_pictures/");

        // 🏢 ORGANIZATION LOGO IMAGES
        registry.addResourceHandler("/organization_picture/**")
                .addResourceLocations("file:C:/Project/docuchain/docuchain-api/organization_picture/");

        // 🚢 SHIP PROFILE PICTURES
        registry.addResourceHandler("/ship-profilepic/**")
                .addResourceLocations("file:C:/Project/docuchain/docuchain-api/ship-profilepic/");
                // 🖼️ EMAIL LOGOS AND GENERAL IMAGES (Added this!)
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:C:/Project/docuchain/docuchain-api/uploads/images/");


    }
}

