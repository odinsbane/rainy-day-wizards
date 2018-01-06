package wizards.maps;

import wizards.actors.Obstacle;
import wizards.actors.Rock;
import wizards.actors.Tree;
import wizards.images.WizardImages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

/**
 * This is a tool for creating/editing maps.
 *
 *
 * User: melkor
 * Date: 5/31/11
 * Time: 7:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class MapEditor {
    String type;

    public static void main(String[] args){
        final JFrame frame = new JFrame("map maker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final EditorPanel panel = new EditorPanel();
        //panel.setSize(600,600);
        panel.setMinimumSize(new Dimension(600, 600));
        panel.setMaximumSize(new Dimension(600, 600));
        panel.setPreferredSize(new Dimension(600, 600));
        panel.addMouseListener(panel);
        panel.addMouseMotionListener(panel);
        panel.addMouseWheelListener(panel);
        frame.add(panel,BorderLayout.CENTER);
        final ToolsPanel tools = new ToolsPanel();
        tools.setSize(600,200);
        tools.setMinimumSize(new Dimension(600,200));
        tools.setMaximumSize(new Dimension(600, 200));
        tools.setPreferredSize(new Dimension(600, 200));

        frame.add(tools,BorderLayout.SOUTH);

        frame.setSize(600,800);

        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("file");
        menu.add(file);

        JMenuItem open = new JMenuItem("open");
        open.addActionListener(panel);
        file.add(open);

        JMenuItem save = new JMenuItem("save");
        save.addActionListener(panel);
        file.add(save);

        frame.setJMenuBar(menu);
        frame.pack();
        frame.setLocation(100,500);
        EventQueue.invokeLater(new Runnable(){
           public void run(){
               frame.setVisible(true);
           }
        });

        final MapEditor e = new MapEditor();
        EventQueue.invokeLater(new Runnable(){
           public void run(){
               tools.init(panel);
           }
        });





    }



    /**
     * Load objects from a file and create a wizard map.
     *
     * @param io - from an input stream via a jar file.
     * @param map
     */
    public static void loadMap(InputStream io, WizardMap map) throws IOException, DataFormatException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(io));
        loadMap(reader,map);
    }

   /**
    *  For loading from editor.
     *
     * @param reader
     * @param map
     * @throws IOException
     * @throws DataFormatException
     */
    public static void loadMap(BufferedReader reader, WizardMap map) throws IOException, DataFormatException {
        boolean READING = true;
        MapRegion region = null;

        while(READING){
            String line = reader.readLine();
            if(line==null){ break;
            }
            
            String[] values = line.split(Pattern.quote("\t"));
            if(values.length==0){
                continue;

            }
            String type = values[0];

            if(type.equals("region")){
                if(region==null){
                    int x = Integer.parseInt(values[1]);
                    int y = Integer.parseInt(values[2]);
                    int w = Integer.parseInt(values[3]);
                    int h = Integer.parseInt(values[4]);
                    region = new RectangularRegion(x,y,w,h);
                    region.ACTIVE= Integer.parseInt(values[5])>0;

                } else{
                    map.addRegion(region);
                    region = null;
                }
            }
            if(type.equals("tree")){

                int x = Integer.parseInt(values[1]);
                int y = Integer.parseInt(values[2]);
                if(region==null) throw new java.util.zip.DataFormatException("map file is corrupt");
                region.addObstacle(new Tree(new Point2D.Double(x,y)));

            }

            if(type.equals("rock")){
                int x = Integer.parseInt(values[1]);
                int y = Integer.parseInt(values[2]);
                if(region==null) throw new java.util.zip.DataFormatException("map file is corrupt");
                region.addObstacle(new Rock(new Point2D.Double(x,y),Rock.MEDIUM));
            }
        }
        
    }

    public static File getOpenFileName(Frame parent,String title){
        FileDialog fd = new FileDialog(parent,title,FileDialog.LOAD);

        fd.setVisible(true);
        String fname = fd.getFile();
        String dirname = fd.getDirectory();
        String fullname = dirname +  fname;
        if(fname!=null)
            return new File(fullname);
        else
            return null;
    }

    public static File getSaveFileName(Frame parent,String title){
        FileDialog fd = new FileDialog(parent,title,FileDialog.SAVE);

        fd.setVisible(true);
        String fname = fd.getFile();
        String dirname = fd.getDirectory();
        String fullname = dirname +  fname;
        if(fname!=null)
            return new File(fullname);
        else
            return null;
    }

    public static void saveMap(BufferedWriter writer, ArrayList<MapRegion> regions) throws IOException {
        writer.write("map version 1.0\n");
        for(MapRegion region: regions){
            Rectangle s = region.getShape().getBounds();
            int x = (int)s.getX();
            int y = (int)s.getY();
            int w = (int)s.getWidth();
            int h = (int)s.getHeight();
            String head = String.format("region\t%s\t%s\t%s\t%s\t%s\n",x,y,w,h,1);
            writer.write(head);
            for(Obstacle obs: region.getObstacles()){
                String oline="";
                if(Tree.class.isInstance(obs)){
                    Rectangle sp = obs.getRegion().getBounds();
                    int tx = (int)(sp.getX() + sp.getWidth()/2);
                    int ty = (int)(sp.getY() + sp.getHeight()/2);
                    oline = String.format("tree\t%s\t%s\n",tx,ty);
                }else if(Rock.class.isInstance(obs)){
                    Rectangle sp = obs.getRegion().getBounds();
                    int tx = (int)(sp.getX() + sp.getWidth()/2);
                    int ty = (int)(sp.getY() + sp.getHeight()/2);
                    oline = String.format("rock\t%s\t%s\n",tx,ty);
                }
                writer.write(oline);
            }
            writer.write("region\n");



        }
        writer.close();
        

    }

}

