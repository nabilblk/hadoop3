# Flume

# logger.flume: A single-node Flume configuration

# Name the components on this agent
a1.sources = r1
a1.sinks = k1
a1.channels = c1

# Describe/configure the source
a1.sources.r1.type = netcat
a1.sources.r1.bind = localhost
a1.sources.r1.port = 44444


# Describe the sink
a1.sinks.k1.type = logger

# Use a channel which buffers events in memory
a1.channels.c1.type = memory
a1.channels.c1.capacity = 1000
a1.channels.c1.transactionCapacity = 100

# Bind the source and sink to the channel
a1.sources.r1.channels = c1
a1.sinks.k1.channel = c1

$ bin/ flume-ng agent --conf conf --conf-file logger.flume --name a1 -Dflume.root.logger=INFO,console

# This will limit the output to 16 bytes. To change it you need AdvancedFlumeSink. Turn to the next example for the same..

# open nc and type lines which will be moved through flume 
nc localhost 44444 