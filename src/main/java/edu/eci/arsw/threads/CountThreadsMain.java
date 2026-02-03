/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 *
 * @author hcadavid
 */
public class CountThreadsMain {

    public static void main(String[] args) {

        Thread[] threads = {
                new CountThread(0, 99),
                new CountThread(100, 199),
                new CountThread(200, 299)
        };

        System.out.println("Using start()");

        for (Thread t : threads) {
            t.start();
        }
        System.out.println("Todos los hilos terminaron");

        waitForThreads(threads);

        System.out.println("\nUsing run()");

        Thread[] threadsRun = {
                new CountThread(0, 99),
                new CountThread(100, 199),
                new CountThread(200, 299)
        };

        for (Thread t : threadsRun) {
            t.run();
        }
    }

    private static void waitForThreads(Thread[] threads) {
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread interrupted");
            }
        }
    }
}
