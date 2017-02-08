
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.Box;
import static java.lang.Float.max;
import static java.lang.Float.min;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.Timer;

public class Year_2194_VCG_DriverSimulator extends Applet implements ActionListener, KeyListener {

    private Button go = new Button("Start");
    private TransformGroup objTrans;
    private TransformGroup objTrans1;
    private TransformGroup objTrans5;
    private TransformGroup[] objTrans2 = new TransformGroup[30];
    private Transform3D trans = new Transform3D();
    private float sign = 0; // muda a direçao da esfera
    private Timer timer;
    private float linePosition = 0;
    private float xloc = 0.0f;
    BranchGroup objRoot = new BranchGroup(); //objroot será o branch group
    //variaveis das particulas
    private TransformGroup[] objTrans3 = new TransformGroup[40];
    private int[] skyZPosition = new int[40];
    private int[] skyXPosition = new int[40];
    private int[] skyYPosition = new int[40];
    //variaveis dos obstaculos
    private int dificuldade = 20;
    private TransformGroup[] objTrans4 = new TransformGroup[dificuldade];
    private int[] obsZPosition = new int[dificuldade];
    private int[] obsXPosition = new int[dificuldade];
    
    private int gameOver = 1;
    
    public void crashVerify(){
        for(int i = 0; i < dificuldade; i++){
            if(obsZPosition[i]*0.1f <= 0f && obsZPosition[i]*0.1f >= -0.6f){
                if(obsXPosition[i]*0.1f <= 0.05f+xloc && obsXPosition[i]*0.1f >= -0.05f+xloc){
                    if(gameOver == 1){
                        gameOver = 1000;
                        System.out.println("FIM DE JOGO");
                    }
                }
            }
        }
    }
    
