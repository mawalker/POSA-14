import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @class QueueAdapter
 * 
 * @brief Applies a variant of the GoF Adapter pattern that
 *        enables us to test several implementations of the
 *        BlockingQueue interface.
 */
class QueueAdaptor<E> {

	static int TIMEOUT_SECONDS = 5;

	/**
	 * Stores the queue that we're adapting.
	 */
	private BlockingQueue<E> mQueue;

	/**
	 * Store the queue that we're adapting.
	 */
	QueueAdaptor(BlockingQueue<E> queue) {
		mQueue = queue;
	}

	/**
	 * Insert msg at the tail of the queue.
	 * 
	 * @throws TimeoutException
	 *            , InterruptedException
	 */
	public void put(E msg) throws InterruptedException, TimeoutException {
		// Keep track of how many times we're called.
		SynchronizedQueueTest.mProducerCounter++;
		boolean timeoutValue = mQueue.offer(msg, TIMEOUT_SECONDS,
				TimeUnit.SECONDS);
		if (timeoutValue == false) { throw new TimeoutException(); }
	}

	/**
	 * Remove msg from the head of the queue.
	 */
	public E take() throws InterruptedException {
		// Keep track of how many times we're called.
		SynchronizedQueueTest.mConsumerCounter++;
		return mQueue.poll(TIMEOUT_SECONDS, TimeUnit.SECONDS);
	}

	public static int getTIMEOUT_SECONDS() {
		return TIMEOUT_SECONDS;
	}

	public static void setTIMEOUT_SECONDS(int tIMEOUT_SECONDS) {
		TIMEOUT_SECONDS = tIMEOUT_SECONDS;
	}

}
