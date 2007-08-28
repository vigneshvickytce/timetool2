
package com.timeTool;

/**
 *  This runnable job waits for the keyboard and mouse to go idle and then it calls
 * the listener with the number of seconds the computer has been idle. 
 */

public final class IdleJob implements Runnable {
	private final IdleListener listener;
	private final int idleThreshold;


	static {
		System.loadLibrary("IdleTime");
	}

	public IdleJob(IdleListener listener, int idleThreshold) {
		this.listener = listener;
		this.idleThreshold = idleThreshold;
	}

	public void run() {
		final int idleTime = getIdleTime();
		if (idleTime > idleThreshold) {
			listener.onIdle(idleTime);
		}
	}


	/**
	 * Returns Idle Time in seconds.
	 *
	 * @return
	 * 		idle time in seconds
	 */
	private native int getIdleTime();


	/**
	 * Interface that gets notified when an idle occurs. 
	 */
	public interface IdleListener {
		void onIdle(int seconds); 
	}
}
