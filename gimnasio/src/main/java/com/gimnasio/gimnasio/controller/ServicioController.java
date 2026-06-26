package com.gimnasio.gimnasio.controller;

import com.gimnasio.gimnasio.domain.Servicio;
import com.gimnasio.gimnasio.service.CategoriaService;
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
@RequestMapping("/servicio")
public class ServicioController {

    private final ServicioService servicioService;
    private final CategoriaService categoriaService;

    public ServicioController(ServicioService servicioService, CategoriaService categoriaService) {
        this.servicioService = servicioService;
        this.categoriaService = categoriaService;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var servicios = servicioService.getServicios();
        model.addAttribute("servicios", servicios);
        model.addAttribute("totalServicios", servicios.size());
        model.addAttribute("servicio", new Servicio());
        model.addAttribute("categorias", categoriaService.getCategorias());
        return "/servicio/listado";
    }

    @PostMapping("/guardar")
    public String guardar(Servicio servicio,
            @RequestParam Integer categoriaId,
            RedirectAttributes redirectAttributes) {
        var categoria = categoriaService.getCategoria(categoriaId);
        categoria.ifPresent(servicio::setCategoria);
        servicioService.save(servicio);
        redirectAttributes.addFlashAttribute("todoOk", "Servicio guardado correctamente.");
        return "redirect:/servicio/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            servicioService.delete(id);
            redirectAttributes.addFlashAttribute("todoOk", "Servicio eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar el servicio.");
        }
        return "redirect:/servicio/listado";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Servicio> servicioOpt = servicioService.getServicio(id);
        if (servicioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El servicio no existe.");
            return "redirect:/servicio/listado";
        }
        model.addAttribute("servicio", servicioOpt.get());
        model.addAttribute("categorias", categoriaService.getCategorias());
        return "/servicio/modifica";
    }
}
