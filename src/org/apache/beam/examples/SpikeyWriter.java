package org.apache.beam.examples;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.io.neo4j.*;
import org.apache.beam.sdk.options.*;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.kafka.common.protocol.types.Field.Str;
import org.joda.time.Duration;
import java.nio.charset.StandardCharsets;

public class SpikeyWriter {
    public interface PubsubToGcsOptions extends StreamingOptions {

        @Description("Name of the topic to read from")
        @Default.String("projects/jitendra-dataflow-18042023/topics/topic1")
        String getTopicName();

        void setTopicName(String value);

        @Description("Path of the file to write to")
        @Default.String("gs://ja_df/output")
        String getOutputBucket();

        void setOutputBucket(String value);
    }

    public static void main(String[] args) {
        PubsubToGcsOptions options = PipelineOptionsFactory.fromArgs(args).withValidation()
                .as(PubsubToGcsOptions.class);
        readingPubSubData(options);
    }

    private static void readingPubSubData(PubsubToGcsOptions options) {
        options.setStreaming(true);
        Pipeline pipeline = Pipeline.create(options);
        pipeline.apply("ReadPubSubMsg", PubsubIO.readMessages().fromTopic(options.getTopicName()))
                .apply("ExtractMsg", ParDo.of(new ExtractMessage()))
                // .apply("ApplyWindow", Window.<String>into(FixedWindows.of(Duration.standardSeconds(20))))
                .apply(Neo4jIO.<String> writeUnwind()
                .withDriverConfiguration(Neo4jIO.DriverConfiguration.create("SDF", "ADF", "null")).withCypher("null"))
                      ;
        pipeline.run().waitUntilFinish();
    }


                    // .apply("WriteResult",
                   // TextIO.write().to(options.getOutputBucket()).withWindowedWrites().withNumShards(1))
                    // .apply("WriteResult", Neo4jIO.writeUnwind().withCypher("MERGE (a:A{ a: 1})")
                    // .withDriverConfiguration(Neo4jIO.DriverConfiguration.create("SDF", "ADF", "null"))
    private static class NeoWriteFn extends DoFn<String,String>{

        private String in;
        public NeoWriteFn(String in) {
            this.in = in;
        }


    }

    private static class ExtractMessage extends DoFn<PubsubMessage, String> {
        private static final long serialVersionUID = 1L;

        @ProcessElement
        public void processElement(ProcessContext c) {
            String payload = new String(c.element().getPayload(), StandardCharsets.UTF_8);
            c.output(payload);
        }
    }
}