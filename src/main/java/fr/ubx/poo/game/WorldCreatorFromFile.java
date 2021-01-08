package fr.ubx.poo.game;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class WorldCreatorFromFile {

    private int x;
    private int y;
    private WorldEntity[][] mapEntities;

    /**Method calculates the height and width of the loaded map
     * @param bRead buffer with loaded file.
     */
    public void getXYFromFile(BufferedReader bRead) throws IOException {
        int j; //Used if there is no next line character at the end of the level file.
        int i = bRead.read();
        while(i != -1) {

            char dataChar = (char) i;
            Optional<WorldEntity> owe = WorldEntity.fromCode(dataChar);

            if(owe.isEmpty()){
                if(i == 10)
                    y++;
                else {
                    System.err.println("Error loading level");
                    x = 0;
                    y = 0;
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

    /**Method for setting items from file to map
     * @param bRead buffer with loaded file.
     */
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
    /**World creator from file constructor
     * @param path path to files
     * @param prefixWorlds world standard name
     * @param index world index
     */
    public WorldCreatorFromFile(String path, String prefixWorlds, int index) throws IOException {
        x = 0;
        y = 0;

        try(FileInputStream fIn = new FileInputStream(path+"\\"+prefixWorlds + index + ".txt")) {
            BufferedReader bRead = new BufferedReader(new InputStreamReader(fIn));
            getXYFromFile(bRead);
            if(x > 0 && y > 0) {
                fIn.getChannel().position(0);
                bRead = new BufferedReader(new InputStreamReader(fIn));

                mapEntities = new WorldEntity[y][x];
                getMapFromFile(bRead);
            }
            bRead.close();
        }
        catch (IOException ex) {
            System.err.println("Error loading level from path");
        }
    }

    public WorldEntity[][] getMapEntities() {
        return mapEntities;
    }
}
