package com.dossantosh.springfirstproject.news;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dossantosh.springfirstproject.common.config.annotations.module.RequiereModule;
import com.dossantosh.springfirstproject.common.controllers.GenericController;
import com.dossantosh.springfirstproject.common.controllers.PermisosUtils;
import com.dossantosh.springfirstproject.user.models.UserAuth;

import com.dossantosh.springfirstproject.user.service.objects.PreferencesService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequiereModule({ 2L })
public class NewsController extends GenericController {

        private final PreferencesService preferencesService;

        public NewsController( PermisosUtils permisosUtils, PreferencesService preferencesService) {
                super(permisosUtils);
                this.preferencesService = preferencesService;
        }

        @GetMapping("/objects/news")
        public String showPrincipal(Model model, HttpSession session, News news) {

                Set<Long> lecturaMod = new HashSet<>();
                Set<Long> escrituraMod = new HashSet<>();

                Set<Long> lecturaSub = new HashSet<>();
                Set<Long> escrituraSub = new HashSet<>();

                lecturaMod.add(1L);
                escrituraMod.add(1L);
                lecturaSub.add(1L);
                escrituraSub.add(1L);

                addPrincipalAttributes(model, session, lecturaMod, escrituraMod, lecturaSub, escrituraSub);

                model.addAttribute("activeNavLink", "news");

                UserAuth userAuth = (UserAuth) model.getAttribute("userAuth");

                LinkedHashSet<News> setNoticias = new LinkedHashSet<>();

                News salida;
                News fechaSalida;
                News prueba;

                if (preferencesService.obtenerPreferencias(userAuth.getId()).getIdioma().equals("es")) {
                        salida = new News(1L, "Seb's Fragances: disponible en...",
                                        "Ya se puede comprar Seb's Fragances en las plataformas oficiales...",
                                        "13/05/2025", "image");
                        fechaSalida = new News(1L, "Fecha de salida: Seb's Fragances",
                                        "La fecha de salida ya está confirmada para el 13/05/2025. Ya podéis reservar en...",
                                        "10/04/2025",
                                        "image");
                        prueba = new News(1L, "Esto es una prueba que...",
                                        "Primera prueba del sistema de news y si o is tiene que tener, al menos, dos lineas",
                                        "03/04/2025", "image");
                } else {
                        salida = new News(1L, "Seb's Fragances: available...",
                                        "Seb's Fragances is now available for purchase on official platforms..",
                                        "13/05/2025", "image");
                        fechaSalida = new News(1L, "Departure date: Seb's Fragances",
                                        "The departure date is now confirmed for May 13, 2025. You can now book at...",
                                        "10/04/2025",
                                        "image");
                        prueba = new News(1L, "This is a test that...",
                                        "First test of the news system and it has to have at least two lines.",
                                        "03/04/2025", "image");
                }

                setNoticias.add(salida);
                setNoticias.add(fechaSalida);
                setNoticias.add(prueba);

                model.addAttribute("news", setNoticias);
                return "objects/news";
        }
}
