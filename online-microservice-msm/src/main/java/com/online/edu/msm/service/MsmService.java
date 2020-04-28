package com.online.edu.msm.service;

import java.util.HashMap;

public interface MsmService {
    boolean send(String phone, String templateCode, HashMap<String, Object> param);
}