class EditorPanel extends JPanel implements MouseListener,MouseMotionListener, ActionListener, MouseWheelListener{


    enum Selected{
        box,rock,tree;
    }

    Selected type = Selected.box;
    boolean NEW_REGION;

    Point2D start, opposite;
    Rectangle2D new_region = new Rectangle2D.Double();

    AffineTransform translate = AffineTransform.getTranslateInstance(0,0);
    double DX, DY;
    double MAG = 1;
    double IMAG = 1;
    String coordinates = "";
    //final ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
    final ArrayList<MapRegion> regions = new ArrayList<MapRegion>();

    @Override
    public void paintComponent(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0,0,600,600);
        AffineTransform at = ((Graphics2D)g).getTransform();
        ((Graphics2D)g).setTransform(translate);
        g.setColor(Color.GRAY);

        for(MapRegion region: regions){
            ((Graphics2D)g).fill(region.getShape());

        }
        for(MapRegion region: regions){
            for(Obstacle o: region.getObstacles()){
                o.paint(g);
                o.paintHigh(g);
            }

        }


        if(NEW_REGION){
            g.setColor(Color.WHITE);
            ((Graphics2D)g).draw(new_region);
        }

        ((Graphics2D)g).setTransform(at);
        g.setColor(Color.WHITE);
        g.drawString(coordinates,450,575);



    }

    public void setType(String s){
        if(NEW_REGION){
            NEW_REGION=false;
            repaint();
        }
        type = Selected.valueOf(s);
    }


    public void addObstacle(Point2D point,Obstacle o){
        
        for(MapRegion r: regions){
            if(r.contains(point)){
                r.addObstacle(o);
            }
        }

    }

    public void removeObstacle(Obstacle o){
        for(MapRegion r: regions){
            ArrayList<Obstacle> obstacles = r.getObstacles();
            if(obstacles.contains(o)){
                obstacles.remove(o);
            }
        }
    }


    public void mouseClicked(MouseEvent evt) {
        Point2D point = getRealPoint(evt.getPoint());
        switch (type) {

            case box:
                if(NEW_REGION&&new_region.contains(point)){
                    regions.add(new RectangularRegion(new_region));
                    NEW_REGION=false;
                    repaint();
                } else if(NEW_REGION){
                    NEW_REGION=false;
                    repaint();
                }
                break;

            case rock:
                addObstacle(point, new Rock(point, Rock.MEDIUM));
                repaint();
                break;

            case tree:
                addObstacle(point, new Tree(point));
                repaint();
                break;
        }

        
    }

    public void mousePressed(MouseEvent e) {
        switch(type){
            case box:
                if(NEW_REGION)
                    return;
                start = getRealPoint(e.getPoint());
                opposite = getRealPoint(e.getPoint());
                new_region.setFrameFromDiagonal(start,opposite);
                NEW_REGION = true;


               
            default:
                //pass
        }
    }

    public void mouseReleased(MouseEvent e) {

        down=null;
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
    Point2D down;
    public void mouseDragged(MouseEvent evt) {
        Point2D point = getRealPoint(evt.getPoint());
        coordinates = String.format("%.2f :: %.2f",point.getX(), point.getY());

        if(NEW_REGION){
                opposite = point;
                new_region.setFrameFromDiagonal(start,opposite);
                repaint();

        } else{
            if(down==null){

                down=evt.getPoint();

            } else{

                Point2D p = evt.getPoint();
                DX += p.getX() - down.getX();
                DY += p.getY() - down.getY();

                updateTransform();
                down = p;
            }
        }



    }

    public void mouseMoved(MouseEvent e) {

        Point2D p = getRealPoint(e.getPoint());
        coordinates = String.format("%.2f :: %.2f",p.getX(), p.getY());

        repaint();

    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if(cmd.equals("save")){
            saveMap();
            return;
        }
        if(cmd.equals("open")){
            openMap();
            return;
        }
    }

    /**
     *
     * @param e
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
        //negative is zoom in.
        if(e.getUnitsToScroll()>0){
            if(MAG>0.1){
                MAG -= 0.1;
                updateTransform();
            }
        }else{
            if(MAG<2){
                MAG+= 0.1;
                updateTransform();
            }
        }
    }

   /**
       *  Changes from the panel click location to the map location
       * @param p
       * @return
       */
    public Point2D getRealPoint(Point2D p){

        return new Point2D.Double(p.getX()/MAG - DX/MAG,p.getY()/MAG - DY/MAG);

    }

    public void updateTransform(){
        IMAG = 1/MAG;
        //float m00, float m10, float m01, float m11, float m02, float m12
        translate.setTransform(MAG,0,0,MAG,DX,DY);
        repaint();

    }

    public void saveMap(){

        File outfile = MapEditor.getSaveFileName(null,"Save Map File");
        if(outfile==null) return;

        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(outfile));
            MapEditor.saveMap(writer,regions);
        }catch(Exception e){
            return;
        }
    }
    public void openMap(){
        File infile = MapEditor.getOpenFileName(null,"Get Map File");
        if(infile==null) return;

        regions.clear();

        WizardMap map = new WizardMap() {
            @Override
            public void paintBackGround(Graphics g) {

            }

            @Override
            public boolean checkOutOfBounds(Point2D p, double radius, int move_type) {
                return false;
            }

            @Override
            public void addRegion(MapRegion r){
                regions.add(r);
            }
        };

        try{
            BufferedReader reader = new BufferedReader(new FileReader(infile));
            MapEditor.loadMap(reader,map);
        }catch(Exception e){
                //wtf
            return;
        }


    }


}

