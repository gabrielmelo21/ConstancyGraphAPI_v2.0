package constacygraph.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;

import constacygraph.api.models.Graphs;
import constacygraph.api.repository.GraphRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GraphService {

    @Autowired
    private GraphRepository repository;


    /**
     *   0. Oque precisamos fazer é gerar aquela grade de atividade diaria igual a do github
     *    e cada quadrado representa um dia, em nosso json mockup temos uma lista com 366 itens, que representa
     *    366 dias do ano, são pequenos quadrados, que possuem status true or false, true fica verde, false vermelho
     *   1. Pegamos o file calendar_empty.json que é um mockup para geração de novos json
     *   2. Usamos a lib jackson para manipular o json
     *   3. O que difere essas grades chamadas por mim de Graphs, é o attr Objetivo String,
     *   4. Toda vez que criamos uma nova grade/Graph, usamos o mockup de base e colocamos apenas o novo dado chamado Objetivo
     *    que é enviado pelo usuario
     *
     *    {
     *   "dataDia" : "01/01",
     *   "status" : false,
     *   "numeroDoDia" : 1,
     *   "objetivo" : ""
     * }, {
     *   "dataDia" : "02/01",
     *   "status" : false,
     *   "numeroDoDia" : 2,
     *   "objetivo" : ""
     * } ...
     *
     *
     */

    public ResponseEntity<String> newGraph(String objetivo) {

        ArrayList<Graphs> diaDoAnoArrayList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");

        for (int i = 1; i <= 365; i++) {
            Calendar calendar = Calendar.getInstance();

            // Definir o ano para 2024 (ou o ano desejado)
            calendar.set(Calendar.YEAR, 2024);

            // Definir o dia do ano
            calendar.set(Calendar.DAY_OF_YEAR, i);

            // Obter a data formatada
            String dataFormatada = dateFormat.format(calendar.getTime());


            Graphs diaDoAno = new Graphs(null,dataFormatada, false, i, objetivo);
            diaDoAnoArrayList.add(diaDoAno);
        }

        repository.saveAll(diaDoAnoArrayList);

        return new ResponseEntity<>("Novo Graph criado com sucesso", HttpStatus.CREATED);
    }


    public ResponseEntity<List<Graphs>> listAllGraphs(){
        List<Graphs> list =  repository.findAll();
         return new ResponseEntity<>(list, HttpStatus.OK);
    }

    public ResponseEntity<List<Graphs>> listByObjetivo(String objetivo) {
        List<Graphs> items = repository.findAllByObjetivo(objetivo, Sort.by(Sort.Order.asc("id")));




        if (!items.isEmpty()) {
            return new ResponseEntity<>(items, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<String> limparDados(){
         repository.deleteAll();
        return new ResponseEntity<>("Todos dados apagados com sucesso.", HttpStatus.OK);
    }


    public ResponseEntity<List<String>> listObjetivos(){
        List<Graphs> list =  repository.findAll();
        List<String> uniqueObjectives = new ArrayList<>();

        for (Graphs obj: list) {
            String objetivo = obj.getObjetivo();

            // Verifica se o objetivo já está na lista única
            if (!uniqueObjectives.contains(objetivo)) {
                // Adiciona o objetivo à lista única
                uniqueObjectives.add(objetivo);
            }
        }

        return new ResponseEntity<>(uniqueObjectives, HttpStatus.OK);



    }




    public ResponseEntity<String> updateFrequency(String objetivo) {
        List<Graphs> graphs = repository.findAllByObjetivo(objetivo, Sort.by(Sort.Order.asc("id")));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
        String currentDate = dateFormat.format(new Date());

        for (Graphs graph : graphs) {
            if (currentDate.equals(graph.getDataDia())) {
                graph.setStatus(true);
                repository.save(graph);
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body("Atualizado com sucesso");
    }





    public ResponseEntity<String> updateFrequencyById(String id) {
        Optional<Graphs> optionalGraph = repository.findById(Long.valueOf(id));

        if (optionalGraph.isPresent()) {
            Graphs graph = optionalGraph.get();
            Boolean statusValue = graph.getStatus();

            if (!statusValue) {
                graph.setStatus(true);
                repository.save(graph); // Salva a entidade atualizada no repositório
                return ResponseEntity.status(HttpStatus.OK).body("Atualizado com sucesso");
            } else {
                graph.setStatus(false);
                repository.save(graph); // Salva a entidade atualizada no repositório
                return ResponseEntity.status(HttpStatus.OK).body("Atualizado com sucesso");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar, tente novamente.");
        }
    }


}
