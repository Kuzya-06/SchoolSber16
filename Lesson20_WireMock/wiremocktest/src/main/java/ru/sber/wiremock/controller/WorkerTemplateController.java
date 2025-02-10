package ru.sber.wiremock.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;
import ru.sber.wiremock.model.Worker;
import ru.sber.wiremock.model.WorkerRequest;

import java.util.Arrays;
import java.util.List;

@SessionScope
@RestController
@RequestMapping("api/v2/works")
@RequiredArgsConstructor
public class WorkerTemplateController {

    @Value("${works_base_url}")
    private String baseUrl;

    private static final Logger log = LoggerFactory.getLogger(WorkerTemplateController.class);


    private final RestTemplate restTemplate;

    /**
     * Получить работника по ID
     *
     * @param id ID работника
     * @return Worker объект
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Worker getWorker(@PathVariable int id) {
        log.info("Get worker with id {}", id);
        String url = baseUrl+"/worker/{id}";

        Worker worker = restTemplate.getForObject(url, Worker.class, id);

        log.info("Return with id {} worker {}", id, worker);
        return worker;
    }

    /**
     * Получить список всех работников
     *
     * @return Список работников
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Worker> getWorkers() {
        log.info("Get workers");
        String url = baseUrl+"/worker";

        Worker[] workers = restTemplate.getForObject(url, Worker[].class);

        log.info("Return workers {}", Arrays.toString(workers));
        return Arrays.asList(workers);
    }

    /**
     * Получить количество работников
     *
     * @return Количество работников в формате JSON
     */
    @GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public String countWorkers() {
        int size = getWorkers().size();
        log.info("Get worker count {}", size);
        return String.format("{\"count\" : %d}", size);
    }

    /**
     * Создание нового работника
     *
     * @param workerRequest Данные работника для создания
     * @return Новый Worker объект
     */
    @PostMapping(value = "/new-worker", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Worker createWorker(@RequestBody WorkerRequest workerRequest) {
        log.info("Create new workerRequest {}", workerRequest);
        String url = baseUrl+"/worker/new";

        HttpEntity<WorkerRequest> requestEntity = new HttpEntity<>(workerRequest);

        Worker temp = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Worker.class).getBody();

        log.info("Return Worker {}", temp);
        return temp;
    }
}
