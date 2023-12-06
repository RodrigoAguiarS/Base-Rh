package br.com.rodrigo.api.rest;

import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.model.Usuario;
import br.com.rodrigo.api.model.dto.DadosUsuariosDto;
import br.com.rodrigo.api.model.dto.RefreshTokenRequestDto;
import br.com.rodrigo.api.model.dto.RefreshTokenResponseDto;
import br.com.rodrigo.api.security.JWTUtil;
import br.com.rodrigo.api.service.ImpersonationService;
import br.com.rodrigo.api.service.UsuarioService;
import br.com.rodrigo.api.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class ImpersonationController {

    private final ImpersonationService impersonationService;

    private final UsuarioService usuarioService;

    private final JWTUtil jwtUtil;


    /**
     * Atualiza o token de acesso.
     *
     * @param refreshTokenRequest Dados da solicitação de atualização do token.
     * @param username            Nome de usuário associado ao token.
     * @return Resposta com o novo token de acesso.
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(
            @RequestBody RefreshTokenRequestDto refreshTokenRequest,
            @RequestParam("username") String username) {
        String currentToken = refreshTokenRequest.getCurrentToken();

        if (jwtUtil.tokenValido(currentToken)) {
            String newToken = jwtUtil.generateToken(username);

            RefreshTokenResponseDto response = new RefreshTokenResponseDto(newToken);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body("Token inválido.");
        }
    }

    /**
     * Obtém informações do usuário com base no token de acesso.
     *
     * @param token Token de acesso.
     * @return Resposta com as informações do usuário.
     */
    @GetMapping("/info")
    public ResponseEntity<DadosUsuariosDto> getUserInfo(@RequestParam("token") String token) {
        String username = jwtUtil.getUsername(token);

        Usuario usuario = usuarioService.buscarPorNomeUsuario(username);

        Funcionario funcionario = usuarioService.getFuncionarioDoUsuarioLogado();

        DadosUsuariosDto dadosUsuariosDto = DadosUsuariosDto.fromEntity(usuario, funcionario);

        if (ValidatorUtil.isNotEmpty(usuario)) {
            return ResponseEntity.ok(dadosUsuariosDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Impersonifica o login como outro usuário.
     *
     * @param username Nome de usuário a ser impersonificado.
     * @return Resposta com a mensagem de sucesso ou erro.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN_GERAL')")
    @GetMapping("/impersonate/{username}")
    public ResponseEntity<Map<String, String>> impersonateUser(@PathVariable String username) {
        try {
            impersonationService.executarAcaoComoUsuario(username);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Login como usuário bem-sucedido.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Usuário não encontrado.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Reverte para o usuário original após a impersonificação.
     *
     * @return Resposta com a mensagem de sucesso.
     */
    @GetMapping("/revert-to-original")
    public ResponseEntity<Map<String, String>> reverterParaUsuarioOriginal() {
        impersonationService.reverterParaUsuarioOriginal();

        Map<String, String> response = new HashMap<>();
        response.put("message", "Retornou ao usuário original.");
        return ResponseEntity.ok(response);
    }
}
