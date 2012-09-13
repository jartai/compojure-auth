(ns compojure-auth.auth
  (:refer-clojure :exclude [get])
  (:use ring.middleware.session
        ring.middleware.session.memory
        ring.middleware.flash))
