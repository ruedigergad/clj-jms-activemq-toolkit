
;;;
;;;   Copyright 2014, University of Applied Sciences Frankfurt am Main
;;;
;;;   This software is released under the terms of the Eclipse Public License 
;;;   (EPL) 1.0. You can find a copy of the EPL at: 
;;;   http://opensource.org/licenses/eclipse-1.0.php
;;;

(ns 
  ^{:author "Ruediger Gad",
    :doc "Tests for data exchange interaction"}  
  clj-jms-activemq-toolkit.test.data-exchange
  (:use clojure.test
        clj-assorted-utils.util
        clj-jms-activemq-toolkit.jms)
  (:import (clj_data_exchange Consumer DataExchangeController Producer)
           (clj_jms_activemq_data_exchange ActiveMqDataExchangeController ActiveMqProducer)))

(def local-jms-server "tcp://localhost:42424")
(def test-topic "/topic/testtopic.foo")

(defn jms-broker-fixture [f]
  (let [broker (start-broker local-jms-server)]
    (f)
    (.stop broker)))

(use-fixtures :each jms-broker-fixture)

(deftest test-create-activemq-controller
  (let [controller (ActiveMqDataExchangeController. local-jms-server)]
    (is (instance? DataExchangeController controller))))

(deftest test-create-activemq-producer
  (let [controller (ActiveMqDataExchangeController. local-jms-server)
        producer (.createProducer controller test-topic)]
    (is (instance? Producer producer))))

(deftest test-create-activemq-producer
  (let [controller (ActiveMqDataExchangeController. local-jms-server)
        producer (.createProducer controller test-topic)
        flag (prepare-flag)
        data (ref nil)
        consumer (proxy [Consumer] []
                   (processObject [obj]
                     (set-flag flag)
                     (dosync (ref-set data obj))))
        _ (.connectConsumer controller test-topic ^Consumer consumer)]
    (.sendObject producer "foo")
    (await-flag flag)
    (is (flag-set? flag))
    (is (= "foo" @data))))

(deftest test-start-stop-embedded-broker
  (let [controller (ActiveMqDataExchangeController. "tcp://localhost:42425")]
    (.startEmbeddedBroker controller)
    (.stopEmbeddedBroker controller)))

(deftest test-start-stop-embedded-broker-with-data-exchange
  (let [controller (ActiveMqDataExchangeController. "tcp://localhost:42425")
        _ (.startEmbeddedBroker controller)
        producer (.createProducer controller test-topic)
        flag (prepare-flag)
        data (ref nil)
        consumer (proxy [Consumer] []
                   (processObject [obj]
                     (set-flag flag)
                     (dosync (ref-set data obj))))
        _ (.connectConsumer controller test-topic ^Consumer consumer)]
    (.sendObject producer "foo")
    (await-flag flag)
    (is (flag-set? flag))
    (is (= "foo" @data))
    (.stopEmbeddedBroker controller)))


