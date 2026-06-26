package com.gimnasio.gimnasio.service;

import com.gimnasio.gimnasio.domain.Reserva;
import com.gimnasio.gimnasio.repository.ReservaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @Transactional(readOnly = true)
    public List<Reserva> getReservas() {
        return reservaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Reserva> getReserva(Integer id) {
        return reservaRepository.findById(id);
    }

    @Transactional
    public void save(Reserva reserva) {
        reservaRepository.save(reserva);
    }

    @Transactional
    public void delete(Integer id) {
        reservaRepository.deleteById(id);
    }
}
