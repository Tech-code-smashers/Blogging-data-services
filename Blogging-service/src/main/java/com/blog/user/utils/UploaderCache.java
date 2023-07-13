package com.blog.user.utils;

import java.util.HashMap;
import java.util.Map;

public class UploaderCache {
    public Map<String, HashMap<Object, Object>> cache= new HashMap<>();

    void setMapValues(Map <String, HashMap<Object, Object>> object) {
        this.cache=object;
    }

    public  HashMap<Object, Object> checkMapValues(String serviceCenterCode) {
        HashMap<Object, Object> hmap =this.cache.get("serviceCenter");
        if(hmap.containsKey(serviceCenterCode));
        return (HashMap<Object, Object>) hmap.get(serviceCenterCode);
    }

    public void putValue(String cacheName, Object Key, Object value) {
        if((Integer)value==0) {
            return;
        }
        HashMap map =cache.get(cacheName);
        if(map==null) {
            map=new HashMap<Object,Object>();
            map.put(Key, value);
            cache.put(cacheName, map);
        }
        map.put(Key, value);
    }

    public Object getValue(String cacheName, Object key) {
        HashMap<Object, Object> map = cache.get(cacheName);
        if (map != null) {
            return map.get(key);
        }
        return null;
    }
}
