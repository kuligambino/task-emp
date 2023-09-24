package com.empik.kulig.app.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

@JdbcTest
@Sql({"classpath:test-data.sql"})
@Import(StatsRepository.class)
@Execution(CONCURRENT) // Umożliwia równoległe wykonywanie testów
class StatsRepositoryConcurrentTest {

    @Autowired
    private StatsRepository statsRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ExecutorService executorService;

    @BeforeEach
    public void setUp() {
        // Inicjalizacja repozytorium i executora przed każdym testem
        executorService = newFixedThreadPool(5); // 5 wątków
    }

    @AfterEach
    public void tearDown() {
        // Zamykanie executora po każdym teście
        executorService.shutdown();
    }

    @Test
    void testConcurrentIncrement() throws InterruptedException {
        String login = "testUser";
        int numThreads = 5; // Liczba wątków symulujących równoległe żądania
        CountDownLatch latch = new CountDownLatch(numThreads);

        // Wywołanie metody incrementRequestCount w wielu wątkach
        for (int i = 0; i < numThreads; i++) {
            executorService.submit(() -> {
                statsRepository.incrementRequestCount(login);
                latch.countDown();
            });
        }

        // Oczekiwanie na zakończenie wszystkich wątków
        latch.await();

        // Sprawdzenie, czy licznik został poprawnie zaktualizowany (powinien wynosić numThreads)
        int finalCount = jdbcTemplate.queryForObject("SELECT request_count FROM api_request_stats WHERE login = ?", Integer.class, login);
        assertEquals(numThreads, finalCount);
    }
}
