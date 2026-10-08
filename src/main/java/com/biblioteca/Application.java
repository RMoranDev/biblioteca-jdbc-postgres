package com.biblioteca;

import com.biblioteca.ui.MenuPrincipal;

import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            new MenuPrincipal(scanner).iniciar();
        }
    }
}