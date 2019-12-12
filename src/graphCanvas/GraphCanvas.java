
package graphCanvas;

import java.awt.Color;
import java.util.*;
import javax.swing.*;
import java.util.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage; 
import javax.imageio.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author abhia
 */

class PreExistenceException extends Exception{
    String message;
    
    PreExistenceException(String message){
        this.message = message;
    }
    
    public String toString(){
        return message;
    }
}

class NoExistenceException extends Exception{
    String message;
    
    NoExistenceException(String message){
        this.message = message;
    }
    
    public String toString(){
        return message;
    }
}

class Vertex implements Comparable<Vertex>{
    String name;
    double x,y;
    double reldist;
    
    Vertex(String name, double x, double y){
        this.name = name;
        this.x = x;
        this.y = y;
    }
    
    void printDetails(){
        String s = " ";
        System.out.println(name + s + (int)x + s + (int)y);
    }
    public String toString(){
        String s = " ";
        return (name + s + (int)x + s + (int)y);
    }
    public int compareTo(Vertex v){
        return this.name.compareTo(v.name);
    }
}

class Edge implements Comparable<Edge>{
    Vertex from,to;
    double weight;
    
    Edge(Vertex from,Vertex to, double weight){
        this.from=from;
        this.to = to;
        this.weight = weight;
    }
    
    void printDetails(){
        String s = " ";
        System.out.println(from.name + s + to.name + s + (int)weight);
    }
    
    public int compareTo(Edge e){
       int i = this.from.name.compareTo(e.from.name);
       if(i==0) return this.to.name.compareTo(e.to.name);
       return i;
    }
    
    public boolean has(Coord c){
        double x1 = to.x, y1 = to.y, x2 = from.x, y2 = from.y;
        int x = c.x, y = c.y;
        double m1 = (double)(y-y1)/(x-x1)-(double)(y2-y1)/(x2-x1), m2 = (double)(y-y2)/(x-x2)-(double)(y1-y2)/(x1-x2);
        System.out.println(m1 + "yse" + m2);
        if(x>=Math.min(x1,x2)&&y>=Math.min(y1,y2)&&x<=Math.max(x1,x2)&&y<=Math.max(y1, y2)&&Math.abs(m1)<1 && Math.abs(m2)<1) return true;
        return false;
    }
    
}

class qCompare implements Comparator<Vertex>{
    public int compare(Vertex v1, Vertex v2){
        return ((Double)v2.reldist).compareTo(v1.reldist);
    }
}

class Graph{
    ArrayList<Vertex> vertices;
    HashMap<String,ArrayList<Edge>> adjList; 
    
    Graph() {
        vertices = new ArrayList<Vertex>();
        adjList = new HashMap<String,ArrayList<Edge>>();
    }
    
