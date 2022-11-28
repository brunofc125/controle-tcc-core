package com.controletcc.repository.projection;

import java.time.LocalDateTime;

public interface VersaoTccProjection {
    Long getId();

    String getObservacao();

    Long getVersao();

    String getPublicadoPelo();

    LocalDateTime getDataInclusao();
}
