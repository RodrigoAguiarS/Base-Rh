package br.com.rodrigo.api.service;

import br.com.rodrigo.api.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.exception.ViolocaoIntegridadeDadosException;
import br.com.rodrigo.api.model.Cargo;
import br.com.rodrigo.api.model.Departamento;
import br.com.rodrigo.api.model.dto.CargoDto;
import br.com.rodrigo.api.model.dto.DepartamentoDto;
import br.com.rodrigo.api.repository.CargoRepository;
import br.com.rodrigo.api.repository.DepartamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static br.com.rodrigo.api.exception.ValidationError.ERRO_CARGO_NAO_ENCONTRADO;
import static br.com.rodrigo.api.exception.ValidationError.ERRO_DEPARTAMENTO_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class CargoService {

    private final CargoRepository cargoRepository;

    private final DepartamentoRepository departamentoRepository;

    public List<Cargo> getAllCargos() {
        return cargoRepository.findAll();
    }

    public Optional<Cargo> getCargoById(Long id) {
        return cargoRepository.findById(id);
    }

    public CargoDto salvarCargo(CargoDto cargoDto) {
        Cargo cargo = new Cargo();
        cargo.setNome(cargoDto.getNome());
        cargo.setDescricao(cargoDto.getDescricao());
        cargo.setResponsabilidades(cargoDto.getResponsabilidades());
        cargo.setSalarioBase(cargoDto.getSalarioBase());

        Departamento departamento = departamentoRepository.findById(cargoDto.getDepartamento().getId()).
                orElseThrow(() -> new ViolocaoIntegridadeDadosException
                        (ERRO_CARGO_NAO_ENCONTRADO + cargoDto.getDepartamento()));

        cargo.setDepartamento(departamento);

        Cargo savedCargo = cargoRepository.save(cargo);

        DepartamentoDto departamentoDto = new DepartamentoDto();
        departamentoDto.setId(departamento.getId());
        departamentoDto.setDescricao(departamento.getDescricao());
        departamentoDto.setNome(departamento.getNome());

        CargoDto dto = new CargoDto();
        dto.setId(savedCargo.getId());
        dto.setNome(savedCargo.getNome());
        dto.setDescricao(savedCargo.getDescricao());
        dto.setResponsabilidades(savedCargo.getResponsabilidades());
        dto.setSalarioBase(savedCargo.getSalarioBase());
        dto.setDepartamento(departamentoDto);
        return dto;
    }

    public CargoDto atualizarCargo(Long id, CargoDto cargoDto) {
        Cargo cargoExistente = cargoRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_CARGO_NAO_ENCONTRADO));

        cargoExistente.setNome(cargoDto.getNome());
        cargoExistente.setDescricao(cargoDto.getDescricao());
        cargoExistente.setResponsabilidades(cargoDto.getResponsabilidades());
        cargoExistente.setSalarioBase(cargoDto.getSalarioBase());

        Departamento departamento = departamentoRepository.findById(cargoDto.getDepartamento().getId()).
                orElseThrow(() -> new ViolocaoIntegridadeDadosException
                        (ERRO_DEPARTAMENTO_NAO_ENCONTRADO + cargoDto.getDepartamento()));

        cargoExistente.setDepartamento(departamento);

        Cargo updatedCargo = cargoRepository.save(cargoExistente);

        DepartamentoDto departamentoDto = new DepartamentoDto();
        departamentoDto.setId(departamento.getId());
        departamentoDto.setDescricao(departamento.getDescricao());
        departamentoDto.setNome(departamento.getNome());

        CargoDto dto = new CargoDto();
        dto.setId(updatedCargo.getId());
        dto.setNome(updatedCargo.getNome());
        dto.setDescricao(updatedCargo.getDescricao());
        dto.setResponsabilidades(updatedCargo.getResponsabilidades());
        dto.setSalarioBase(updatedCargo.getSalarioBase());
        dto.setDepartamento(departamentoDto);

        return dto;
    }
    public void deleteCargo(Long id) {
        cargoRepository.deleteById(id);
    }
}
