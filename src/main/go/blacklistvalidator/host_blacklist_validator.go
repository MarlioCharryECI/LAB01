package blacklistvalidator

import (
	"blacklist-search-go/datasource"
	"log"
)

type HostBlackListsValidator struct{}

func (v *HostBlackListsValidator) CheckHost(ip string, n int) []int {
	ds := datasource.NewHostBlacklistsDataSourceFacade()
	totalServers := ds.GetRegisteredServersCount()

	var globalOccurrences int32 = 0
	var stopSearch int32 = 0

	serversPerWorker := totalServers / n
	done := make(chan *SearchWorker, n)

	start := 0
	for i := 0; i < n; i++ {
		end := start + serversPerWorker
		if i == n-1 {
			end = totalServers
		}

		worker := &SearchWorker{
			start:             start,
			end:               end,
			ip:                ip,
			dataSource:        ds,
			globalOccurrences: &globalOccurrences,
			stopSearch:        &stopSearch,
		}

		go worker.Run(done)
		start = end
	}

	var checkedLists int
	var occurrences []int

	for i := 0; i < n; i++ {
		w := <-done
		checkedLists += w.checkedLists
		occurrences = append(occurrences, w.occurrences...)
	}

	if len(occurrences) >= 5 {
		ds.ReportAsNotTrustworthy(ip)
	} else {
		ds.ReportAsTrustworthy(ip)
	}

	log.Printf("Checked Black Lists: %d of %d", checkedLists, totalServers)
	return occurrences
}
