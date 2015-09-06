import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Sink;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.EventHelper;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdvancedLoggerSink extends AbstractSink implements Configurable {

    private static final int defaultMaxBytes = 16;

    private int maxBytesProp;

    private static final Logger logger = LoggerFactory
            .getLogger(AdvancedLoggerSink.class);

    @Override
    public void configure(Context context) {
        // maxBytes of 0 means to log the entire event
        int maxBytesProp = context.getInteger("maxBytes", defaultMaxBytes);
        if (maxBytesProp < 0) {
            maxBytesProp = defaultMaxBytes;
        }

        this.maxBytesProp = maxBytesProp;
    }

    @Override
    public Status process() throws EventDeliveryException {
        Status result = Status.READY;
        Channel channel = getChannel();
        Transaction transaction = channel.getTransaction();
        Event event = null;

        try {
            transaction.begin();
            event = channel.take();

            if (event != null) {
                if (logger.isInfoEnabled()) {
                    logger.info("Event: " + EventHelper.dumpEvent(
                                    event,
                                    this.maxBytesProp == 0 ? event.getBody().length : this.maxBytesProp
                                ));
                }
            } else {
                // No event found, request back-off semantics from the sink
                // runner
                result = Status.BACKOFF;
            }
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
            throw new EventDeliveryException("Failed to log event: " + event,
                    ex);
        } finally {
            transaction.close();
        }

        return result;
    }
}