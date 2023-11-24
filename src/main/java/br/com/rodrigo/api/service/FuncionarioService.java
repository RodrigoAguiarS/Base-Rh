package br.com.rodrigo.api.service;


import br.com.rodrigo.api.model.Funcionario;
import br.com.rodrigo.api.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FuncionarioService {


    private final FuncionarioRepository funcionarioRepository;

    public List<Funcionario> listarTodosFuncionarios() {
        return funcionarioRepository.findAll();

    }
}
