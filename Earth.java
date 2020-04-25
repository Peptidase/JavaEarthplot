package assignment;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

public class Earth extends JComponent
{
    private double[][] arrayOfEarth;
    private Map<List<Double>, Double> mapOfEarth = new HashMap<>();
    private int Length = 0; //Used to specify the length of the file for bigger or smaller calculations
    public static int BOX_WIDTH = 720; //Used to specify constant window size that i can change easily
    public static int BOX_HEIGHT= 360;
    private int Sealevel = 0;



    //Use a list as a key as an array will not work due to the get command not functioning the same way as you would expect
    //https://stackoverflow.com/questions/16839182/can-a-java-array-be-used-as-a-hashmap-key
/////////////////////////////////////////////////////////////////////////////////////Array Handling
    public void readDataArray(String filename){
        try{


            int count = 0; //Used to specify which array we are assigning within the loop
            String[] line; //Used to temporarily store the line values
            double[] Values = new double[3]; //Used to store the values we will append to the ArrayOfEarth


            Scanner FileLength = new Scanner(new File(filename)); //Creates scanner object for the file object that is the earth.xyz file
            while(FileLength.hasNextLine() ){//This one is ran through to check How long the file actually is
                Length++;
                FileLength.nextLine();
            }
            FileLength.close();
            arrayOfEarth = new double[Length][3];
            Scanner input = new Scanner(new File(filename)); //Creates scanner object for the file object that is the earth.xyz file

            while(input.hasNextLine()){//This one is ran to read each of the lats and longs and altitudes
                line = input.nextLine().split("\\s+");
                for(int i = 0; i<line.length; i++){
                    //This for loop reads through the line fetched and assigns all the strings to doubles in an array
                    arrayOfEarth[count][i] = Double.parseDouble(line[i]);
                }

                count++;
            }//TODO Ensure these are optimized, maybe these can be 1 loop?
            input.close();

        }
        catch (FileNotFoundException fnf){
            System.out.println("This is a pre-determined error, PLEASE ENSURE YOU HAVE THE RIGHT PATH");
            System.out.println(fnf);
        }

    }

    public List<Double[]> coordinatesAbove(double altitude){
        List<Double[]> CoordsAbove = new ArrayList<>(); //Uses ArrayList because it makes more sense to add

        for (double[] SinglePoint:arrayOfEarth) { //For each set of coordinates in the array of earth
            if(SinglePoint[2] > altitude){ //Checks all values
                CoordsAbove.add(new Double[] {SinglePoint[0],SinglePoint[1]});
            }

        }
        return CoordsAbove;
    }
    public List<Double[]> coordinatesBelow(double altitude){
        List<Double[]> CoordsBelow = new ArrayList<>();//Uses ArrayList because it makes more sense to add

        for (double[] SinglePoint:arrayOfEarth) {//For each set of coordinates in the array of earth
            if(SinglePoint[2] < altitude){ //Checks all values
                CoordsBelow.add(new Double[] {SinglePoint[0],SinglePoint[1]});
            }

        }
        return CoordsBelow;
    }



    public void percentageAbove(double altitude){
        if(Length > 0){
            System.out.println((double)Math.round(((coordinatesAbove(altitude).size() / (double)Length) *100)*10) /10+" % is above that altitude");
        }
    }
    public void percentageBelow(double altitude){
        if(Length > 0){
            System.out.println((double)Math.round(((coordinatesBelow(altitude).size() / (double)Length) *100)*10) /10+" % is below that altitude");
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////Map  Handling

    public void generateMap(double resolution){
        double width = 360.0; //Stores default resolution
        double height = 180.0;
        ArrayList<Double> Coords = new ArrayList<>();

        width = (width * (1/(resolution))); //Uses a reciprocal to find out the correct scaling
        height = (height *(1/(resolution)));
        for(int i = 0; i < width; i++){ //Uses nested for loops to generate a point for every value
            for(int j = 0; j<height;j++){
                Coords.add(width);
                Coords.add(height);
                double altitude = ((1000*Math.random())/10);
                mapOfEarth.put(Coords,altitude);
                Coords.clear();
            }
        }
        System.out.println("Map Generated!");
    }

    public void readDataMap(String filename){
        try{

            String [] line;
            Scanner input = new Scanner(new File(filename));
            while(input.hasNextLine())
            {
                ArrayList<Double> Coords = new ArrayList<>();
                //TODO Make a clearing method that can ensure to improve performance rather than making a new ArrayList everytime
                line = input.nextLine().split("\\s+");
                Coords.add(Double.parseDouble(line[0]));
                Coords.add(Double.parseDouble(line[1]));
                mapOfEarth.put(Coords,Double.parseDouble(line[2]));

                //Coords.clear() doesnt work in this case which is frustrating

            }
            input.close();
        }
        catch(FileNotFoundException fnf)
        {
            System.out.println("This is a pre-determined error, PLEASE ENSURE YOU HAVE THE RIGHT PATH");
            System.out.println(fnf);
        }

    }

    public double getAltitude(double longitude, double latitude){
        ArrayList<Double> Coords = new ArrayList<>();
        double Altitude;
        //Lat -90 to 90
        //long 0 to 360
        Coords.add(longitude);
        Coords.add(latitude);
        //Can only use get methods with a Arraylist of double data type,
        // cannot specify it in parameters though therefore create new method before searching
        try{
            Altitude = mapOfEarth.get(Coords);
            return Altitude;
        }
        catch(Exception e) {
            return 0.0;
        }
    }




    public void setSealevel(int Sealvl){
        Sealevel = Sealvl;
    }



//////////////////////////////////////////////////////////////////////////////////////Java Graphical


    public void paintComponent(Graphics graphics){
        int MinAltitude = 0;

        for (Map.Entry<List<Double>, Double> entry:mapOfEarth.entrySet()){
            int x,y;
            int NewColour;

            List<Double> Coordinates = entry.getKey();
            double Altitude = entry.getValue();
            x = (int)(Coordinates.get(0)*2)- 360; //Long
            //x = (int)Math.round(Coordinates.get(0)*2); //Long
            y = (int)(((-1*((Coordinates.get(1)-90)*2)))); //Lat

         if(x < 0){
             x+=720;
         }

            //The reason we -90 and multiply by negative 1 is to ensure that the 90 to -90 scale is now 0 to 180

            if(Altitude >= Sealevel){


                if(Altitude < 250){
                    NewColour = (int)(((Altitude - 0) * (255-225))/(250-(-10000)))+225;
                    graphics.setColor(new Color(0, NewColour, 0));
                }
                else if(Altitude <  500) {
                    NewColour = (int)(((Altitude - 250)*(225-200))/(500-250))+200;
                    graphics.setColor(new Color(0, NewColour, 0));
                }
                else if(Altitude < 2000){
                    NewColour = (int)(((Altitude - 500)*(200-150))/(3000-500))+150;
                    graphics.setColor(new Color(0,NewColour,0));
                }
                else if(Altitude < 5000){
                    NewColour = (int)(((Altitude - 3000)*(150-125))/(5000-3000))+125;
                    graphics.setColor(new Color(0,NewColour,0));
                }

            }
            if(Altitude < Sealevel){

                int Oldrange = Sealevel - (-15000);
                int NewRange = 255;
                NewColour = ((((int) Altitude - (-15000)) * NewRange) / Oldrange);//Use formula agin till this works

                graphics.setColor(new Color(0,0,NewColour));
            }

            if(!(x > BOX_WIDTH && y > BOX_HEIGHT))
            {
                graphics.fillOval(x,y,2,2);

            }
        }


    }


}
