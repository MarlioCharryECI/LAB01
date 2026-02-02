package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SearchThread extends Thread {

    private final int start;
    private final int end;
    private final String ip;
    private final HostBlacklistsDataSourceFacade dataSource;

    private final AtomicInteger globalOccurrences;
    private final AtomicBoolean stopSearch;

    private final List<Integer> blackListOccurrences = new LinkedList<>();
    private int checkedLists = 0;

    public SearchThread(
            int start,
            int end,
            String ip,
            HostBlacklistsDataSourceFacade dataSource,
            AtomicInteger globalOccurrences,
            AtomicBoolean stopSearch
    ) {
        this.start = start;
        this.end = end;
        this.ip = ip;
        this.dataSource = dataSource;
        this.globalOccurrences = globalOccurrences;
        this.stopSearch = stopSearch;
    }

    @Override
    public void run() {
        for (int i = start; i < end && !stopSearch.get(); i++) {
            checkedLists++;

            if (dataSource.isInBlackListServer(i, ip)) {
                blackListOccurrences.add(i);

                if (globalOccurrences.incrementAndGet() >= 5) {
                    stopSearch.set(true);
                    break;
                }
            }
        }
    }

    public List<Integer> getBlackListOccurrences() {
        return blackListOccurrences;
    }

    public int getCheckedLists() {
        return checkedLists;
    }
}
