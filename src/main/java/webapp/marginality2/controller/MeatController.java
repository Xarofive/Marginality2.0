package webapp.marginality2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webapp.marginality2.model.Meat;
import webapp.marginality2.service.MeatService;

@RestController
@RequestMapping("/meats")
public class MeatController {

    private final MeatService meatService;

    @Autowired
    public MeatController(MeatService meatService) {
        this.meatService = meatService;
    }

    @PostMapping
    public Mono<Meat> createMeat(@RequestBody Meat meat) {
        return meatService.create(meat);
    }

    @PutMapping("/{id}")
    public Mono<Meat> updateMeat(@PathVariable Integer id, @RequestBody Meat meat) {
        return meatService.update(id, meat);
    }

    @GetMapping("/{id}")
    public Mono<Meat> getMeatById(@PathVariable Integer id) {
        return meatService.findById(id);
    }

    @GetMapping
    public Flux<Meat> getAllMeats() {
        return meatService.findAll();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteMeat(@PathVariable Integer id) {
        return meatService.deleteById(id);
    }
}
