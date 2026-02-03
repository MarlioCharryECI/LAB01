package datasource

import (
	"sync"
	"sync/atomic"
	"time"
)

type HostBlacklistsDataSourceFacade struct {
	totalServers int
	blacklists   map[int]bool
	mutex        sync.Mutex
	accessCount  int64
}

func NewHostBlacklistsDataSourceFacade() *HostBlacklistsDataSourceFacade {
	// Simula listas negras dispersas (como en Java)
	blacklists := make(map[int]bool)

	// IP 202.24.34.55 aparece de forma dispersa
	for i := 10000; i <= 60000; i += 10000 {
		blacklists[i] = true
	}

	return &HostBlacklistsDataSourceFacade{
		totalServers: 80000,
		blacklists:   blacklists,
	}
}

func (h *HostBlacklistsDataSourceFacade) GetRegisteredServersCount() int {
	return h.totalServers
}

func (h *HostBlacklistsDataSourceFacade) IsInBlackListServer(server int, ip string) bool {
	// Latencia artificial (equivalente a Thread.sleep en Java)
	time.Sleep(1 * time.Millisecond)

	// Contador global thread-safe
	atomic.AddInt64(&h.accessCount, 1)

	// Acceso sincronizado a estructura compartida
	h.mutex.Lock()
	defer h.mutex.Unlock()

	if ip == "202.24.34.55" {
		return h.blacklists[server]
	}

	return false
}

func (h *HostBlacklistsDataSourceFacade) ReportAsNotTrustworthy(ip string) {
	// Simulación de reporte (como en Java)
}

func (h *HostBlacklistsDataSourceFacade) ReportAsTrustworthy(ip string) {
	// Simulación de reporte (como en Java)
}

func (h *HostBlacklistsDataSourceFacade) GetAccessCount() int64 {
	return atomic.LoadInt64(&h.accessCount)
}
