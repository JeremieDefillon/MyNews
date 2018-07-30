package com.gz.jey.mynews.models;

public class Data {
    /**
     * @return
     * all the Getters & Setters for the foolwings vars
     */

    private static int actualTab = 0;
    private static String url = "";
    private static String lastUrl = "";
    private static int secTop = 0;
    private static int secMost = 0;
    private static int tNum = 0;
    private static int pNum = 0;
    private static String searchQuery = "";
    private static String searchFilters= "";
    private static String beginDate;
    private static String endDate;
    private static boolean loadNotif=false;
    private static String notifQuery = "";
    private static String notifFilters = "";
    private static int hour = 0;
    private static int minutes = 0;

    public static int getActualTab() {
        return actualTab;
    }

    public static void setActualTab(int a) {
        actualTab = a;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String u) {
        url = u;
    }

    public static String getLastUrl() {
        return lastUrl;
    }

    public static void setLastUrl(String l) {
        lastUrl = l;
    }

    public static int getSecTop() {
        return secTop;
    }

    public static void setSecTop(int s) {
        secTop = s;
    }

    public static int getSecMost() {
        return secMost;
    }

    public static void setSecMost(int s) {
        secMost = s;
    }

    public static int gettNum() {
        return tNum;
    }

    public static void settNum(int t) {
        tNum = t;
    }

    public static int getpNum() {
        return pNum;
    }

    public static void setpNum(int p) {
        pNum = p;
    }

    public static String getSearchQuery() {
        return searchQuery;
    }

    public static void setSearchQuery(String q) {
        searchQuery = q;
    }

    public static String getSearchFilters() {
        return searchFilters;
    }

    public static void setSearchFilters(String s) {
        searchFilters = s;
    }

    public static String getBeginDate() {
        return beginDate;
    }

    public static void setBeginDate(String b) {
        beginDate = b;
    }

    public static String getEndDate() {
        return endDate;
    }

    public static void setEndDate(String e) {
        endDate = e;
    }

    public static boolean isLoadNotif() {
        return loadNotif;
    }

    public static void setLoadNotif(boolean l) {
        loadNotif = l;
    }

    public static String getNotifQuery() {
        return notifQuery;
    }

    public static void setNotifQuery(String n) {
        notifQuery = n;
    }

    public static String getNotifFilters() {
        return notifFilters;
    }

    public static void setNotifFilters(String n) {
        notifFilters = n;
    }

    public static int getHour() {
        return hour;
    }

    public static void setHour(int h) {
        hour = h;
    }

    public static int getMinutes() {
        return minutes;
    }

    public static void setMinutes(int m) {
        minutes = m;
    }
}
