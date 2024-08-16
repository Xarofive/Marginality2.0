package webapp.marginality2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webapp.marginality2.model.Egg;
import webapp.marginality2.service.EggService;

@RestController
@RequestMapping("/eggs")
public class EggController {

    private final EggService eggService;

    @Autowired
    public EggController(EggService eggService) {
        this.eggService = eggService;
    }

    @PostMapping
    public Mono<Egg> createEgg(@RequestBody Egg egg) {
        return eggService.create(egg);
    }

    @PutMapping("/{id}")
    public Mono<Egg> updateEgg(@PathVariable Integer id, @RequestBody Egg egg) {
        return eggService.update(id, egg);
    }

    @GetMapping("/{id}")
    public Mono<Egg> getEggById(@PathVariable Integer id) {
        return eggService.findById(id);
    }

    @GetMapping
    public Flux<Egg> getAllEggs() {
        return eggService.findAll();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteEgg(@PathVariable Integer id) {
        return eggService.deleteById(id);
    }
}
