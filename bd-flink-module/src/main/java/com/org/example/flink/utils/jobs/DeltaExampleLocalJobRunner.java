/*
 * Copyright (2021) The Delta Lake Project Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.org.example.flink.utils.jobs;

import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.runtime.minicluster.MiniCluster;
import org.apache.flink.runtime.minicluster.MiniClusterConfiguration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.File;
import java.net.URL;

/**
 * Internal class providing utility methods to run local Flink job in memory.
 */
public interface DeltaExampleLocalJobRunner extends DeltaExampleJobRunner {

    default String getRunnerType(){
        return "local";
    }

    default MiniCluster getMiniCluster() {

        URL url = ClassLoader.getSystemResource(".");
        File file = (url != null) ? new File(url.getFile()) : new File(".");

        final org.apache.flink.configuration.Configuration config = GlobalConfiguration.loadConfiguration();
        //FileSystem.initialize(config);

        config.setString(RestOptions.BIND_PORT, "18081-19000");

        final MiniClusterConfiguration cfg =
                new MiniClusterConfiguration.Builder()
                        .setNumTaskManagers(2)
                        .setNumSlotsPerTaskManager(4)
                        .setConfiguration(config)
                        .build();
        return new MiniCluster(cfg);
    }

    default void runFlinkJobInBackground(StreamExecutionEnvironment env) {
        new Thread(() -> {
            try (MiniCluster miniCluster = getMiniCluster()) {
                miniCluster.start();
                miniCluster.executeJobBlocking(env.getStreamGraph().getJobGraph());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }).start();
    }
}