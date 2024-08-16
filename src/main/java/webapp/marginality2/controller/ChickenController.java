package webapp.marginality2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webapp.marginality2.model.Chicken;
import webapp.marginality2.service.ChickenService;

@RestController
@RequestMapping("/chickens")
public class ChickenController {

    private final ChickenService chickenService;

    @Autowired
    public ChickenController(ChickenService chickenService) {
        this.chickenService = chickenService;
    }

    @PostMapping
    public Mono<Chicken> createChicken(@RequestBody Chicken chicken) {
        return chickenService.create(chicken);
    }

    @PutMapping("/{id}")
    public Mono<Chicken> updateChicken(@PathVariable Integer id, @RequestBody Chicken chicken) {
        return chickenService.update(id, chicken);
    }

    @GetMapping("/{id}")
    public Mono<Chicken> getChickenById(@PathVariable Integer id) {
        return chickenService.findById(id);
    }

    @GetMapping
    public Flux<Chicken> getAllChickens() {
        return chickenService.findAll();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteChicken(@PathVariable Integer id) {
        return chickenService.deleteById(id);
    }
}
