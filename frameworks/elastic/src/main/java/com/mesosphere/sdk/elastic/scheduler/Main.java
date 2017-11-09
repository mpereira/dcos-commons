package com.mesosphere.sdk.elastic.scheduler;

import com.mesosphere.sdk.scheduler.DefaultScheduler;
import com.mesosphere.sdk.scheduler.SchedulerBuilder;
import com.mesosphere.sdk.scheduler.SchedulerConfig;
import com.mesosphere.sdk.scheduler.SchedulerRunner;
import com.mesosphere.sdk.specification.DefaultServiceSpec;
import com.mesosphere.sdk.specification.ServiceSpec;
import com.mesosphere.sdk.specification.yaml.RawServiceSpec;

import java.io.File;
import java.util.Arrays;

/**
 * Main entry point for the Scheduler.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException("Expected service YAML file as argument, got: " + Arrays.toString(args));
        }

        SchedulerRunner
            .fromSchedulerBuilder(createSchedulerBuilder(new File(args[0])))
            .run();
    }

    private static SchedulerBuilder createSchedulerBuilder(File yamlSpecFile) throws Exception {
        RawServiceSpec rawServiceSpec = RawServiceSpec.newBuilder(yamlSpecFile).build();
        SchedulerConfig schedulerConfig = SchedulerConfig.fromEnv();
        // Elasticsearch 5.6.4 didn't seem to have an issue with `cluster.name`s containing slashes.
        ServiceSpec serviceSpec = DefaultServiceSpec
            .newGenerator(rawServiceSpec, schedulerConfig, yamlSpecFile.getParentFile())
            .build();

        return DefaultScheduler
            .newBuilder(serviceSpec, schedulerConfig)
            .setPlansFrom(rawServiceSpec);
    }
}
