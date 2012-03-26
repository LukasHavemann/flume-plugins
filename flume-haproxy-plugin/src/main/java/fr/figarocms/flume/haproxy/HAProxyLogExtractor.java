/**
 * Licensed to Cloudera, Inc. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Cloudera, Inc. licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.figarocms.flume.haproxy;

import com.google.common.base.Preconditions;

import com.cloudera.flume.conf.Context;
import com.cloudera.flume.conf.SinkFactory.SinkDecoBuilder;
import com.cloudera.flume.core.Event;
import com.cloudera.flume.core.EventSink;
import com.cloudera.flume.core.EventSinkDecorator;
import com.cloudera.util.Pair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.figarocms.flume.haproxy.processor.DateProcessor;
import fr.figarocms.flume.haproxy.processor.HeaderProcessor;


public class HAProxyLogExtractor<S extends EventSink> extends EventSinkDecorator<S> {

  static final Logger LOG = LoggerFactory.getLogger(HAProxyLogExtractor.class);

  private Mapping mapping;

  /**
   * @param s : SinkDecorator context from Flume
   */
  public HAProxyLogExtractor(S s) {

    super(s);

    this.mapping = new Mapping();

    this.mapping.addEntry("clientIp")
        .addEntry("clientPort")
        .addEntry("timestamp", new DateProcessor())
        .addEntry("frontend")
        .addEntry("backend")
        .addEntry("host")
        .addEntry("times")
        .addEntry("statusCode")
        .addEntry("byteRead")
        .addEntry("capturedRequestCookie")
        .addEntry("capturedResponseCookie")
        .addEntry("terminationState")
        .addEntry("connStates")
        .addEntry("queuesStates")
        .addEntry("capturedRequestHeaders", new HeaderProcessor())
        .addEntry("capturedResponseHeaders", new HeaderProcessor())
        .addEntry("verb")
        .addEntry("uri")
        .addEntry("version");

  }

  @Override
  public void append(Event e) throws IOException, InterruptedException {

    this.mapping.process(e);

    if (LOG.isDebugEnabled()) {
      LOG.debug("unable to match the given event's body {}", e.toString());
    }
    super.append(e);
  }


  /**
   * Retrieve the SinkDecoBuilder the hbase
   *
   * @return SinkDecoBuilder
   */
  public static SinkDecoBuilder builder() {
    return new SinkDecoBuilder() {
      // construct a new parameterized flume
      @Override
      public EventSinkDecorator<EventSink> build(Context context, String... argv) {
        Preconditions.checkArgument(argv.length == 0, "usage: haproxy");
        return new HAProxyLogExtractor<EventSink>(null);
      }
    };
  }

  /**
   * Used by flume.flume to register this hbase
   *
   * @return a list of pair name => deco builder
   */
  public static List<Pair<String, SinkDecoBuilder>> getDecoratorBuilders() {
    List<Pair<String, SinkDecoBuilder>> builders =
        new ArrayList<Pair<String, SinkDecoBuilder>>();
    builders.add(new Pair<String, SinkDecoBuilder>("haproxy",
                                                   builder()));
    return builders;
  }
}
