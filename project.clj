(defproject fg-netzwerksicherheit/clj-jms-activemq-toolkit "1.99.1"
  :description "Toolkit for using the ActiveMQ JMS implementation in Clojure."
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.cli "0.2.4"]
                 [com.ning/compress-lzf "1.0.0"]
                 [org.xerial.snappy/snappy-java "1.0.4.1"]
                 [com.esotericsoftware.kryo/kryo "2.22"]
                 [org.apache.activemq/activemq-broker "5.11.1"]
                 [org.apache.activemq/activemq-client "5.11.1"]
                 [org.apache.activemq/activemq-openwire-legacy "5.11.1"]
                 [org.apache.activemq/activemq-stomp "5.11.1"]
                 [org.fusesource.stompjms/stompjms-client "1.19"]
                 [org.slf4j/slf4j-simple "1.7.10"]
                 [clj-assorted-utils "1.9.1"]
                 [cheshire "5.4.0"]]
  :jvm-opts ["-Djavax.net.ssl.keyStore=test/ssl/broker.ks" "-Djavax.net.ssl.keyStorePassword=password" "-Djavax.net.ssl.trustStore=test/ssl/broker.ts" "-Djavax.net.ssl.trustStorePassword=password"]
  :license {:name "Eclipse Public License (EPL) - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "This is the same license as used for Clojure."}
  :global-vars {*warn-on-reflection* true}
  :aot :all
  :java-source-paths ["src-java"]
  :prep-tasks [["compile" "clj-jms-activemq-toolkit.java-interfaces"]
               ["javac" "src-java/clj_jms_activemq_toolkit/PooledBytesMessageProducer.java"]
               ["compile" "clj-jms-activemq-toolkit.ActiveMqJmsController"] "javac" "compile"]
  :main clj-jms-activemq-toolkit.main
  :plugins [[lein-cloverage "1.0.2"]])
