package com.example.springfirstproject.config.initializer;

import com.example.springfirstproject.models.objects.Brands;
import com.example.springfirstproject.models.objects.Perfumes;
import com.example.springfirstproject.models.objects.Tipos;
import com.example.springfirstproject.service.objects.BrandService;
import com.example.springfirstproject.service.objects.PerfumeService;
import com.example.springfirstproject.service.objects.TipoService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(BrandService brandService, PerfumeService perfumeService,
            TipoService tipoService) {
        return args -> {
            if (brandService.count() == 0) {
                Brands tomford = new Brands();
                tomford.setName("Tom Ford");

                Brands guerlain = new Brands();
                guerlain.setName("Guerlain");

                Brands hermes = new Brands();
                hermes.setName("Hermes");

                brandService.save(tomford);
                brandService.save(guerlain);
                brandService.save(hermes);

                Perfumes perfume1 = new Perfumes();
                perfume1.setName("Black Orchid");
                perfume1.setBrand(tomford);
                perfume1.setPrice(150.0f);
                perfume1.setVolume(100.0f);
                perfume1.setSeason("Invierno");
                perfume1.setDescription("Morado");
                perfume1.setFecha(2006);
                perfume1.setTipo(tipoService.findById(2L).get());

                Perfumes perfume2 = new Perfumes();
                perfume2.setName("Habit Rouge Parfum");
                perfume2.setBrand(guerlain);
                perfume2.setPrice(90.0f);
                perfume2.setVolume(100.0f);
                perfume2.setSeason("Invierno");
                perfume2.setDescription("Alcohol");
                perfume2.setFecha(2024);
                perfume2.setTipo(tipoService.findById(1L).get());

                Perfumes perfume3 = new Perfumes();
                perfume3.setName("Terre D'hermes");
                perfume3.setBrand(hermes);
                perfume3.setPrice(90.0f);
                perfume3.setVolume(100.0f);
                perfume3.setSeason("Verano");
                perfume3.setDescription("Marte");
                perfume3.setFecha(2006);
                perfume3.setTipo(tipoService.findById(4L).get());

                perfumeService.save(perfume1);
                perfumeService.save(perfume2);
                perfumeService.save(perfume3);
            }
            if (tipoService.count()== 0) {
                Tipos perfume = new Tipos();
                perfume.setName("Perfume");

                Tipos aguaDePerfume = new Tipos();
                aguaDePerfume.setName("Agua de perfume");

                Tipos aguaDeGragancia = new Tipos();
                aguaDeGragancia.setName("Agua de fragancia");
                
                Tipos aguaDeColonia = new Tipos();
                aguaDeColonia.setName("Agua de colonia");

                tipoService.save(perfume);
                tipoService.save(aguaDePerfume);
                tipoService.save(aguaDeGragancia);
                tipoService.save(aguaDeColonia);
            }
        };
    }
}
