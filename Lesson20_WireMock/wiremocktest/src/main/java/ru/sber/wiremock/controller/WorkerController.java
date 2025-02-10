package ru.sber.wiremock.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.reactive.function.client.WebClient;
import ru.sber.wiremock.model.Worker;
import ru.sber.wiremock.model.WorkerRequest;

import java.util.List;

@SessionScope
@RestController
@RequestMapping("api/works")
public class WorkerController {

    Logger log = LoggerFactory.getLogger(WorkerController.class);
    @Autowired
    private WebClient webClient;

    @GetMapping(value = {"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Worker getWorker(@PathVariable int id) {
        log.info("Get worker with id {}", id);
        Worker worker = webClient.get()
                .uri(x -> x
                        .path("/worker/{id}")
                        .build(id))
                .retrieve()
                .bodyToMono(Worker.class)
                .block();

        log.info("Return with id {} worker {}", id, worker);
        return worker;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Worker> getWorkers() {
        log.info("Get worker");
        List<Worker> workers = webClient.get()
                .uri("/worker")
                .retrieve()
                .bodyToFlux(Worker.class)
                .collectList()
                .block();

        log.info("Return worker {}", workers);
        return workers;
    }

    @GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public String countWorkers() {
        int size = getWorkers().size();
        log.info("Get worker count {}", size);
        return String.format("{\"count\" : %d}", size);
    }


    @PostMapping("/new-worker")
    public Worker createWorker(@RequestBody WorkerRequest workerRequest) {
        log.info("Create new workerRequest {}", workerRequest);
       Worker temp = webClient.post().uri("/worker/new").bodyValue(workerRequest)
                .retrieve()
                .bodyToMono(Worker.class)
               .block();
       log.info("Return Worker {}", temp);
       return temp;
    }

}
