import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * @class SynchronizedQueueTest
 * 
 * @brief This program tests the use of Java Threads and several
 *        implementations of the Java BlockingQueue interface.
 */
public class SynchronizedQueueTest {

	/**
	 * Keep track of the number of times the producer test iterates.
	 */
	static volatile int mProducerCounter = 0;

	/**
	 * Keep track of the number of times the consumer test iterates.
	 */
	static volatile int mConsumerCounter = 0;

	/**
	 * Timeout length
	 */
	static int TIMEOUT_SECONDS = 5;

	/**
	 * enum for return values of testing logic,
	 * has String for easy output.
	 */
	public enum SynchronizedQueueResult {

		RAN_PROPERLY("Threads Ran Properly."), JOIN_NEVER_CALLED(
				"Join() never called."), THREADS_NEVER_RAN("Threads never ran."), THREADS_NEVER_INTERUPTED(
				"Threads never interrupted."), THREADS_THREW_EXCEPTION(
				"Thread threw an exception."), THREADS_NEVER_CREATED(
				"Threads never created."), TESTING_LOGIC_THREW_EXCEPTION(
				"Testing Logic threw Exception.");

		private String val = "";

		private SynchronizedQueueResult(String val) {
			this.val = val;
		}

		public String getString() {
			return val;
		}
	}

	/**
	 * Adapter object used to test different BlockingQueue
	 * implementations.
	 */
	static QueueAdaptor<Integer> mQueue = null;

	/**
	 * This runnable loops for mMaxIterations and calls put() on
	 * mQueue to insert the iteration number into the queue.
	 */
	static Runnable producerRunnable = new Runnable() {

		public void run() {
			for (int i = 0; i < mMaxIterations; i++)
				try {
					mQueue.put(i);
					if (Thread.interrupted())
						throw new InterruptedException();
				} catch (InterruptedException e) {
					System.out.println("Thread properly interrupted by "
							+ e.toString() + " in producerRunnable");
					// This isn't an error - it just means that
					// we've been interrupted.
					return;
				} catch (TimeoutException e) {
					System.out.println("TimeoutException " + e.toString()
							+ " occurred in producerRunnable");
					// Indicate a failure.
					mProducerCounter = -1;
					return;
				} catch (Exception e) {
					System.out.println("Exception " + e.toString()
							+ " occurred in producerRunnable");
					// Indicate a failure.
					mProducerCounter = -1;
					return;
				}
		}
	};

	/**
	 * This runnable loops for mMaxIterations and calls take() on
	 * mQueue to remove the iteration from the queue.
	 */
	static Runnable consumerRunnable = new Runnable() {

		public void run() {
			for (int i = 0; i < mMaxIterations; i++)
				try {
					if (Thread.interrupted()) { throw new InterruptedException(); }
					Integer result = (Integer) mQueue.take();
					if (result == null) { throw new TimeoutException(); }
					System.out.println("iteration = " + result);
				} catch (InterruptedException e) {
					System.out.println("Thread properly interrupted by "
							+ e.toString() + " in consumerRunnable");
					// This isn't an error - it just means that
					// we've been interrupted.
					return;
				} catch (TimeoutException e) {
					System.out.println("TimeoutException " + e.toString()
							+ " occurred in consumerRunnable");
					// Indicate a failure.
					mConsumerCounter = -1;
					return;
				} catch (Exception e) {
					System.out.println("Exception " + e.toString()
							+ " occurred in consumerRunnable");
					// Indicate a failure.
					mConsumerCounter = -1;
					return;
				}
		}
	};

	/**
	 * Number of iterations to test (the actual test shouldn't run
	 * this many iterations since the Threads ought to be interrupted
	 * long before it gets this far).
	 */
	static int mMaxIterations = 1000000;

	@SuppressWarnings("unused")
	static SynchronizedQueueResult SynchronizedQueueTestLogic(
			QueueAdaptor<Integer> queue) {
		try {
			// TODO - you fill in here to replace the null
			// initialization below to create two Java Threads, one
			// that's passed the producerRunnable and the other that's
			// passed the consumerRunnable.
			Thread consumer = null;
			Thread producer = null;

			// TODO - you fill in here to start the threads. More
			// interesting results will occur if you start the
			// consumer first.


			// Give the Threads a chance to run before interrupting
			// them.
			Thread.sleep(100);

			// TODO - you fill in here to interrupt the threads.

			
			// TODO - you fill in here to wait for the threads to
			// exit.


			// Do some sanity checking to see if the Threads work as
			// expected. (Leave this unchanged.)
			if (consumer == null || producer == null) {
				return SynchronizedQueueResult.THREADS_NEVER_CREATED;
			} else if (consumer.isAlive() || producer.isAlive()) {
				return SynchronizedQueueResult.JOIN_NEVER_CALLED;
			} else if (mConsumerCounter == 0 || mProducerCounter == 0) {
				return SynchronizedQueueResult.THREADS_NEVER_RAN;
			} else if (mConsumerCounter == mMaxIterations
					|| mProducerCounter == mMaxIterations) {
				return SynchronizedQueueResult.THREADS_NEVER_INTERUPTED;
			} else if (mConsumerCounter == -1 || mProducerCounter == -1) {
				return SynchronizedQueueResult.THREADS_THREW_EXCEPTION;
			} else {
				return SynchronizedQueueResult.RAN_PROPERLY;
			}
		} catch (InterruptedException e) {
			return SynchronizedQueueResult.TESTING_LOGIC_THREW_EXCEPTION;
		}
	}

	/**
	 * Run the test for the queue parameter.
	 */
	static void testQueue(String testName, QueueAdaptor<Integer> queue) {

		SynchronizedQueueResult result = SynchronizedQueueTestLogic(queue);

		String resultString = "*** Test " + testName + " ";
		if (result != SynchronizedQueueResult.RAN_PROPERLY) {
			resultString += "Failed because: " + result.getString();
		} else {
			resultString += "Passed.";
		}

		System.out.println(resultString);

	}

	/**
	 * Main entry point method into the test program.
	 */
	public static void main(String argv[]) {
		System.out.println("Starting SynchronizedQueueTest");
		// Indicate how big the queue should be, which should be
		// smaller than the number of iterations to induce blocking
		// behavior.
		int queueSize = mMaxIterations / 10;

		// Test the ArrayBlockingQueue, which should pass the test.
		mQueue = new QueueAdaptor<Integer>(new ArrayBlockingQueue<Integer>(
				queueSize));
		testQueue("ArrayBlockingQueue", mQueue);

		// Test the BuggyBlockingQueue, which should fail the test.
		mQueue = new QueueAdaptor<Integer>(new BuggyBlockingQueue<Integer>(
				queueSize));
		testQueue("BuggyBlockingQueue", mQueue);

		System.out.println("Finishing SynchronizedQueueTest");
	}
}
