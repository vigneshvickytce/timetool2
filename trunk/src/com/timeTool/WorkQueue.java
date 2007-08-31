package com.timeTool;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;

public final class WorkQueue {

	private ScheduledExecutorService jobExecutor = Executors.newScheduledThreadPool(3);
	private List<ScheduledFuture<?>> jobs = new ArrayList<ScheduledFuture<?>>();

	public void scheduleAtFixedRate(java.lang.Runnable runnable, long initialDelay, long period, TimeUnit timeUnit) {
		jobs.add(jobExecutor.scheduleAtFixedRate(runnable, initialDelay, period, timeUnit));
	}

	public void clear() {
		for (ScheduledFuture job : jobs) {
			job.cancel(false);
		}
	}
}
