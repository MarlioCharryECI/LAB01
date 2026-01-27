package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;

public class SearchThread extends Thread {

    private final int start;
    private final int end;
    private final String ip;
    private final HostBlacklistsDataSourceFacade dataSource;

    private final List<Integer> blackListOccurrences = new LinkedList<>();
    private int checkedLists = 0;

    public SearchThread(int start, int end, String ip, HostBlacklistsDataSourceFacade dataSource) {
        this.start = start;
        this.end = end;
        this.ip = ip;
        this.dataSource = dataSource;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            checkedLists++;
            if (dataSource.isInBlackListServer(i, ip)) {
                blackListOccurrences.add(i);
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
