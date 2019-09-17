package com.github.wxiaoqi.learning.common.util.jwt;

import java.io.Serializable;

/**
 * Created by ace on 2017/9/10.
 */
public class JWTInfo implements Serializable,IJWTInfo {


    private String uniqueName;
    private String id;
    private String name;

    public JWTInfo(){

    }

    public JWTInfo(String name,String id,String uniqueName){
        this.uniqueName = uniqueName;
        this.id = id;
        this.name = name;
    }

    @Override
    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JWTInfo jwtInfo = (JWTInfo) o;

        if (uniqueName != null ? !uniqueName.equals(jwtInfo.uniqueName) : jwtInfo.uniqueName != null) return false;
        return id != null ? id.equals(jwtInfo.id) : jwtInfo.id == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}