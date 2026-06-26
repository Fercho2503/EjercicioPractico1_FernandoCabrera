package com.gimnasio.gimnasio.controller;

import com.gimnasio.gimnasio.domain.Reserva;
import com.gimnasio.gimnasio.service.ReservaService;
import com.gimnasio.gimnasio.service.ServicioService;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reserva")
public class ReservaController {

    private final ReservaService reservaService;
    private final ServicioService servicioService;

    public ReservaController(ReservaService reservaService, ServicioService servicioService) {
        this.reservaService = reservaService;
        this.servicioService = servicioService;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var reservas = reservaService.getReservas();
        model.addAttribute("reservas", reservas);
        model.addAttribute("totalReservas", reservas.size());
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("servicios", servicioService.getServicios());
        return "/reserva/listado";
    }

    @PostMapping("/guardar")
    public String guardar(Reserva reserva,
            @RequestParam Integer servicioId,
            RedirectAttributes redirectAttributes) {
        var servicio = servicioService.getServicio(servicioId);
        servicio.ifPresent(reserva::setServicio);
        reservaService.save(reserva);
        redirectAttributes.addFlashAttribute("todoOk", "Reserva guardada correctamente.");
        return "redirect:/reserva/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            reservaService.delete(id);
            redirectAttributes.addFlashAttribute("todoOk", "Reserva eliminada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar la reserva.");
        }
        return "redirect:/reserva/listado";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Reserva> reservaOpt = reservaService.getReserva(id);
        if (reservaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "La reserva no existe.");
            return "redirect:/reserva/listado";
        }
        model.addAttribute("reserva", reservaOpt.get());
        model.addAttribute("servicios", servicioService.getServicios());
        return "/reserva/modifica";
    }
}
