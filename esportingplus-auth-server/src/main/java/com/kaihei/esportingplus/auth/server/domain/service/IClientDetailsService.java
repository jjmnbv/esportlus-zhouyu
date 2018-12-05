package com.kaihei.esportingplus.auth.server.domain.service;

import com.kaihei.esportingplus.auth.server.domain.entity.ClientDetails;
import java.util.List;

public interface IClientDetailsService {

    List<ClientDetails> listAll();
}
