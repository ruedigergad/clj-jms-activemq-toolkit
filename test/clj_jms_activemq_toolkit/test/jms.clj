;;;
;;;   Copyright 2014, University of Applied Sciences Frankfurt am Main
;;;
;;;   This software is released under the terms of the Eclipse Public License 
;;;   (EPL) 1.0. You can find a copy of the EPL at: 
;;;   http://opensource.org/licenses/eclipse-1.0.php
;;;

(ns 
  ^{:author "Ruediger Gad",
    :doc "Tests for JMS interaction"}  
  clj-jms-activemq-toolkit.test.jms
  (:use clojure.test
        clj-assorted-utils.util
        clj-jms-activemq-toolkit.jms))

(def ^:dynamic *local-jms-server* "tcp://127.0.0.1:42424")
(def test-topic "/topic/testtopic.foo")

(defn run-test [t]
  (let [broker (start-broker *local-jms-server*)]
    (t)
    (.stop broker)))

(defn single-test-fixture [t]
  (run-test t)
  (binding [*local-jms-server* "stomp://127.0.0.1:42423"]
    (run-test t)))

(use-fixtures :each single-test-fixture)

(deftest test-fixture
  (is true))

(deftest test-create-topic
  (let [topic (create-producer *local-jms-server* test-topic)]
    (is (not (nil? topic)))))

(deftest producer-consumer
  (let [producer (create-producer *local-jms-server* test-topic)
        was-run (prepare-flag)
        consume-fn (fn [_] (set-flag was-run))
        consumer (create-consumer *local-jms-server* test-topic consume-fn)]
    (is (not (nil? producer)))
    (is (not (nil? consumer)))
    (producer "¡Hola!")
    (await-flag was-run)
    (is (flag-set? was-run))
    (close producer)
    (close consumer)))

(deftest send-list
  (let [producer (create-producer *local-jms-server* test-topic)
        received (ref '())    
        flag (prepare-flag)
        consume-fn (fn [obj] (dosync (ref-set received obj)) (set-flag flag))
        consumer (create-consumer *local-jms-server* test-topic consume-fn)
        data '(:a :b :c)]
    (is (not= data @received))
    (producer data)
    (await-flag flag)
    (is (= data @received))))

