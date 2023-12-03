package br.com.rodrigo.api.service;

import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImpersonationService {

    private Authentication originalAuthentication;

    private final UserDetailsServiceImpl userDetailsService;

    public void executarAcaoComoUsuario(String targetUsername) {
        UserDetails targetUser = userDetailsService.loadUserByUsername(targetUsername);
        originalAuthentication = SecurityContextHolder.getContext().getAuthentication();

        try {
            UsernamePasswordAuthenticationToken impersonationToken =
                    new UsernamePasswordAuthenticationToken(targetUser, null, targetUser.getAuthorities());

            impersonationToken.setDetails(originalAuthentication.getDetails());

            SecurityContextHolder.getContext().setAuthentication(impersonationToken);

        } finally {
            SecurityContextHolder.getContext().setAuthentication(originalAuthentication);
        }
    }

    /**
     * Reverte para o usuário autenticado original após a impersonificação.
     */
    public void reverterParaUsuarioOriginal() {
        if (ValidatorUtil.isNotEmpty(originalAuthentication)) {
            SecurityContextHolder.getContext().setAuthentication(originalAuthentication);
            originalAuthentication = null;
        }
    }
}
