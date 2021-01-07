package fr.ubx.poo.game;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class WorldCreatorFromFile {

    public int x;
    public int y;
    public WorldEntity[][] mapEntities;

    public void getXYFromFile(BufferedReader bRead) throws IOException {
        int j;
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

            j = i;
            i = bRead.read();

            if(j != 10 && i == -1){
                y++;
            }
        }
    }

    public void getMapFromFile(BufferedReader bRead) throws IOException {
        int xi = 0;
        int yi = 0;
        Optional<WorldEntity> owe;

        int i = bRead.read();
        while(i != -1) {
            char dataChar = (char) i;
            owe = WorldEntity.fromCode(dataChar);

            if(owe.isPresent()){
                mapEntities[yi][xi] = owe.get();
                xi++;
            }
            else {
                if(xi >= x) {
                    xi = 0;
                }
                yi++;
            }

            i = bRead.read();
        }
    }

    public WorldCreatorFromFile(String path, String prefixMondes, int index) throws IOException {
        x = 0;
        y = 0;

        FileInputStream fIn = new FileInputStream(path+"\\"+prefixMondes + index + ".txt");
        BufferedReader bRead = new BufferedReader(new InputStreamReader(fIn));
        getXYFromFile(bRead);
        fIn.getChannel().position(0);
        bRead = new BufferedReader(new InputStreamReader(fIn));

        mapEntities = new WorldEntity[y][x];
        getMapFromFile(bRead);

        bRead.close();
    }
}
