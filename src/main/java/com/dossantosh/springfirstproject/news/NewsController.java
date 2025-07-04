package com.dossantosh.springfirstproject.news;

import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dossantosh.springfirstproject.common.controllers.GenericController;
import com.dossantosh.springfirstproject.common.security.custom.auth.UserContextService;
import com.dossantosh.springfirstproject.common.security.module.RequiereModule;
import com.dossantosh.springfirstproject.common.security.others.PermisosUtils;
import com.dossantosh.springfirstproject.pref.PreferencesService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequiereModule({ 1L })
public class NewsController extends GenericController {

        private final PreferencesService preferencesService;

        public NewsController(UserContextService userContextService, PermisosUtils permisosUtils,
                        PreferencesService preferencesService) {
                super(userContextService, permisosUtils);
                this.preferencesService = preferencesService;
        }

        @GetMapping("/objects/news")
        public String showPrincipal(Model model, HttpSession session, News news) {

                Set<Long> readAll = Set.of(1L);
                Set<Long> writeAll = Set.of(2L);

                Set<Long> readUsers = Set.of(3L);
                Set<Long> writeUsers = Set.of(4L);

                Set<Long> readPerfumes = Set.of(5L);
                Set<Long> writePerfumes = Set.of(6L);

                addPrincipalAttributes(model, readAll, writeAll, readUsers, writeUsers, readPerfumes, writePerfumes);

                model.addAttribute("activeNavLink", "news");

                LinkedHashSet<News> setNoticias = new LinkedHashSet<>();

                News salida;
                News fechaSalida;
                News prueba;

                if (preferencesService.findByUserId(userContextService.getId()).getIdioma().equals("es")) {
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
