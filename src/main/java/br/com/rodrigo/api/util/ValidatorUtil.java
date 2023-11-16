package br.com.rodrigo.api.util;

import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.repository.PessoaRepository;
import br.com.rodrigo.api.repository.UsuarioRepository;

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

    public static void validarCpfEmailUnico(
            PessoaRepository pessoaRepository,
            UsuarioRepository usuarioRepository,
            String cpf,
            String email,
            Long id
    ) {
        if (id != null) {
            if (pessoaRepository.existsByCpfAndIdNot(cpf, id)) {
                throw new ViolocaoIntegridadeDadosException("CPF já cadastrado. Por favor, insira um CPF único.");
            }

            if (usuarioRepository.existsByEmailAndIdNot(email, id)) {
                throw new ViolocaoIntegridadeDadosException("E-mail já cadastrado. Por favor, insira um e-mail único.");
            }
        } else {
            if (pessoaRepository.existsByCpf(cpf)) {
                throw new ViolocaoIntegridadeDadosException("CPF já cadastrado. Por favor, insira um CPF único.");
            }

            if (usuarioRepository.existsByEmail(email)) {
                throw new ViolocaoIntegridadeDadosException("E-mail já cadastrado. Por favor, insira um e-mail único.");
            }
        }
    }
}
