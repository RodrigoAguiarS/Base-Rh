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
        Departamento departamento = departamentoRepository.findById(cargoDto.getDepartamento().getId())
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_CARGO_NAO_ENCONTRADO + cargoDto.getDepartamento()));

        Cargo cargo = CargoDto.toEntity(cargoDto);
        cargo.setDepartamento(departamento);

        Cargo savedCargo = cargoRepository.save(cargo);

        DepartamentoDto departamentoDto = DepartamentoDto.fromEntity(departamento);
        CargoDto savedCargoDto = CargoDto.fromEntity(savedCargo);
        savedCargoDto.setDepartamento(departamentoDto);

        return savedCargoDto;
    }

    public CargoDto atualizarCargo(Long id, CargoDto cargoDto) {
        Cargo cargoExistente = cargoRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(ERRO_CARGO_NAO_ENCONTRADO));

        Departamento departamento = departamentoRepository.findById(cargoDto.getDepartamento().getId())
                .orElseThrow(() -> new ViolocaoIntegridadeDadosException(ERRO_DEPARTAMENTO_NAO_ENCONTRADO + cargoDto.getDepartamento()));

        Cargo cargoAtualizado = CargoDto.toEntity(cargoDto);
        cargoAtualizado.setId(cargoExistente.getId());
        cargoAtualizado.setDepartamento(departamento);

        Cargo updatedCargo = cargoRepository.save(cargoAtualizado);

        DepartamentoDto departamentoDto = DepartamentoDto.fromEntity(departamento);
        CargoDto updatedCargoDto = CargoDto.fromEntity(updatedCargo);
        updatedCargoDto.setDepartamento(departamentoDto);

        return updatedCargoDto;
    }
    public void deleteCargo(Long id) {
        cargoRepository.deleteById(id);
    }
}
