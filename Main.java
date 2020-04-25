package assignment;


import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import javax.swing.*;

public class Main {


    /*
    Keypoints to code
    -----------------
    Sealevel is a new variable within Earth meaning i don't change values in the MapOfEarth I change a single variable which
    edits the graphical representation

    The Mouse adapter section was unaligned and now is refined to fit the maps graphical representation


     */
    public static void WriteList(String Filename,ArrayList<MapCoordinate> MapCoordinateList){

        try{
            PrintStream output = new PrintStream(Filename);
            for (MapCoordinate a:MapCoordinateList) {
                output.println(a.toString());
            }
            output.close();
        }
        catch(FileNotFoundException fnf){
            System.out.println(fnf.toString());
            System.out.println("OUTPUT FILE NOT FOUND");
        }

    }






    public static void main(String[] args) {

        //Class initialization
        Earth Earth1 = new Earth();
        Earth1.readDataArray("C:\\Users\\ArifM\\Desktop\\Arif\\WebDevelopment-master\\EarthJavaAssignment\\src\\assignment\\earthHD.xyz");
        Earth1.readDataMap("C:\\Users\\ArifM\\Desktop\\Arif\\WebDevelopment-master\\EarthJavaAssignment\\src\\assignment\\earthHD.xyz");



        //////////////////////////////////////////////////////////////////////////////////////////////////////////////Graphical Management
        JFrame EarthDrawing = new JFrame("EarthDrawing");//New Frame for drawing points
        EarthDrawing.setPreferredSize(new Dimension(Earth1.BOX_WIDTH+14,Earth1.BOX_HEIGHT+37));
        //Need to add 14 and 37 due to the Frame not being the actual correct size to begin with to fit all the values of the Earth
        if(args.length != 0){

            try{
                Earth1.setSealevel(Integer.parseInt(args[0]));
            }
            catch(IllegalArgumentException ARGerror){
                System.out.println(ARGerror);
                System.out.println("Please enter a correct argument! Only integers!");
            }

        }
        ArrayList<MapCoordinate> MapCoordinateList = new ArrayList<>();
        ArrayList<MapCoordinate> OLDCOORDS = new ArrayList<>();

        //Mouseadapter is abstract class that is an implementation of mouselistener, for mouselistener you need to
        //Override all the methods whilst mouseadapter lets you do only one, (pressed in this case)
        EarthDrawing.addMouseListener(new MouseAdapter() {
            int fileVersion = 1;
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {

                    //System.out.println("X :" + e.getX() + " Y: " + e.getY());
                    double X = e.getX();
                    double Y = e.getY();
                    X -= 14;
                    X /= 2;
                    X -= 180;
                    if (X < 0) {
                        X += 360;
                    }
                    Y -= 37;
                    Y /= 2;
                    Y -= 90;
                    Y *= -1;
                    X += 7;
                    Y -= 3;
                    if (X > 360.0) {
                        X -= (X - 360.0);
                    }
                    X -= 4;
                    //The Scaling system is very complex and  took me the longest to correct and finish out of all the sections here,
                    // the inaccuracy of X Y coords to Map figures due to the rounding in drawing
                    double altitude = Earth1.getAltitude(X, Y);

                    MapCoordinate COORD = new MapCoordinate(X,Y,altitude);

                    //TODO THE DISTANCE IS CORRECT, IT doesnt scale well to the MAP though due to scaling


                    if(MapCoordinateList.size() > 0){
                        System.out.println(COORD.toString());
                        System.out.println("Distance from last Coordinate:");
                        System.out.println(COORD.distanceto(OLDCOORDS.get(OLDCOORDS.size()-1)));
                    }
                    OLDCOORDS.add(COORD);
                    MapCoordinateList.add(COORD);


                    Collections.sort(MapCoordinateList);
                    System.out.println("---------------------------------");
                    WriteList("Output"+fileVersion,MapCoordinateList);
                }
                else if(SwingUtilities.isRightMouseButton(e)){
                    if(MapCoordinateList.size() > 0){
                        System.out.println("REMOVED POINT "+ OLDCOORDS.get(OLDCOORDS.size()-1).toString()); //PRints last added COord
                        MapCoordinateList.remove(OLDCOORDS.get(OLDCOORDS.size()-1));//Removes it from OG List
                        OLDCOORDS.remove(OLDCOORDS.size()-1);//Removes it from the list that holds all the values added
                        if(MapCoordinateList.size() == 0){
                            fileVersion++;
                        }
                    }
                    else{
                        System.out.println("ERROR: There aren't any values added to the list yet");
                    }

                }
            }

        });
        EarthDrawing.add(Earth1);
        EarthDrawing.setResizable(false);
        EarthDrawing.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        EarthDrawing.pack();
        EarthDrawing.setVisible(true);

        ////////////////////////////////////////////////////////////////////////////////////Program code for functionality
        boolean running = true;//Boolean for when the user exits the program loop
        Scanner in = new Scanner(System.in);
        while(running) {
            System.out.println("Please enter \n1) for percentage Above \n2) for altitude at a coordinate \n3) To change the sealevel");
            String s = in.nextLine();

            switch (s) {
                case "quit":
                    System.out.println("Bye!");
                    running = false;
                    break;
                case "1":
                    try {
                        System.out.println("Please enter an altitude");
                        String altitude = in.nextLine();
                        Earth1.percentageAbove(Double.parseDouble(altitude));
                    } catch (Exception E) {
                        System.out.println("Please enter a correct Altitude or \"quit\" to Exit the program");
                    }

                    break;
                case "2":
                    System.out.println("Please enter Longitude (0-360) and latitude(-90 - 90)");
                    String[] Coordinates = in.nextLine().split("\\s+");
                    try {
                        double altitude = Earth1.getAltitude(Double.parseDouble(Coordinates[0]), Double.parseDouble(Coordinates[1]));
                        System.out.println("The altitude at longitude " + Coordinates[0] + " and latitude " + Coordinates[1] + " is " + altitude);
                    } catch (Exception E) {
                        System.out.println("Please enter valid longitude/latitude or \"quit\" to end program");
                    }
                    break;
                case "3":
                    try {
                        System.out.println("Please enter a new Sealevel: ");
                        String altitude = in.nextLine();
                        if (Integer.parseInt(altitude) > -10000) {
                            Earth1.setSealevel(Integer.parseInt(altitude));
                        }
                    } catch (Exception E) {
                        System.out.println("Please enter a correct Sealevel or \"quit\" to Exit the program");
                    }
                    Earth1.repaint();
                    break;
                default:
                    System.out.println("Please enter \"quit\" To exit the program");
                    break;
            }

        }
        in.close();

    }

}
