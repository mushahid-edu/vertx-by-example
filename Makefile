SHELL := /bin/bash

.PHONY: Exercise1 Exercise2 Exercise3_1 Exercise3_2 Exercise3_3 Exercise3_3_1 Exercise4 Exercise5 Exercise6 Exercise7 Exercise8

BASEDIR := $(PWD)

vertx/bin/vertx:
	@curl -L https://bintray.com/artifact/download/vertx/downloads/vert.x-3.3.3-full.tar.gz | tar -xz

Test:
	@echo First Prerquisite $@

Exercise1: vertx/bin/vertx
	@cd $(BASEDIR)/$@; vertx run $@.groovy

Exercise2: vertx/bin/vertx
	@cd $(BASEDIR)/$@; vertx run $@.groovy

Exercise3_1: vertx/bin/vertx
	@cd $(BASEDIR)/Exercise3; vertx run $@.groovy

Exercise3_2: vertx/bin/vertx
	@cd $(BASEDIR)/Exercise3; vertx run $@.groovy

Exercise3_3: vertx/bin/vertx
	@cd $(BASEDIR)/Exercise3; vertx run $@.groovy

Exercise3_3_1: vertx/bin/vertx
	@cd $(BASEDIR)/Exercise3; vertx run $@.groovy

Exercise4: vertx/bin/vertx
	@cd $(BASEDIR)/$@; vertx run $@.groovy

Exercise5: vertx/bin/vertx
	@cd $(BASEDIR)/$@; vertx run $@.groovy

Exercise6: vertx/bin/vertx
	@cd $(BASEDIR)/$@; vertx run $@.groovy

Exercise7: vertx/bin/vertx
	@cd $(BASEDIR)/$@; vertx run $@.groovy

Exercise8: vertx/bin/vertx
	@cd $(BASEDIR)/$@; vertx run $@.groovy