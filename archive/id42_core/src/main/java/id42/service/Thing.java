package id42.service;

import org.slf4j.Logger;

import javax.inject.Inject;

public class Thing {
    @Inject
    Logger log;

    protected Logger log() {
        return log;
    }
}
