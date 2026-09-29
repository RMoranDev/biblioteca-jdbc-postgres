package com.projetojdbc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

// Classe fictícia, só para o exemplo
class LimiteRenovacoes {
    private final int max;
    private int usadas;

    LimiteRenovacoes(int max) {
        if (max < 1) throw new IllegalArgumentException("max deve ser >= 1");
        this.max = max;
    }

    void renovar() {
        if (usadas >= max) throw new IllegalStateException("Limite de renovações atingido");
        usadas++;
    }

    int usadas() { return usadas; }
}

@DisplayName("LimiteRenovacoes")
class LimiteRenovacoesTest {   // não precisa ser public no JUnit 5

    private LimiteRenovacoes limite;

    @BeforeEach
    void setUp() {
        limite = new LimiteRenovacoes(2);
    }

    @Nested
    @DisplayName("quando ainda há renovações disponíveis")
    class ComRenovacoesDisponiveis {

        @Test
        @DisplayName("incrementa o contador")
        void incrementaContador() {
            limite.renovar();
            assertEquals(1, limite.usadas());
        }
    }

    @Nested
    @DisplayName("quando o limite foi atingido")
    class ComLimiteAtingido {

        @BeforeEach
        void esgotar() {
            limite.renovar();
            limite.renovar();
        }

        @Test
        @DisplayName("lança IllegalStateException e não altera o contador")
        void lancaExcecao() {
            var ex = assertThrows(IllegalStateException.class, () -> limite.renovar());
            assertAll(
                () -> assertEquals("Limite de renovações atingido", ex.getMessage()),
                () -> assertEquals(2, limite.usadas())
            );
        }
    }

    @ParameterizedTest(name = "max={0} -> {1}")
    @CsvSource({
        "0,  false",
        "-1, false",
        "1,  true",
        "5,  true"
    })
    @DisplayName("valida o valor de max no construtor")
    void validaMax(int max, boolean valido) {
        if (valido) {
            assertDoesNotThrow(() -> new LimiteRenovacoes(max));
        } else {
            assertThrows(IllegalArgumentException.class, () -> new LimiteRenovacoes(max));
        }
    }
}