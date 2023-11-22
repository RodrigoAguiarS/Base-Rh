package br.com.rodrigo.api.service;

import br.com.rodrigo.api.model.Cargo;
import br.com.rodrigo.api.model.dto.CargoDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversaoService {

    private final ModelMapper modelMapper;

    public <S, D> D converter(S source, Class<D> destinationType) {
        return modelMapper.map(source, destinationType);
    }
}
