
package com.timeTool;

/**
 *  This runnable job waits for the keyboard and mouse to go idle and then it calls
 * the listener with the number of seconds the computer has been idle. 
 */

public final class IdleJob implements java.lang.Runnable {
	private final Runnable<Integer> listener;
	private final int idleThreshold;


	static {
		System.loadLibrary("IdleTime");
	}

	public IdleJob(Runnable<Integer> listener, int idleThreshold) {
		this.listener = listener;
		this.idleThreshold = idleThreshold;
	}

	public void run() {
		final int idleTime = getIdleTime();
		if (idleTime > idleThreshold) {
			listener.run(idleTime);
		}
	}

	/**
	 * Returns Idle Time in seconds.
	 *
	 * @return
	 * 		idle time in seconds
	 */
	private native int getIdleTime();

}
