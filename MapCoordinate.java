package assignment;


public class MapCoordinate implements Comparable<MapCoordinate> {
    public final double  LATITUDE;
    public final double LONGITUDE;
    public final double ALTITUDE;

    MapCoordinate(double LAT, double LONG, double ALT){
        this.LATITUDE = LAT;
        this.LONGITUDE = LONG;
        this.ALTITUDE = ALT;
    }


    public double distanceto(MapCoordinate Loc2){
        //USing haversine formula
        int EarthRadius = 6371;
        double Latitude1 = Math.toRadians(this.LATITUDE);
        double Latitude2 = Math.toRadians(Loc2.LATITUDE);
        double DeltaLat = Math.toRadians(Loc2.LATITUDE - this.LATITUDE);
        double DeltaLong = Math.toRadians(Loc2.LONGITUDE-this.LONGITUDE);

        double Haversine = Math.sin(DeltaLat/2)*Math.sin(DeltaLat/2)+Math.cos(Latitude1)*Math.cos(Latitude2)*Math.sin(DeltaLong/2)*Math.sin(DeltaLong/2);
        double c = 2*Math.atan2(Math.sqrt(Haversine),Math.sqrt(1-Haversine));

        return EarthRadius*c; //Distance
    }

    @Override
    public int compareTo(MapCoordinate mapCoordinate) {
        int result = (int)(this.ALTITUDE - mapCoordinate.ALTITUDE);
        if(result == 0){
            result = (int)(this.LATITUDE - mapCoordinate.LATITUDE);
            if(result == 0){
                result = (int)(this.LONGITUDE - mapCoordinate.LONGITUDE);
            }
        } //THe comaprison this.Longitude.compareTo(mapcoordinate.Longitude) wasnt recognized
        return result;
        } //TODO ensure that This is the correct comparison, use LEcture 7 slide 28


    @Override
    public String toString(){
        return "Latitude:"+this.LATITUDE+" Longitude:"+this.LONGITUDE+" Altitude:"+this.ALTITUDE;
    }
    @Override
    public boolean equals(Object Coord1){
        if(getClass() != Coord1.getClass())
            return false;
        if(this.ALTITUDE == ((MapCoordinate) Coord1).ALTITUDE)
            if(this.LATITUDE == ((MapCoordinate) Coord1).LATITUDE)
                if(this.LONGITUDE == ((MapCoordinate) Coord1).LONGITUDE)
                    return true; //Could use shorthand If statements or just return the statement outcome,

    return false;
    }


}
