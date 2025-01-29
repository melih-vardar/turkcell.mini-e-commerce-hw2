package com.turkcell.mini_e_commere_hw2.core.web;

import an.awesome.pipelinr.Pipeline;

public class BaseController {
    protected final Pipeline pipeline;

    public BaseController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }
}
