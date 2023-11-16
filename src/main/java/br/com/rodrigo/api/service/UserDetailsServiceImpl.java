package br.com.rodrigo.api.service;

import br.com.rodrigo.api.model.Usuario;
import br.com.rodrigo.api.repository.UsuarioRepository;
import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository repository;
    private Authentication originalAuthentication;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> user = repository.findByEmailIgnoreCase(email);
        if(user.isPresent()) {
            return new Usuario(user.get().getId(), user.get().getEmail(), user.get().getSenha(), user.get().getPerfis());
        }
        throw new UsernameNotFoundException(email);
    }

    public void executarAcaoComoUsuario(String targetUsername) {
        UserDetails targetUser = loadUserByUsername(targetUsername);
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

    public void reverterParaUsuarioOriginal() {
        if (ValidatorUtil.isNotEmpty(originalAuthentication)) {
            SecurityContextHolder.getContext().setAuthentication(originalAuthentication);
            originalAuthentication = null;
        }
    }
}
