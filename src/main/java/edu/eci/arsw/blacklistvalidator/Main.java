/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import java.util.List;

/**
 *
 * @author hcadavid
 */

public class Main {

    public static void main(String[] args) {

        HostBlackListsValidator validator = new HostBlackListsValidator();

        int numberOfThreads = 4;

        List<Integer> blackListOccurrences =
                validator.checkHost("200.24.34.55", numberOfThreads);

        System.out.println("The host was found in the following blacklists:");
        System.out.println(blackListOccurrences);
    }
}
