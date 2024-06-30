# ClimateDataCollector
### Executar Versão Sequencial (Sem Threads)
1. Modifique o método `runAllExperiments` para:
    ```java
    private static void runAllExperiments(List<City> cities) throws InterruptedException {
        // Versão sem threads
        System.out.println("Versão sem threads:");
        runExperimentWithoutThreads(cities);
    }
    ```
2. Execute o método `main` da classe `ClimateDataCollector`:
    - Clique com o botão direito na classe `ClimateDataCollector` e selecione "Run 'ClimateDataCollector.main()'".

### Executar Versão com 3 Threads
1. Modifique o método `runAllExperiments` para:
    ```java
    private static void runAllExperiments(List<City> cities) throws InterruptedException {
        // Versão com 3 threads
        System.out.println("Versão com 3 threads:");
        runExperimentWithThreads(cities, 3);
    }
    ```
2. Execute o método `main` da classe `ClimateDataCollector`:
    - Clique com o botão direito na classe `ClimateDataCollector` e selecione "Run 'ClimateDataCollector.main()'".

### Executar Versão com 9 Threads
1. Modifique o método `runAllExperiments` para:
    ```java
    private static void runAllExperiments(List<City> cities) throws InterruptedException {
        // Versão com 9 threads
        System.out.println("Versão com 9 threads:");
        runExperimentWithThreads(cities, 9);
    }
    ```
2. Execute o método `main` da classe `ClimateDataCollector`:
    - Clique com o botão direito na classe `ClimateDataCollector` e selecione "Run 'ClimateDataCollector.main()'".

### Executar Versão com 27 Threads
1. Modifique o método `runAllExperiments` para:
    ```java
    private static void runAllExperiments(List<City> cities) throws InterruptedException {
        // Versão com 27 threads
        System.out.println("Versão com 27 threads:");
        runExperimentWithThreads(cities, 27);
    }
    ```
2. Execute o método `main` da classe `ClimateDataCollector`:
    - Clique com o botão direito na classe `ClimateDataCollector` e selecione "Run 'ClimateDataCollector.main()'".

## Descrição
Este projeto realiza a coleta e processamento de dados climáticos das 27 capitais brasileiras durante janeiro de 2024, utilizando a API da Open-Meteo. O objetivo é comparar a performance de diferentes implementações de algoritmos variando o número de threads utilizadas para realizar as requisições HTTP.

## Estrutura do Projeto
- `src/main/java/substitutiva/com/br/ClimateDataCollector.java`: Classe principal que executa os experimentos.
- `src/main/java/substitutiva/com/br/City.java`: Classe que representa uma cidade com nome, latitude e longitude.
- `src/main/java/substitutiva/com/br/HttpClient.java`: Classe utilitária para fazer requisições HTTP.
- `src/main/java/substitutiva/com/br/TemperatureData.java`: Classe para processar e armazenar dados de temperatura.
- `pom.xml`: Arquivo de configuração do Maven.

## Requisitos
- Java 17
- Maven

## Observações
- O método `runAllExperiments` foi configurado para facilitar a execução de cada versão do experimento.
