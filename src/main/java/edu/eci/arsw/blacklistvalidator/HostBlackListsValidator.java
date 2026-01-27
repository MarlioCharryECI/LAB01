package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HostBlackListsValidator {

    /** Check the given host's IP address in all the available blacklists, and report it as NOT Trustworthy when
     *  such IP was reported in at least * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case. * The
     *  search is not exhaustive: When the number of occurrences is equal to * BLACK_LIST_ALARM_COUNT, the search is
     *  finished, the host reported as * NOT Trustworthy, and the list of the five blacklists returned.
     *  * @param ipaddress suspicious host's IP address.
     *  * @return Blacklists numbers where the given host's IP address was found. */

    private static final int BLACK_LIST_ALARM_COUNT = 5;
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());

    public List<Integer> checkHost(String ipaddress, int N) {

        HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();
        int totalServers = skds.getRegisteredServersCount();

        List<SearchThread> threads = new LinkedList<>();
        List<Integer> blackListOccurrences = new LinkedList<>();

        int serversPerThread = totalServers / N;
        int start = 0;

        for (int i = 0; i < N; i++) {
            int end = (i == N - 1) ? totalServers : start + serversPerThread;
            SearchThread thread = new SearchThread(start, end, ipaddress, skds);
            threads.add(thread);
            start = end;
        }

        for (SearchThread t : threads) {
            t.start();
        }

        int checkedListsCount = 0;
        for (SearchThread t : threads) {
            try {
                t.join();
                blackListOccurrences.addAll(t.getBlackListOccurrences());
                checkedListsCount += t.getCheckedLists();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (blackListOccurrences.size() >= BLACK_LIST_ALARM_COUNT) {
            skds.reportAsNotTrustworthy(ipaddress);
        } else {
            skds.reportAsTrustworthy(ipaddress);
        }

        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}",
                new Object[]{checkedListsCount, totalServers});

        return blackListOccurrences;
    }
}
