package com.latrattoria.backend.service;

import com.latrattoria.backend.model.Mesa;
import com.latrattoria.backend.repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    public List<Mesa> findAll() {
        return mesaRepository.findAll();
    }

    public Optional<Mesa> findById(Integer id) {
        return mesaRepository.findById(id);
    }

    public Optional<Mesa> findByNumero(Integer numero) {
        return mesaRepository.findByNumero(numero);
    }

    public Optional<Mesa> findByCodigoQr(String codigoQr) {
        return mesaRepository.findByCodigoQr(codigoQr);
    }

    public Mesa save(Mesa mesa) {
        return mesaRepository.save(mesa);
    }

    public void deleteById(Integer id) {
        mesaRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return mesaRepository.existsById(id);
    }
}
