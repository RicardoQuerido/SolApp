package pt.ua.querido.solapp.database.converter;

public class Local {

    private String precipitaProb;
    private String tMin;
    private String tMax;
    private String predWindDir;
    private int idWeatherType;
    private int classWindSpeed;
    private String longitude;
    private String forecastDate;
    private String latitude;

    public Local(String precipitaProb, String tMin, String tMax, String predWindDir, int idWeatherType, int classWindSpeed, String longitude, String forecastDate, String latitude) {
        this.precipitaProb = precipitaProb;
        this.tMin = tMin;
        this.tMax = tMax;
        this.predWindDir = predWindDir;
        this.idWeatherType = idWeatherType;
        this.classWindSpeed = classWindSpeed;
        this.longitude = longitude;
        this.forecastDate = forecastDate;
        this.latitude = latitude;
    }

    public String getPrecipitaProb() {
        return precipitaProb;
    }

    public void setPrecipitaProb(String precipitaProb) {
        this.precipitaProb = precipitaProb;
    }

    public String gettMin() {
        return tMin;
    }

    public void settMin(String tMin) {
        this.tMin = tMin;
    }

    public String gettMax() {
        return tMax;
    }

    public void settMax(String tMax) {
        this.tMax = tMax;
    }

    public String getPredWindDir() {
        return predWindDir;
    }

    public void setPredWindDir(String predWindDir) {
        this.predWindDir = predWindDir;
    }

    public int getIdWeatherType() {
        return idWeatherType;
    }

    public void setIdWeatherType(int idWeatherType) {
        this.idWeatherType = idWeatherType;
    }

    public int getClassWindSpeed() {
        return classWindSpeed;
    }

    public void setClassWindSpeed(int classWindSpeed) {
        this.classWindSpeed = classWindSpeed;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getForecastDate() {
        return forecastDate;
    }

    public void setForecastDate(String forecastDate) {
        this.forecastDate = forecastDate;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
