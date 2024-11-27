package com.sky.utils;


import com.alibaba.fastjson.JSON;
import com.sky.properties.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import java.util.Map;
@Component
@Data
@AllArgsConstructor
@Slf4j
public class AddressUtil {

    @Autowired
    private Address addressConfig;

        public Map<String,Double> getAddress(String address) throws Exception {
//            String AK = "TnDcRg6tW9gfMpqZpMJraA4aOIHLuSo2";
            String AK=addressConfig.getAK();
            String strUrl = "http://api.map.baidu.com/geocoding/v3/?address="+ address+"&output=json&ak=" + AK;

            StringBuilder json = new StringBuilder();
                try {
                    URL url = new URL(strUrl);
                    URLConnection urlConnection = url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                    String inputLine = null;
                    while ((inputLine = in.readLine()) != null) {
                        json.append(inputLine);
                    }
                    in.close();
                } catch (MalformedURLException e) {
                } catch (IOException e) {
                }
                String data = json.toString();
                if (data != null && !"".equals(data)) {
                    Map map = JSON.parseObject(data, Map.class);
                    if ("0".equals(map.getOrDefault("status", "500").toString())) {
                        Map childMap = (Map) map.get("result");
                        Map posMap = (Map) childMap.get("location");
                        // 经度
                        double lng = Double.parseDouble(posMap.getOrDefault("lng", "0").toString());
                        // 纬度
                        double lat = Double.parseDouble(posMap.getOrDefault("lat", "0").toString());

                        Map<String, Double> addressMap = new HashMap<>();
                        addressMap.put("lng", lng);
                        addressMap.put("lat", lat);
                        return addressMap;
                    }
                }
                return null;


        }

    }