    public void buildObstacles(){
        Appearance aparencia = new Appearance();
        Color3f cor1 = new Color3f(0.6f, 0.6f, 0.1f);
        Color3f cor2 = new Color3f(0,0,0);
        aparencia.setMaterial(new Material(cor1, cor2, cor1, cor2, 1f));
        Box linha = new Box();
        Transform3D[] pos3 = new Transform3D[40];//adiciona a rua ao segundo transform group
        for (int i = 0; i < dificuldade; i++) {
            objTrans4[i] = new TransformGroup(); // objtrans será o transform group
            linha = new Box(0.05f, 0.03f, 0.3f, aparencia);
            pos3[i] = new Transform3D();
            obsZPosition[i] = ThreadLocalRandom.current().nextInt(-400, -200 + 1);
            obsXPosition[i] = ThreadLocalRandom.current().nextInt(-6, 6 + 1);
            pos3[i].setTranslation(new Vector3f(obsXPosition[i]*0.1f, -0.3f, obsZPosition[i]*0.1f));//posiciona o transform group da rua
            objTrans4[i].addChild(linha);
            objTrans4[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            objTrans4[i].setTransform(pos3[i]);
            objRoot.addChild(objTrans4[i]);
        }
    }
    
    public void moveObstacles(){
        for(int i = 0; i<dificuldade; i++){
            if(obsZPosition[i]>=5f){
                obsZPosition[i] = ThreadLocalRandom.current().nextInt(-400, -200 + 1);
                obsXPosition[i] = ThreadLocalRandom.current().nextInt(-6, 6 + 1);
            }else{
                obsZPosition[i] = obsZPosition[i] + 1;
            }
            trans.setTranslation(new Vector3f(obsXPosition[i]*0.1f, -0.3f, obsZPosition[i]*0.1f));
            objTrans4[i].setTransform(trans);
        }
    }

    public void buildStreet() {
        objTrans1 = new TransformGroup(); // objtrans será o transform group
        Appearance aparencia = new Appearance();
        aparencia.setMaterial(new Material(new Color3f(0.0f, 0.0f, 0.0f), new Color3f(0.0f, 0.0f, 0.0f), new Color3f(0f, 0f, 0f), new Color3f(0f, 0f, 0f), 10f));
        Box rua = new Box(0.7f, 0.1f, 40f, aparencia);
        Transform3D pos2 = new Transform3D();//adiciona a rua ao segundo transform group
        pos2.setTranslation(new Vector3f(0.0f, -0.5f, 0.0f));//posiciona o transform group da rua
        objTrans1.addChild(rua);
        objTrans1.setTransform(pos2);
        objRoot.addChild(objTrans1);

    }

    public void drawStreetLines() {
        Appearance aparencia = new Appearance();
        aparencia.setMaterial(new Material(new Color3f(0.1f, 0.7f, 0.7f), new Color3f(0.1f, 0.7f, 0.7f), new Color3f(0.1f, 0.7f, 0.7f), new Color3f(0.1f, 0.7f, 0.7f), 1f));
        Box linha = new Box();
        Transform3D[] pos3 = new Transform3D[30];//adiciona a rua ao segundo transform group
        for (int i = 0; i < 30; i++) {
            objTrans2[i] = new TransformGroup(); // objtrans será o transform group
            linha = new Box(0.01f, 0.001f, 0.1f, aparencia);
            pos3[i] = new Transform3D();
            pos3[i].setTranslation(new Vector3f(0.0f, -0.4f, 0.4f - 1f * i));//posiciona o transform group da rua
            objTrans2[i].addChild(linha);
            objTrans2[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            objTrans2[i].setTransform(pos3[i]);
            objRoot.addChild(objTrans2[i]);

        }
    }
    
    public void drawSkyEffects(){
        Appearance aparencia = new Appearance();
        aparencia.setMaterial(new Material(new Color3f(0.5f, 0f, 0.4f), new Color3f(0.5f, 0f, 0.4f), new Color3f(0.5f, 0.0f, 0.4f), new Color3f(0.5f, 0.0f, 0.4f), 1f));
        Box linha = new Box();
        Transform3D[] pos3 = new Transform3D[40];//adiciona a rua ao segundo transform group
        for (int i = 0; i < 40; i++) {
            objTrans3[i] = new TransformGroup(); // objtrans será o transform group
            linha = new Box(0.003f, 0.003f, 0.1f, aparencia);
            pos3[i] = new Transform3D();
            skyZPosition[i] = ThreadLocalRandom.current().nextInt(-100, 10 + 1);
            skyYPosition[i] = ThreadLocalRandom.current().nextInt(-10, 10 + 1);
            skyXPosition[i] = ThreadLocalRandom.current().nextInt(-10, 10 + 1);
            pos3[i].setTranslation(new Vector3f(skyXPosition[i]*0.1f, skyYPosition[i]*0.1f, skyZPosition[i]*0.1f));//posiciona o transform group da rua
            objTrans3[i].addChild(linha);
            objTrans3[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            objTrans3[i].setTransform(pos3[i]);
            objRoot.addChild(objTrans3[i]);
        }
    }
    
    public void moveSkyEffects(){
        for(int i = 0; i<40; i++){
            if(skyZPosition[i]>=1.0f){
                skyZPosition[i] = ThreadLocalRandom.current().nextInt(-100, 10 + 1);
                skyYPosition[i] = ThreadLocalRandom.current().nextInt(-10, 10 + 1);
                skyXPosition[i] = ThreadLocalRandom.current().nextInt(-10, 10 + 1);
            }else{
                skyZPosition[i] = skyZPosition[i] + 1;
            }
            trans.setTranslation(new Vector3f(skyXPosition[i]*0.1f, skyYPosition[i]*0.1f, skyZPosition[i]*0.1f));
            objTrans3[i].setTransform(trans);
        }
    }

    public void moveStreetLines() {
            if (linePosition >= 1.0f) {
                linePosition = 0;
            }else{
                linePosition = linePosition + 0.1f;
            }
        for (int i = 0; i < 30; i++) {
            trans.setTranslation(new Vector3f(0.0f, -0.4f, linePosition + 0.4f - 1f * i));
            objTrans2[i].setTransform(trans);

        }

    }

    public void buildCar() {
        // Create the root of the branch graph
        Appearance aparencia = new Appearance();
        Appearance aparencia1 = new Appearance();
        aparencia.setMaterial(new Material(new Color3f(0.0f, 0.0f, 0.6f), new Color3f(0.0f, 0.0f, 0.6f), new Color3f(1.0f, 1.0f, 0.6f), new Color3f(1.0f, 0f, 0.6f), 10f));
        objTrans = new TransformGroup(); // objtrans será o transform group
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        // Create a simple shape leaf node, add it to the scene graph.
        Box sphere = new Box(0.05f, 0.03f, 0.3f, aparencia); // cria uma esfera

        aparencia1.setMaterial(new Material(new Color3f(0.0f, 0.0f, 0.0f), new Color3f(0.0f, 0.0f, 0.0f), new Color3f(0f, 0f, 0.0f), new Color3f(0f, 0f, 0f), 10f));
        Box sphere1 = new Box(0.04f, 0.01f, 0.31f, aparencia1);

        Box sphere2 = new Box(0.0501f, 0.01f, 0.27f, aparencia1);

        Transform3D pos1 = new Transform3D();//cria o responsavel pelas posicoes

        pos1.setTranslation(new Vector3f(0.0f, -0.3f, 0.2f)); //cria um vetor para transladar pos1

        objTrans.setTransform(pos1); //aplica o vetor criado na linha de cima em objTrans, ou seja todos os objetos contidos nesse transformGroup serao tranladados

        objTrans.addChild(sphere1); // a esfera criada passa a fazer parte do transformGroup
        objTrans.addChild(sphere);
        objTrans.addChild(sphere2);
        objRoot.addChild(objTrans); //O transformGroup passa a fazer parte do BranchGroup

    }

    public BranchGroup createSceneGraph() {
        Background background = new Background(new Color3f(0.1f, 0f, 0.2f));
        BoundingSphere sphere = new BoundingSphere(new Point3d(0, 0, 0), 100000);
        background.setApplicationBounds(sphere);
        objRoot.addChild(background);
        this.drawStreetLines();
        this.buildCar();
        this.buildStreet();
        this.drawSkyEffects();
        this.buildObstacles();
        BoundingSphere bounds
                = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

        Color3f light1Color = new Color3f(0.0f, 1.0f, 0.6f);

        Vector3f light1Direction = new Vector3f(0.0f, -7.0f, -10.0f);

        DirectionalLight light1
                = new DirectionalLight(light1Color, light1Direction);

        light1.setInfluencingBounds(bounds);

        objRoot.addChild(light1); //cria uma luz direcional e adiciona ao branch group

        // Set up the ambient light
        Color3f ambientColor = new Color3f(1.0f, 1.0f, 0.0f);

        AmbientLight ambientLightNode = new AmbientLight(ambientColor);

        ambientLightNode.setInfluencingBounds(bounds);

        objRoot.addChild(ambientLightNode);

        return objRoot;

    }

    public Year_2194_VCG_DriverSimulator() {

        setLayout(new BorderLayout());

        GraphicsConfiguration config
                = SimpleUniverse.getPreferredConfiguration();

        Canvas3D c = new Canvas3D(config);

        add("Center", c);

        c.addKeyListener(this);

        timer = new Timer(10, this);

        //timer.start();
        Panel p = new Panel();

        p.add(go);

        add("North", p);

        go.addActionListener(this);

        go.addKeyListener(this);

        // Create a simple scene and attach it to the virtual universe
        BranchGroup scene = createSceneGraph();

        SimpleUniverse u = new SimpleUniverse(c);

        u.getViewingPlatform().setNominalViewingTransform();

        u.addBranchGraph(scene);

    }

    public void keyPressed(KeyEvent e) {

        //Invoked when a key has been pressed.
        if (e.getKeyChar() == 's') {
            sign = 1;
        }

        if (e.getKeyChar() == 'a') {
            sign = -1;
        }

    }

    public void keyReleased(KeyEvent e) {

        if (e.getKeyChar() == 's') {
            sign = 0;
        }

        if (e.getKeyChar() == 'a') {
            sign = 0;
        }

        // Invoked when a key has been released.
    }

    public void keyTyped(KeyEvent e) {

        //Invoked when a key has been typed.
    }

    public void actionPerformed(ActionEvent e) {
        // start timer when button is pressed
        if (e.getSource() == go) {

            if (!timer.isRunning()) {

                timer.start();

            }

        } else {

            xloc += .01 * sign;
            if(xloc >= .6){
                xloc -= 0.01;
            }
            if(xloc <= -.6){
                xloc += 0.01;
            }
            this.moveStreetLines();
            this.moveSkyEffects();
            this.moveObstacles();
            this.crashVerify();
            trans.setTranslation(new Vector3f(xloc, -0.3f, gameOver*0.2f));
            objTrans.setTransform(trans);

        }

    }

    public static void main(String[] args) {

        System.out.println("Program Started");

        Year_2194_VCG_DriverSimulator bb = new Year_2194_VCG_DriverSimulator();

        bb.addKeyListener(bb);

        MainFrame mf = new MainFrame(bb, 1280, 720);

    }

}

