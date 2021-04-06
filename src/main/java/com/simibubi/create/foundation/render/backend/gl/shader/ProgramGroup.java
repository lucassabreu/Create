package com.simibubi.create.foundation.render.backend.gl.shader;

import com.simibubi.create.foundation.render.backend.gl.GlFogMode;

import java.util.Map;

public class ProgramGroup<P extends GlProgram> {

    private final Map<GlFogMode, P> programs;

    public ProgramGroup(Map<GlFogMode, P> programs) {
        this.programs = programs;
    }

    public P get(GlFogMode fogMode) {
        return programs.get(fogMode);
    }

    public void delete() {
        programs.values().forEach(GlProgram::delete);
    }
}
