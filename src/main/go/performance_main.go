package main

import (
	"blacklist-search-go/blacklistvalidator"
	"fmt"
	"runtime"
	"time"
)

func main() {
	validator := blacklistvalidator.HostBlackListsValidator{}
	ip := "202.24.34.55"

	cores := runtime.NumCPU()

	threadConfigurations := []int{
		1,
		cores,
		cores * 2,
		50,
		100,
		1000,
		10000,
		100000,
	}

	fmt.Println("Available cores:", cores)
	fmt.Println("--------------------------------")

	for _, threads := range threadConfigurations {
		start := time.Now()

		validator.CheckHost(ip, threads)

		elapsed := time.Since(start)

		fmt.Printf(
			"Goroutines: %d | Execution time: %d ms\n",
			threads,
			elapsed.Milliseconds(),
		)

		// Pausa para observar métricas
		time.Sleep(3 * time.Second)
	}
}
