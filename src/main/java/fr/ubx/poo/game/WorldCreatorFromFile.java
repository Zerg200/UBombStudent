package fr.ubx.poo.game;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class WorldCreatorFromFile {

    public int x = 0;
    public int y = 0;


    public WorldEntity[][] mapEntities;


    public void getXYFromFile(BufferedReader bRead) throws IOException {
        int j = 0;
        int i = bRead.read();
        while(i != -1) {
            char dataChar = (char) i;
            Optional<WorldEntity> owe = WorldEntity.fromCode(dataChar);
            if(owe.isEmpty()){
                if(i == 10)
                    y++;
                else {
                    System.err.println("Error loading level");
                    break;
                }

            }
            if(y == 0){
                x++;
            }
            //System.out.println(i + " " + dataChar + " x:" + x + " y:" + y);
            j = i;
            i = bRead.read();
            if(j != 10 && i == -1){
                y++;
            }
        }
        //System.out.println(x + " " + y);

    }

    public void getMapFromFile(BufferedReader bRead) throws IOException {

        int xi = 0;
        int yi = 0;
        Optional<WorldEntity> owe;

        int i = bRead.read();
        while(i != -1) {
            if(yi >= y){
                yi = 0;
                //System.out.println(" !!!");
                xi++;
            }
            char dataChar = (char) i;
            owe = WorldEntity.fromCode(dataChar);
            if(!owe.isEmpty()){
                mapEntities[xi][yi] = WorldEntity.fromCode(dataChar).get();
                //System.out.print(" " + mapEntities[xi][yi]);
                yi++;
            }

            i = bRead.read();
        }
        //System.out.println(x + " " + y);

    }

    public WorldCreatorFromFile(String path, String prefixMondes, int index) throws IOException {

        FileInputStream fIn = new FileInputStream(path+"\\"+prefixMondes + index + ".txt");
        BufferedReader bRead = new BufferedReader(new InputStreamReader(fIn));
        getXYFromFile(bRead);
        fIn.getChannel().position(0);
        bRead = new BufferedReader(new InputStreamReader(fIn));

        if(y < x){
            int tmp = x;
            x = y;
            y = tmp;
        }
        mapEntities = new WorldEntity[x][y];
        getMapFromFile(bRead);

        /*System.out.println(mapEntities[0].length);
        for(int i = 0; i <mapEntities.length; i++) {
            for(int j = 0; j < mapEntities[0].length; j++) {
                System.out.print("" + mapEntities[i][j]);
            }
            System.out.println("");
        }*/

        bRead.close();
    }
}
