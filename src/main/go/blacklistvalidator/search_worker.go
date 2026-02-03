package blacklistvalidator

import (
	"sync/atomic"

	"blacklist-search-go/datasource"
)

type SearchWorker struct {
	start, end int
	ip         string
	dataSource *datasource.HostBlacklistsDataSourceFacade

	globalOccurrences *int32
	stopSearch        *int32

	checkedLists int
	occurrences  []int
}

func (w *SearchWorker) Run(done chan<- *SearchWorker) {
	for i := w.start; i < w.end && atomic.LoadInt32(w.stopSearch) == 0; i++ {
		w.checkedLists++

		if w.dataSource.IsInBlackListServer(i, w.ip) {
			w.occurrences = append(w.occurrences, i)

			if atomic.AddInt32(w.globalOccurrences, 1) >= 5 {
				atomic.StoreInt32(w.stopSearch, 1)
				break
			}
		}
	}
	done <- w
}
