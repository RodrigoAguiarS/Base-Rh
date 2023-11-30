package br.com.rodrigo.api.util;

import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.repository.PessoaRepository;
import br.com.rodrigo.api.repository.UsuarioRepository;

import static br.com.rodrigo.api.exception.ValidationError.ERRO_CPF_DUPLICADO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_EMAIL_DUPLICADO;

public class ValidatorUtil {

    private ValidatorUtil() {
    }

    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    public static Object isEmpty(Object o, Object ret) {
        return isEmpty(o) ? ret : o;
    }

    public static boolean isEmpty(String s) {
        return (s == null || s.trim().length() == 0);
    }

    public static boolean isEmpty(Object o) {
        if (o == null)
            return true;
        if (o instanceof String)
            return isEmpty((String) o);
        if (o instanceof Number) {
            Number i = (Number) o;
            return (i.intValue() == 0);
        }
        return false;
    }

    public static boolean isNotEqual(Object o1, Object o2) {
        return !isEmpty(o1) && !isEmpty(o2) && !o1.equals(o2);
    }

    public static void validarCpfExistenteComId(PessoaRepository pessoaRepository, String cpf, Long id) {
        if (pessoaRepository.existsByCpfAndIdNot(cpf, id)) {
            throw new ViolocaoIntegridadeDadosException(ERRO_CPF_DUPLICADO);
        }
    }

    public static void validarEmailExistenteComId(UsuarioRepository usuarioRepository, String email, Long id) {
        if (usuarioRepository.existsByEmailAndIdNot(email, id)) {
            throw new ViolocaoIntegridadeDadosException(ERRO_EMAIL_DUPLICADO);
        }
    }

    public static void validarCpfExistente(PessoaRepository pessoaRepository, String cpf) {
        if (pessoaRepository.existsByCpf(cpf)) {
            throw new ViolocaoIntegridadeDadosException(ERRO_CPF_DUPLICADO);
        }
    }

    public static void validarEmailExistente(UsuarioRepository usuarioRepository, String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new ViolocaoIntegridadeDadosException(ERRO_EMAIL_DUPLICADO);
        }
    }
}