    void fileInput(JFrame f,String path, CanvasFrame c){
        try{
            File fr = new File(path);
            Scanner sc = new Scanner(fr);
            if(sc.hasNext()){
                int n = Integer.parseInt(sc.next());
                while(n-->0){
                    try{
                        addVertex(new Vertex(sc.next(),Double.parseDouble(sc.next()),Double.parseDouble(sc.next())));
                    }catch(Exception e){
                        JOptionPane.showMessageDialog(f,e,"Error",JOptionPane.ERROR_MESSAGE);
                    }
                }
                n = Integer.parseInt(sc.next());
                while(n-->0){
                    try{
                        addEdge(new Edge(getVertex(sc.next()),getVertex(sc.next()),Double.parseDouble(sc.next())));
                    }
                    catch(Exception e){
                        JOptionPane.showMessageDialog(f,e,"Error",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            sc.close();
            c.repaint();
        }catch(IOException e){
           JOptionPane.showMessageDialog(f,"Invalid Path","Error",JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    void addVertex(Vertex v) throws PreExistenceException{
        boolean flag = true;
        try{
            Vertex p = getVertex(v.name);
        }
        catch(NoExistenceException e){
            vertices.add(v);
            adjList.put(v.name,new ArrayList<Edge>());
            System.out.println(v.name + " " + v.x + " " + v.y);
            flag = false;
        }
        if(flag) throw new PreExistenceException(v.name + " Vertex Already Exists");
        
    }
    
    Vertex getVertex(String s) throws NoExistenceException{
        for(int i = 0; i < vertices.size(); i++) if(s.equals(vertices.get(i).name)) return vertices.get(i);
        throw new NoExistenceException(s + " Vertex Doesn't Exists");
    }
    
    void delVertex(Vertex v)throws NoExistenceException{
        int i = vertices.indexOf(v);
        if(i==-1) throw new NoExistenceException(v.name + " Vertex Doesn't Exists");
               System.out.println("hihi");
        for(int j = 0; j < vertices.size(); j++){
            ArrayList<Edge> temp = adjList.get(vertices.get(j).name);
            for(int k = 0; k < temp.size(); k++){
                if(temp.get(k).from.name.equals(v.name)||temp.get(k).to.name.equals(v.name))
                    temp.remove(k);
            }
        }
        adjList.remove(v.name);
        vertices.remove(v);
      
    }
    
    void modify(Vertex v) throws NoExistenceException{
        Vertex t = getVertex(v.name);
        t.x = v.x;
        t.y = v.y;
    }
    
    void addEdge(Edge e) throws PreExistenceException{
        boolean flag = true;
        try{
            Edge ex = getEdge(e.from.name,e.to.name);
        }
        catch(NoExistenceException ex){
            adjList.get(e.from.name).add(e);
            System.out.println(e.from.name + " " + e.to.name + " " + e.weight);
            flag = false;
        }
        if(flag) throw new PreExistenceException(e.from.name + " to " + e.to.name + " Edge Already Exists");
    }
    
    Edge getEdge(String from, String to) throws NoExistenceException{
        System.out.println("jlkhkhgj");
        ArrayList<Edge> temp = adjList.get(from);
        for(int i = 0; i < temp.size() ; i++){
            if(temp.get(i).to.name.compareTo(to)==0) return temp.get(i);
        }
        throw new NoExistenceException(from + " to " + to + " Edge Doesn't exists");
    }
    
    ArrayList<Edge> getEdges(String name){
        return adjList.get(name);
    }
    
    void modifyEdge(String from, String to, double weight) throws NoExistenceException{
        Edge e = getEdge(from,to);
        e.weight = weight;
    }
    
    void delEdge(Edge e) throws NoExistenceException{
        e.printDetails();
        adjList.get(e.from.name).remove(e);
    }
    
    Edge isEdge(Coord c){
        for(int j = 0; j < vertices.size(); j++){
            ArrayList<Edge> temp = adjList.get(vertices.get(j).name);
            for(int k = 0; k < temp.size(); k++){
                if(temp.get(k).has(c)) return temp.get(k);
            }
        }
        return null;
    }
    
    ArrayList<String> getPath(String from, String to) throws NoExistenceException{
        ArrayList<String> path = new ArrayList<String>();
        int nov = vertices.size();
        ArrayList<Vertex> queue = new ArrayList<Vertex>();
        HashMap<String,Vertex> prev = new HashMap<String,Vertex>();
        
        Iterator it = vertices.iterator();
        for(int i = 0; i <nov; i++){
            Vertex v = vertices.get(i);
            prev.put(v.name,null);
            v.reldist = Double.MAX_VALUE;
            queue.add(v);
        }
        
        int si = vertices.indexOf(getVertex(from));
        queue.get(si).reldist = 0;
        
        while(!queue.isEmpty())
        {
            Collections.sort(queue,new qCompare());
            Vertex u = queue.remove(queue.size()-1);
            System.out.println(u.name+u.reldist);
            it = adjList.get(u.name).iterator();
            while(it.hasNext()){
                Edge e = (Edge)it.next();
                e.printDetails();
                Vertex v = getVertex(e.to.name);
                v.printDetails();
                if(queue.contains(v)){
                    double alt = u.reldist + e.weight;
                    if(alt<v.reldist){
                        v.reldist = alt;
                        prev.replace(v.name, u);
                    }
                }
            }
        }
        
        Vertex v = prev.get(to);
        if(v==null) return path;
        Stack<String> st = new Stack<String>();
        st.push(to);
        while(v!=null){
            st.push(v.name);
            v = prev.get(v.name);
        }
        while(!st.empty()) path.add(st.pop());
        return path;
    }
    
    void export(JFrame f,String path){
        try{
            File of = new File(path);
            if(!of.exists()) of.createNewFile();
            PrintStream p = new PrintStream(of);
            PrintStream console = System.out;
            System.setOut(p);
            ArrayList<Edge> edge = new ArrayList<Edge>();
            for(int j = 0; j < vertices.size(); j++){
                ArrayList<Edge> temp = adjList.get(vertices.get(j).name);
                for(int k = 0; k < temp.size(); k++){
                    edge.add(temp.get(k));
                }
            }
            Collections.sort(edge);
            Collections.sort(vertices);
        
            System.out.println(vertices.size());
            Iterator it = vertices.iterator();
            while(it.hasNext()) ((Vertex)it.next()).printDetails();
        
            System.out.println(edge.size());
            it = edge.iterator();
            while(it.hasNext()) ((Edge)it.next()).printDetails();
        
            System.setOut(console);
            p.close();
        }catch(IOException e){
           JOptionPane.showMessageDialog(f,"Invalid Path","Error",JOptionPane.ERROR_MESSAGE);
        }
    }
}

interface TextFocus extends FocusListener
{
    
    Font f = new Font(Font.MONOSPACED,Font.ITALIC,10);
    Font df = new Font(Font.SANS_SERIF,Font.BOLD,10);
    default public void focusLost(FocusEvent e){
        if(((JTextField)e.getComponent()).getText().equalsIgnoreCase("")){
            ((JTextField)e.getComponent()).setFont(f);
            ((JTextField)e.getComponent()).setForeground(Color.LIGHT_GRAY);
            ((JTextField)e.getComponent()).setText("Empty!!");
        }
    }
    
    default public void focusGained(FocusEvent e){
        ((JTextField)e.getComponent()).setText("");
        ((JTextField)e.getComponent()).setFont(df);
        ((JTextField)e.getComponent()).setForeground(Color.BLACK);
    }
    
}

abstract class AddModify extends JPanel implements TextFocus,ActionListener
{
    JTextField field1,field2,field3;
    JButton button1,button2;
    JLabel title;
    Graph g;
    CanvasFrame c;
    String default1,default2,default3, deftitle;
    
    AddModify(Graph g,CanvasFrame c,String default1, String default2, String default3, String title){
        this.g = g;
        this.c = c;
        this.default1 = default1;
        this.default2 = default2;
        this.default3 = default3;
        field1 = new JTextField(default1);
        field2 = new JTextField(default2);
        field3 = new JTextField(default3);
        button1 = new JButton("Add");
        button2 = new JButton("Mod");
        this.title  =new JLabel(title);
        
        field2.setFont(f);
        field3.setFont(f);
        field1.setFont(f);
       
        reset();
        
        button1.setBounds(0, 160, 100, 30);
        button2.setBounds(100, 160, 100, 30);
        field2.setBounds(0, 80,200 ,30);
        field3.setBounds(0, 120,200 ,30);
        field1.setBounds(0, 40,200 ,30);
        this.title.setBounds(70,0,100,30);
        
        field2.addFocusListener(this);
        field3.addFocusListener(this);
        field1.addFocusListener(this);
        button1.addActionListener(this);
        button2.addActionListener(this);
        
       
        add(button2);
        add(button1);
        add(field2);
        add(field3);
        add(field1);
        add(this.title);
        setLayout(null);
        setVisible(true);
        //button1.requestFocus();
    }
    
    void reset(){
        field2.setForeground(Color.LIGHT_GRAY);
        field3.setForeground(Color.LIGHT_GRAY);
        field1.setForeground(Color.LIGHT_GRAY);
        field1.setText(default1);
        field2.setText(default2);
        field3.setText(default3);
    }
    
    boolean checkIfEmpty(){
        String s1 = field1.getText();
        String s2 = field1.getText();
        String s3 = field1.getText();
        if(s1.equals("")||s1.equals("Empty!!")||s1.equals(default1)||s2.equals("")||s2.equals("Empty!!")||s2.equals(default2)||s3.equals("")||s3.equals("Empty!!")||s3.equals(default3)) return true;
        return false;
    }
}

class AddModifyVertex extends AddModify{
    
    AddModifyVertex(Graph g,CanvasFrame c){
        super(g,c,"Name","X","Y","Vertices");
        
    }
        
    static public void bhi(){
    
    }
    public void actionPerformed(ActionEvent e){
        if(checkIfEmpty()) return;
        try{
            if(e.getActionCommand()=="Add"){
                Vertex v = new Vertex(field1.getText(),Double.parseDouble(field2.getText()),Double.parseDouble(field3.getText()));
                g.addVertex(v);
                c.repaint();
                GraphCanvas.stat.setText("Vertex Added: " + field2.getText() +", " + field3.getText());
            }
        
            if(e.getActionCommand()=="Mod"){
                Vertex v =g.getVertex(field1.getText());
                c.moveVertex(new Coord((int)v.x,(int)v.y),new Coord(Integer.parseInt(field2.getText()),Integer.parseInt(field3.getText())) );
                g.modify(new Vertex(field1.getText(),Double.parseDouble(field2.getText()),Double.parseDouble(field3.getText())));
                c.repaint();
                GraphCanvas.stat.setText("Vertex Modified: " + field2.getText() +", " + field3.getText());
            }
            reset();
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(this, ex, "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    } 
}

class AddModifyEdge extends AddModify{
    
    AddModifyEdge(Graph g,CanvasFrame c){
        super(g,c,"From Vertex Name","To Vertex Name","Weight", "Edges");
    }
    
    public void actionPerformed(ActionEvent e){
        if(checkIfEmpty()) return;
        try{
            if(e.getActionCommand()=="Add"){
                Edge ed =new Edge(g.getVertex(field1.getText()),g.getVertex(field2.getText()),Double.parseDouble(field3.getText()));
                g.addEdge(ed);
                c.repaint();
                GraphCanvas.stat.setText("Edge Added: " + field1.getText() + " to " + field2.getText() +", " + field3.getText());
            }
            if(e.getActionCommand()=="Mod"){
                    g.modifyEdge(field1.getText(),field2.getText(),Double.parseDouble(field3.getText()));
                    GraphCanvas.stat.setText("Edge Modified: " +field1.getText() + " to " +  field2.getText() +", " + field3.getText());
            }
        }
        catch (Exception ex){
            JOptionPane.showMessageDialog(this, ex, "Input Error", JOptionPane.ERROR_MESSAGE);
        }
        reset();
    }
}

abstract class SearchDelete extends JPanel implements TextFocus,ActionListener
{
    JTextField field1,field2;
    JTextArea result;
    JButton search,delete;
    JLabel title;
    String default1,default2;
    Graph g;
    CanvasFrame c;
    SearchDelete(Graph g,CanvasFrame c, String default1,String title, boolean isFinal){
        this.g  = g;
        this.c = c;
        this.default1 = default1;
        field1 = new JTextField(default1);
        search = new JButton("Go");
        result = new JTextArea();
        this.title = new JLabel(title);
        delete = new JButton("Del");
        
        field1.setFont(f);
        field1.setForeground(Color.lightGray);
        
       
        field1.setBounds(0, 40, 200, 30);
        search.setBounds(0,80,100,30);
        delete.setBounds(100,80,100,30);
        result.setBounds(0,120,200,100);
        this.title.setBounds(75,0,200,30);
        
        result.setFocusable(false);
        result.setForeground(Color.GRAY);
        result.setFont(f);
        delete.setFont(df);
        search.setFont(df);
        
        add(field1);
        add(result);
        add(search);
        add(this.title);
        add(delete);
        
        field1.addFocusListener(this);
        search.addActionListener(this);
        delete.addActionListener(this);
        
        if(isFinal) frameSet(); 
        
    }
    
    SearchDelete(Graph g,CanvasFrame c, String default1, String default2, String title,boolean isFinal){
        this(g,c,default1,title, false);
        this.default2 = default2;
        field2 = new JTextField(default2);
        field2.setBounds(0, 80, 200, 30);
        search.setBounds(0,120,100,30);
        delete.setBounds(100,120,100,30);
        result.setBounds(0,160,200,100);
        
        field2.setFont(f);
        field2.setForeground(Color.lightGray);
        
        field2.addFocusListener(this);
        
        add(field2);
        
        if(isFinal) frameSet();
    }
    
    protected void frameSet(){
        setLayout(null);
        setVisible(true);
    }
     
    public void reset(){
        field1.setForeground(Color.LIGHT_GRAY);
        field1.setText(default1);
        if(field2 == null) return;
        field2.setForeground(Color.LIGHT_GRAY);
        field2.setText(default2);
    }
    
    public boolean checkIfEmpty(){
        String s1 = field1.getText();
        if(s1.equals("")||s1.equals("Empty!!")||s1.equals(default1)) return true;
        if(field2==null) return false;
        String s2 = field2.getText();
        if(s2.equals("")||s2.equals("Empty!!")||s2.equals(default2)) return true;
        return false;
    }
}

class SearchDeleteVertex extends SearchDelete
{
    SearchDeleteVertex(Graph g,CanvasFrame c){
        super(g,c,"Enter Name","Vertices",true);
    }
    
    public void actionPerformed(ActionEvent e){
        if(checkIfEmpty()) return;
        try{
            Vertex v = g.getVertex(field1.getText());
            result.setText("");
            result.append("Name: " + v.name + "\n");
            result.append("X: " + v.x + "\n");
            result.append("Y: " + v.y + "\n");
            reset();
            if (e.getActionCommand()=="Del"){ result.append("Deleted!!"); c.delVertex(new Coord((int)v.x,(int)v.y));}
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(this, ex, "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

class SearchDeleteEdge extends SearchDelete
{
    SearchDeleteEdge(Graph g, CanvasFrame c){
        super(g,c,"Enter From", "Enter To","Edges",true);
    }
    
    public void actionPerformed(ActionEvent e){
        if(checkIfEmpty()) return;
        try{
            Edge ed = g.getEdge(field1.getText(),field2.getText());
            result.setText("");
            result.append("From: " + ed.from.name + "\n");
            result.append("To: " + ed.to.name + "\n");
            result.append("Weight: " + ed.weight + "\n");
            
            if (e.getActionCommand()=="Del"){ result.append("Deleted!!"); c.delEdge(ed);}
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(this, ex, "Input Error", JOptionPane.ERROR_MESSAGE);
        }
        reset();
    }
}

class SearchPath extends SearchDelete
{
    JComboBox select;
    SearchPath(Graph g, CanvasFrame c){
        super(g,c,"Enter Source", "Enter Dest", "Path",false);
        String[] st = {"Circle", "Square", "Triangle", "Cross", "Plus"};
        select = new JComboBox(st);
        select.setToolTipText("Select Shape");
        select.setBounds(0,120,200,30);
        add(select);
        result.setBounds(0,200,200,100);
        search.setBounds(0,160,200,30);
        delete.setVisible(false);
        frameSet();
    }
    
    public void actionPerformed(ActionEvent e){
        if(checkIfEmpty()) return;
        try{
            String from = field1.getText();
            String to = field2.getText();
            result.setText("");
            if(from.equals(to)) result.append(from);
            else{
                ArrayList path = g.getPath(from,to);
                if(path.size()==0) result.append("no path exists");
                else if(path.size()==1) result.append(from);
                else{
                    c.repaint();
                    c.paint(path,select.getSelectedIndex());
                    Iterator it = path.iterator();
                    while(it.hasNext()) result.append(it.next()+"\n");
                }
            }
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(this, ex, "Input Error", JOptionPane.ERROR_MESSAGE);
        }
        reset();
    }
}

class Coord{
    int x,y;
    Coord(int x, int y){
        this.x = x;
        this.y = y;
    }
}

class CanvasFrame extends JPanel implements MouseListener,KeyListener{
    JFrame f;
    JLabel stat;
    Graph g;
    Graphics gphx;
    Graphics2D gphx2D;
    Coord cur;
    Edge curEdge;
    ArrayList<Coord> occupied;
    ArrayList<String> names;
    boolean delFlag,delEdge;
    Coord[] drag;
    java.util.Timer timer;
    int not = 0;
    
    CanvasFrame(Graph g, JFrame f){
        drag = new Coord[2];
        this.stat = GraphCanvas.stat;
        this.f = f;
        this.g = g;
        timer = new java.util.Timer();
        occupied = new ArrayList<Coord>();
        names = new ArrayList<String>();
        this.setBackground(Color.lightGray);
        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
        repaint();
    }
    
    public void delEdge(Edge e){
        try{
            g.delEdge(e);
            repaint();
            GraphCanvas.stat.setText("Edge between " + e.to.name + " and " + e.from.name + " deleted ");
        }
        catch(Exception ex){JOptionPane.showMessageDialog(this, ex, "Input Error", JOptionPane.ERROR_MESSAGE);}
    }
    
    public void keyPressed(KeyEvent e){
        System.out.println(e.getKeyChar());
        if(e.getKeyChar()=='d') {
            if(delFlag) {delVertex(cur); cur=null; delFlag = false; drag[0] = null; drag[1]=null;}
            if(delEdge) {delEdge(curEdge); curEdge=null; delEdge = false;drag[0] = null; drag[1]=null;}
        }
        if(e.getKeyChar()=='m'){
            if(delEdge){
                try{
                String weight = JOptionPane.showInputDialog(f, curEdge.from.name + "->" + curEdge.to.name +": Enter new weight");
                
                if(weight!="" || weight!=null)    g.modifyEdge(curEdge.from.name, curEdge.to.name, Double.parseDouble(weight));
                }
                catch(Exception ex){JOptionPane.showMessageDialog(this, ex, "Input Error", JOptionPane.ERROR_MESSAGE);}
            }
        }
    }
    
    public void keyReleased(KeyEvent e){
        delFlag = false;
    }
    
    public void keyTyped(KeyEvent e){}
    
    public void delVertex(Coord c){
        try {
            System.out.println("kl");
            Vertex v =g.getVertex(getVertex(c));
            v.printDetails();
            g.delVertex(v);
            repaint();
            GraphCanvas.stat.setText("Vertex " + v.name + " deleted");
        } catch (Exception ex){
            System.out.println(ex);
        }
    }
    
    public void mouseClicked(MouseEvent e){
        System.out.println("Mouse Clicked");
        int x = e.getX()/4;
        int y = e.getY()/4;
        Coord c = new Coord(x,y);
        cur = c;
        System.out.println(x+" "+y);
        if(isOccupied(c)){
            delFlag = true;
            GraphCanvas.stat.setText("Vertex "+getVertex(c)+" is Selected");
            return;
        }
        curEdge = g.isEdge(c);
        if(curEdge !=null){
            delEdge = true;
            GraphCanvas.stat.setText("Edge from " + curEdge.from.name + " to " + curEdge.to.name + " selected ");
            curEdge.printDetails();
            return;
        }
        String name = JOptionPane.showInputDialog(f, "(X,Y) = (" + x +"," + y + ") : Input Name");
        if(name==null || name.equals("")) return;
        try{
            Vertex v = new Vertex(name,c.x,c.y);
            g.addVertex(v);
            repaint();
            GraphCanvas.stat.setText("Vertex " + v.name+ " added at "+x + ", " + y);
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this, ex, "Input Error", JOptionPane.ERROR_MESSAGE);;
        }  
    }
    
    public void mousePressed(MouseEvent e){
        requestFocus();
        mouseClicked(e);
        System.out.println("Mouse Pressed");
        int x = e.getX()/4;
        int y = e.getY()/4;
        Coord c = new Coord(x,y);
        System.out.println(x+" "+y);
        drag[0] = c;
    }
    
    public void mouseReleased(MouseEvent e){
        int x = e.getX()/4;
        int y = e.getY()/4;
        if(drag!=null||drag[0]!=null||drag[1]!=null)
        {if(x==drag[0].x&&y ==drag[0].y) return;}
        Coord c = new Coord(x,y);
        System.out.println(x+" "+y);
        drag[1] = c;
        if(!isOccupied(drag[0]) || !isOccupied(drag[1])){
            if(!isOccupied(drag[0])) return;
            moveVertex(drag[0],drag[1]);
            return;
        }
        if(isDrawn(drag[0],drag[1])) return;
        String response = JOptionPane.showInputDialog(f, (getVertex(drag[0]) + " to " + getVertex(drag[1]) + ": Enter Weight"));
        try {
            Edge ed = new Edge(g.getVertex(getVertex(drag[0])),g.getVertex(getVertex(drag[1])),Double.parseDouble(response));
            g.addEdge(ed);
            GraphCanvas.stat.setText("Edge from " + ed.from.name + " to " + ed.to.name + " added ");
            repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex, "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void moveVertex(Coord c1,Coord c2){
        try {
            g.modify(new Vertex(getVertex(c1),c2.x,c2.y));
            repaint();
        } catch (NoExistenceException ex) {
            JOptionPane.showMessageDialog(this, ex, "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public Coord getCoord(Coord c){
        for(int i = 0; i < occupied.size(); i++) if(c.x==occupied.get(i).x && c.y==occupied.get(i).y) return occupied.get(i);
        return null;
    }
    
    public boolean isOccupied(Coord c){
        for(int i = 0; i < occupied.size(); i++) if(c.x==occupied.get(i).x && c.y==occupied.get(i).y) return true;
        return false;
    }
    
    public String getVertex(Coord c){
        for(int i = 0; i < occupied.size(); i++) if(c.x==occupied.get(i).x && c.y==occupied.get(i).y) return names.get(i);
        return null;
    }
    
    public boolean isDrawn(Coord c1, Coord c2){
        String s1 = getVertex(c1), s2 = getVertex(c2);
        try{
            g.getEdge(s1, s2);
            g.getEdge(s2, s1);
            return true;
        }
        catch(NoExistenceException e){
            return false;
        }
    }
    
    public void paintComponent(Graphics gphx){
        super.paintComponent(gphx);
        this.gphx = gphx;
        gphx2D = (Graphics2D)gphx;
        Graphics2D gphx2D = (Graphics2D)gphx;
        occupied = new ArrayList<Coord>();
        names = new ArrayList<String>();
        for(int i = 0; i < g.vertices.size(); i++){
            Vertex v = g.vertices.get(i);
            occupied.add(new Coord((int)v.x, (int)v.y));
            names.add(v.name);
            gphx.setColor(Color.BLACK);
            gphx.fillRect((int)v.x*4, (int)v.y*4, 4, 4);
            ArrayList<Edge> temp = g.adjList.get(g.vertices.get(i).name);
            for(int j =0;j<temp.size();j++){ 
                Edge e = temp.get(j);
                gphx2D.setStroke(new BasicStroke(2));
                gphx2D.setColor(Color.WHITE);
                gphx2D.drawLine((int)e.from.x*4, (int)e.from.y*4,(int)e.to.x*4,(int)e.to.y*4);
                gphx.setColor(Color.BLACK);
                gphx.fillRect((int)e.from.x*4, (int)e.from.y*4, 4, 4);
                gphx.fillRect((int)e.to.x*4, (int)e.to.y*4, 4, 4);
            }
        }
    }
    
    public void stopTimer(){
        timer.cancel();
    }
    
    public void drawAnimatedLine(ArrayList<Edge> e, int j){
        timer = new java.util.Timer();
        TimerTask task=null;
        switch(j){
            case 0:
                task = new CircleAnimator((Graphics2D)this.getGraphics(),e,this);   
                break;
            case 1:
                task = new RectangleAnimator((Graphics2D)this.getGraphics(),e,this);
                break;
            case 2:
                task = new TriangleAnimator((Graphics2D)this.getGraphics(),e,this);
                break;
            case 3:
                task = new CrossAnimator((Graphics2D)this.getGraphics(),e,this);
                break;
            case 4:
                task = new PlusAnimator((Graphics2D)this.getGraphics(),e,this);
                break;
        }
        JPanel g = this;
        timer.schedule(task, new java.util.Date(), 80);
        if(not==0){
            timer.schedule(new TimerTask(){
            public void run(){
                g.repaint();
            }
        },new java.util.Date(),500);
            not = 1;
        }
        
    }
    
    public void paint(ArrayList<String> path, int j) throws NoExistenceException{
        ArrayList<Edge> edges = new ArrayList<Edge>();
        for(int i = 0; i < path.size()-1; i++)  edges.add( g.getEdge(path.get(i), path.get(i+1)));
        drawAnimatedLine(edges,j);
    }
    
    public void mouseEntered(MouseEvent e){}
    
    public void mouseExited(MouseEvent e){}
}

abstract class Animator extends java.util.TimerTask{
    Graphics2D g;
    CanvasFrame c;
    java.util.Timer timer;
    ArrayList<Edge> edges;
    Edge e;
    int x1, y1, x2, y2,x,y;
    int i,j;
    int m,size;
    
    Animator(Graphics2D g,ArrayList<Edge> edges,CanvasFrame c){
        this.c = c;
        this.g = g;
        this.edges = edges;
        i = 0;
        j = 0;
        size = edges.size();
        setCur();
    }
    
    void setCur(){
        //c.repaint();
        this.e = edges.get(j);
        x1 = (int)e.from.x*4;
        y1 = (int)e.from.y*4;
        x2 = (int)e.to.x*4;
        y2 = (int)e.to.y*4;
        m = (int)Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1))*2;
        System.out.println(m);
    }
    
    public void run(){
        g.setColor(Color.lightGray);
        drawShape(g,x,y,12,12);
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.white);
        g.drawLine(x1,y1,x2,y2);
        g.setColor(Color.BLACK);
        g.fillRect(x1,y1, 4, 4);
        g.fillRect(x2,y2, 4, 4);
        x = ((m-(i*10))*x1 + (i*10)*x2)/(m);
        y = ((m-(i*10))*y1 + (i*10)*y2)/(m);
        g.setColor(Color.BLUE);
        g.setStroke(new BasicStroke(2));
        drawShape(g,x,y,12,12);
        i=(i+1);
        if(i*10>m){
            j=(j+1)%size;
            setCur();
            i=0;
        }
        
    }
    
    abstract void drawShape(Graphics2D g, int x, int y, int w, int h);
    
}

class CircleAnimator extends Animator{
    CircleAnimator(Graphics2D g,ArrayList<Edge> edges,CanvasFrame c){
        super(g, edges,c);
    }
    void drawShape(Graphics2D g, int x, int y, int w, int h){
        g.drawOval(x, y, w, h);
    }
}

class RectangleAnimator extends Animator{
    RectangleAnimator(Graphics2D g,ArrayList<Edge> edges,CanvasFrame c){
        super(g, edges,c);
    }
    void drawShape(Graphics2D g, int x, int y, int w, int h){
        g.drawRect(x, y, w, h);
    }
}

class TriangleAnimator extends Animator{
    TriangleAnimator(Graphics2D g,ArrayList<Edge> edges,CanvasFrame c){
        super(g, edges,c);
    }
    void drawShape(Graphics2D g, int x, int y, int w, int h){
        double theta = Math.atan((double)-(x2-x1)/(y2-y1));
        double theta1 = Math.atan((double)(y2-y1)/(x2-x1));
        int[] xpoints = {(int)(x-8*Math.cos(theta1)),(int)(x+8*Math.cos(theta)),(int)(x-8*Math.cos(theta))};
        int[] ypoints = {(int)((int)(y-8*Math.sin(theta1))),(int)(y+8*Math.sin(theta)),(int)(y-8*Math.sin(theta))};
        
        g.drawPolygon(xpoints, ypoints, 3);
    }
}

class CrossAnimator extends Animator{
    CrossAnimator(Graphics2D g,ArrayList<Edge> edges,CanvasFrame c){
        super(g, edges,c);
    }
    void drawShape(Graphics2D g, int x, int y, int w, int h){
        double theta = Math.atan((double)(y2-y1)/(x2-x1))-Math.PI/4;
        double theta1 = Math.atan((double)(y2-y1)/(x2-x1))+Math.PI/4;
        g.draw(new Line2D.Double((x+8*Math.cos(theta1)),(y+8*Math.sin(theta1)),(x-8*Math.cos(theta1)),(y-8*Math.sin(theta1))));
        g.draw(new Line2D.Double((x+8*Math.cos(theta)),(y+8*Math.sin(theta)),(x-8*Math.cos(theta)),(y-8*Math.sin(theta))));
        int[] xpoints = {x,x+12,x};
        int[] ypoints = {y,y,y-12};
        //g.drawPolygon(xpoints, ypoints, 3);
    }
}

class PlusAnimator extends Animator{
    PlusAnimator(Graphics2D g,ArrayList<Edge> edges,CanvasFrame c){
        super(g, edges,c);
    }
    void drawShape(Graphics2D g, int x, int y, int w, int h){
        double theta = Math.atan((double)-(x2-x1)/(y2-y1));
        double theta1 = Math.atan((double)(y2-y1)/(x2-x1));
        g.draw(new Line2D.Double((x+8*Math.cos(theta1)),(y+8*Math.sin(theta1)),(x-8*Math.cos(theta1)),(y-8*Math.sin(theta1))));
        g.draw(new Line2D.Double((x+8*Math.cos(theta)),(y+8*Math.sin(theta)),(x-8*Math.cos(theta)),(y-8*Math.sin(theta))));
        int[] xpoints = {x,x+12,x};
        int[] ypoints = {y,y,y-12};
        //g.drawPolygon(xpoints, ypoints, 3);
    }
}

public class GraphCanvas{
   static JLabel stat;
  public static void main(String args[]) throws IOException,NoExistenceException,PreExistenceException
  {
    
    Graph g = new Graph();
    stat = new JLabel("Status Bar");
    stat.setBounds(1300,770,250,20);
    JFrame f = new JFrame("GraphCanvas");
    CanvasFrame c = new CanvasFrame(g,f);
    JButton im = new JButton();
    JButton ex = new JButton();
    JLabel imex = new JLabel("Import/Export: ");
    
    stat.setFont(new Font(Font.MONOSPACED,Font.ITALIC,12));
    f.add(stat);
    JLabel title1 = new JLabel("G");
    JLabel title3 = new JLabel("C");
    Font bigf = new Font(Font.MONOSPACED,Font.BOLD,50);
    title1.setFont(bigf);
    title3.setFont(bigf);
    Font smallf = new Font(Font.MONOSPACED,Font.BOLD,25);
    JLabel title2 = new JLabel("RAPH");
    JLabel title4 = new JLabel("ANVAS");
    title2.setFont(smallf);
    title4.setFont(smallf);
    title1.setBounds(1300,20,30,40);
    title2.setBounds(1330,40,100,20);
    title3.setBounds(1390,55,30,40);
    title4.setBounds(1420,75,100,20);
    imex.setBounds(1300,130,90,30);
    im.setIcon(UIManager.getIcon("FileChooser.newFolderIcon"));
    im.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e) {
            String path = JOptionPane.showInputDialog(f, "InputPath");
            if(path!=null) g.fileInput(f,path,c);
        }
    });
    ex.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e) {
            String path = JOptionPane.showInputDialog(f, "InputPath");
            if(path!=null) g.export(f,path);
        }
    });
    ex.setIcon(UIManager.getIcon("FileChooser.upFolderIcon"));
    im.setBounds(1390,130,50,30);
    ex.setBounds(1450,130,50,30);
    f.add(title1);
    f.add(title2);
    f.add(title3);
    f.add(title4);
    f.add(ex);
    f.add(im);
    f.add(imex);
    JRadioButton amv = new JRadioButton("Add / Modify Vertex");
    amv.setSelected(true);
    JRadioButton ame = new JRadioButton("Add / Modify Edge");
    JRadioButton sev = new JRadioButton("Search / Delete Vertex");
    JRadioButton see = new JRadioButton("Search / Delete Edge");
    JRadioButton sp = new JRadioButton("Search Path");
    ButtonGroup bg = new ButtonGroup();
    bg.add(sp);
    bg.add(see);
    bg.add(sev);
    bg.add(ame);
    bg.add(amv);
    //f.add(((JLabel)(new JLabel("-----------------"))).setBounds(1300,10,200,10));
    amv.setBounds(1300,190,200,20);
    ame.setBounds(1300,240,200,20);
    sev.setBounds(1300,290,200,20);
    see.setBounds(1300,340,200,20);
    sp.setBounds(1300,390,200,20);
    JPanel amvp = new AddModifyVertex(g,c);
    amvp.setBounds(1300,440,200,300);
    amvp.setVisible(true);
    f.add(amvp);
    JPanel amep = new AddModifyEdge(g,c);
    amep.setBounds(1300,440,200,300);
    amep.setVisible(false);
    f.add(amep);
    JPanel sevp = new SearchDeleteVertex(g,c);
    sevp.setBounds(1300,440,200,300);
    sevp.setVisible(false);
    f.add(sevp);
    JPanel seep = new SearchDeleteEdge(g,c);
    seep.setBounds(1300,440,200,300);
    seep.setVisible(false);
    f.add(seep);
    JPanel spp = new SearchPath(g,c);
    spp.setBounds(1300,440,200,300);
    spp.setVisible(false);
    f.add(spp);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    amv.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent a){
            amep.setVisible(false);
            spp.setVisible(false);
            seep.setVisible(false);
            sevp.setVisible(false);
            amvp.setVisible(true);
        }
    });
    ame.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent a){
            sevp.setVisible(false);
            spp.setVisible(false);
            seep.setVisible(false);
            amvp.setVisible(false);
            amep.setVisible(true);
        }
    });
    sev.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent a){
            amep.setVisible(false);
            spp.setVisible(false);
            seep.setVisible(false);
            amvp.setVisible(false);
            sevp.setVisible(true);
        }
    });
    see.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent a){
            sevp.setVisible(false);
            spp.setVisible(false);
            amep.setVisible(false);
            amvp.setVisible(false);
            seep.setVisible(true);
        }
    });
    sp.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent a){
            sevp.setVisible(false);
            amep.setVisible(false);
            seep.setVisible(false);
            amvp.setVisible(false);
            spp.setVisible(true);
        }
    });
    f.add(amv);
    f.add(ame);
    f.add(sev);
    f.add(see);
    f.add(sp);
    f.add(c);
    c.setBounds(0,0,1250,800);
    f.setLayout(null);
    f.setVisible(true);
    f.setExtendedState( f.getExtendedState()|JFrame.MAXIMIZED_BOTH );
    JOptionPane.showMessageDialog(f,String.format("IMPORTANT INSTRUCTIONs\n1. Click a vertex/edge to select it.\n2. To confirm selection check the statusbar at the bottom right.\n3. Select then press 'd' to delete vertex.\n4. Select then press 'd' to delete edge.\n5. Select then press 'm' to modify edge.", args));
  }
  
}

