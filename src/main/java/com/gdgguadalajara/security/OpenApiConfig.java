package com.gdgguadalajara.security;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlow;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlows;
import org.eclipse.microprofile.openapi.annotations.security.OAuthScope;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

@OpenAPIDefinition(info = @Info(title = "Open Badges Platform API", version = "1.0.0", description = "API para la emisión y gestión de insignias digitales del GDG Guadalajara"), security = @SecurityRequirement(name = "GoogleAuth"))
@SecurityScheme(securitySchemeName = "GoogleAuth", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(authorizationCode = @OAuthFlow(authorizationUrl = "https://accounts.google.com/o/oauth2/v2/auth", tokenUrl = "https://oauth2.googleapis.com/token", scopes = {
        @OAuthScope(name = "openid", description = "Identidad"),
        @OAuthScope(name = "email", description = "Correo electrónico"),
        @OAuthScope(name = "profile", description = "Perfil de usuario")
})))
public class OpenApiConfig {
}