class ToolsPanel extends JPanel implements MouseListener {
    ToolsButton box,tree,rock;

    ToolsButton selected;
    EditorPanel editor;
    public void init(EditorPanel me){
        editor = me;
        WizardImages.loadImages();
        BufferedImage img = new BufferedImage(50,50,BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        g.setColor(Color.WHITE);
        g.drawLine(25,20,25,30);
        g.drawLine(20,25,30,25);
        g.dispose();
        box = new ToolsButton(img,"box");

        box.addMouseListener(this);

        add(box);

        tree = new ToolsButton(WizardImages.tree_top,"tree");

        tree.addMouseListener(this);

        add(tree);
        rock = new ToolsButton(WizardImages.rocks[0],"rock");

        rock.addMouseListener(this);
        add(rock);

        selected = box;

    }
    @Override
    public void paintComponent(Graphics g){
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, 600, 200);

        if(selected!=null){
            g.drawImage(selected.graphic,0,0,50,50,this);
        }
        
    }

    public void mouseClicked(MouseEvent evt) {

        try{
            selected = (ToolsButton)evt.getSource();
            editor.setType(selected.getText());
        }catch (Exception exc){
            selected = null;
            exc.printStackTrace();
        }
        repaint(0,0,50,50);
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }


}

class ToolsButton extends JButton{
    Image graphic;

    ToolsButton(Image graphic, String cmd){

        super(cmd);
        this.graphic = graphic;
        setMinimumSize(new Dimension(50,50));
        setMaximumSize(new Dimension(50,50));
        setPreferredSize(new Dimension(50,50));

    }

    public void paintComponent(Graphics g){

        g.drawImage(graphic,(50-graphic.getWidth(this))/2,(50 - graphic.getHeight(this))/2,graphic.getWidth(this), graphic.getHeight(this),this);

    }
}