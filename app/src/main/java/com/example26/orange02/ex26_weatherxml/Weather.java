package com.example26.orange02.ex26_weatherxml;

import java.io.Serializable;

public class Weather implements Serializable {
    private String tmEf; //시간
    private String tmx; //최고 기온
    private String tmn; //최저 기온
    private String wf; // 날씨

    @Override
    public String toString() {
        String s = "Wheater{"+wf+"}"+"최고기온{"+tmx+"}"+"최저기온{"+tmn+"}"+"시간{"+tmEf+"}";
        return s;
    }

    public String getTmEf() {
        return tmEf;
    }

    public void setTmEf(String tmEf) {
        this.tmEf = tmEf;
    }

    public String getTmx() {
        return tmx;
    }

    public void setTmx(String tmx) {
        this.tmx = tmx;
    }

    public String getTmn() {
        return tmn;
    }

    public void setTmn(String tmn) {
        this.tmn = tmn;
    }

    public String getWf() {
        return wf;
    }

    public void setWf(String wf) {
        this.wf = wf;
    }
}
