;;;
;;;   Copyright 2014, University of Applied Sciences Frankfurt am Main
;;;
;;;   This software is released under the terms of the Eclipse Public License 
;;;   (EPL) 1.0. You can find a copy of the EPL at: 
;;;   http://opensource.org/licenses/eclipse-1.0.php
;;;

(ns
  ^{:author "Ruediger Gad",
    :doc "Interfaces for Java interop."} 
  clj-data-exchange.java-interfaces)

(gen-interface
  :name clj_data_exchange.Consumer
  :methods [[processObject [Object] void]])

(gen-interface
  :name clj_data_exchange.Producer
  :methods [[sendObject [Object] void]])

(gen-interface
  :name clj_data_exchange.DataExchangeController
  :methods [[connectConsumer [String clj_data_exchange.Consumer] void]
            [createProducer [String] clj_data_exchange.Producer]])

